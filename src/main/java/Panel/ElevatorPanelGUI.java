package Panel;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.InputStream;

public class ElevatorPanelGUI {



    private final Label floorLabel = new Label("Floor: --");
    private final Label directionLabel = new Label("Idle");

    private final Button firstFloor = createButton("/buttons-grey/1.png");
    private final Button secondFloor = createButton("/buttons-grey/2.png");
    private final Button thirdFloor = createButton("/buttons-grey/3.png");
    private final Button fourthFloor = createButton("/buttons-grey/4.png");
    private final Button fifthFloor = createButton("/buttons-grey/5.png");
    private final Button sixthFloor = createButton("/buttons-grey/6.png");
    private final Button seventhFloor = createButton("/buttons-grey/7.png");
    private final Button eighthFloor = createButton("/buttons-grey/8.png");
    private final Button ninthFloor = createButton("/buttons-grey/9.png");
    private final Button tenthFloor = createButton("/buttons-grey/10.png");



    private final Button downButton = new Button("Down");

    public VBox getView() {
        VBox layout = new VBox(10,
                floorLabel,
                directionLabel,
                firstFloor,
                secondFloor,
                thirdFloor,
                fourthFloor,
                fifthFloor,
                sixthFloor,
                seventhFloor,
                eighthFloor,
                ninthFloor,
                tenthFloor
        );
        return layout;
    }

    public void updateFloorDisplay(String floor) {
        floorLabel.setText("Floor: " + floor);
    }

    public void updateDirDisplay(String direction) {
        directionLabel.setText(direction);
    }

    private Button createButton(String imagePath) {
        InputStream stream = getClass().getResourceAsStream(imagePath);

        if (stream == null) {
            throw new IllegalArgumentException(
                    "Could not find image: " + imagePath
            );
        }

        Image image = new Image(stream);
        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(24);
        imageView.setFitHeight(24);
        imageView.setPreserveRatio(true);

        Button button = new Button();
        button.setGraphic(imageView);

        return button;
    }
}
