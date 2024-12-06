/**
 * @Author Zoe Millage
 * @Brief The CityVisitor interface, the template for
 * the FixRoads visitor
 */
package millage_zoe;

public interface CityVisitor {

    /**
     * @brief visits a building tile
     * @param b - the visited building
     * @param c - the current city
     */
    public void acceptBuilding( Building b, City c );



    /**
     * @brief visits an empty tile
     * @param e - the visited empty
     * @param c - the current city
     */
    public void acceptEmpty( Empty e, City c );



    /**
     * @brief visits a greenspace tile
     * @param g - the visited greenspace
     * @param c - the current city
     */
    public void acceptGreenspace(Greenspace g, City c );



    /**
     * @brief visits a street tile
     * @param s - the visited street
     * @param c - the current city
     */
    public void acceptStreet(Street s, City c );



    /**
     * @brief visits a water tile
     * @param w - the visited water
     * @param c - the current city
     */
    public void acceptWater( Water w, City c );
}
