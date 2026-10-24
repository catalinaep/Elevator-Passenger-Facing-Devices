package testing;

import Panel.ElevatorPanel;
import Panel.ElevatorPanelGUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        // Create the root layout
        stage.setTitle("Elevator Panel");
        VBox root = new VBox(10);

        // Create the panel and its GUI
        ElevatorPanelGUI gui = new ElevatorPanelGUI();
        ElevatorPanel panel = new ElevatorPanel(1, gui);

        // Add the panel GUI to the layout
        root.getChildren().add(gui.getView());

        // Create the scene and display the window
        Scene scene = new Scene(root, 100, 800);
        stage.setTitle("Elevator Panel");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}