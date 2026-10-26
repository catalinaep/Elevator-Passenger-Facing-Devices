import Panel.ElevatorPanel;
import Panel.ElevatorPanelGUI;
import Panel.FloorPanel;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Runs the one floor demonstration of the passenger facing devices
 */
public class Main extends Application {
    @Override
    public void start(Stage stage) {
        ElevatorPanelGUI elevatorGUI = new ElevatorPanelGUI();
        ElevatorPanel elevatorPanel = new ElevatorPanel(1, elevatorGUI);
        FloorPanel floorPanel = new FloorPanel("4");

        // the elevator state for elevator #1
        elevatorPanel.updateFloorDisplay(1);
        elevatorPanel.updateDirDisplay(ElevatorPanelGUI.Direction.IDLE);

        // The panel displays the state of all 4 elevators, started at a random state for now
        floorPanel.updateElevatorStatus(1, 1, ElevatorPanelGUI.Direction.IDLE);
        floorPanel.updateElevatorStatus(2, 4, ElevatorPanelGUI.Direction.UP);
        floorPanel.updateElevatorStatus(3, 7, ElevatorPanelGUI.Direction.DOWN);
        floorPanel.updateElevatorStatus(4, 10, ElevatorPanelGUI.Direction.IDLE);

        floorPanel.setDirectionRequestHandler(direction -> {
            elevatorPanel.updateDirDisplay(direction);
            floorPanel.updateElevatorStatus(1, 1, direction);
        });

        Label interiorTitle = new Label("Interior Panel");
        interiorTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        VBox elevatorColumn = new VBox(8, interiorTitle, elevatorGUI.getView());
        elevatorColumn.setAlignment(Pos.TOP_CENTER);

        Label floorPanelTitle = new Label("Floor Panel");
        floorPanelTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        VBox landingColumn = new VBox(8, floorPanelTitle, floorPanel.getComponents());
        landingColumn.setAlignment(Pos.TOP_CENTER);

        HBox root = new HBox(28, elevatorColumn, landingColumn);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(24));
        root.setStyle("-fx-background-color: #f8f8f8;");

        Scene scene = new Scene(root, 660, 680);
        stage.setTitle("Passenger Facing Devices");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}