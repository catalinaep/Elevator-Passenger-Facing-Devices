package Panel;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ElevatorPanelGUI {
    private final Label floorLabel = new Label("Floor: --");
    private final Label directionLabel = new Label("Idle");
    private final Map<String, Button> buttons = new HashMap<>();
    private Consumer<String> buttonHandler;

    public ElevatorPanelGUI() {
        addFloorButton("firstFloor", "/buttons-grey/1.png");
        addFloorButton("secondFloor", "/buttons-grey/2.png");
        addFloorButton("thirdFloor", "/buttons-grey/3.png");
        addFloorButton("fourthFloor", "/buttons-grey/4.png");
        addFloorButton("fifthFloor", "/buttons-grey/5.png");
        addFloorButton("sixthFloor", "/buttons-grey/6.png");
        addFloorButton("seventhFloor", "/buttons-grey/7.png");
        addFloorButton("eighthFloor", "/buttons-grey/8.png");
        addFloorButton("ninthFloor", "/buttons-grey/9.png");
        addFloorButton("tenthFloor", "/buttons-grey/10.png");
    }

    public VBox getView() {
        VBox layout = new VBox(10);
        layout.getChildren().addAll(floorLabel, directionLabel);

        for (int i = 1; i <= 10; i++) {
            layout.getChildren().add(buttons.get(getButtonIdAtFloor(i)));
        }

        return layout;
    }

    public void setButtonHandler(Consumer<String> buttonHandler) {
        this.buttonHandler = buttonHandler;
    }

    public void updateFloorDisplay(String floor) {
        floorLabel.setText("Floor: " + floor);
    }

    public void updateDirDisplay(String direction) {
        directionLabel.setText(direction);
    }

    public void changeButtonImage(String buttonId, String imagePath) {
        Button button = buttons.get(buttonId);
        if (button == null) {
            throw new IllegalArgumentException("No button found with ID: " + buttonId);
        }
        button.setGraphic(createImage(imagePath));
    }

    public int getButtonIdAtFloor(String buttonId) {
        return switch (buttonId) {
            case "firstFloor" -> 1;
            case "secondFloor" -> 2;
            case "thirdFloor" -> 3;
            case "fourthFloor" -> 4;
            case "fifthFloor" -> 5;
            case "sixthFloor" -> 6;
            case "seventhFloor" -> 7;
            case "eighthFloor" -> 8;
            case "ninthFloor" -> 9;
            case "tenthFloor" -> 10;
            default -> -1;
        };
    }

    public String getButtonIdAtFloor(int floor) {
        return switch (floor) {
            case 1 -> "firstFloor";
            case 2 -> "secondFloor";
            case 3 -> "thirdFloor";
            case 4 -> "fourthFloor";
            case 5 -> "fifthFloor";
            case 6 -> "sixthFloor";
            case 7 -> "seventhFloor";
            case 8 -> "eighthFloor";
            case 9 -> "ninthFloor";
            case 10 -> "tenthFloor";
            default -> throw new IllegalArgumentException(
                    "Floor number must be between 1 and 10."
            );
        };
    }

    private void addFloorButton(String buttonId, String imagePath) {
        Button button = new Button();
        button.setGraphic(createImage(imagePath));
        button.setOnAction(e -> {
            if (buttonHandler != null) buttonHandler.accept(buttonId);
        });
        buttons.put(buttonId, button);
    }

    private ImageView createImage(String imagePath) {
        InputStream stream = getClass().getResourceAsStream(imagePath);
        if (stream == null) throw new IllegalArgumentException("Could not find image at: " + imagePath);

        ImageView imageView = new ImageView(new Image(stream));
        imageView.setFitWidth(24);
        imageView.setFitHeight(24);
        imageView.setPreserveRatio(true);
        return imageView;
    }
}