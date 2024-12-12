/**
 * Author: Zoe Millage
 * Description: This file contains the data for table tiles
 */

package millage_zoe.catcafe;

public class Table extends FloorArea {

    private int profits = 150;
    private int totalProfits = 0;


    public Table() {
        totalCost = 300;
        name = "Table";
        weeklyCost = 50;
    }



    /**
     * appends how much money this tile makes to its details
     * @return a string with weekly earnings
     */
    @Override
    public String getDetails() {
        return "\n$" + profits;
    }



    /**
     * calculates profits - costs for weekly revenue
     * @return weekly revenue
     */
    @Override
    public int getRevenue() {
        return profits - weeklyCost;
    }



    /**
     * updates costs, profits, and tile age
     * @return false, value is for the parent and some other subclasses
     */
    @Override
    public boolean nextWeek() {
        totalCost += weeklyCost;
        age += 1;
        totalProfits += profits;

        return false;
    }



    /**
     * returns the data about the tile as a string: the last time
     * the tile was changed, the age of the tile, the total cost,
     * and the total profits
     * @return the tile details
     */
    @Override
    public String toString() {
        return super.toString() + "\nTotal Revenue: $" + totalProfits;
    }

}
