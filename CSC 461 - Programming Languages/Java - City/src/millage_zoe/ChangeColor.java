/**
 * @Author Zoe Millage
 * @Brief Holds the ChangeColor Visitor, which changes the given tile type to
 * the given color
 */

package millage_zoe;

public class ChangeColor implements Visitor{
    private int group;
    private ColorText.Color color;


    /**
     * @brief changes the color of this tile if it's the right type
     * @param b - the building to change
     */
    @Override
    public void acceptBuilding( Building b ){
        if ( group == 1)
            b.setColor(color);
    }



    /**
     * @brief this intentionally does nothing
     * @param e - the empty to accept
     */
    @Override
    public void acceptEmpty( Empty e ){
    }



    /**
     * @brief changes the color of this tile if it's the right type
     * @param g - the greenspace to change
     */
    @Override
    public void acceptGreenspace(Greenspace g ){
        if ( group == 3 )
            g.setColor(color);
    }



    /**
     * @brief changes the color of this tile if it's the right type
     * @param s - the street to change
     */
    @Override
    public void acceptStreet(Street s ){
        if ( group == 2 )
            s.setColor(color);
    }



    /**
     * @brief changes the color of this tile if it's the right type
     * @param w - the water to change
     */
    @Override
    public void acceptWater( Water w ){
        if ( group == 3 )
            w.setColor(color);
    }



    public void setColor( ColorText.Color newColor ){
        color = newColor;
    }



    public void setGroup( int newGroup){
        group = newGroup;
    }

}
