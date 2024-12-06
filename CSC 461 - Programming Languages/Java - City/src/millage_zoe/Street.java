/**
 * @Author Zoe Millage
 * @Brief Holds the Street class, a subclass of Tile. More complex than
 * the other tiles due to its 15 possible visual orientations.
 */

package millage_zoe;

public class Street extends Tile {

    private int adjCode;

    public Street(){
        symbol = '━';
        color = ColorText.Color.BLACK;
        adjCode = 0;
    }



    /**
     * @brief accepts any basic visitor, lets the visitor know this
     * is a street
     * @param v - the visitor to accept
     */
    @Override
    public void accept(Visitor v) {
        v.acceptStreet(this);
    }



    /**
     * @brief accepts any city visitor, lets the visitor know this
     * is a street
     * @param v - the visiting visitor
     * @param c - the current city
     */
    @Override
    public void accept(CityVisitor v, City c) {
        v.acceptStreet(this, c );
    }



    // written by Dr. Lisa Rebenitsch
    /**
     * \brief Indicate the road tiles adjacent to this one
     *
     * The road time image chosen is dependent on the tiles around
     * it. This is where the adjacency of road tiles is indicated.
     *
     * \param left True if road tile to left
     * \param top True if road tile to top
     * \param bot True if road tile to lower left
     * \param right True if road tile to lower right
     */
    public void setAdjacencies(boolean left, boolean top, boolean bot, boolean right)
    {
        // Create the adjacency code
        int code = (left ? 1 : 0) | (top ? 2 : 0) | (bot ? 4 : 0) | (right ? 8 : 0);
        if (adjCode == code)
        {
            // We are already set. Do nothing
            return;
        }

        //unicode starts at 2510
        char symbols[] = {
                '━',      // 0 right
                '━',      // 1 right
                '┃',      // 2 ud
                '┛',      // 3 lu
                '┃',      // 4 ud
                '┓',      // 5 ld
                '┃',      // 6 ud
                '┫',     // 7 lud
                '━',      // 8 right
                '━',      // 9 right
                '┗',      // 10 top
                '┻',     // 11 lur
                '┏',      // 12 dr
                '┳',    // 13 ldr
                '┣',     // 14 udr
                '╋'    // 15 ludr
        };

        // Select the appropriate image
        adjCode = code;

        symbol = symbols[code];
    }

}
