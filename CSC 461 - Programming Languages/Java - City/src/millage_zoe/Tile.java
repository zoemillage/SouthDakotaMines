/**
 * @Author Zoe Millage
 * @Brief The abstract class containing the main info for each tile in the
 * city. Tiles have a symbol, a color, and coordinates.
 */
package millage_zoe;

abstract public class Tile {
    protected char symbol;
    protected ColorText.Color color;

    protected int col;
    protected int row;


    /**
     * @brief the template to accept any basic visitor
     * @param v - the visiting visitor
     */
    abstract public void accept( Visitor v );



    /**
     * @brief the template to accept any city visitor
     * @param v - the visiting visitor
     * @param c - the current city
     */
    abstract public void accept( CityVisitor v, City c );



    public int getCol(){
        return col;
    }



    public int getRow(){
        return row;
    }



    public void setCol( int newCol ){
        col = newCol;
    }



    public void setColor(ColorText.Color newColor) {
        color = newColor;
    }



    public void setRow( int newRow ){
        row = newRow;
    }



    /**
     * @brief prints the tile's symbol in the tile's color
     * @return the symbol with ANSI codes to change its color
     */
    @Override
    public String toString(){
        return ColorText.colorString(symbol, color);
    }
}
