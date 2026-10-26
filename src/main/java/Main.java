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

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        ElevatorPanelGUI elevatorGUI = new ElevatorPanelGUI();
        ElevatorPanel elevatorPanel = new ElevatorPanel(1, elevatorGUI);
        FloorPanel floorPanel = new FloorPanel("1");

        elevatorPanel.updateFloorDisplay(1);
        elevatorPanel.updateDirDisplay(ElevatorPanelGUI.Direction.IDLE);
        floorPanel.setDirectionRequestHandler(elevatorPanel::updateDirDisplay);

        Label elevatorTitle = new Label("ELEVATOR 1");
        elevatorTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        VBox elevatorColumn = new VBox(8, elevatorTitle, elevatorGUI.getView());
        elevatorColumn.setAlignment(Pos.TOP_CENTER);

        Label landingTitle = new Label("LANDING PANEL");
        landingTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        Label instructions = new Label("Click ▲ or ▼ to test the direction light.");
        instructions.setStyle("-fx-font-size: 11px; -fx-text-fill: #555555;");
        VBox landingColumn = new VBox(8, landingTitle, floorPanel.getComponents(), instructions);
        landingColumn.setAlignment(Pos.TOP_CENTER);

        HBox root = new HBox(28, elevatorColumn, landingColumn);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(24));
        root.setStyle("-fx-background-color: #f8f8f8;");

        Scene scene = new Scene(root, 500, 520);
        stage.setTitle("Passenger Facing Devices");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
