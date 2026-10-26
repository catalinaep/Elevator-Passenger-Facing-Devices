import Panel.ElevatorPanel;
import Panel.ElevatorPanelGUI;
import Panel.FloorPanel;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    public void start(Stage stage) {
        ElevatorPanelGUI elevatorGUI = new ElevatorPanelGUI();
        ElevatorPanel elevatorPanel = new ElevatorPanel(1, elevatorGUI);
        FloorPanel floorPanel = new FloorPanel("1");

        VBox elevatorView = elevatorGUI.getView();
        VBox floorView = floorPanel.getComponents();
        HBox root = new HBox(30, elevatorView, floorView);
        Scene scene = new Scene(root, 350, 500);

        stage.setTitle("Passenger Facing Devices");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

