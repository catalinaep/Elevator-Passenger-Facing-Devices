//TODO guys should we make the floor number an enum so we can do a switch to check if pressed? thoughts
/**
 * This is the working gui for the floor panel. this class can integrate javafx objects. we need to
 * give functionality to this api.
 */
public class ElevatorPanel {

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
    public boolean btnPressed(int floorNum){return false;}

    /**
     * Resets the state of a floor number button
     * @param floorNum A floor in the building.
     */
    public void resetBtn(int floorNum){}

    /**
     * Plays a sound to alert passengers of destination
     */
    public void ding(){}

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
        //TODO the type of the args can change idk what it should be rn. maybe the enum
    }

    /**
     * updates the GUI with the elevators current direction.
     * @param newDirection
     */
    public void updateDirDisplay(String newDirection){}




}

