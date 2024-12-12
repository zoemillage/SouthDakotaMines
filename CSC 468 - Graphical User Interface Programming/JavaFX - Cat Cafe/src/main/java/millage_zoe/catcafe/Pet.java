/**
 * Author: Zoe Millage
 * Description: This file contains the parent class for the
 * cat and kitten, and holds the base data for them
 */

package millage_zoe.catcafe;

public class Pet extends FloorArea{
    protected int countdown = 8;
    protected int petAge = 52;


    public Pet() {

    }



    /**
     * emulates getting a new pet after one has been adopted.
     * resets the countdown and pet age variables to their default
     */
    @Override
    public String getDetails() {
        return "\n" + countdown;
    }



    /**
     * updates the tile/pet age, cost, and setting an "adopted" flag
     * @return if the cat gets adopted from this update
     */
    @Override
    public boolean nextWeek() {
        countdown -= 1;

        totalCost += weeklyCost;
        age += 1;
        petAge += 1;

        return (countdown < 1);
    }



    /**
     * gets the pet tile's data: name, change status, floor age,
     * total cost, pet(cat) age, adoption countdown
     * @return the cat data
     */
    @Override
    public String toString() {
        String temp = super.toString();
        temp = temp + "\nCat age: " + petAge
                + "\nWeeks until adoption: " + countdown;
        return temp;
    }
}
