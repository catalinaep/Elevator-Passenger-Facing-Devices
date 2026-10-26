package Panel;

import java.util.Objects;

/**
 * This is the working gui for the floor panel. this class can integrate javafx objects. we need to
 * give functionality to this api.
 */
public class ElevatorPanel {
    private final int elevatorNum;
    private final ElevatorPanelGUI gui;
    private final boolean[] floorButtonPressed = new boolean[ElevatorPanelGUI.MAX_FLOOR];

    public ElevatorPanel(int elevatorNum, ElevatorPanelGUI gui) {
        if (elevatorNum < 1) {
            throw new IllegalArgumentException("Elevator number must be positive.");
        }

        this.elevatorNum = elevatorNum;
        this.gui = Objects.requireNonNull(gui, "ElevatorPanelGUI cannot be null.");
        this.gui.setButtonHandler(this::requestFloor);
    }

    private void requestFloor(int floorNum) {
        checkFloor(floorNum);
        floorButtonPressed[floorNum-1] = true;
        gui.setFloorButtonLit(floorNum, true);
    }

    public void pressFloorButton(int floorNum) { requestFloor(floorNum); }

    /**
     * Checks if the key gui component has been interacted with. IDK if a button or some other object would work better.
     * @return True if inserted, false otherwise
     */
    public boolean keyInserted(){return false;}

    /**
     * Checks if a given floor button has been pressed by a passenger.
     * @param floorNum A floor in the building
     * @return True if pressed, false otherwise
     */
    public boolean btnPressed(int floorNum){
        checkFloor(floorNum);
        return floorButtonPressed[floorNum -1];
    }

    /**
     * Resets the state of a floor number button
     * @param floorNum A floor in the building.
     */
    public void resetBtn(int floorNum){
        checkFloor(floorNum);
        floorButtonPressed[floorNum -1] = false;
        gui.setFloorButtonLit(floorNum, false);
    }

    /**
     * Plays a sound to alert passengers of destination
     */
    public void ding(){ java.awt.Toolkit.getDefaultToolkit().beep(); }

    /**
     * Plays a sound to alert of an overload, not stopped until stopBuzz() is called
     */
    public void playBuzz(){}

    /**
     * stops the Buzz sound
     */
    public void stopBuzz(){}

    /**
     * updates the GUI with the elevators current floor.
     * @param newFloor
     */
    public void updateFloorDisplay(String newFloor){
        gui.updateFloorDisplay(newFloor);
    }

    public void updateFloorDisplay(int newFloor) {
        checkFloor(newFloor);
        gui.updateFloorDisplay(newFloor);
    }

    /**
     * updates the GUI with the elevators current direction.
     * @param newDirection
     */
    public void updateDirDisplay(String newDirection){ gui.updateDirDisplay(newDirection); }

    public void updateDirDisplay(ElevatorPanelGUI.Direction newDirection) { gui.setDirection(newDirection); }

    /** get the number of the elevator for the panel */
    public int getElevatorNum() { return elevatorNum; }

    /**
     * Make sure the floor is a valid floor number between 1 and 10
     * @param floorNum the floor number to check
     */
    private void checkFloor(int floorNum) {
        if (floorNum < ElevatorPanelGUI.MIN_FLOOR || floorNum > ElevatorPanelGUI.MAX_FLOOR) {
            throw new IllegalArgumentException("Floor number must be between " + ElevatorPanelGUI.MIN_FLOOR +
                    " and " + ElevatorPanelGUI.MAX_FLOOR + ".");
        }
    }
}

