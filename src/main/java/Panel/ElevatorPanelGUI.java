package Panel;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

/**
 * The JavaFX view for the elevator panel, configures all of the visual components
 */
public class ElevatorPanelGUI {

    /**
     * The enum that holds information associated with an elevator direction
     */
    public enum Direction {
        IDLE("IDLE"),
        UP("UP"),
        DOWN("DOWN");
        private final String displayText;

        Direction(String displayText) {
            this.displayText = displayText;
        }

        // get the text display for that direction
        public String getDisplayText() { return displayText;}

        // get the direction it is heading or the status
        public static Direction fromText(String value) {
            if (value == null) throw new IllegalArgumentException("Direction cannot be null.");

            return switch (value.trim().toLowerCase(Locale.ROOT)) {
                case "up", "going up" -> UP;
                case "down", "going down" -> DOWN;
                case "idle", "stopped", "stop", "" -> IDLE;
                default -> throw new IllegalArgumentException("Direction must be UP, DOWN, or IDLE: " + value);
            };
        }
    }

    public static final int MIN_FLOOR = 1;
    public static final int MAX_FLOOR = 10;

    // file paths for arrows and buttons
    static final String UP_ARROW_OFF = "/arrow-up-off.png";
    static final String UP_ARROW_ON = "/arrow-up-on.png";
    static final String DOWN_ARROW_OFF = "/arrow-down-off.png";
    static final String DOWN_ARROW_ON = "/arrow-down-on.png";
    private static final String FLOOR_BUTTON_OFF = "/buttons-grey/%d.png";
    private static final String FLOOR_BUTTON_ON = "/buttons-green/%d-green.png";

    // styles for each type of arrow and button
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
    private static final String CONTROL_OFF_STYLE = """
            -fx-background-color: #e8e8e8;
            -fx-border-color: #444444;
            -fx-border-width: 1;
            -fx-font-size: 10px;
            -fx-font-weight: bold;
            -fx-text-fill: #222222;
            """;
    private static final String CONTROL_ON_STYLE = """
            -fx-background-color: #ffc75a;
            -fx-border-color: #8a5800;
            -fx-border-width: 2;
            -fx-font-size: 10px;
            -fx-font-weight: bold;
            -fx-text-fill: #222222;
            """;

    private static final String[] BUTTON_IDS = {null,
            "firstFloor", "secondFloor", "thirdFloor", "fourthFloor", "fifthFloor",
            "sixthFloor", "seventhFloor", "eighthFloor", "ninthFloor", "tenthFloor"};

    private final Label elevatorNumberLabel = new Label("ELEVATOR");
    private final Label floorLabel = new Label("1");
    private final Label directionLabel = new Label(Direction.IDLE.getDisplayText());
    private final Button upIndicator;
    private final Button downIndicator;
    private final ToggleButton keyButton = new ToggleButton("KEY");
    private final ToggleButton buzzerButton = new ToggleButton("BUZZER");
    private final Map<Integer, Button> floorButtons = new LinkedHashMap<>();
    private final Map<Integer, Boolean> floorButtonStatus = new LinkedHashMap<>();
    private final Map<String, Integer> floorsByButtonId = new LinkedHashMap<>();
    private final VBox view = new VBox();

    private IntConsumer buttonHandler;
    private Consumer<Boolean> keyHandler;
    private Consumer<Boolean> buzzerHandler;
    private Direction direction = Direction.IDLE;
    private int elevatorNumber;

    public ElevatorPanelGUI() {
        upIndicator = createDirectionIndicator(UP_ARROW_OFF);
        downIndicator = createDirectionIndicator(DOWN_ARROW_OFF);
        buildView();
        setDirection(Direction.IDLE);
        setKeyInserted(false);
        setBuzzerOn(false);
    }

    /**
     * Make the elevator panel display with all of its elements
     */
    private void buildView() {
        GridPane grid = new GridPane();
        grid.setHgap(6);
        grid.setVgap(6);
        grid.setAlignment(Pos.CENTER);

        // elevator number label
        elevatorNumberLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        GridPane.setColumnSpan(elevatorNumberLabel, 2);
        grid.add(elevatorNumberLabel, 0, 0);

        // floor number label
        Label displayTitle = sectionLabel("CURRENT FLOOR");
        GridPane.setColumnSpan(displayTitle, 2);
        grid.add(displayTitle, 0, 1);

        // actual floor number display
        floorLabel.setAlignment(Pos.CENTER);
        floorLabel.setMinSize(122, 92);
        floorLabel.setMaxSize(122, 92);
        floorLabel.setStyle("""
                -fx-background-color: #111111;
                -fx-border-color: #b7b7b7;
                -fx-border-width: 2;
                -fx-text-fill: #f7f7f7;
                -fx-font-size: 42px;
                -fx-font-family: "Monospaced";
                -fx-font-weight: bold;
                """);
        GridPane.setColumnSpan(floorLabel, 2);
        grid.add(floorLabel, 0, 2);

        // direction label style and grid
        directionLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #4b4b4b;");
        GridPane.setColumnSpan(directionLabel, 2);
        grid.add(directionLabel, 0, 3);
        grid.add(upIndicator, 0, 4);
        grid.add(downIndicator, 1, 4);

        // title for the buttons
        Label buttonTitle = sectionLabel("SELECT FLOOR");
        GridPane.setColumnSpan(buttonTitle, 2);
        grid.add(buttonTitle, 0, 5);

        // loop to place all of the floor change buttons
        for (int floor = MIN_FLOOR; floor <= MAX_FLOOR; floor++) {
            Button button = createFloorButton(floor);
            floorButtons.put(floor, button);
            floorButtonStatus.put(floor, false);
            floorsByButtonId.put(getButtonIdAtFloor(floor), floor);

            int index = floor - MIN_FLOOR;
            grid.add(button, index % 2, 6 + index / 2);
        }

        // set key and buzzer buttons up
        configureSmallControl(keyButton);
        configureSmallControl(buzzerButton);
        keyButton.setOnAction(event -> {
            applyControlStyle(keyButton);
            if (keyHandler != null) { keyHandler.accept(keyButton.isSelected()); }
        });
        buzzerButton.setOnAction(event -> {
            applyControlStyle(buzzerButton);
            if (buzzerHandler != null) {
                buzzerHandler.accept(buzzerButton.isSelected());
            }
        });
        grid.add(keyButton, 0, 11);
        grid.add(buzzerButton, 1, 11);

        view.setPadding(new Insets(18));
        view.setAlignment(Pos.CENTER);
        view.setStyle("-fx-background-color: #dedede; -fx-border-color: #676767; -fx-border-width: 1;");
        view.getChildren().add(grid);
    }

    // make a label for one section
    private static Label sectionLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #4b4b4b;");
        return label;
    }

    // make one button for a floor with the floor number
    private Button createFloorButton(int floor) {
        Button button = new Button();
        button.setMinSize(58, 48);
        button.setMaxSize(58, 48);
        button.setStyle(FLOOR_BUTTON_OFF_STYLE);
        setFloorButtonGraphic(button, floor, false);
        button.setOnAction(event -> {
            if (buttonHandler != null) {
                buttonHandler.accept(floor);
            }
        });
        return button;
    }

    // make the button that shows the direction
    private Button createDirectionIndicator(String imagePath) {
        Button button = new Button();
        button.setMinSize(58, 40);
        button.setMaxSize(58, 40);
        button.setFocusTraversable(false);
        button.setMouseTransparent(true);
        setArrowGraphic(button, imagePath);
        return button;
    }

    // make the style for a small button
    private static void configureSmallControl(ToggleButton button) {
        button.setMinSize(58, 30);
        button.setMaxSize(58, 30);
        button.setFocusTraversable(false);
    }

    // toggle whether a button is on or off
    private static void applyControlStyle(ToggleButton button) {
        button.setStyle(button.isSelected() ? CONTROL_ON_STYLE : CONTROL_OFF_STYLE);
    }

    // panel GUI info
    public VBox getView() { return view; }
    public int getElevatorNumber() { return elevatorNumber; }

    // set the label for the elevator number
    public void setElevatorNumber(int elevatorNumber) {
        if (elevatorNumber < 1) throw new IllegalArgumentException("Elevator number must be positive.");
        this.elevatorNumber = elevatorNumber;
        elevatorNumberLabel.setText("ELEVATOR " + elevatorNumber);
    }

    /** setters for GUI handlers */
    public void setButtonHandler(IntConsumer buttonHandler) { this.buttonHandler = buttonHandler; }
    public void setKeyHandler(Consumer<Boolean> keyHandler) { this.keyHandler = keyHandler; }
    public void setBuzzerHandler(Consumer<Boolean> buzzerHandler) {this.buzzerHandler = buzzerHandler; }

    /** update the display of what floor we're on, either string or int input */
    public void updateFloorDisplay(String floor) {
        if (floor == null || floor.isBlank()) {
            throw new IllegalArgumentException("Floor display text cannot be blank.");
        }
        floorLabel.setText(floor.trim());
    }
    public void updateFloorDisplay(int floor) {
        checkFloor(floor);
        updateFloorDisplay(String.valueOf(floor));
    }

    /** One direction arrow glows while moving */
    public void setDirection(Direction newDirection) {
        direction = Objects.requireNonNull(newDirection, "Direction cannot be null.");
        directionLabel.setText(direction.getDisplayText());
        upIndicator.setStyle(direction == Direction.UP ? ARROW_ON_STYLE : ARROW_OFF_STYLE);
        downIndicator.setStyle(direction == Direction.DOWN ? ARROW_ON_STYLE : ARROW_OFF_STYLE);
        setArrowGraphic(upIndicator, direction == Direction.UP ? UP_ARROW_ON : UP_ARROW_OFF);
        setArrowGraphic(downIndicator, direction == Direction.DOWN ? DOWN_ARROW_ON : DOWN_ARROW_OFF);
    }

    // update direction
    public void updateDirDisplay(String directionText) { setDirection(Direction.fromText(directionText)); }

    // get direction
    public Direction getDirection() { return direction; }

    /** set if floor button is on/off */
    public void setFloorButtonStatus(int floor, boolean status) {
        checkFloor(floor);
        floorButtonStatus.put(floor, status);
        floorButtons.get(floor).setStyle(status ? FLOOR_BUTTON_ON_STYLE : FLOOR_BUTTON_OFF_STYLE);
        setFloorButtonGraphic(floorButtons.get(floor), floor, status);
    }

    // check a floor button status of on or off
    public boolean floorButtonStaus(int floor) {
        checkFloor(floor);
        return floorButtonStatus.get(floor);
    }

    // set key inserted as true/false
    public void setKeyInserted(boolean inserted) {
        keyButton.setSelected(inserted);
        applyControlStyle(keyButton);
    }

    // check if key is inserted
    public boolean isKeyInserted() {
        return keyButton.isSelected();
    }

    /** set the buzzer on and check its status and the button id at a floor */
    public void setBuzzerOn(boolean on) {
        buzzerButton.setSelected(on);
        applyControlStyle(buzzerButton);
    }
    public boolean isBuzzerOn() { return buzzerButton.isSelected();}

    /** get the button id at a floor with different parameters */
    public int getButtonIdAtFloor(String buttonId) { return floorsByButtonId.getOrDefault(buttonId, -1);}
    public String getButtonIdAtFloor(int floor) {
        checkFloor(floor);
        return BUTTON_IDS[floor];
    }

    /**
     * Make an image from a file path and the width and height
     */
    private ImageView createImage(String imagePath, double width, double height) {
        if (imagePath == null || imagePath.isBlank()) return null;

        try (InputStream stream = getClass().getResourceAsStream(imagePath)) {
            if (stream == null) return null;
            ImageView imageView = new ImageView(new Image(stream));
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
            imageView.setPreserveRatio(true);
            return imageView;
        } catch (IOException | RuntimeException exception) {
            return null;
        }
    }

    /** set the image for an arrow display */
    private void setArrowGraphic(Button button, String imagePath) {
        ImageView arrowImage = createImage(imagePath, 28, 28);
        button.setText("");
        button.setGraphic(arrowImage);
    }

    /** set the image for a floor button */
    private void setFloorButtonGraphic(Button button, int floor, boolean lit) {
        String imagePath = lit ? FLOOR_BUTTON_ON.formatted(floor) : FLOOR_BUTTON_OFF.formatted(floor);
        button.setText("");
        button.setGraphic(createImage(imagePath, 46, 36));
    }

    /** check if a floor number is valid */
    private static void checkFloor(int floor) {
        if (floor < MIN_FLOOR || floor > MAX_FLOOR) {
            throw new IllegalArgumentException("Floor number must be between " + MIN_FLOOR + " and " + MAX_FLOOR + ".");
        }
    }
}