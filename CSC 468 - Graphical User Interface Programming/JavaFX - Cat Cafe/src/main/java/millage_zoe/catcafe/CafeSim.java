/**
 * Author: Zoe Millage
 * Description: This file contains the primary model for the catfe.
 */

package millage_zoe.catcafe;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class CafeSim {
    private int cols = 3;
    private int funds = 0;
    private int numAdopted = 0;
    private int numFilled = 0;
    private int rows = 3;
    private int selectedTile = 0;
    private PropertyChangeSupport subject;
    private Controller theController;
    private ArrayList<Tile> tiles;
    private int timeSinceReset = 0;


    public CafeSim() {
        // initialize the tiles and subject
        Tile temp;

        tiles = new ArrayList<Tile>();
        subject = new PropertyChangeSupport(this);

        int numTiles = cols * rows;

        for ( int i = 0; i < numTiles; i++ ) {
            temp = new Tile();
            temp.setSim(this);
            tiles.add(temp);
        }
    }



    /**
     * adds an observer for the observer pattern
     * @param obv the observer to connect
     */
    public void addObserver(PropertyChangeListener obv) {
        subject.addPropertyChangeListener(obv);
    }



    /**
     * forwards call to get the radio button group selection
     * @return the radio button selected as a string
     */
    public String getRadioOption() {
        return theController.getRadioOption();
    }



    public int getSelectedTile() {
        return selectedTile;
    }



    /**
     * gets the tile details: name, change status, age, total cost, and
     * possibly cat age and adoption countdown
     * @param index the index of the tile with the details
     * @return the details
     */
    public String getTileDetails(int index){
        return tiles.get(index).getTileDetails();
    }



    /**
     * gets the index of the given tile
     * @param tile the tile to find
     * @return the index of the tile
     */
    public int getTileIndex (Tile tile) {
        int i;
        int numTiles = rows * cols;
        for (i = 0; i < numTiles; i++) {
            if ( tile.equals(tiles.get(i)) )
                return i;
        }

        return i;
    }



    public ArrayList<Tile> getTiles() {
        return tiles;
    }



    public int getTimeSinceReset() {
        return timeSinceReset;
    }



    /**
     * calls each tile's next week function, and handles cats getting adopted
     */
    public void nextWeek() {
        int i;
        int numTiles = rows * cols;
        boolean adpoted;
        Tile temp;

        // updates the week
        timeSinceReset += 1;

        // updates each tile
        for ( i = 0; i < numTiles; i++ ) {
                temp = tiles.get(i);
                adpoted = temp.nextWeek();
                funds += temp.getRevenue();

                // handle a cat/kitten getting adopted
                if (adpoted) {
                    numAdopted += 1;
                    temp.resetTimers();
                }
        }

        // fires layout update
        subject.firePropertyChange("next week", null, this.timeSinceReset);
    }



    /**
     * resets the sim's values
     */
    public void reset() {
        funds = 0;
        numAdopted = 0;
        numFilled = 0;
        timeSinceReset = 0;
    }



    /**
     * resizes and resets the sim
     * @param col the new number of columns
     * @param row the new number of rows
     */
    public void resize(int col, int row) {
        // resets values and clears tiles
        reset();
        tiles.clear();

        cols = col;
        rows = row;
        int numTiles = cols * rows;
        Tile tempTile;

        // reinitialize the tiles
        for ( int i = 0; i < numTiles; i++ ) {
            tempTile = new Tile();
            tempTile.setSim(this);
            tiles.add(tempTile);
        }

    }



    public void setController (Controller controller) {
        theController = controller;
    }



    /**
     * gets the cafe's data as a string: the current week,
     * the number of non-empty tiles, the current funds, and the total
     * number of cats adopted.
     * @return the cafe's data
     */
    @Override
    public String toString() {
        return "Week: " + timeSinceReset +
                "\nFilled: " + numFilled +
                "\nFunds: $" + funds +
                "\nAdopted: " + numAdopted;
    }



    /**
     * forwards a call to update the cafe's details
     */
    public void updateCafeDetails() {
        theController.updateCafeInfo();
    }



    /**
     * either adds or subtracts to the number of filled (non empty) tiles
     * @param num the value to add to numFilled, may be negative
     */
    public void updateFilled(int num) {
        numFilled += num;
    }



    /**
     * adds or subtracts the amount of funds the cafe has
     * @param revenue the value to add, may be negative
     */
    public void updateFunds(int revenue) {
        funds += revenue;
    }



    /**
     * forwards a call to update the gui
     * @param index the index of the tile to display details of
     */
    public void updateTileDetails(int index) {
        selectedTile = index;
        theController.updateTileDetails(index);
    }

}
