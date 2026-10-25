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

    URL DING = getClass().getResource("src/main/resources/ding.mp3");

    private final String floorNumber;
    private static BooleanProperty alarmOn;
    private BooleanProperty upBtn;
    private BooleanProperty downBtn;


    public FloorPanel(String floorNumber) {
        this.floorNumber = floorNumber;
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
        return alarmOn.getValue();}

    /**
     * Resets to the default false state of the alarm button
     */
    public void resetAlarm(){
        alarmOn.setValue(false);
    }

    /**
     * Plays a ding sound to alert passengers of elevator's arrival
     */
    public void ding() {
        if (DING != null) {
            AudioClip audioClip = new AudioClip(DING.toExternalForm());
            audioClip.play();
        } else {
            System.err.println("Could not find sound file: " + DING);
        }
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

    }

    public String getFloorNumber() {
        return floorNumber;
    }
}
