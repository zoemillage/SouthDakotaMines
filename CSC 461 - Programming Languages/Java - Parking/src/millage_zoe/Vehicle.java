/**
 * Author: Zoe Millage
 * Description: Holds the Vehicle class, which is functionally a struct that
 *                  is held by the ParkingLot class and its subclasses.
 *                  Contains the id of the vehicle and the time (in minutes)
 *                  after the opening of its lot that this vehicle entered
 */


package millage_zoe;


public class Vehicle {
    private int id;
    private int entryMin;   // minute this vehicle entered the lot

    public Vehicle( int newId, int newEntry ){
        id = newId;
        entryMin = newEntry;
    }



    public int getId (){
        return id;
    }



    public int getEntry (){
        return entryMin;
    }
}
