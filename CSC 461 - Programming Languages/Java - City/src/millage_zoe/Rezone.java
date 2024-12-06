/**
 * @Author Zoe Millage
 * @Brief Visits empty tiles and puts their coordinates into lists, marking
 * them to be overwritten by the city class.
 */

package millage_zoe;

import java.util.ArrayList;

public class Rezone implements Visitor{
    ArrayList<Integer> rezoneRows = new ArrayList<Integer>();
    ArrayList<Integer> rezoneCols = new ArrayList<Integer>();


    /**
     * @brief this intentionally does nothing
     * @param b - the visited building
     */
    @Override
    public void acceptBuilding( Building b ){

    }



    /**
     * @brief Gets the coordinates of the empty tile and adds it to arraylists
     * so the city can change them to greenspaces.
     * @param e - the visited empty tile
     */
    @Override
    public void acceptEmpty( Empty e ){
        rezoneRows.add( Integer.valueOf(e.getCol()) );
        rezoneCols.add( Integer.valueOf(e.getRow()) );
    }



    /**
     * @brief this intentionally does nothing
     * @param g - the visited greenspace
     */
    @Override
    public void acceptGreenspace(Greenspace g ){

    }



    /**
     * @brief this intentionally does nothing
     * @param s - the visited street
     */
    @Override
    public void acceptStreet(Street s ){

    }



    /**
     * @brief this intentionally does nothing
     * @param w - the visited water
     */
    @Override
    public void acceptWater( Water w ){

    }



    public ArrayList<Integer> getCList(){
        return rezoneCols;
    }



    public ArrayList<Integer> getRList(){
        return rezoneRows;
    }



    /**
     * clears the array lists so some tiles aren't unintentionally
     * marked to be changed
     */
    public void clearLists(){
        rezoneRows.clear();
        rezoneCols.clear();
    }
}
