/**
 * @Author Zoe Millage
 * @Brief Holds the Greenspace class, a subclass of Tile. its functionality
 * is the same as all other extending tiles except street. It can accept
 * a visitor.
 */

package millage_zoe;

public class Greenspace extends Tile{

    public Greenspace() {
        symbol = '░';
        color = ColorText.Color.BLACK;
    }



    /**
     * @brief accepts any basic visitor, lets the visitor know this
     * is a greenspace
     * @param v - the visitor to accept
     */
    @Override
    public void accept(Visitor v) {
        v.acceptGreenspace(this);
    }



    /**
     * @brief accepts any city visitor, lets the visitor know this
     * is a greenspace
     * @param v - the visiting visitor
     * @param c - the current city
     */
    @Override
    public void accept(CityVisitor v, City c) {
        v.acceptGreenspace( this, c );
    }
}
