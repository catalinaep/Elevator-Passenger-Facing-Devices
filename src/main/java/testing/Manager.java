package testing;

import Panel.Direction;
import Panel.ElevatorPanel.ElevatorPanel;
import Panel.FloorPanel.FloorPanel;
import Utilities.Message;
import Utilities.Protocol;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class Manager implements AutoCloseable {
    private record Subscription(int topic, int subtopic) { }

    private final Socket socket;
    private final InputStream in;
    private final OutputStream out;
    private final Set<Subscription> subscriptions = new HashSet<>();

    // panel references
    private final Map<Integer, ElevatorPanel> elevatorPanels = new HashMap<>();
    private final Map<Integer, FloorPanel> floorPanels = new HashMap<>();

    // topics for elevator panels
    private static final int CAR_BUTTON_PRESSED = 10;
    private static final int CAR_BUTTON_RESET = 11;
    private static final int CAR_KEY_STATE = 12;
    private static final int CAR_DING = 13;
    private static final int CAR_BUZZER = 14;
    private static final int CAR_FLOOR_DISPLAY = 15;
    private static final int CAR_DIRECTION_DISPLAY = 16;

    // topics for floor panels
    private static final int HALL_CALL = 20;
    private static final int FLOOR_DING = 21;
    private static final int HALL_REQUEST_STATE = 22;
    private static final int ELEVATOR_STATUS = 23;
    private static final int FLOOR_ALARM_STATE = 24;

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
        try {
            Protocol.writeMessage(out, Message.of(topic, subtopic, value));
            System.out.printf("Posted: %d to %d:%d%n", value, topic, subtopic);
        } catch (IOException e) {
            System.err.println("Could not publish event: " + e.getMessage());
        }
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
        int topic = message.topic();
        int elevatorNumber = message.subtopic();
        long value = message.value();

        System.out.printf("Received topic: %d, subtopic: %d, value: %d%n", topic, elevatorNumber, value);

        switch (topic) {
            // elevator panel commands
            case CAR_BUTTON_RESET -> elevatorPanels.get(elevatorNumber).resetBtn((int) value);
            case CAR_KEY_STATE -> elevatorPanels.get(elevatorNumber).setKeyInserted(value != 0);
            case CAR_DING -> elevatorPanels.get(elevatorNumber).ding();
            case CAR_BUZZER -> {
                if (value == 1) {
                    elevatorPanels.get(elevatorNumber).playBuzz();
                } else if (value == 0) {
                    elevatorPanels.get(elevatorNumber).stopBuzz();
                } else {
                    throw new IllegalArgumentException("Buzzer value must be 0 or 1: " + value);
                }
            }
            case CAR_FLOOR_DISPLAY -> elevatorPanels.get(elevatorNumber).updateFloorDisplay((int) value);
            case CAR_DIRECTION_DISPLAY -> elevatorPanels.get(elevatorNumber).updateDirDisplay(Direction.fromValue(value));
            
            // floor panel commands
            case FLOOR_DING -> floorPanels.get(elevatorNumber).ding();
            case HALL_REQUEST_STATE -> floorPanels.get(elevatorNumber).updateDirection(Direction.fromValue(value));
            case ELEVATOR_STATUS -> {
                int currentFloor = (int) (value >> 2);
                Direction direction = Direction.fromValue(value & 0b11);
                for (FloorPanel floor : floorPanels.values()) {
                    floor.updateElevatorStatus(elevatorNumber, currentFloor, direction);
                }
            }
            default -> System.out.println("Unknown topic: " + topic + " will be ignored.");
        }
    }

    /** get the elevator panel identified by a number */
    private ElevatorPanel getElevatorPanel(int elevatorNum) {
        ElevatorPanel panel = elevatorPanels.get(elevatorNum);
        if (panel == null) throw new IllegalArgumentException("No elevator panel registered for " + elevatorNum);
        return panel;
    }

    /** get the floor panel identified by floor number */
    private FloorPanel getFloorPanel(int elevatorNum) {
        FloorPanel panel = floorPanels.get(elevatorNum);
        if (panel == null) throw new IllegalArgumentException("No elevator panel registered for " + elevatorNum);
        return panel;
    }

    // add an elevator panel to the collection
    public void addElevatorPanel(ElevatorPanel panel) {
        int elevatorNumber = panel.getElevatorNum();
        elevatorPanels.put(elevatorNumber, panel);
        panel.setFloorButtonRequestHandler(floorNumber -> {
            try {
                publish(CAR_BUTTON_PRESSED, elevatorNumber, floorNumber);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        panel.setKeyStateHandler(inserted -> {
            try {
                publish(CAR_KEY_STATE, elevatorNumber, inserted ? 1 : 0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // add a floor panel to the collection
    public void addFloorPanel(FloorPanel panel) {
        int floorNumber = panel.getFloorNumber();
        floorPanels.put(floorNumber, panel);
        panel.setDirectionRequestHandler(direction -> {
            try {
                publish(HALL_CALL, floorNumber, direction.toValue());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        panel.setAlarmHandler((floor, alarmOn) -> {
            try {
                publish(FLOOR_ALARM_STATE, floor, alarmOn ? 1 : 0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // close socket
    @Override
    public void close() throws IOException {
        socket.close();
    }
}
