/**
 * Author: Zoe Millage
 * Description: This file contains ParkingLot class, which holds vehicles
 *                  with specific ids. Vehicles may enter or exit the lot,
 *                  and may give revenue to the lot if they have been parked
 *                  in the lot for over 15 minutes.
 *                  This class was meant to be in its own package with
 *                  FreeParkingLot and Vehicle while District was in
 *                  a separate package, all under the "millage_zoe"
 *                  package. The requirements of the program
 *                  and the compiler seem to make this impossible.
 */


package millage_zoe;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class ParkingLot {

    public final static double CLOSED_THRESHOLD = 80;

    protected ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();

    protected double fee;
    private double profit = 0;

    protected int id = 0;
    protected int minutesClosed = 0;
    protected int numVehicles = 0;
    protected int size;
    protected int time = 0;

    protected String name;



    // GRADING: CONSTRUCTION
    public ParkingLot ( String newName, int newSize, double newFee){
        name = newName;
        size = newSize;
        fee = newFee;
    }


    public ParkingLot ( String newName, int newSize){
        this( newName, newSize, 1.0);
    }


    public ParkingLot ( int newSize, double newFee){
        this( "test", newSize, newFee );
    }


    public ParkingLot ( int newSize ){
        this( "test", newSize, 1.0 );
    }



    /**
     * @description If a vehicle with a given id is in the lot,
     *                  return its index in the array
     * @param id - The vehicle id to find
     * @return The index of the vehicle in the array
     * @return -1 - The vehicle is not in the lot
     */
    protected int findVehicle( int id ){
        int i;

        // go through the list of vehicles
        for ( i = 0; i < numVehicles; i++)
        {
            // return the index if the id is found
            if ( vehicleList.get(i).getId() == id )
                return i;
        }

        // the id isn't in the list
        return -1;
    }



    /**
     * @description Gets the total number of minutes the lot was closed
     * @return The total number of minutes the lot was closed
     */
    public int getClosedMinutes(){
        return minutesClosed;
    }



    /**
     * @description Returns the fullness of the lot in a human-readable percent
                        form ( e.g. returns "44.4" for 44.4% rather than .444 )
     * @return The percent fullness
     */
    public double getFullness() {
        return (double)numVehicles / (double)size * 100;
    }



    /**
     * @description Returns the name of the lot
     * @return The name of the lot
     */
    public String getName(){
        return name;
    }



    /**
     * @description returns the lot's profit
     * @return The lot's profit
     */
    public double getProfit(){
        return profit;
    }



    /**
     * @description Returns the number of vehicles in the lot
     * @return The number of vehicles in the lot
     */
    public int getVehiclesInLot (){
        return numVehicles;
    }



    /**
     * @description Returns whether or not the number of vehicles in the lot
     *                  has surpassed a constant threshold in the total size
     *                  of the lot
     * @return true - The number of vehicles is above the threshold
     * @return false - the number of vehicles is below the threshold
     */
    public boolean isClosed(){
        return getFullness() >= CLOSED_THRESHOLD;
    }



    /**
     * @description Returns whether or not the lot is empty
     * @return true - the lot is empty
     * @return false - the lot is not empty
     */
    public boolean isEmpty(){
        return numVehicles == 0;
    }



    /**
     * @description Returns whether or not the lot is full
     * @return true - the lot is full
     * @return false - the lot is not full
     */
    public boolean isFull(){
        return numVehicles == size;
    }



    /**
     * @description This function adds a vehicle to the lot. This fails if
     *                  a time before what is stored as the "current time"
     *                  is given or if the lot is 100% full. minutesClosed
     *                  will be updated if the lot was closed before and after
     *                  the vehicle entry. A successful entry will update
     *                  the current time.
     * @param minsSinceOpen - The time when the vehicle enters
     * @return -1 - The entry failed
     * @return The id of the new vehicle
     */
    public int markVehicleEntry( int minsSinceOpen ){
        boolean wasClosed = false;

        int vehicleId;

        Vehicle newVehicle;

        // stop if given negative time
        if ( minsSinceOpen < time )
            return -1;

        // stop if the lot is 100% full
        if ( isFull() )
            return -1;

        // check if the lot is currently closed. May update minutesClosed
        if ( isClosed() )
            wasClosed = true;

        // set the new vehicle's id and increment the id counter
        vehicleId = id;
        id++;

        // add the new vehicle
        newVehicle = new Vehicle( vehicleId, minsSinceOpen );
        vehicleList.add( newVehicle );
        numVehicles++;

        // check if the lot is closed, updated closed time if it already was
        if ( isClosed() && wasClosed )
            updateClosedMinutes( time, minsSinceOpen );

        // update the time
        time = minsSinceOpen;

        return vehicleId;
    }


    /**
     * @description This function removes a vehicle to the lot. This fails if
     *                  a time before what is stored as the "current time"
     *                  is given, if the lot is empty, or if the given vehicle
     *                  wasn't in the lot. minutesClosed will be updated if the
     *                  lot was closed before the vehicle's exit. A successful
     *                  entry will update the current time and profit.
     * @param minsSinceOpen - The time when the vehicle enters
     * @return -1 - The exit failed
     * @return 0 - the exit succeeded
     */
    public int markVehicleExit ( int minsSinceOpen, int id){
        boolean updateClosed = false;

        int index;
        int entryTime;

        // stop if given negative time
        if ( minsSinceOpen < time )
            return -1;

        // stop if the lot is already empty
        if ( isEmpty() )
            return -1;

        // see if the vehicle with the given id is in the lot
        index = findVehicle( id );

        // stop if the given id isn't here
        if ( index == -1 )
            return -1;

        // if the lot was closed, we're going to update closedMinutes
        if ( isClosed() )
            updateClosed = true;

        // get the vehicle's entry time to do profit calculations
        entryTime = vehicleList.get( index ).getEntry();

        updateProfit( entryTime, minsSinceOpen);

        // remove the vehicle from the lot
        removeVehicle( index );

        // update closedMinutes if applicable
        if ( updateClosed )
            updateClosedMinutes( time, minsSinceOpen);

        // update the time
        time = minsSinceOpen;

        return 0;
    }



    /**
     * @description Removes a vehicle from the lot
     * @param index - The index of the vehicle to remove
     */
    protected void removeVehicle ( int index ){
        // remove the vehicle
        vehicleList.remove(index);
        numVehicles--;
    }



    /**
     * @description A helper function for the toString function.
     *                  Gets the first part of the string including
     *                  the lot's name and number of vehicles
     * @return Part of toString's full string
     */
    protected String strPart1 (){
        // make the string and return it
        String str =  "Status for " + name + " parking lot: " + numVehicles
                + " vehicles (";

        return str;
    }



    /**
     * @description A helper function for the toString function.
     *                  Gets (one of) the last part of the string including
     *                  the lot's fullness percentage
     * @return Part of toString's full string
     */
    protected String strPart2 (){
        double percentFull;
        String str;

        // print (CLOSED) for fullness% if above the threshold
        if ( isClosed() )
            str = "CLOSED)";

        // print the fullness% with
        // 0 decimal places if .0, or 1 decimal place otherwise
        else{
            percentFull = getFullness();
            DecimalFormat percentFormat = new DecimalFormat("##.#");
            str = percentFormat.format(percentFull) + "%)";
        }

        return str;
    }



    /**
     * @description Prints the status of the lot in the form "Status for
     *                  [name] parking lot: [numVehicles] vehicles
     *                  ([%full]) Money Collected: $[totalProfit]".
     *                  Will replace [%full] with "CLOSED" if the
     *                  fullness percent is above CLOSED_THRESHOLD
     * @return The completed string
     */
    @Override
    public String toString (){
        // combine the helper function's strings with the profit
        String str1 = String.format( " Money Collected: $%#.2f", profit);
        String str2 = strPart1() + strPart2() + str1;

        // return the resulting string
        return str2;
    }



    /**
     * @description Given a time range where the lot was closed, find
     *                  the number of (additional) minutes the lot
     *                  was closed and add it to the minutesClosed attribute
     * @param startTime - When this segment of closed time started
     * @param endTime - When this segment of closed time ended
     */
    protected void updateClosedMinutes ( int startTime, int endTime ){
        int temp = endTime - startTime;

        minutesClosed += temp;
    }



    /**
     * @description Gets the amount a vehicle needs to pay based on how
     *                  long it was in the lot. No charges apply when parked
     *                  for less than 15 minutes. Charges after 15 minutes are
     *                  based on an hourly fee, calculated as the number of
     *                  hours ( fractional, not as integers ) multiplied by
     *                  the hourly fee
     * @param minsEntry - The time when the vehicle entered
     * @param minsExit - The time when the vehicle exited
     */
    private void updateProfit ( int minsEntry, int minsExit ){
        double hoursInLot;

        // get the actual time parked in the lot
        int timeInLot = minsExit - minsEntry;


        // no profit if a vehicle is in a lot for 15 mins or less
        if ( timeInLot <= 15)
            return;

        // get number of hours in the lot
        hoursInLot = (double)timeInLot / (double)60;

        // add the fee multiplied by the hours to the total profit
        profit += hoursInLot * fee;
    }
}
