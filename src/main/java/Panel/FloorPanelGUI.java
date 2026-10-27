package Panel;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Makes the GUI components of a floor panel
 */
public class FloorPanelGUI {
    // styles for arrows
    private static final String UP_ARROW_OFF = "/arrow-up-off.png";
    private static final String UP_ARROW_ON = "/arrow-up-on.png";
    private static final String DOWN_ARROW_OFF = "/arrow-down-off.png";
    private static final String DOWN_ARROW_ON = "/arrow-down-on.png";
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

    private final VBox view = new VBox();
    private final Label title = new Label();
    private final Map<Integer, Label> elevatorFloorDisplays = new LinkedHashMap<>();
    private final Map<Integer, Label> elevatorDirectionDisplays = new LinkedHashMap<>();
    private final Button upButton = createArrowButton("▲", UP_ARROW_OFF);
    private final Button downButton = createArrowButton("▼", DOWN_ARROW_OFF);
    private final Button alarmButton = new Button("ALARM");
    private Consumer<Direction> directionRequestHandler;
    private Runnable alarmRequestHandler;

    public FloorPanelGUI() { buildView(); }

    /** set handlers */
    public void setDirectionRequestHandler(Consumer<Direction> handler) { directionRequestHandler = handler; }
    public void setAlarmRequestHandler(Runnable handler) { alarmRequestHandler = handler; }

    /** Get the full GUI for the floor panel */
    public VBox getView() { return view; }

    /** render the display of the panel */
    public void render(FloorPanel.floorState snapshot) {
        title.setText("FLOOR " + snapshot.floorNumber());
        setCallButtonAppearance(snapshot.requestedDirection());
        alarmButton.setStyle(snapshot.alarmOn() ? ALARM_ON_STYLE : ALARM_OFF_STYLE);
        alarmButton.setText(snapshot.alarmOn() ? "ALARM ON" : "ALARM");

        for (int elevator = 1; elevator <= FloorPanel.ELEVATOR_COUNT; elevator++) {
            FloorPanel.ElevatorStatus status = snapshot.elevatorStatuses().get(elevator);
            elevatorFloorDisplays.get(elevator).setText(status == null ? "—" : String.valueOf(status.currentFloor()));
            setStatusDirectionGraphic(elevatorDirectionDisplays.get(elevator),
                    status == null ? Direction.IDLE : status.direction());
        }
    }

    /** put together all the elements of the floor panel display */
    private void buildView() {
        GridPane grid = new GridPane();
        grid.setHgap(7);
        grid.setVgap(6);
        grid.setAlignment(Pos.CENTER);

        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        GridPane.setColumnSpan(title, 3);
        grid.add(title, 0, 0);

        Label statusTitle = sectionLabel("ELEVATOR STATUS");
        GridPane.setColumnSpan(statusTitle, 3);
        grid.add(statusTitle, 0, 1);
        grid.add(statusHeader("#"), 0, 2);
        grid.add(statusHeader("FLOOR"), 1, 2);
        grid.add(statusHeader("DIRECTION"), 2, 2);

        for (int elevator = 1; elevator <= FloorPanel.ELEVATOR_COUNT; elevator++) {
            int row = elevator + 2;
            Label elevatorFloor = statusCell("—");
            Label elevatorDirection = statusCell("");
            elevatorFloorDisplays.put(elevator, elevatorFloor);
            elevatorDirectionDisplays.put(elevator, elevatorDirection);
            grid.add(statusCell(String.valueOf(elevator)), 0, row);
            grid.add(elevatorFloor, 1, row);
            grid.add(elevatorDirection, 2, row);
        }

        Label callTitle = sectionLabel("CALL ELEVATOR");
        GridPane.setColumnSpan(callTitle, 3);
        grid.add(callTitle, 0, 7);
        HBox callButtons = new HBox(8, upButton, downButton);
        callButtons.setAlignment(Pos.CENTER);
        GridPane.setColumnSpan(callButtons, 3);
        grid.add(callButtons, 0, 8);

        alarmButton.setMinWidth(205);
        alarmButton.setOnAction(event -> {
            if (alarmRequestHandler != null) {
                alarmRequestHandler.run();
            }
        });
        GridPane.setColumnSpan(alarmButton, 3);
        grid.add(alarmButton, 0, 9);

        view.setPadding(new Insets(18));
        view.setAlignment(Pos.CENTER);
        view.setStyle("-fx-background-color: #dedede; -fx-border-color: #676767; -fx-border-width: 1;");
        view.getChildren().add(grid);
    }

    /**
     * Individual methods that create various elements of the GUI
     */
    private Button createArrowButton(String fallbackArrow, String imagePath) {
        Button button = new Button(fallbackArrow);
        button.setMinSize(58, 42);
        button.setMaxSize(58, 42);
        button.setStyle(ARROW_OFF_STYLE);
        createArrow(button, fallbackArrow, imagePath);
        button.setOnAction(event -> {
            Direction direction = button == upButton ? Direction.UP : Direction.DOWN;
            if (directionRequestHandler != null) {
                directionRequestHandler.accept(direction);
            }
        });
        return button;
    }
    private void setCallButtonAppearance(Direction direction) {
        setCallButtonGraphic(upButton, "▲", direction == Direction.UP, UP_ARROW_OFF, UP_ARROW_ON);
        setCallButtonGraphic(downButton, "▼", direction == Direction.DOWN, DOWN_ARROW_OFF, DOWN_ARROW_ON);
    }
    private void setCallButtonGraphic(Button button, String fallbackArrow, boolean selected, String offImage, String onImage) {
        button.setStyle(selected ? ARROW_ON_STYLE : ARROW_OFF_STYLE);
        createArrow(button, fallbackArrow, selected ? onImage : offImage);
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
    private void setStatusDirectionGraphic(Label label, Direction direction) {
        String imagePath = switch (direction) {
            case UP -> UP_ARROW_ON;
            case DOWN -> DOWN_ARROW_ON;
            case IDLE -> null;
        };
        label.setText("");
        label.setGraphic(createImage(imagePath, 22, 22));
        label.setStyle("-fx-border-color: #aaaaaa; -fx-border-width: 0 0 1 0; -fx-padding: 3 2 3 2;");
    }
    private void createArrow(Button button, String fallbackArrow, String imagePath) {
        ImageView image = createImage(imagePath, 28, 28);
        button.setText(image == null ? fallbackArrow : "");
        button.setGraphic(image);
    }

    /** create an image from an image path and specified size */
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
}