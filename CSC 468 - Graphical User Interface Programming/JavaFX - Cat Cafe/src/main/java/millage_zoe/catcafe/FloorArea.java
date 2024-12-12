/**
 * Author: Zoe Millage
 * Description: This file contains the base data class for all the tiles
 */

package millage_zoe.catcafe;

public abstract class FloorArea {
    protected int age = 0;
    protected int lastChanged = 0;
    protected String name = "";
    protected int totalCost = 0;
    protected int weeklyCost = 0;


    public FloorArea() {
    }



    public int getCost() {
        return weeklyCost;
    }



    public String getDetails() {
        return "";
    }



    public String getName() {
        return name;
    }



    /**
     * returns how much money this tile gains/loses per week
     * @return the amount of money gained/lost this week
     */
    public int getRevenue() {
        return (weeklyCost * -1);
    }



    /**
     * handles going to the next week; updating age, cost, and any
     * special conditions if applicable
     * @return if a tile will be changed by the update (e.g. a cat getting adopted)
     */
    public abstract boolean nextWeek();



    /**
     * handles timers being reset, such as if a new cat was brought into a tile
     */
    public void resetTimers() {
    }



    public void setLastChanged (int week) {
        lastChanged = week;
    }



    public void setTotalCost(int cost) {
        totalCost = cost;
    }



    /**
     * returns the data about the tile as a string: the last time
     * the tile was changed, the age of the tile, and the total cost
     * @return the tile details
     */
    @Override
    public String toString() {
        return name
                + "\nFloor Changed: " + lastChanged
                + "\nFloor Age: " + age
                + "\nTotal Cost: $" + totalCost;
    }

}
