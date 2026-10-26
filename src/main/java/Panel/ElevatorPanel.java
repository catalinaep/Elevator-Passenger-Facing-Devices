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
        if (elevatorNum < 1) {
            throw new IllegalArgumentException("Elevator number must be positive.");
        }

        this.elevatorNum = elevatorNum;
        this.gui = Objects.requireNonNull(gui, "ElevatorPanelGUI cannot be null.");
        this.gui.setElevatorNumber(elevatorNum);
        this.gui.setButtonHandler(this::requestFloor);
        this.gui.setKeyHandler(this::setKeyInserted);
        this.gui.setBuzzerHandler(this::setBuzzerOn);
    }

    private void requestFloor(int floorNum) {
        checkFloor(floorNum);
        floorButtonPressed[floorNum - 1] = true;
        gui.setFloorButtonLit(floorNum, true);
    }

    public void pressFloorButton(int floorNum) {
        requestFloor(floorNum);
    }

    public boolean btnPressed(int floorNum) {
        checkFloor(floorNum);
        return floorButtonPressed[floorNum - 1];
    }

    public void resetBtn(int floorNum) {
        checkFloor(floorNum);
        floorButtonPressed[floorNum - 1] = false;
        gui.setFloorButtonLit(floorNum, false);
    }

    public boolean keyInserted() {
        return keyInserted;
    }

    public void setKeyInserted(boolean inserted) {
        keyInserted = inserted;
        gui.setKeyInserted(inserted);
    }

    /** A one-time arrival notification using /ding.mp3. */
    public void ding() {
        SoundEffects.playDing();
    }

    /** Starts /ding.mp3 in a loop until stopBuzz() is called. */
    public void playBuzz() {
        if (buzzerOn) {
            return;
        }
        buzzerOn = true;
        gui.setBuzzerOn(true);
        SoundEffects.startBuzzer();
    }

    /** Stops the overload-buzzer state and turns off its panel light. */
    public void stopBuzz() {
        if (!buzzerOn) {
            return;
        }
        buzzerOn = false;
        gui.setBuzzerOn(false);
        SoundEffects.stopBuzzer();
    }

    public boolean isBuzzerOn() {
        return buzzerOn;
    }

    private void setBuzzerOn(boolean on) {
        if (on) {
            playBuzz();
        } else {
            stopBuzz();
        }
    }

    public void updateFloorDisplay(String newFloor) {
        gui.updateFloorDisplay(newFloor);
    }

    public void updateFloorDisplay(int newFloor) {
        checkFloor(newFloor);
        gui.updateFloorDisplay(newFloor);
    }

    public void updateDirDisplay(String newDirection) {
        gui.updateDirDisplay(newDirection);
    }

    public void updateDirDisplay(ElevatorPanelGUI.Direction newDirection) {
        gui.setDirection(newDirection);
    }

    public int getElevatorNum() {
        return elevatorNum;
    }

    private static void checkFloor(int floorNum) {
        if (floorNum < ElevatorPanelGUI.MIN_FLOOR || floorNum > ElevatorPanelGUI.MAX_FLOOR) {
            throw new IllegalArgumentException(
                    "Floor number must be between " + ElevatorPanelGUI.MIN_FLOOR
                            + " and " + ElevatorPanelGUI.MAX_FLOOR + "."
            );
        }
    }
}