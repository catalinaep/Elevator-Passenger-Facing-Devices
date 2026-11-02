import Panel.ElevatorPanel.ElevatorPanel;
import Panel.FloorPanel.FloorPanel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
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

        // make the elevator panels
        VBox elevatorPanels = new VBox(15);
        elevatorPanels.getChildren().add(new Label("Elevator Panels"));
        for (int elevatorNumber = 1; elevatorNumber <= FloorPanel.ELEVATOR_COUNT; elevatorNumber++) {
            ElevatorPanel elevatorPanel = new ElevatorPanel(elevatorNumber);
            manager.addElevatorPanel(elevatorPanel);

            VBox elevatorBox = new VBox(5, new Label("Elevator " + elevatorNumber), elevatorPanel.getGui());
            elevatorPanels.getChildren().add(elevatorBox);
        }

        // make the floor panels
        VBox floorPanels = new VBox(15);
        floorPanels.getChildren().add(new Label("Floor Panels"));
        for (int floorNumber = FloorPanel.MIN_FLOOR; floorNumber <= FloorPanel.MAX_FLOOR; floorNumber++) {
            FloorPanel floorPanel = new FloorPanel(floorNumber);
            manager.addFloorPanel(floorPanel);

            VBox floorBox = new VBox(5, new Label("Floor " + floorNumber), floorPanel.getGui());
            floorPanels.getChildren().add(floorBox);
        }

        HBox root = new HBox(25, new ScrollPane(elevatorPanels), new ScrollPane(floorPanels));
        stage.setScene(new Scene(root, 1200, 800));
        stage.setTitle("Elevator Passenger Panels");
        stage.show();

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