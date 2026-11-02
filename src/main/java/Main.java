import Panel.ElevatorPanel.ElevatorPanel;
import Panel.FloorPanel.FloorPanel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import testing.Manager;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Runs the one floor demonstration of the passenger facing devices
 */
public class Main extends Application {
    private Manager manager;
    private Thread scriptThread;

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage stage) throws Exception {
        List<String> args = getParameters().getRaw();
        if (args.size() != 3) {
            System.out.println("Usage: java Main <ip> <port> <test-file>");
            Platform.exit();
            return;
        }

        String ip = args.get(0);
        int port = Integer.parseInt(args.get(1));
        Path testScript = Path.of(args.get(2));
        List<String> commands = Files.readAllLines(testScript);

        manager = new Manager(ip, port);

        ElevatorPanel elevatorPanel = new ElevatorPanel(1);
        manager.addElevatorPanel(elevatorPanel);
        VBox elevatorRoot = new VBox(10, new Label("Elevator 1"), elevatorPanel.getGui());
        elevatorRoot.setPadding(new Insets(15));

        stage.setTitle("Elevator 1 Panel");
        stage.setScene(new Scene(elevatorRoot));
        stage.show();

        FloorPanel floorPanel = new FloorPanel(1);
        manager.addFloorPanel(floorPanel);
        VBox floorRoot = new VBox(10, new Label("Floor 1 Panel"), floorPanel.getGui());
        floorRoot.setPadding(new Insets(15));

        Stage floorStage = new Stage();
        floorStage.setTitle("Floor 1 Panel");
        floorStage.setScene(new Scene(floorRoot));
        floorStage.show();

        scriptThread = new Thread(() -> {
            try {
                manager.run(commands);
            } catch (Exception e) {
                System.err.println("Test script failed: " + e.getMessage());
                e.printStackTrace();
            }
        }, "manager-test-script");

        scriptThread.setDaemon(true);
        scriptThread.start();
    }

    @Override
    public void stop() throws Exception {
        if (scriptThread != null) scriptThread.interrupt();
        if (manager != null) manager.close();
    }
}