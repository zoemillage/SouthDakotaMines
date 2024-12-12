/**
 * Author: Zoe Millage
 * Description: This file contains the data for kitten tiles
 */

package millage_zoe.catcafe;

public class Kitten extends Pet {


    public Kitten() {
        petAge = 10;
        countdown = 4;
        name = "Kitten";
        totalCost = 200;
        weeklyCost = 20;
    }



    /**
     * emulates getting a new kitten after one has been adopted.
     * resets the countdown and pet age variables to their default
     */
    @Override
    public void resetTimers() {
        petAge = 10;
        countdown = 4;
    }



    /**
     * gets the kitten tile's data: name, change status, floor age,
     * total cost, kitten age, adoption countdown
     * @return the cat data
     */
    @Override
    public String toString() {
        return super.toString();
    }

}
