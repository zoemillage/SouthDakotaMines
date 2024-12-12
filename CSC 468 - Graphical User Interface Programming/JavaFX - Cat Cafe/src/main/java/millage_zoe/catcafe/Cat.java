/**
 * Author: Zoe Millage
 * Description: This file contains the data for adult cat tiles
 */

package millage_zoe.catcafe;

public class Cat extends Pet {


    public Cat() {
        petAge = 52;
        countdown = 8;
        name = "Cat";
        totalCost = 200;
        weeklyCost = 30;

    }



    /**
     * emulates getting a new cat after one has been adopted.
     * resets the countdown and pet age variables to their default
     */
    @Override
    public void resetTimers() {
        countdown = 8;
        petAge = 52;
    }



    /**
     * gets the cat tile's data: name, change status, floor age,
     * total cost, cat age, adoption countdown
     * @return the cat data
     */
    @Override
    public String toString() {
        return super.toString();
    }

}
