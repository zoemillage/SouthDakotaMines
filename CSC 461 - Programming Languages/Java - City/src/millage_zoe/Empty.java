/**
 * @Author Zoe Millage
 * @Brief Holds the Empty class, a subclass of Tile. its functionality
 * is the same as all other extending tiles except street. It can accept
 * a visitor.
 */

package millage_zoe;

public class Empty extends Tile{


    public Empty(){
        symbol = '▫';
        color = ColorText.Color.BLACK;
    }



    /**
     * @brief accepts any basic visitor, lets the visitor know this
     * an empty tile
     * @param v - the visitor to accept
     */
    @Override
    public void accept(Visitor v) {
        v.acceptEmpty(this);
    }



    /**
     * @brief accepts any city visitor, lets the visitor know this
     * an empty tile
     * @param v - the visiting visitor
     * @param c - the current city
     */
    @Override
    public void accept(CityVisitor v, City c) {
        v.acceptEmpty( this, c );
    }
}
