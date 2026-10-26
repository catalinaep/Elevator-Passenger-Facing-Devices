package Panel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import java.net.URL;

public class FloorPanel {

    private final URL DING = getClass().getResource("/ding.mp3");

    private final String floorNumber;
    private static final BooleanProperty alarmOn = new SimpleBooleanProperty(false);
    private final BooleanProperty up = new SimpleBooleanProperty(false);
    private final BooleanProperty down = new SimpleBooleanProperty(false);
    private final VBox components = new VBox(12);

    public FloorPanel(String floorNumber) {
        this.floorNumber = floorNumber;
        createGUIComponents();
    }

    public FloorPanel() {
        this.floorNumber = "1";
    }


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
    }

    /**
     * Resets the down button boolean to default false value.
     */
    public void resetDownBtn() {
        down.set(false);
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

    private void createGUIComponents() {
        //TODO style and add elevator number
        //create up, down, and alarm buttons
        Button upBtn= new Button("up");
        upBtn.setOnAction(event -> {
            if (!down.getValue()) {
                up.set(true);
            }
            System.out.println("up button pressed");
        });

        Button downBtn = new Button("down");
        downBtn.setOnAction(event -> {
            if (!up.get()) {
                down.set(true);
            }
            System.out.println("down button pressed");
        });

        Button alarmBtn = new Button("alarm");
        alarmBtn.setOnAction(event -> {
            alarmOn.setValue(true);
            System.out.println("alarm button pressed");
        });

        components.getChildren().addAll(upBtn,downBtn,alarmBtn);
        components.setPadding(new Insets(20));

    }

    public String getFloorNumber() {
        return floorNumber;
    }

    public VBox getComponents() {
        return components;
    }
}