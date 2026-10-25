package Panel;//TODO guys should we make the floor number an enum so we can do a switch to check if pressed? thoughts

import javafx.scene.control.Button;

/**
 * This is the working gui for the floor panel. this class can integrate javafx objects. we need to
 * give functionality to this api.
 */
public class ElevatorPanel {

    private final int elevatorNumber;
    private final ElevatorPanelGUI gui;


    public ElevatorPanel(int elevatorNumber, ElevatorPanelGUI gui) {
        this.elevatorNumber = elevatorNumber;
        this.gui = gui;

        gui.setButtonHandler(buttonId -> {
            switch (buttonId) {
                case "firstFloor" -> {
                    gui.changeButtonImage(
                            buttonId,
                            "/buttons-green/1-green.png"
                    );
                }
                case "secondFloor" -> {
                    gui.changeButtonImage(
                            buttonId,
                            "/buttons-green/2-green.png"
                    );
                }
                case "thirdFloor" -> {
                    gui.changeButtonImage(
                            buttonId,
                            "/buttons-green/3-green.png"
                    );
                }
                case "fourthFloor" -> {
                    gui.changeButtonImage(
                            buttonId,
                            "/buttons-green/4-green.png"
                    );
                }
                case "fifthFloor" -> {
                    gui.changeButtonImage(
                            buttonId,
                            "/buttons-green/5-green.png"
                    );
                }
                case "sixthFloor" -> {
                    gui.changeButtonImage(
                            buttonId,
                            "/buttons-green/6-green.png"
                    );
                }
                case "seventhFloor" -> {
                    gui.changeButtonImage(
                            buttonId,
                            "/buttons-green/7-green.png"
                    );
                }
                case "eighthFloor" -> {
                    gui.changeButtonImage(
                            buttonId,
                            "/buttons-green/8-green.png"
                    );
                }
                case "ninthFloor" -> {
                    gui.changeButtonImage(
                            buttonId,
                            "/buttons-green/9-green.png"
                    );
                }
                case "tenthFloor" -> {
                    gui.changeButtonImage(
                            buttonId,
                            "/buttons-green/10-green.png"
                    );
                }
            }
        });

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

