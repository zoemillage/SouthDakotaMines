/**
 * @Author Zoe Millage
 * @Brief Creates exceptions for the menu used for the city:
 *        Trying to rezone a city that has 5 or more empty tiles,
 *        going out of bounds in the main menu ( choosing values below 0 or
 *        above 6 ),
 *        or going out of bounds in various sub menus:
 *        below 1 and above 5 in the 1st part of submenu 1 and the 2nd part
 *        of submenu 4,
 *        out of the bounds of the city in the 2nd part of submenu 1,
 *        and below 1 and above 3 in the first part of submenu 4.
 */

package millage_zoe;

public class MenuExceptions {

    static class OutOfBounds extends Exception {
        public OutOfBounds() {
            super();
        }

    }



    static class CannotRezone extends Exception{
        public CannotRezone() {
            super();
        }
    }



    /**
     * @brief throws exception for the main menu (which wants values between
     * 0 and 6) when improper values are given
     * @param tested - the menu choice to test
     * @throws OutOfBounds - the choice is out of bounds
     */
    public static void mainOOB(int tested) throws OutOfBounds {
        if(tested < 0 || tested > 6)
            throw new OutOfBounds();
    }



    /**
     * @brief throws exception for submenu 4 (which wants values
     * between 1 and 3) when improper values are given
     * @param tested - the menu choice to test
     * @throws OutOfBounds - the choice is out of bounds
     */
    public static void subOOB1to3( int tested ) throws OutOfBounds {
        if(tested < 1 || tested > 3)
            throw new OutOfBounds();
    }



    /**
     * @brief throws exception for submenus 1 and 4 (which want values
     * between 1 and 5) when improper values are given
     * @param tested - the menu choice to test
     * @throws OutOfBounds - the choice is out of bounds
     */
    public static void subOOB1to5( int tested ) throws OutOfBounds {
        if(tested < 1 || tested > 5)
            throw new OutOfBounds();
    }



    /**
     * @brief throws exception for submenu 1 when the user inputs a tile
     * row or column location that does not exist in the city
     * @param tested - the menu choice to test
     * @throws OutOfBounds - the choice is out of bounds
     */
    public static void subOOBBounded( int tested, int bound) throws OutOfBounds {
        if(tested < 0 || tested > bound)
            throw new OutOfBounds();
    }



    /**
     * @brief throws exception for submenu 5 when 5 or more tiles are empty
     * @param tested - the number of empty tiles
     * @throws CannotRezone - too many tiles to rezone
     */
    public static void testRezone( int tested) throws CannotRezone {
        if ( tested > 4 )
            throw new CannotRezone();
    }
}
