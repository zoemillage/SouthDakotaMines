/**
 * Author: Zoe Millage
 * Description: This file contains the controller for the catfe. Handles events
 */

package millage_zoe.catcafe;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.beans.PropertyChangeListener;

public class Controller {
    private Layout theLayout;
    private CafeSim theSim;


    /**
     * handles the 3x3 button
     */
    private class size3Listener implements EventHandler<ActionEvent> {

        /**
         * resizes the sim to be 3x3
         * @param actionEvent the button 3x3 button click
         */
        @Override
        public void handle(ActionEvent actionEvent) {
            theSim.updateTileDetails(0);
            theLayout.resize(3, 3);
        }
    }


    /**
     * handles the 5x5 button
     */
    private class size5Listener implements EventHandler<ActionEvent> {

        /**
         * resizes the sim to be 5x5
         * @param actionEvent the 5x5 button click
         */
        @Override
        public void handle(ActionEvent actionEvent) {
            theSim.updateTileDetails(0);
            theLayout.resize(5, 5);
        }
    }


    /**
     * handles the 9x9 button
     */
    private class size9Listener implements EventHandler<ActionEvent> {

        /**
         * resizes the sim to be 9x9
         * @param actionEvent the 9x9 button click
         */
        @Override
        public void handle(ActionEvent actionEvent) {
            theSim.updateTileDetails(0);
            theLayout.resize(9, 9);
        }
    }


    /**
     * handles the next week button
     */
    private class weekListener implements EventHandler<ActionEvent> {

        /**
         * tells the sim to call its next week functions
         * @param actionEvent the next week button click
         */
        @Override
        public void handle(ActionEvent actionEvent) {
            theSim.nextWeek();
        }
    }



    public Controller( Layout layout){
        theLayout = layout;

        theSim = new CafeSim();
        theSim.setController(this);

        theLayout.setViewModel(theSim);

        // get on click events for all the non-tile view buttons
        theLayout.getNextWeek().addEventFilter(ActionEvent.ACTION, new weekListener());
        theLayout.getSize3().addEventFilter(ActionEvent.ACTION, new size3Listener());
        theLayout.getSize5().addEventFilter(ActionEvent.ACTION, new size5Listener());
        theLayout.getSize9().addEventFilter(ActionEvent.ACTION, new size9Listener());
    }



    /**
     * adds an observer for the observer pattern
     * @param obv the observer to connect
     */
    public void addObserver(PropertyChangeListener obv) {
        theSim.addObserver(obv);
    }



    /**
     * gets the cafe info as a string
     * @return the cafe info as a string
     */
    public String getCafeInfo() {
        return theSim.toString();
    }



    /**
     * gets the selected radio button as a string
     * @return the radio button option as a string
     */
    public String getRadioOption() {
        return theLayout.getRadioOption();
    }



    /**
     * forwards a get selected tile call to the model
     * @return the index of the selected tile button
     */
    public int getSelectedTile() {
        return theSim.getSelectedTile();
    }



    /**
     * gets the details of a specific tile in the catfe
     * @param index the tile's index
     * @return the tile details
     */
    public String getTileDetails(int index){
        return theSim.getTileDetails(index);
    }



    /**
     * forwards a call to the layout to update the gui top bar
     */
    public void updateCafeInfo() {
        theLayout.updateDetails();
    }



    /**
     * forwards a call to the layout to update the tile view side panel
     * @param index the index of the tile whose details should be displayed
     */
    public void updateTileDetails(int index) {
        theLayout.updateTileDetails(index);
    }

}