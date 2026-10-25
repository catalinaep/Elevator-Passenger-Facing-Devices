import Panel.FloorPanel;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private FloorPanel floorPanel;
    private Label resultLabel;

    @Override
    public void start(Stage primaryStage) {
        floorPanel = new FloorPanel();

        // Main elevator panel window
        VBox root = new VBox(floorPanel.getComponents());
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Scene mainScene = new Scene(root, 250, 350);

        primaryStage.setTitle("Elevator Floor Panel");
        primaryStage.setScene(mainScene);
        primaryStage.setX(100);
        primaryStage.setY(100);
        primaryStage.show();

        openApiTestWindow();
    }

   /*
   helper to test api functions
    */
    private void openApiTestWindow() {
        Stage testStage = new Stage();

        Label title = new Label("FloorPanel API Tester");


        Label floorNumberLabel = new Label();
        floorNumberLabel.setText(
                "getFloorNumber(): " + floorPanel.getFloorNumber()
        );

        Label upStatus = new Label();
        Label downStatus = new Label();
        Label alarmStatus = new Label();

        resultLabel = new Label("Select a function to test.");
        resultLabel.setWrapText(true);
        resultLabel.setStyle(
                "-fx-background-color: #d696f6;" +
                        "-fx-padding: 10;" +
                        "-fx-background-radius: 5;"
        );

        // Check upBtnPressed()
        Button checkUpButton = new Button("Test upBtnPressed()");
        checkUpButton.setOnAction(event -> {
            boolean result = floorPanel.upBtnPressed();
            upStatus.setText("Up pressed: " + result);
            resultLabel.setText(
                    "upBtnPressed() returned: " + result
            );
        });

        // Check downBtnPressed()
        Button checkDownButton = new Button("Test downBtnPressed()");
        checkDownButton.setOnAction(event -> {
            boolean result = floorPanel.downBtnPressed();
            downStatus.setText("Down pressed: " + result);
            resultLabel.setText(
                    "downBtnPressed() returned: " + result
            );
        });

        // Check static alarmPressed()
        Button checkAlarmButton = new Button("Test alarmPressed()");
        checkAlarmButton.setOnAction(event -> {
            boolean result = FloorPanel.alarmPressed();
            alarmStatus.setText("Alarm pressed: " + result);
            resultLabel.setText(
                    "alarmPressed() returned: " + result
            );
        });

        // Check resetUpBtn()
        Button resetUpButton = new Button("Test resetUpBtn()");
        resetUpButton.setOnAction(event -> {
            floorPanel.resetUpBtn();
            boolean result = floorPanel.upBtnPressed();

            upStatus.setText("Up pressed: " + result);
            resultLabel.setText(
                    "resetUpBtn() executed.\n" +
                            "upBtnPressed() now returns: " + result
            );
        });

        // Check resetDownBtn()
        Button resetDownButton = new Button("Test resetDownBtn()");
        resetDownButton.setOnAction(event -> {
            floorPanel.resetDownBtn();
            boolean result = floorPanel.downBtnPressed();

            downStatus.setText("Down pressed: " + result);
            resultLabel.setText(
                    "resetDownBtn() executed.\n" +
                            "downBtnPressed() now returns: " + result
            );
        });

        // Check resetAlarm()
        Button resetAlarmButton = new Button("Test resetAlarm()");
        resetAlarmButton.setOnAction(event -> {
            floorPanel.resetAlarm();
            boolean result = FloorPanel.alarmPressed();

            alarmStatus.setText("Alarm pressed: " + result);
            resultLabel.setText(
                    "resetAlarm() executed.\n" +
                            "alarmPressed() now returns: " + result
            );
        });

        // Check ding()
        Button dingButton = new Button("Test ding()");
        dingButton.setOnAction(event -> {
            floorPanel.ding();
            resultLabel.setText(
                    "ding() executed. Check whether your system beep played."
            );
        });

        // Check getFloorNumber()
        Button floorNumberButton = new Button("Test getFloorNumber()");
        floorNumberButton.setOnAction(event -> {
            String result = floorPanel.getFloorNumber();
            floorNumberLabel.setText(
                    "getFloorNumber(): " + result
            );
            resultLabel.setText(
                    "getFloorNumber() returned: " + result
            );
        });

;

        // Refresh all current states
        Button refreshButton = new Button("Refresh All States");
        refreshButton.setOnAction(event -> {
            upStatus.setText(
                    "Up pressed: " + floorPanel.upBtnPressed()
            );
            downStatus.setText(
                    "Down pressed: " + floorPanel.downBtnPressed()
            );
            alarmStatus.setText(
                    "Alarm pressed: " + FloorPanel.alarmPressed()
            );
            resultLabel.setText("All API states refreshed.");
        });

        // Initial state
        upStatus.setText(
                "Up pressed: " + floorPanel.upBtnPressed()
        );
        downStatus.setText(
                "Down pressed: " + floorPanel.downBtnPressed()
        );
        alarmStatus.setText(
                "Alarm pressed: " + FloorPanel.alarmPressed()
        );

        VBox layout = new VBox(
                10,
                title,
                floorNumberLabel,
                new Label("Current API State"),
                upStatus,
                downStatus,
                alarmStatus,
                refreshButton,
                new Label("Individual Function Tests"),
                checkUpButton,
                checkDownButton,
                checkAlarmButton,
                resetUpButton,
                resetDownButton,
                resetAlarmButton,
                dingButton,
                floorNumberButton,
                new Label("Test Result"),
                resultLabel
        );

        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPrefWidth(340);

        Scene scene = new Scene(layout, 380, 720);

        testStage.setScene(scene);
        testStage.setX(400);
        testStage.setY(100);
        testStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

