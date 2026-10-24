//package Panel;
//
//import javafx.application.Application;
//import javafx.beans.property.BooleanProperty;
//import javafx.stage.Stage;
//
///**
// * This is the working gui for the floor panel. this class can integrate javafx objects. we need to
// * give functionality to this api.
// */
//public class FloorPanel extends Application {
//    private final String floorNumber;
//    //using BooleanProperty to bind with a toggle button
//    private static BooleanProperty alarmOn;
//    private BooleanProperty upBtn;
//    private BooleanProperty downBtn;
//
//    public FloorPanel(String floorNumber) {
//
//    }
//
//
//    /**
//     * Checks if the up button has been pressed at a given floor.
//     * ex. was the button pressed on floor 10?
//     * @param floorNum The floor 1-10 in the building
//     * @return True if pressed, false otherwise
//     */
//    public boolean upBtnPressed(int floorNum){
//        //TODO write code that queries if the up button has been pressed
//        return false;
//    }
//
//    /**
//     * Checks if the down button has been pressed at a given floor.
//     * ex. was the button pressed on floor 10?
//     * @param floorNum The floor 1-10 in the building
//     * @return True if pressed, false otherwise
//     */
//    public boolean downBtnPressed(int floorNum){
//        //TODO write code that queries if the up button has been pressed
//        return false;
//    }
//
//    /**
//     * Resets the up button boolean to false, so basically removes the action
//     */
//    public void resetUpBtn(){
//        //TODO write body
//    }
//
//    /**
//     * Resets the up button boolean to false, so basically removes the action
//     */
//    public void resetDownBtn(){
//        //TODO write body
//    }
//
//    /**
//     * Checks if the alarm button was pressed. All alarms are connected so we refer to the
//     * static alarmOn variable.
//     * @return True if pressed, false otherwise
//     */
////    public static boolean alarmPressed(){
////        //TODO write body should check the alarm button in javafx gui
////        return alarmOn;}
//
//    /**
//     * Resets the state of the alarm button
//     */
//    public void resetAlarm(){
//        //TODO write code to reset the button
//    }
//
//    /**
//     * Plays a ding sound to alert passengers of elevator's arrival
//     */
//    public void ding(){
//        //TODO figure out how to add an audio resource and connect it to javafx
//    }
//
//
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//
//    }
//}
