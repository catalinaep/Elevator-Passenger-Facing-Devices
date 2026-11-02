import testing.Manager;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Runs the one floor demonstration of the passenger facing devices
 */
public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.out.println("Usage: java Main <ip> <port> <test-file>");
            return;
        }

        String ip = args[0];
        int port = Integer.parseInt(args[1]);
        Path testScript = Path.of(args[2]);

        List<String> commands = Files.readAllLines(testScript);
        try (Manager manager = new Manager(ip, port)) {
            manager.run(commands);
        }
    }
}