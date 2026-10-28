import Panel.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.animation.PauseTransition;
import javafx.util.Duration;

/**
 * Runs the one floor demonstration of the passenger facing devices
 */
public class Main extends Application {
    @Override
    public void start(Stage elevatorStage) {
        showElevatorWindow(elevatorStage);
        showFloorPanelWindow();
    }

    /** Create a window that only contains the elevator window */
    private void showElevatorWindow(Stage stage) {
        ElevatorPanelGUI elevatorGUI = new ElevatorPanelGUI();
        ElevatorPanel elevatorPanel = new ElevatorPanel(1, elevatorGUI);
        elevatorPanel.updateFloorDisplay(1);
        elevatorPanel.updateDirDisplay(Direction.IDLE);

        VBox root = new VBox(8, elevatorGUI.getView());
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(24));
        root.setStyle("-fx-background-color: #f8f8f8;");

        stage.setTitle("Elevator Panel");
        stage.setScene(new Scene(root, 260, 650));
        stage.show();
    }

    /** Creates a separate window that only contains the floor panel */
    private void showFloorPanelWindow() {
        FloorPanel floorPanel = new FloorPanel("1");
        FloorPanelGUI floorPanelGUI = new FloorPanelGUI();

        floorPanelGUI.setDirectionRequestHandler(floorPanel::requestDirection);
        floorPanelGUI.setAlarmRequestHandler(floorPanel::toggleAlarm);
        floorPanel.setStateListener(floorPanelGUI::render);

        Label title = new Label("LANDING PANEL");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        Label instructions = new Label("Click ▲ or ▼ to light that call button.");
        instructions.setStyle("-fx-font-size: 11px; -fx-text-fill: #555555;");

        VBox root = new VBox(8, title, floorPanelGUI.getView(), instructions);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(24));
        root.setStyle("-fx-background-color: #f8f8f8;");

        Stage floorStage = new Stage();
        floorStage.setTitle("Floor Panel");
        floorStage.setScene(new Scene(root, 300, 420));
        floorStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}