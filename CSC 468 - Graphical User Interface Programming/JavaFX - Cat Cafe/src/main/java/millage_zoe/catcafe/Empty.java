/**
 * Author: Zoe Millage
 * Description: This file contains the data for empty tiles
 */

package millage_zoe.catcafe;

public class Empty extends FloorArea{

    public Empty() {
        name = "Empty";
        totalCost = 0;
        weeklyCost = 10;
    }



    /**
     * increments the age and total cost
     * @return false, value is for the parent and some other subclasses
     */
    @Override
    public boolean nextWeek() {
        totalCost += weeklyCost;
        age += 1;

        return false;
    }



    /**
     * returns the data about the tile as a string: the last time
     * the tile was changed, the age of the tile, and the total cost
     * @return the tile details
     */
    @Override
    public String toString() {
        return super.toString();
    }

}
