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
 * JavaFX landing panel for one floor. The panel shows four elevator-status
 * rows above the call buttons, while retaining state for its own calls/alarm.
 */
public class FloorPanel {
    public static final int ELEVATOR_COUNT = 4;

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
    private Button upButton;
    private Button downButton;
    private Button alarmButton;

    public FloorPanel(String floorNumber) {
        if (floorNumber == null || floorNumber.isBlank()) {
            throw new IllegalArgumentException("Floor number cannot be blank.");
        }

        this.floorNumber = floorNumber.trim();
        createGUIComponents();
    }

    public FloorPanel() {
        this("1");
    }

    public boolean upBtnPressed() {
        return up.get();
    }

    public boolean downBtnPressed() {
        return down.get();
    }

    public void resetUpBtn() {
        up.set(false);
        updateArrowStyles();
    }

    public void resetDownBtn() {
        down.set(false);
        updateArrowStyles();
    }

    public boolean alarmPressed() {
        return alarmOn.get();
    }

    public void resetAlarm() {
        setAlarmOn(false);
    }

    /** The landing alarm acts as an on/off toggle for this particular floor. */
    public void toggleAlarm() {
        setAlarmOn(!alarmOn.get());
    }

    public void setAlarmOn(boolean on) {
        alarmOn.set(on);
        updateAlarmStyle();
        if (alarmHandler != null) {
            alarmHandler.accept(floorNumber, on);
        }
    }

    /** One-time arrival sound; the outside panel has no sustained buzzer. */
    public void ding() {
        SoundEffects.playDing();
    }

    /** Changes the lit call-arrow state. IDLE clears both arrow buttons. */
    public void updateDirection(ElevatorPanelGUI.Direction direction) {
        Objects.requireNonNull(direction, "Direction cannot be null.");
        up.set(direction == ElevatorPanelGUI.Direction.UP);
        down.set(direction == ElevatorPanelGUI.Direction.DOWN);
        updateArrowStyles();
    }

    public void updateDirection(String directionText) {
        updateDirection(ElevatorPanelGUI.Direction.fromText(directionText));
    }

    /**
     * Updates one row in the four-elevator status board. This is separate from
     * the landing call buttons because a call direction is not the same thing
     * as the direction in which every elevator is currently travelling.
     */
    public void updateElevatorStatus(
            int elevatorNumber,
            int currentFloor,
            ElevatorPanelGUI.Direction direction
    ) {
        checkElevatorNumber(elevatorNumber);
        checkFloor(currentFloor);
        Objects.requireNonNull(direction, "Direction cannot be null.");

        elevatorFloorDisplays.get(elevatorNumber).setText(String.valueOf(currentFloor));
        setStatusDirectionGraphic(elevatorDirectionDisplays.get(elevatorNumber), direction);
        elevatorDirections.put(elevatorNumber, direction);
    }

    public ElevatorPanelGUI.Direction getElevatorDirection(int elevatorNumber) {
        checkElevatorNumber(elevatorNumber);
        return elevatorDirections.get(elevatorNumber);
    }

    public String getElevatorFloorDisplay(int elevatorNumber) {
        checkElevatorNumber(elevatorNumber);
        return elevatorFloorDisplays.get(elevatorNumber).getText();
    }

    public void setDirectionRequestHandler(Consumer<ElevatorPanelGUI.Direction> handler) {
        directionRequestHandler = handler;
    }

    /** Receives this panel's floor number and the new alarm state. */
    public void setAlarmHandler(BiConsumer<String, Boolean> handler) {
        alarmHandler = handler;
    }

    private void createGUIComponents() {
        GridPane grid = new GridPane();
        grid.setHgap(7);
        grid.setVgap(6);
        grid.setAlignment(Pos.CENTER);

        Label title = new Label("FLOOR " + floorNumber);
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        GridPane.setColumnSpan(title, 3);
        grid.add(title, 0, 0);

        Label statusTitle = sectionLabel("ELEVATOR STATUS");
        GridPane.setColumnSpan(statusTitle, 3);
        grid.add(statusTitle, 0, 1);
        grid.add(statusHeader("#"), 0, 2);
        grid.add(statusHeader("FLOOR"), 1, 2);
        grid.add(statusHeader("DIRECTION"), 2, 2);

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

        Label callTitle = sectionLabel("CALL ELEVATOR");
        GridPane.setColumnSpan(callTitle, 3);
        grid.add(callTitle, 0, 7);

        Label floorDisplay = new Label(floorNumber);
        floorDisplay.setAlignment(Pos.CENTER);
        floorDisplay.setMinSize(130, 92);
        floorDisplay.setMaxSize(130, 92);
        floorDisplay.setStyle("""
                -fx-background-color: #111111;
                -fx-border-color: #b7b7b7;
                -fx-border-width: 2;
                -fx-text-fill: #f7f7f7;
                -fx-font-size: 42px;
                -fx-font-family: "Monospaced";
                -fx-font-weight: bold;
                """);
        GridPane.setColumnSpan(floorDisplay, 2);
        GridPane.setRowSpan(floorDisplay, 2);
        grid.add(floorDisplay, 0, 8);

        upButton = createArrowButton(ElevatorPanelGUI.UP_ARROW_OFF_IMAGE_PATH);
        downButton = createArrowButton(ElevatorPanelGUI.DOWN_ARROW_OFF_IMAGE_PATH);
        upButton.setOnAction(event -> requestDirection(ElevatorPanelGUI.Direction.UP));
        downButton.setOnAction(event -> requestDirection(ElevatorPanelGUI.Direction.DOWN));
        grid.add(upButton, 2, 8);
        grid.add(downButton, 2, 9);

        alarmButton = new Button("ALARM");
        alarmButton.setMinWidth(205);
        alarmButton.setOnAction(event -> toggleAlarm());
        GridPane.setColumnSpan(alarmButton, 3);
        grid.add(alarmButton, 0, 10);

        components.setPadding(new Insets(18));
        components.setAlignment(Pos.CENTER);
        components.setStyle("-fx-background-color: #dedede; -fx-border-color: #676767; -fx-border-width: 1;");
        components.getChildren().add(grid);
        updateArrowStyles();
        updateAlarmStyle();
    }

    private static Label sectionLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #4b4b4b;");
        return label;
    }

    private static Label statusHeader(String text) {
        Label label = new Label(text);
        label.setMinWidth(62);
        label.setAlignment(Pos.CENTER);
        label.setStyle("-fx-font-size: 9px; -fx-font-weight: bold; -fx-text-fill: #555555;");
        return label;
    }

    private static Label statusCell(String text) {
        Label label = new Label(text);
        label.setMinWidth(62);
        label.setAlignment(Pos.CENTER);
        label.setStyle("-fx-border-color: #aaaaaa; -fx-border-width: 0 0 1 0; -fx-padding: 3 2 3 2;");
        return label;
    }

    private Button createArrowButton(String imagePath) {
        Button button = new Button();
        button.setMinSize(58, 42);
        button.setMaxSize(58, 42);
        setArrowGraphic(button, imagePath);
        return button;
    }

    private void requestDirection(ElevatorPanelGUI.Direction direction) {
        updateDirection(direction);
        if (directionRequestHandler != null) {
            directionRequestHandler.accept(direction);
        }
    }

    private void updateArrowStyles() {
        upButton.setStyle(up.get() ? ARROW_ON_STYLE : ARROW_OFF_STYLE);
        downButton.setStyle(down.get() ? ARROW_ON_STYLE : ARROW_OFF_STYLE);
        setArrowGraphic(upButton,
                up.get() ? ElevatorPanelGUI.UP_ARROW_ON_IMAGE_PATH
                        : ElevatorPanelGUI.UP_ARROW_OFF_IMAGE_PATH);
        setArrowGraphic(downButton,
                down.get() ? ElevatorPanelGUI.DOWN_ARROW_ON_IMAGE_PATH
                        : ElevatorPanelGUI.DOWN_ARROW_OFF_IMAGE_PATH);
    }

    private void updateAlarmStyle() {
        alarmButton.setStyle(alarmOn.get() ? ALARM_ON_STYLE : ALARM_OFF_STYLE);
        alarmButton.setText(alarmOn.get() ? "ALARM ON" : "ALARM");
    }

    private void setStatusDirectionGraphic(Label label, ElevatorPanelGUI.Direction direction) {
        String imagePath = switch (direction) {
            case UP -> ElevatorPanelGUI.UP_ARROW_ON_IMAGE_PATH;
            case DOWN -> ElevatorPanelGUI.DOWN_ARROW_ON_IMAGE_PATH;
            case IDLE -> null;
        };
        label.setText("");
        label.setGraphic(createImage(imagePath, 22, 22));
        label.setStyle("-fx-border-color: #aaaaaa; -fx-border-width: 0 0 1 0; -fx-padding: 3 2 3 2;");
    }

    private ImageView createImage(String imagePath, double width, double height) {
        if (imagePath == null || imagePath.isBlank()) {
            return null;
        }

        try (InputStream stream = getClass().getResourceAsStream(imagePath)) {
            if (stream == null) {
                return null;
            }

            ImageView imageView = new ImageView(new Image(stream));
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
            imageView.setPreserveRatio(true);
            return imageView;
        } catch (IOException | RuntimeException exception) {
            return null;
        }
    }

    private void setArrowGraphic(Button button, String imagePath) {
        ImageView arrowImage = createImage(imagePath, 28, 28);
        button.setText("");
        button.setGraphic(arrowImage);
    }

    public String getFloorNumber() {
        return floorNumber;
    }

    public VBox getComponents() {
        return components;
    }

    private static void checkFloor(int floor) {
        if (floor < ElevatorPanelGUI.MIN_FLOOR || floor > ElevatorPanelGUI.MAX_FLOOR) {
            throw new IllegalArgumentException(
                    "Floor number must be between " + ElevatorPanelGUI.MIN_FLOOR
                            + " and " + ElevatorPanelGUI.MAX_FLOOR + "."
            );
        }
    }

    private static void checkElevatorNumber(int elevatorNumber) {
        if (elevatorNumber < 1 || elevatorNumber > ELEVATOR_COUNT) {
            throw new IllegalArgumentException(
                    "Elevator number must be between 1 and " + ELEVATOR_COUNT + "."
            );
        }
    }
}