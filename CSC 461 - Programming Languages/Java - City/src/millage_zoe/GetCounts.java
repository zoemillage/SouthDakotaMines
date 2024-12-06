/**
 * @Author Zoe Millage
 * @Brief A derived class from the Visitor interface. Visits each tile,
 * and holds a counter for each tile type that increments each time it
 * visits a tile of that type.
 */
package millage_zoe;

public class GetCounts implements Visitor{
    private int numBuilding = 0;
    private int numEmpty = 0;
    private int numGreen = 0;
    private int numStreet = 0;
    private int numWater = 0;



    /**
     * visits a building tile and increments the building counter
     * @param b - the visited building tile
     */
    @Override
    public void acceptBuilding( Building b ){
        numBuilding++;
    }



    /**
     * visits an empty tile and increments the empty counter
     * @param e - the visited empty tile
     */
    @Override
    public void acceptEmpty( Empty e ){
        numEmpty++;
    }



    /**
     * visits a greenspace tile and increments the greenspace counter
     * @param g - the visited greenspace tile
     */
    @Override
    public void acceptGreenspace(Greenspace g ){
        numGreen++;
    }



    /**
     * visits a street tile and increments the street counter
     * @param s - the visited street tile
     */
    @Override
    public void acceptStreet(Street s ){
        numStreet++;
    }



    /**
     * visits a water tile and increments the water counter
     * @param w - the visited water tile
     */
    @Override
    public void acceptWater( Water w ){
        numWater++;
    }



    public int getNumBuildings(){
        return numBuilding;
    }



    public int getNumEmpty(){
        return  numEmpty;
    }



    public int getNumGreenspace(){
        return  numGreen;
    }



    public int getNumStreet(){
        return  numStreet;
    }



    public int getNumWater(){
        return  numWater;
    }


    /**
     * @brief resets all the counters to 0
     */
    public void reset(){
        numBuilding = 0;
        numEmpty = 0;
        numGreen = 0;
        numStreet = 0;
        numWater = 0;
    }
}
