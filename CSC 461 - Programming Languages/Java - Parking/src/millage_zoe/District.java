/*
Program: Basic Java
Author: Zoe Millage
Class: CSC-461-M01, 2023f
Date Due: 14 September 2023
Description: Simulates a district of parking lots, with paid and free lots.
             Paid lots' price is determined by how long a vehicle stays.
             This file contains the District class, which manages
             collections of parking lots, free and paid.

Additional OOP  requirements
    toString properly extended				            __hopefully complete__
    Constructors properly handled			            __hopefully complete__
    Access properly handled (code style requirement)	__hopefully complete__

Last tier completed: ___Tier 11___

No known bugs
*/


package millage_zoe;

import java.util.ArrayList;


public class District {
    ArrayList<ParkingLot> lots = new ArrayList<ParkingLot>(); // lots list

    private int minutesClosed = 0;
    private int numLots = 0;
    private int time = 0;   // the current time


    /**
     * @description Adds a new lot to the district
     * @param newLot - The lot to add
     * @return The index of the new lot in the arraylist
     */
    public int add( ParkingLot newLot ){
        int index;

        // add the lot
        lots.add(newLot);

        // increment the number of lots
        numLots++;

        // get the new lot's index and return it
        index = lots.size() - 1;

        return index;
    }



    /**
     * @description Gets the total number of minutes ALL lots in the
     *      district were closed
     * @return The total number of minutes all lots were
     *      closed
     */
    public int getClosedMinutes(){
        return minutesClosed;
    }



    /**
     * @description Returns the lot stored at the given index of the arraylist.
     *      Assuming a valid index has been allowed.
     * @param index - The index of the lot to return
     * @return The lot at the given index
     */
    public ParkingLot getLot( int index ){
        return lots.get(index);
    }



    /**
     * @description Gets the sum of the profits of all lots in the district
     * @return The total profit from all lots
     */
    public double getTotalMoneyCollected(){
        double totProfit = 0;

        int i;

        // get the profit from each lot, add it to a running total
        for ( i = 0; i < numLots; i++ )
            totProfit += lots.get(i).getProfit();

        // return the running total
        return totProfit;
    }



    /**
     * @description Gets the total number of vehicles in the district
     * @return The total number of vehicles parked in the district
     */
    public int getVehiclesParkedInDistrict ( ){
        int i;
        int totVehicles = 0;

        // add the number of vehicles from each lot to a running total
        for ( i = 0; i < numLots; i++ )
            totVehicles += lots.get(i).getVehiclesInLot();

        // return the total
        return totVehicles;
    }



    /**
     * @description Returns whether or not ALL lots in the district are closed
     *                  ( where a lot is closed if the number of vehicles
     *                    parked in it is above some threshold of the
     *                    lot's total size )
     * @return true - All lots are closed; the district is closed
     * @return false - At least 1 lot isn't closed; the district is open
     */
    public boolean isClosed(){
        int i;

        // go through each lot
        for ( i = 0; i < numLots; i++ )
        {
            // if any are open, return false
            if ( !lots.get(i).isClosed() )
                return false;
        }

        // only return true if all lots are closed
        return true;
    }



    /**
     * @description Tries to add a parked vehicle to a specified lot
     *                  within the district
     * @param lotIndex - The index of the lot to add a vehicle to
     * @param minsSinceOpen - The time since initial opening at which the
     *                            vehicle is trying to enter.
     *                            should be a positive value greater
     *                            or equal to this value given to the
     *                            previous call of this function or
     *                            the markVehicleExit function
     * @return The id of the added vehicle
     * @return -1 - vehicle failed to enter
     */
    public int markVehicleEntry ( int lotIndex, int minsSinceOpen ){
        boolean updateMins = false;

        int retVal;

        // if the district is already closed, maybe update the closed minutes
        if ( isClosed() )
            updateMins = true;

        // try to add the vehicle to the given lot
        retVal = lots.get( lotIndex ).markVehicleEntry(minsSinceOpen);

        // only do updates if the entry was successful
        if ( retVal != -1 ) {
            // update the mins closed if the lot is closed and was closed
            if (updateMins && isClosed() )
                updateClosedMins( time, minsSinceOpen);

            // update the current time
            time = minsSinceOpen;
        }

        return retVal;
    }



    /**
     * @description Tries to remove a parked vehicle to a specified lot
     *                  within the district and gain profit
     * @param lotIndex - The index of the lot to remove a vehicle from
     * @param minsSinceOpen - The time since initial opening at which the
     *                            vehicle is trying to exit.
     *                            should be a positive value greater
     *                            or equal to this value given to the
     *                            previous call of this function or
     *                            the markVehicleEntry function
     * @return 0 - Success
     * @return -1 - Failure
     */
    public int markVehicleExit ( int lotIndex, int minsSinceOpen, int id ){
        boolean updateMins = false;

        int retVal;

        // if the district is closed, update the closed minutes later
        if ( isClosed() )
            updateMins = true;

        // try to remove the vehicle to the given lot
        retVal = lots.get( lotIndex ).markVehicleExit(minsSinceOpen, id);

        // only do updates if the exit was successful
        if ( retVal != -1 ) {
            // update the mins closed if applicable
            if (updateMins)
                updateClosedMins( time, minsSinceOpen);

            // update the current time
            time = minsSinceOpen;
        }

        return retVal;
    }



    /**
     * @description Outputs the state of the district, including a list of
     *                  all the lots, their filled capacity, and profits if
     *                  applicable.
     * @return The state as a string
     */
    @Override
    public String toString(){
        int i;

        // District specific text
        String str = "District status:\n";

        // get the toString() output from all lots separated by a new line
        for ( i = 0; i < numLots; i++ )
            str = str + lots.get(i).toString() + "\n";

        return str;
    }



    /**
     * @description Given a time range where the district was closed, find
     *                  the number of (additional) minutes the district
     *                  was closed and add it to the minutesClosed attribute
     * @param startTime - When this segment of closed time started
     * @param endTime - When this segment of closed time ended
     */
    public void updateClosedMins( int startTime, int endTime ){
        // get the number of minutes to add
        int temp = endTime - startTime;

        // add this number of minutes to the total minutes closed
        minutesClosed += temp;
    }
}
