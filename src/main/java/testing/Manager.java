package testing;

import Utilities.Message;
import Utilities.Protocol;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Manager implements AutoCloseable {
    private record Subscription(int topic, int subtopic) { }

    private final Socket socket;
    private final InputStream in;
    private final OutputStream out;
    private final Set<Subscription> subscriptions = new HashSet<>();

    public Manager(String ip, int port) throws IOException {
        socket = new Socket(ip,port);
        socket.setSoTimeout(1000);

        in = socket.getInputStream();
        out = socket.getOutputStream();

        System.out.println("Connected to bus at " + ip + ":" + port);
    }

    /**
     * Run the list of commands from a test script
     */
    public void run(List<String> commands) throws IOException, InterruptedException {
        for (String line : commands) {
            line = line.trim();
            if (line.isBlank() || line.startsWith("#")) continue;

            String[] parts = line.split("\\s+");
            switch (parts[0].toUpperCase()) {
                case "SUBSCRIBE" -> subscribe(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                case "POST" -> publish(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Long.parseLong(parts[3]));
                case "GET" -> getAndDistribute(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                case "SLEEP" -> Thread.sleep(Long.parseLong(parts[1]));
                default -> throw new IllegalArgumentException("Unknown Command: " + parts[0]);
            }
        }
    }

    /**
     * subscribe to something on the software bus, taking in a topic and subtopic
     */
    private synchronized void subscribe(int topic, int subtopic) throws IOException {
        Protocol.writeMessage(out, Message.of(Protocol.SUBSCRIBE, topic, subtopic));
        subscriptions.add(new Subscription(topic, subtopic));
        System.out.printf("Subscribed to: Topic %d, Subtopic %d%n", topic, subtopic);
    }

    /**
     * publish a message to the software bus with the topic and subtopic
     */
    private synchronized void publish(int topic, int subtopic, long value) throws IOException {
        Protocol.writeMessage(out, Message.of(topic, subtopic, value));
        System.out.printf("Posted: %d to %d:%d%n", value, topic, subtopic);
    }

    /**
     * Get a message from the software bus and send it to the correct function in the panels
     */
    private synchronized void getAndDistribute(int topic, int subtopic) throws IOException {
        Protocol.writeMessage(out, Message.of(Protocol.GET, topic, subtopic));
        Message message = Protocol.readMessage(in);
        if (message == null) throw new IOException("Disconnected from Bus");
        if (message.topic() != Protocol.NO_MESSAGE) distribute(message);
    }

    /**
     * distribute a message from the software bus to the panel components that need to be informed
     */
    private void distribute(Message message) {
        System.out.printf("Received topic: %d, subtopic: %d, value: %d%n",
                message.topic(), message.subtopic(), message.value());

        if (message.topic() == 7 && message.subtopic() == 1) {
            //TODO: put in logic to actually handle message
        } else if (message.topic() == 8 && message.subtopic() == 1) {
            //TODO
        }
    }

    // close socket
    @Override
    public void close() throws IOException {
        socket.close();
    }
}
