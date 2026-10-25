package testing;

import Panel.ElevatorPanel;
import Panel.FloorPanel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FloorPanel floorPanel = new FloorPanel();
        Scene scene = new Scene(floorPanel.getComponents(), 220, 260);
        primaryStage.setScene(scene);
        primaryStage.show();


    }


    //creates floor panel and elevator objects and passes them to manager where it can interact wit hthem
}
