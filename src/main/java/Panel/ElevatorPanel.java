package Panel;//TODO guys should we make the floor number an enum so we can do a switch to check if pressed? thoughts
/**
 * This is the working gui for the floor panel. this class can integrate javafx objects. we need to
 * give functionality to this api.
 */
public class ElevatorPanel {
    private final int elevatorNum;
    private final ElevatorPanelGUI gui;
    private final boolean[] floorButtonPressed = new boolean[10];

    public ElevatorPanel(int elevatorNum, ElevatorPanelGUI gui) {
        this.elevatorNum = elevatorNum;
        this.gui = gui;

        gui.setButtonHandler(buttonID -> {
            int floor = gui.getButtonIdAtFloor(buttonID);
            if (floor != -1) {
                floorButtonPressed[floor -1] = true;
                gui.changeButtonImage(buttonID, "/buttons-green/" + floor + "-green.png");
            }
        });
    }

    private final int elevatorNumber;
    private final ElevatorPanelGUI gui;


    public ElevatorPanel(int elevatorNumber, ElevatorPanelGUI gui) {
        this.elevatorNumber = elevatorNumber;
        this.gui = gui;
    }

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
        String buttonID = gui.getButtonIdAtFloor(floorNum);
        gui.changeButtonImage(buttonID, "/buttons-grey/" + floorNum + ".png");
    }

    /**
     * Plays a sound to alert passengers of destination
     */
    public void ding(){ java.awt.Toolkit.getDefaultToolkit().beep(); }

    /**
     * Plays a sound to alert of an overload, not stoped until stopBuzz() is called
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

    /**
     * updates the GUI with the elevators current direction.
     * @param newDirection
     */
    public void updateDirDisplay(String newDirection){ gui.updateDirDisplay(newDirection); }

    /** get the number of the elevator for the panel */
    public int getElevatorNum() { return elevatorNum; }

    /**
     * Make sure the floor is a valid floor number between 1 and 10
     * @param floorNum the floor number to check
     */
    private void checkFloor(int floorNum) {
        if (floorNum < 1 || floorNum > 10) {
            throw new IllegalArgumentException("Floor number must be between 1 and 10");
        }
    }
}

