package Panel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * The panel for one floor that shows the status of the 4 elevators
 */
public class FloorPanel {
    public static final int ELEVATOR_COUNT = 4;

    // set the styles of all the arrows for the GUI
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
    private static final String ALARM_OFF_STYLE = """
            -fx-background-color: #d94747;
            -fx-border-color: #8d2222;
            -fx-border-width: 1;
            -fx-text-fill: white;
            -fx-font-size: 12px;
            -fx-font-weight: bold;
            """;
    private static final String ALARM_ON_STYLE = """
            -fx-background-color: #ff8d1a;
            -fx-border-color: #8a4100;
            -fx-border-width: 2;
            -fx-text-fill: #111111;
            -fx-font-size: 12px;
            -fx-font-weight: bold;
            -fx-effect: dropshadow(gaussian, #ff8d1a, 14, 0.75, 0, 0);
            """;

    private final String floorNumber;
    private final BooleanProperty alarmOn = new SimpleBooleanProperty(false);
    private final BooleanProperty up = new SimpleBooleanProperty(false);
    private final BooleanProperty down = new SimpleBooleanProperty(false);
    private final VBox components = new VBox();
    private final Map<Integer, Label> elevatorFloorDisplays = new LinkedHashMap<>();
    private final Map<Integer, Label> elevatorDirectionDisplays = new LinkedHashMap<>();
    private final Map<Integer, ElevatorPanelGUI.Direction> elevatorDirections = new LinkedHashMap<>();

    private Consumer<ElevatorPanelGUI.Direction> directionRequestHandler;
    private BiConsumer<String, Boolean> alarmHandler;
    private Button alarmButton;

    public FloorPanel(String floorNumber) {
        if (floorNumber == null || floorNumber.isBlank()) {
            throw new IllegalArgumentException("Floor number cannot be blank.");
        }
        this.floorNumber = floorNumber.trim();
        createGUIComponents();
    }

    /** change and get alarm status for various needs*/
    // check if alarm is pressed
    public boolean alarmPressed() { return alarmOn.get(); }
    // reset the alarm to off
    public void resetAlarm() { setAlarmOn(false); }
    // switch the status of the alarm to opposite state
    public void toggleAlarm() { setAlarmOn(!alarmOn.get()); }
    // turn the alarm on
    public void setAlarmOn(boolean on) {
        alarmOn.set(on);
        updateAlarmStyle();
        if (alarmHandler != null) {
            alarmHandler.accept(floorNumber, on);
        }
    }

    /** play the arrival sound */
    public void ding() { SoundEffects.playDing(); }

    /** Changes the lit call-arrow state. IDLE clears both arrow buttons. */
    public void updateDirection(ElevatorPanelGUI.Direction direction) {
        Objects.requireNonNull(direction, "Direction cannot be null.");
        up.set(direction == ElevatorPanelGUI.Direction.UP);
        down.set(direction == ElevatorPanelGUI.Direction.DOWN);
    }

    // update the direction display
    public void updateDirection(String directionText) {
        updateDirection(ElevatorPanelGUI.Direction.fromText(directionText));
    }

    /**
     * Update the status for one elevator in the floor panel display
     * Needs the number of the elevator, the floor it is on, and the direction it is going
     */
    public void updateElevatorStatus(int elevatorNumber, int currentFloor, ElevatorPanelGUI.Direction direction) {
        checkElevatorNumber(elevatorNumber);
        checkFloor(currentFloor);
        Objects.requireNonNull(direction, "Direction cannot be null.");
        elevatorFloorDisplays.get(elevatorNumber).setText(String.valueOf(currentFloor));
        setStatusDirectionGraphic(elevatorDirectionDisplays.get(elevatorNumber), direction);
        elevatorDirections.put(elevatorNumber, direction);
    }

    // get the direction of travel for an elevator number
    public ElevatorPanelGUI.Direction getElevatorDirection(int elevatorNumber) {
        checkElevatorNumber(elevatorNumber);
        return elevatorDirections.get(elevatorNumber);
    }

    // the handler takes care of changing directions
    public void setDirectionRequestHandler(Consumer<ElevatorPanelGUI.Direction> handler) {
        directionRequestHandler = handler;
    }

    /** gets the floor number for this panel and the alarm state */
    public void setAlarmHandler(BiConsumer<String, Boolean> handler) { alarmHandler = handler; }

    /**
     * Make all the components of the GUI for the floor panel specifically
     */
    private void createGUIComponents() {
        GridPane grid = new GridPane();
        grid.setHgap(7);
        grid.setVgap(6);
        grid.setAlignment(Pos.CENTER);

        // adjust the title
        Label title = new Label("FLOOR " + floorNumber);
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        GridPane.setColumnSpan(title, 3);
        grid.add(title, 0, 0);

        // adjust the status title
        Label statusTitle = sectionLabel("ELEVATOR STATUS");
        GridPane.setColumnSpan(statusTitle, 3);
        grid.add(statusTitle, 0, 1);
        grid.add(statusHeader("#"), 0, 2);
        grid.add(statusHeader("FLOOR"), 1, 2);
        grid.add(statusHeader("DIRECTION"), 2, 2);

        // set the elevator information for each individual elevator
        for (int elevator = 1; elevator <= ELEVATOR_COUNT; elevator++) {
            int row = elevator + 2;
            Label elevatorLabel = statusCell(String.valueOf(elevator));
            Label elevatorFloor = statusCell("—");
            Label elevatorDirection = statusCell("");
            setStatusDirectionGraphic(elevatorDirection, ElevatorPanelGUI.Direction.IDLE);

            elevatorFloorDisplays.put(elevator, elevatorFloor);
            elevatorDirectionDisplays.put(elevator, elevatorDirection);
            elevatorDirections.put(elevator, ElevatorPanelGUI.Direction.IDLE);

            grid.add(elevatorLabel, 0, row);
            grid.add(elevatorFloor, 1, row);
            grid.add(elevatorDirection, 2, row);
        }

        alarmButton = new Button("ALARM");
        alarmButton.setMinWidth(205);
        alarmButton.setOnAction(event -> toggleAlarm());
        GridPane.setColumnSpan(alarmButton, 3);
        grid.add(alarmButton, 0, 10);

        components.setPadding(new Insets(18));
        components.setAlignment(Pos.CENTER);
        components.setStyle("-fx-background-color: #dedede; -fx-border-color: #676767; -fx-border-width: 1;");
        components.getChildren().add(grid);
        updateAlarmStyle();
    }

    /** make the section display */
    private static Label sectionLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #4b4b4b;");
        return label;
    }

    /** make the status display */
    private static Label statusHeader(String text) {
        Label label = new Label(text);
        label.setMinWidth(62);
        label.setAlignment(Pos.CENTER);
        label.setStyle("-fx-font-size: 9px; -fx-font-weight: bold; -fx-text-fill: #555555;");
        return label;
    }

    /** make the status cell display */
    private static Label statusCell(String text) {
        Label label = new Label(text);
        label.setMinWidth(62);
        label.setAlignment(Pos.CENTER);
        label.setStyle("-fx-border-color: #aaaaaa; -fx-border-width: 0 0 1 0; -fx-padding: 3 2 3 2;");
        return label;
    }

    /** make the arrow button display */
    private Button createArrowButton(String imagePath) {
        Button button = new Button();
        button.setMinSize(58, 42);
        button.setMaxSize(58, 42);
        setArrowGraphic(button, imagePath);
        return button;
    }

    /** handle a direction request */
    private void requestDirection(ElevatorPanelGUI.Direction direction) {
        updateDirection(direction);
        if (directionRequestHandler != null) directionRequestHandler.accept(direction);
    }

    /** update the style of the arrow display to indicate status */
    private void updateAlarmStyle() {
        alarmButton.setStyle(alarmOn.get() ? ALARM_ON_STYLE : ALARM_OFF_STYLE);
        alarmButton.setText(alarmOn.get() ? "ALARM ON" : "ALARM");
    }

    /** find the correct arrow for the current direction on the GUI */
    private void setStatusDirectionGraphic(Label label, ElevatorPanelGUI.Direction direction) {
        String imagePath = switch (direction) {
            case UP -> ElevatorPanelGUI.UP_ARROW_ON;
            case DOWN -> ElevatorPanelGUI.DOWN_ARROW_ON;
            case IDLE -> null;
        };
        label.setText("");
        label.setGraphic(createImage(imagePath, 22, 22));
        label.setStyle("-fx-border-color: #aaaaaa; -fx-border-width: 0 0 1 0; -fx-padding: 3 2 3 2;");
    }

    /** create an image on the GUI given the file path and size of the image */
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

    /** set the image for the up and down on and off arrows */
    private void setArrowGraphic(Button button, String imagePath) {
        ImageView arrowImage = createImage(imagePath, 28, 28);
        button.setText("");
        button.setGraphic(arrowImage);
    }

    /** getters for floor display info */
    public String getFloorNumber() { return floorNumber; }
    public VBox getComponents() { return components; }

    /** ensure that a floor number is valid */
    private static void checkFloor(int floor) {
        if (floor < ElevatorPanelGUI.MIN_FLOOR || floor > ElevatorPanelGUI.MAX_FLOOR) {
            throw new IllegalArgumentException(
                    "Floor number must be between " + ElevatorPanelGUI.MIN_FLOOR
                            + " and " + ElevatorPanelGUI.MAX_FLOOR + "."
            );
        }
    }

    /** ensure that an elevator number is valid */
    private static void checkElevatorNumber(int elevatorNumber) {
        if (elevatorNumber < 1 || elevatorNumber > ELEVATOR_COUNT) {
            throw new IllegalArgumentException(
                    "Elevator number must be between 1 and " + ELEVATOR_COUNT + "."
            );
        }
    }
}