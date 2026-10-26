package Panel;

import java.util.Objects;

/**
 * The panel for one elevator car. Holds the state and the behavior of the panel.
 */
public class ElevatorPanel {
    private final int elevatorNum;
    private final ElevatorPanelGUI gui;
    private final boolean[] floorButtonPressed = new boolean[ElevatorPanelGUI.MAX_FLOOR];
    private boolean keyInserted;
    private boolean buzzerOn;

    public ElevatorPanel(int elevatorNum, ElevatorPanelGUI gui) {
        if (elevatorNum < 1) throw new IllegalArgumentException("Elevator number must be positive.");

        this.elevatorNum = elevatorNum;
        this.gui = Objects.requireNonNull(gui, "ElevatorPanelGUI cannot be null.");
        this.gui.setElevatorNumber(elevatorNum);
        this.gui.setButtonHandler(this::pressFloorButton);
        this.gui.setKeyHandler(this::setKeyInserted);
        this.gui.setBuzzerHandler(this::setBuzzerOn);
    }

    /** send a request for an elevator at the floor input */
    private void pressFloorButton(int floorNum) {
        checkFloor(floorNum);
        floorButtonPressed[floorNum - 1] = true;
        gui.setFloorButtonStatus(floorNum, true);
    }

    /** press the button and check if it was already pressed */
    public boolean btnPressed(int floorNum) {
        checkFloor(floorNum);
        return floorButtonPressed[floorNum - 1];
    }

    /** reset the status of the button for the floor number */
    public void resetBtn(int floorNum) {
        checkFloor(floorNum);
        floorButtonPressed[floorNum - 1] = false;
        gui.setFloorButtonStatus(floorNum, false);
    }

    /* check if the key is currently inserted */
    public boolean keyInserted() { return keyInserted; }

    /** set the key inserted status to true or false */
    public void setKeyInserted(boolean inserted) {
        keyInserted = inserted;
        gui.setKeyInserted(inserted);
    }

    /** The ding effect is for an elevator arriving */
    public void ding() {
        SoundEffects.playDing();
    }

    /** The buzz effect is for elevator at weight capacity */
    public void playBuzz() {
        if (buzzerOn) return;
        buzzerOn = true;
        gui.setBuzzerOn(true);
        SoundEffects.startBuzzer();
    }

    /** Stop the buzzer sound effect */
    public void stopBuzz() {
        if (!buzzerOn) return;
        buzzerOn = false;
        gui.setBuzzerOn(false);
        SoundEffects.stopBuzzer();
    }

    /** check if the buzzer is on */
    public boolean isBuzzerOn() { return buzzerOn; }

    /** toggle the buzzer playing */
    private void setBuzzerOn(boolean on) {
        if (on) {
            playBuzz();
        } else {
            stopBuzz();
        }
    }

    /** Update the floor display, takes the new floor as either an int or string */
    public void updateFloorDisplay(String newFloor) { gui.updateFloorDisplay(newFloor); }
    public void updateFloorDisplay(int newFloor) {
        checkFloor(newFloor);
        gui.updateFloorDisplay(newFloor);
    }

    /** update the direction display with either string or direction enum of direction */
    public void updateDirDisplay(String newDirection) { gui.updateDirDisplay(newDirection); }
    public void updateDirDisplay(ElevatorPanelGUI.Direction newDirection) { gui.setDirection(newDirection); }

    // get the number of the elevator for the panel
    public int getElevatorNum() { return elevatorNum; }

    // make sure that the floor number is valid
    private static void checkFloor(int floorNum) {
        if (floorNum < ElevatorPanelGUI.MIN_FLOOR || floorNum > ElevatorPanelGUI.MAX_FLOOR) {
            throw new IllegalArgumentException(
                    "Floor number must be between " + ElevatorPanelGUI.MIN_FLOOR
                            + " and " + ElevatorPanelGUI.MAX_FLOOR + "."
            );
        }
    }
}