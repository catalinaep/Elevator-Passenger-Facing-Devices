package Panel;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.stage.Stage;
import javafx.scene.media.AudioClip;
import java.net.URL;


/**
 * This is the working gui for the floor panel. this class can integrate javafx objects. we need to
 * give functionality to this api.
 */
public class FloorPanel extends Application {
    //private final String floorNumber;
    //using BooleanProperty to bind with a toggle button
    private static BooleanProperty alarmOn;
    private BooleanProperty upBtn;
    private BooleanProperty downBtn;

    URL DING = getClass().getResource(resourcePath);

    public FloorPanel(String floorNumber) {

    }


    /**
     * Checks if the up button has been pressed at a given floor.
     * ex. was the button pressed on floor 10?
     * @return True if pressed, false otherwise
     */
    public boolean upBtnPressed(){
        return upBtn.getValue();
    }

    /**
     * Checks if the down button has been pressed at a given floor.
     * ex. was the button pressed on floor 10?
     * @return True if pressed, false otherwise
     */
    public boolean downBtnPressed(){
        //TODO write code that queries if the up button has been pressed
        return downBtn.getValue();
    }

    /**
     * Resets the up button boolean to default false value, so basically removes the action
     *
     */
    public void resetUpBtn() {
        upBtn.setValue(false);
    }

    /**
     * Resets the up button boolean to false, so basically removes the action
     */
    public void resetDownBtn(){
        downBtn.setValue(false);
    }

    /**
     * Checks if the alarm button was pressed. All alarms are connected so we refer to the
     * static alarmOn variable.
     * @return True if pressed, false otherwise
     */
    public static boolean alarmPressed(){
        //TODO write body should check the alarm button in javafx gui
        return alarmOn.getValue();}

    /**
     * Resets to the default false state of the alarm button
     */
    public void resetAlarm(){
        //TODO write code to reset the button
        alarmOn.setValue(false);
    }

    /**
     * Plays a ding sound to alert passengers of elevator's arrival
     */
    public void ding(){
        //TODO figure out how to add an audio resource and connect it to javafx
    }

    private void playSoundEffect(String resourcePath) {
        URL soundURL = getClass().getResource(resourcePath);

        if (soundURL != null) {
            AudioClip audioClip = new AudioClip(soundURL.toExternalForm());
            audioClip.play();
        } else {
            System.err.println("Could not find sound file: " + resourcePath);
        }
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}
