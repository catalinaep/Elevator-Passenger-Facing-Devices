package Panel;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.InputStream;

public class ElevatorPanelGUI {



    private final Label floorLabel = new Label("Floor: --");
    private final Label directionLabel = new Label("Idle");

    private final Button firstFloor = createNumberButton("/buttons-grey/1.png");
    private final Button secondFloor = createNumberButton("/buttons-grey/2.png");
    private final Button thirdFloor = createNumberButton("/buttons-grey/3.png");
    private final Button fourthFloor = createNumberButton("/buttons-grey/4.png");
    private final Button fifthFloor = createNumberButton("/buttons-grey/5.png");
    private final Button sixthFloor = createNumberButton("/buttons-grey/6.png");
    private final Button seventhFloor = createNumberButton("/buttons-grey/7.png");
    private final Button eighthFloor = createNumberButton("/buttons-grey/8.png");
    private final Button ninthFloor = createNumberButton("/buttons-grey/9.png");
    private final Button tenthFloor = createNumberButton("/buttons-grey/10.png");

    public ElevatorPanelGUI() {
        configureButton(firstFloor, "firstFloor");
        configureButton(secondFloor, "secondFloor");
        configureButton(thirdFloor, "thirdFloor");
        configureButton(fourthFloor, "fourthFloor");
        configureButton(fifthFloor, "fifthFloor");
        configureButton(sixthFloor, "sixthFloor");
        configureButton(seventhFloor, "seventhFloor");
        configureButton(eighthFloor, "eighthFloor");
        configureButton(ninthFloor, "ninthFloor");
        configureButton(tenthFloor, "tenthFloor");
    }

    public VBox getView() {

        HBox buttonRowTop = new HBox(150,ninthFloor,tenthFloor);
        HBox buttonRowTwo = new HBox(150,seventhFloor,eighthFloor);
        HBox buttonRowThird = new HBox(150,sixthFloor,fifthFloor);
        HBox buttonRowFourth = new HBox(150,fourthFloor,thirdFloor);
        HBox buttonRowFifth = new HBox(150,secondFloor,firstFloor);


        VBox layout = new VBox(10,
                floorLabel,
                directionLabel,
                buttonRowTop,
                buttonRowTwo,
                buttonRowThird,
                buttonRowFourth,
                buttonRowFifth
        );
        return layout;
    }

    public void updateFloorDisplay(String floor) {
        floorLabel.setText("Floor: " + floor);
    }

    public void updateDirDisplay(String direction) {
        directionLabel.setText(direction);
    }

    private Button createNumberButton(String imagePath) {
        InputStream stream = getClass().getResourceAsStream(imagePath);

        if (stream == null) {
            throw new IllegalArgumentException(
                    "Could not find image: " + imagePath
            );
        }

        Image image = new Image(stream);
        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);

        Button button = new Button();
        button.setGraphic(imageView);

        return button;
    }
    private void configureButton(Button button, String id) {
        button.setId(id);
    }
    public void setButtonHandler(
            java.util.function.Consumer<String> handler) {

        firstFloor.setOnAction(e -> handler.accept(firstFloor.getId()));
        secondFloor.setOnAction(e -> handler.accept(secondFloor.getId()));
        thirdFloor.setOnAction(e -> handler.accept(thirdFloor.getId()));
        fourthFloor.setOnAction(e -> handler.accept(fourthFloor.getId()));
        fifthFloor.setOnAction(e -> handler.accept(fifthFloor.getId()));
        sixthFloor.setOnAction(e -> handler.accept(sixthFloor.getId()));
        seventhFloor.setOnAction(e -> handler.accept(seventhFloor.getId()));
        eighthFloor.setOnAction(e -> handler.accept(eighthFloor.getId()));
        ninthFloor.setOnAction(e -> handler.accept(ninthFloor.getId()));
        tenthFloor.setOnAction(e -> handler.accept(tenthFloor.getId()));

    }

    public void changeButtonImage(String buttonId, String imagePath) {
        Button button = switch (buttonId){
            case "firstFloor" -> firstFloor;
            case "secondFloor" -> secondFloor;
            case "thirdFloor" -> thirdFloor;
            case "fourthFloor" -> fourthFloor;
            case "fifthFloor" -> fifthFloor;
            case "sixthFloor" -> sixthFloor;
            case "seventhFloor" -> seventhFloor;
            case "eighthFloor" -> eighthFloor;
            case "ninthFloor" -> ninthFloor;
            case "tenthFloor" -> tenthFloor;
            default -> null;
        };


        if (button == null) {
            return;
        }

        Image image = new Image(
                getClass().getResourceAsStream(imagePath)
        );

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);

        button.setGraphic(imageView);
    }
}
