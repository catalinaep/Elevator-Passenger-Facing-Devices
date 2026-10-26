package Panel;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.IntConsumer;

public class ElevatorPanelGUI {
    public enum Direction {
        IDLE("Idle"),
        UP("Going up"),
        DOWN("Going down");

        private final String displayText;

        Direction(String displayText) { this.displayText = displayText; }
        public String getDisplayText() { return displayText; }

        /** Converts controller text into a Direction. */
        public static Direction fromText(String value) {
            if (value == null) throw new IllegalArgumentException("Direction cannot be null.");

            return switch (value.trim().toLowerCase(Locale.ROOT)) {
                case "up", "going up" -> UP;
                case "down", "going down" -> DOWN;
                case "idle", "stopped", "stop", "" -> IDLE;
                default -> throw new IllegalArgumentException(
                        "Direction must be UP, DOWN, or IDLE: " + value
                );
            };
        }
    }

    public static final int MIN_FLOOR = 1;
    public static final int MAX_FLOOR = 10;
    static final String UP_ARROW_OFF_IMAGE_PATH = "/arrow-up-off.png";
    static final String UP_ARROW_ON_IMAGE_PATH = "/arrow-up-on.png";
    static final String DOWN_ARROW_OFF_IMAGE_PATH = "/arrow-down-off.png";
    static final String DOWN_ARROW_ON_IMAGE_PATH = "/arrow-down-on.png";

    private static final String FLOOR_BUTTON_OFF_STYLE = """
            -fx-background-color: #f4f4f4;
            -fx-border-color: #232323;
            -fx-border-width: 1;
            -fx-font-size: 17px;
            -fx-font-weight: bold;
            -fx-text-fill: #111111;
            """;
    private static final String FLOOR_BUTTON_ON_STYLE = """
            -fx-background-color: #a8f52a;
            -fx-border-color: #386b00;
            -fx-border-width: 2;
            -fx-font-size: 17px;
            -fx-font-weight: bold;
            -fx-text-fill: #111111;
            -fx-effect: dropshadow(gaussian, #9dff00, 16, 0.65, 0, 0);
            """;
    private static final String ARROW_OFF_STYLE = """
            -fx-background-color: #f4f4f4;
            -fx-border-color: #232323;
            -fx-border-width: 1;
            -fx-font-size: 24px;
            -fx-font-weight: bold;
            -fx-text-fill: #111111;
            """;
    private static final String ARROW_ON_STYLE = """
            -fx-background-color: #a8f52a;
            -fx-border-color: #386b00;
            -fx-border-width: 2;
            -fx-font-size: 24px;
            -fx-font-weight: bold;
            -fx-text-fill: #111111;
            -fx-effect: dropshadow(gaussian, #9dff00, 18, 0.8, 0, 0);
            """;

    private static final String[] BUTTON_IDS = {null,
            "firstFloor", "secondFloor", "thirdFloor", "fourthFloor", "fifthFloor",
            "sixthFloor", "seventhFloor", "eighthFloor", "ninthFloor", "tenthFloor" };

    private final Label floorLabel = new Label("1");
    private final Label directionLabel = new Label(Direction.IDLE.getDisplayText());
    private final Button upIndicator;
    private final Button downIndicator;
    private final Map<Integer, Button> floorButtons = new LinkedHashMap<>();
    private final Map<String, Integer> floorsByButtonId = new LinkedHashMap<>();
    private final VBox view = new VBox(12);
    private IntConsumer buttonHandler;
    private Direction direction = Direction.IDLE;

    public ElevatorPanelGUI() {
        upIndicator = createDirectionIndicator("▲", UP_ARROW_OFF_IMAGE_PATH);
        downIndicator = createDirectionIndicator("▼", DOWN_ARROW_OFF_IMAGE_PATH);
        buildView();
        setDirection(Direction.IDLE);
    }

    private void buildView() {
        Label displayTitle = new Label("CURRENT FLOOR");
        displayTitle.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #4b4b4b;");

        floorLabel.setAlignment(Pos.CENTER);
        floorLabel.setMinSize(92, 92);
        floorLabel.setStyle("""
                -fx-background-color: #111111;
                -fx-border-color: #b7b7b7;
                -fx-border-width: 2;
                -fx-text-fill: #f7f7f7;
                -fx-font-size: 42px;
                -fx-font-family: "Monospaced";
                -fx-font-weight: bold;
                """);

        GridPane directionGrid = new GridPane();
        directionGrid.setHgap(2);
        directionGrid.setVgap(2);
        directionGrid.setAlignment(Pos.CENTER);
        directionGrid.add(floorLabel, 0, 0, 1, 2);
        directionGrid.add(upIndicator, 1, 0);
        directionGrid.add(downIndicator, 1, 1);

        directionLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #4b4b4b;");

        VBox displayBox = new VBox(4, displayTitle, directionGrid, directionLabel);
        displayBox.setAlignment(Pos.CENTER);

        GridPane floorButtonGrid = new GridPane();
        floorButtonGrid.setHgap(6);
        floorButtonGrid.setVgap(6);
        floorButtonGrid.setAlignment(Pos.CENTER);

        for (int floor = MIN_FLOOR; floor <= MAX_FLOOR; floor++) {
            Button button = createFloorButton(floor);
            floorButtons.put(floor, button);
            floorsByButtonId.put(getButtonIdAtFloor(floor), floor);

            int index = floor - MIN_FLOOR;
            floorButtonGrid.add(button, index % 2, index / 2);
        }

        Label buttonTitle = new Label("SELECT FLOOR");
        buttonTitle.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #4b4b4b;");

        view.setPadding(new Insets(18));
        view.setAlignment(Pos.CENTER);
        view.setStyle("-fx-background-color: #dedede; -fx-border-color: #676767; -fx-border-width: 1;");
        view.getChildren().addAll(displayBox, buttonTitle, floorButtonGrid);
    }

    private Button createFloorButton(int floor) {
        Button button = new Button(String.valueOf(floor));
        button.setMinSize(58, 48);
        button.setMaxSize(58, 48);
        button.setStyle(FLOOR_BUTTON_OFF_STYLE);
        button.setOnAction(event -> {
            if (buttonHandler != null) {
                buttonHandler.accept(floor);
            }
        });
        return button;
    }

    private Button createDirectionIndicator(String fallbackArrow, String imagePath) {
        Button button = new Button(fallbackArrow);
        button.setMinSize(54, 45);
        button.setMaxSize(54, 45);
        button.setFocusTraversable(false);
        button.setMouseTransparent(true);

        ImageView arrowImage = createImage(imagePath, 28, 28);
        if (arrowImage != null) {
            button.setText("");
            button.setGraphic(arrowImage);
        }
        return button;
    }

    public VBox getView() { return view;}

    public void setButtonHandler(IntConsumer buttonHandler) {
        this.buttonHandler = buttonHandler;
    }

    public void updateFloorDisplay(String floor) {
        if (floor == null || floor.isBlank()) {
            throw new IllegalArgumentException("Floor display cannot be blank.");
        }
        floorLabel.setText(floor.trim());
    }

    public void updateFloorDisplay(int floor) {
        checkFloor(floor);
        updateFloorDisplay(String.valueOf(floor));
    }

    public void updateDirDisplay(String directionText) {
        setDirection(Direction.fromText(directionText));
    }

    public void setDirection(Direction newDirection) {
        direction = Objects.requireNonNull(newDirection, "Direction cannot be null.");
        directionLabel.setText(direction.getDisplayText());
        upIndicator.setStyle(direction == Direction.UP ? ARROW_ON_STYLE : ARROW_OFF_STYLE);
        downIndicator.setStyle(direction == Direction.DOWN ? ARROW_ON_STYLE : ARROW_OFF_STYLE);
        setArrowGraphic(upIndicator, "▲",
                direction == Direction.UP ? UP_ARROW_ON_IMAGE_PATH : UP_ARROW_OFF_IMAGE_PATH);
        setArrowGraphic(downIndicator, "▼",
                direction == Direction.DOWN ? DOWN_ARROW_ON_IMAGE_PATH : DOWN_ARROW_OFF_IMAGE_PATH);
    }

    public Direction getDirection() { return direction; }

    public void setFloorButtonLit(int floor, boolean lit) {
        checkFloor(floor);
        floorButtons.get(floor).setStyle(lit ? FLOOR_BUTTON_ON_STYLE : FLOOR_BUTTON_OFF_STYLE);
    }

    public int getButtonIdAtFloor(String buttonId) { return floorsByButtonId.getOrDefault(buttonId, -1); }

    public String getButtonIdAtFloor(int floor) {
        checkFloor(floor);
        return BUTTON_IDS[floor];
    }

    private ImageView createImage(String imagePath, double width, double height) {
        if (imagePath == null || imagePath.isBlank()) {
            return null;
        }

        try (InputStream stream = getClass().getResourceAsStream(imagePath)) {
            if (stream == null) {
                throw new IllegalArgumentException("Could not find image at: " + imagePath);
            }

            ImageView imageView = new ImageView(new Image(stream));
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
            imageView.setPreserveRatio(true);
            return imageView;
        } catch (IOException exception) {
            throw new IllegalStateException("Could not close image stream: " + imagePath, exception);
        }
    }

    private void setArrowGraphic(Button button, String fallbackArrow, String imagePath) {
        ImageView arrowImage = createImage(imagePath, 28, 28);
        if (arrowImage == null) {
            button.setText(fallbackArrow);
            button.setGraphic(null);
        } else {
            button.setText("");
            button.setGraphic(arrowImage);
        }
    }

    private static void checkFloor(int floor) {
        if (floor < MIN_FLOOR || floor > MAX_FLOOR) {
            throw new IllegalArgumentException(
                    "Floor number must be between " + MIN_FLOOR + " and " + MAX_FLOOR + "."
            );
        }
    }
}