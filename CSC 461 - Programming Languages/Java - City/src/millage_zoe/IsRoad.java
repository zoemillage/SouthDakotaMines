/**
 * @Author Zoe Millage
 * @Brief The IsRoad visitor, which visits one tile and stores if it is a road
 */
package millage_zoe;

public class IsRoad implements Visitor{

    private boolean isARoad = false;


    /**
     * @brief sets isARoad to false
     * @param b - the building to accept
     */
    @Override
    public void acceptBuilding( Building b ){
        isARoad = false;
    }



    /**
     * @brief sets isARoad to false
     * @param e - the empty to accept
     */
    @Override
    public void acceptEmpty( Empty e ){
        isARoad = false;
    }



    /**
     * @brief sets isARoad to false
     * @param g - the greenspace to accept
     */
    @Override
    public void acceptGreenspace( Greenspace g ){
        isARoad = false;
    }



    /**
     * @brief this sets isRoad to true
     * @param s - the street to accept
     */
    @Override
    public void acceptStreet( Street s ){
        isARoad = true;
    }



    /**
     * @brief sets isARoad to false
     * @param w - the water to accept
     */
    @Override
    public void acceptWater( Water w ){
        isARoad = false;
    }



    public boolean getIsRoad(){
        return isARoad;
    }

}
