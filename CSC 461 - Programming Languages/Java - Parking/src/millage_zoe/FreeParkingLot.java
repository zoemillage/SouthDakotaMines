/**
 * Author: Zoe Millage
 * Description: This file contains FreeParkingLot class, which is derived
 *                  from ParkingLot, but always has a fee of $0 and does
 *                  not functionally tie an id to a vehicle
 */


package millage_zoe;


public class FreeParkingLot extends ParkingLot{

    public FreeParkingLot ( String newName, int newSize ){
        super( newName, newSize, 0);
    }



    public FreeParkingLot( int newSize ){
        this("test", newSize );
    }



    /**
     * @description Tries to remove a vehicle in the same way as ParkingLot,
     *                  but doesn't care about ids or profits
     * @param minsSinceOpen - The time since initial opening at which the
     *      *                            vehicle is trying to enter.
     *      *                            should be a positive value greater
     *      *                            or equal to this value given to the
     *      *                            previous call of this function or
     *      *                            the markVehicleExit function
     * @return 0 - A vehicle successfully exited
     * @return -1 - The exit failed
     */
    public int markVehicleExit ( int minsSinceOpen ){
        boolean updateClosed = false;

        // stop if given negative time
        if ( minsSinceOpen < time )
            return -1;

        // stop if the lot is already empty
        if ( isEmpty() )
            return -1;

        // if the lot was closed, we'll update closedMinutes
        if ( isClosed() )
            updateClosed = true;

        // remove a vehicle
        removeVehicle( 0 );

        // update closedMinutes if applicable
        if ( updateClosed )
            updateClosedMinutes( time, minsSinceOpen);

        // update time
        time = minsSinceOpen;

        return 0;
    }


    /**
     * @description Allows the 2-parameter version of markVehicleExit
     *                  from ParkingLot to be called, but still ignores
     *                  ids and profits
     * @param minsSinceOpen - The time since initial opening at which the
     *      *                            vehicle is trying to enter.
     *      *                            should be a positive value greater
     *      *                            or equal to this value given to the
     *      *                            previous call of this function or
     *      *                            the markVehicleExit function
     * @param id - A holdover from ParkingLots. Ignored.
     * @return 0 - A vehicle successfully exited
     * @return -1 - The exit failed
     */
    @Override
    public int markVehicleExit ( int minsSinceOpen, int id ){
        return markVehicleExit( minsSinceOpen );
    }


    /**
     * @description Prints the state of the lot much like in ParkingLots.
     *                  The profit part of the state isn't printed, however
     * @return The state of the lot as a string
     */
    @Override
    public String toString(){
        // print ParkingLot's toString, but without the profit part
        return strPart1() + strPart2();
    }

}
