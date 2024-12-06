/**
 * @Author Zoe Millage
 * @Brief The Visitor interface, the template for
 * almost all the other visitors
 */
package millage_zoe;

public interface Visitor {


    /**
     * @brief visits a building tile
     * @param b - the visited building
     */
    public void acceptBuilding( Building b );



    /**
     * @brief visits an empty tile
     * @param e - the visited empty
     */
    public void acceptEmpty( Empty e );



    /**
     * @brief visits a greenspace tile
     * @param g - the visited greenspace
     */
    public void acceptGreenspace(Greenspace g );



    /**
     * @brief visits a street tile
     * @param s - the visited street
     */
    public void acceptStreet(Street s );



    /**
     * @brief visits a water tile
     * @param w - the visited water
     */
    public void acceptWater( Water w );
}
