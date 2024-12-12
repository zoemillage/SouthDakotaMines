/**
 * holds the tile class, which is essentially a wrapper
 * for a floor area, and connects to a tile view with
 * button functionality
 */

package millage_zoe.catcafe;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Tile {
    private FloorArea area;
    private PropertyChangeSupport subject;
    private CafeSim theSim;
    private TileView tileView;


    /**
     * handles the tile being clicked with any of the radio
     * button options
     */
    private class clickListener implements EventHandler<ActionEvent> {

        /**
         * just displays details if view is selected, or
         * changes the tile (with appropriate data changing) and
         * displays its details if another option is selected
         * @param actionEvent
         */
        @Override
        public void handle(ActionEvent actionEvent) {
            boolean wasEmpty;
            int index = theSim.getTileIndex(Tile.this);
            int timeChanged;
            String option = theSim.getRadioOption();

            // view button
            if ( option.equals("View")) {
                // change tile details to this one
                theSim.updateTileDetails(index);
                return;
            }

            // get values needed for tile changing
            timeChanged = theSim.getTimeSinceReset();
            wasEmpty = (area.getName().equals("Empty"));

            // change a tile, taking the initial cost from funds and
            // updating the filled value
            // table
            if (option.equals("Table")){
                area = new Table();
                area.setLastChanged(timeChanged);
                theSim.updateFunds(area.totalCost * -1);

                if (wasEmpty)
                    theSim.updateFilled(1);
            }

            // cat
            else if (option.equals("Cat")){
                area = new Cat();
                area.setLastChanged(timeChanged);
                theSim.updateFunds(area.totalCost * -1);

                if (wasEmpty)
                    theSim.updateFilled(1);
            }

            //kitten
            else if (option.equals("Kitten")){
                area = new Kitten();
                area.setLastChanged(timeChanged);
                theSim.updateFunds(area.totalCost * -1);

                if (wasEmpty)
                    theSim.updateFilled(1);
            }

            // empty
            else {
                area = new Empty();
                area.setLastChanged(timeChanged);

                // only has an initial cost if it wasn't empty before
                if (!wasEmpty) {
                    area.setTotalCost(200);
                    theSim.updateFunds(area.totalCost * -1);
                    theSim.updateFilled(-1);
                }
            }


            // tell the views to update
            // GRADING: TRIGGER
            subject.firePropertyChange(actionEvent.toString(), null, null);
            theSim.updateTileDetails(index);
            theSim.updateCafeDetails();
        }
    }



    public Tile() {
        area = new Empty();
        subject = new PropertyChangeSupport(this);
    }



    /**
     * adds an observer for the observer pattern
     * @param obv the observer to connect
     */
    public void addObserver(PropertyChangeListener obv) {
        // GRADING: SUBJECT
        subject.addPropertyChangeListener(obv);
    }



    public int getRevenue() {
        return area.getRevenue();
    }



    public String getTileDetails(){
        return area.toString();
    }



    public boolean nextWeek() {
        boolean temp;
        temp = area.nextWeek();
        tileView.update();
        return temp;
    }



    public void resetTimers() {
        area.resetTimers();
        tileView.update();
    }



    public void setSim (CafeSim sim) {
        theSim = sim;
    }



    public void setTileView( TileView theView) {
        tileView = theView;
        tileView.addEventFilter(ActionEvent.ACTION, new clickListener());
    }



    @Override
    public String toString() {
        String temp = area.getName();
        temp = "-" + temp.charAt(0) + "-\n" + "-$" + area.getCost();
        temp += area.getDetails();

        return temp;
    }

}
