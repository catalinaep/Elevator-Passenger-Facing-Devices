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
import java.util.Objects;
import java.util.function.Consumer;

public class FloorPanel {
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

    private final String floorNumber;
    private static final BooleanProperty alarmOn = new SimpleBooleanProperty(false);
    private final BooleanProperty up = new SimpleBooleanProperty(false);
    private final BooleanProperty down = new SimpleBooleanProperty(false);
    private final VBox components = new VBox(10);
    private Consumer<ElevatorPanelGUI.Direction> directionRequestHandler;
    private Button upButton;
    private Button downButton;

    public FloorPanel(String floorNumber) {
        if (floorNumber == null || floorNumber.isBlank()) {
            throw new IllegalArgumentException("Floor number cannot be blank.");
        }

        this.floorNumber = floorNumber.trim();
        createGUIComponents();
    }

    public FloorPanel() { this("1"); }

    /**
     * Checks if the up button has been pressed at a given floor.
     * @return True if pressed, false otherwise
     */
    public boolean upBtnPressed() {
        return up.get();
    }

    /**
     * Checks if the down button has been pressed at a given floor.
     * @return True if pressed, false otherwise
     */
    public boolean downBtnPressed() {
        return down.get();
    }

    /**
     * Resets the up button boolean to default false value.
     */
    public void resetUpBtn() {
        up.set(false);
        updateArrowStyles();
    }

    /**
     * Resets the down button boolean to default false value.
     */
    public void resetDownBtn() {
        down.set(false);
        updateArrowStyles();
    }

    /**
     * Checks if the alarm button was pressed.
     * @return True if pressed, false otherwise
     */
    public static boolean alarmPressed() {
        return alarmOn.get();
    }

    /**
     * Resets to the default false state of the alarm button.
     */
    public void resetAlarm() {
        alarmOn.set(false);
    }

    /**
     * Plays a ding sound to alert passengers of elevator's arrival.
     */
    public void ding() {
        java.awt.Toolkit.getDefaultToolkit().beep();
    }

    private void updateDirection(ElevatorPanelGUI.Direction direction) {
        Objects.requireNonNull(direction, "Direction cannot be null.");
        up.set(direction == ElevatorPanelGUI.Direction.UP);
        down.set(direction == ElevatorPanelGUI.Direction.DOWN);
        updateArrowStyles();
    }

    private void updateDirection(String directionText) {
        updateDirection(ElevatorPanelGUI.Direction.fromText(directionText));
    }

    public void setDirectionRequestHandler(Consumer<ElevatorPanelGUI.Direction> handler) {
        directionRequestHandler = handler;
    }

    private void createGUIComponents() {
        Label title = new Label("FLOOR");
        title.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #4b4b4b;");

        Label floorDisplay = new Label(floorNumber);
        floorDisplay.setAlignment(Pos.CENTER);
        floorDisplay.setMinSize(70, 92);
        floorDisplay.setStyle("""
                -fx-background-color: #111111;
                -fx-border-color: #b7b7b7;
                -fx-border-width: 2;
                -fx-text-fill: #f7f7f7;
                -fx-font-size: 38px;
                -fx-font-family: "Monospaced";
                -fx-font-weight: bold;
                """);

        upButton = createArrowButton("▲", ElevatorPanelGUI.UP_ARROW_OFF_IMAGE_PATH);
        downButton = createArrowButton("▼", ElevatorPanelGUI.DOWN_ARROW_OFF_IMAGE_PATH);
        upButton.setOnAction(event -> requestDirection(ElevatorPanelGUI.Direction.UP));
        downButton.setOnAction(event -> requestDirection(ElevatorPanelGUI.Direction.DOWN));

        GridPane arrowGrid = new GridPane();
        arrowGrid.setHgap(2);
        arrowGrid.setVgap(2);
        arrowGrid.setAlignment(Pos.CENTER);
        arrowGrid.add(floorDisplay, 0, 0, 1, 2);
        arrowGrid.add(upButton, 1, 0);
        arrowGrid.add(downButton, 1, 1);

        Button alarmButton = new Button("ALARM");
        alarmButton.setMinWidth(126);
        alarmButton.setStyle("""
                -fx-background-color: #d94747;
                -fx-text-fill: white;
                -fx-font-size: 12px;
                -fx-font-weight: bold;
                """);
        alarmButton.setOnAction(event -> alarmOn.set(true));

        components.setPadding(new Insets(18));
        components.setAlignment(Pos.CENTER);
        components.setStyle("-fx-background-color: #dedede; -fx-border-color: #676767; -fx-border-width: 1;");
        components.getChildren().addAll(title, arrowGrid, alarmButton);
        updateArrowStyles();
    }

    private Button createArrowButton(String fallbackArrow, String imagePath) {
        Button button = new Button(fallbackArrow);
        button.setMinSize(54, 45);
        button.setMaxSize(54, 45);

        setArrowGraphic(button, fallbackArrow, imagePath);
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
        setArrowGraphic(upButton, "▲",
                up.get() ? ElevatorPanelGUI.UP_ARROW_ON_IMAGE_PATH
                        : ElevatorPanelGUI.UP_ARROW_OFF_IMAGE_PATH);
        setArrowGraphic(downButton, "▼",
                down.get() ? ElevatorPanelGUI.DOWN_ARROW_ON_IMAGE_PATH
                        : ElevatorPanelGUI.DOWN_ARROW_OFF_IMAGE_PATH);
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

    public String getFloorNumber() {
        return floorNumber;
    }

    public VBox getComponents() {
        return components;
    }
}