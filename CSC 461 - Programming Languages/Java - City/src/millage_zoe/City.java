/**
 * @Author Zoe Millage
 * @Brief This manages Tiles to create a City. This can initialize the city,
 * change individual ties in the city, and reset the city to a default state.
 */
package millage_zoe;

public class City {
    int cols = 7;
    int rows = 7;

    Tile[][] tiles;

    public City(){
        // initialize the grid
        tiles = new Tile[rows][cols];

        // set every tile as empty
        for ( int i = 0; i < rows; i++) {
            for ( int j = 0; j < cols; j++){
                tiles[i][j] = new Empty();
            }
        }

        setCoordinates();
    }


    /**
     * @brief the general accept class used on basic visitors.
     * Tells each tile to accept the visitor
     * @param v - the visitor
     */
    public void accept ( Visitor v ){
        int i, j;

        for ( i = 0; i < rows; i++)
        {
            for ( j = 0; j < cols; j++)
                tiles[i][j].accept(v);
        }
    }



    /**
     * @brief the general accept class used on city visitors.
     * Tells each tile to accept the visitor
     * @param v - the visitor
     */
    public void accept ( CityVisitor v ){
        int i, j;

        for ( i = 0; i < rows; i++)
        {
            for ( j = 0; j < cols; j++)
                tiles[i][j].accept(v, this);
        }
    }



    /**
     * @brief changes the tile at the given row and column value to the
     * tile of the given type:
     *  1: greenspace
     *  2: water
     *  3: road
     *  4: building
     *  5: empty
     *
     * @param type - the type for the new tile
     * @param row - the row of the tile to change
     * @param col - the column of the tile to change
     */
    public void changeTile ( int type, int row, int col){
        // change to greenspace
        if ( type == 1 )
            tiles[col][row] = new Greenspace();

        // change to water
        else if ( type == 2 )
            tiles[col][row] = new Water();

        // change to road
        else if ( type == 3 )
            tiles[col][row] = new Street();

        // change to building
        else if ( type == 4 )
            tiles[col][row] = new Building();

        // change to empty
        else tiles[col][row] = new Empty();

        // set the coordinates
        tiles[col][row].setRow(col);
        tiles[col][row].setCol(row);
    }



    /**
     * @brief helper function for defaultCity. Handles the building placement
     * for the default city
     */
    private void defaultBuildings(){
        int i = 0;
        int j = 0;

        // 4th through 6th tiles of 2nd column are buildings
        j = 1;
        for ( i = 3; i < 6; i++)
            tiles[i][j] = new Building();

        // 2nd to 4th tiles of 6th row are buildings
        i = 5;
        for ( j = 2; j < 4; j++)
            tiles[i][j] = new Building();

        // 5th row, 4th column  and 2nd row, 2nd column are buldings
        tiles [4][3] = new Building();
        tiles [1][1] = new Building();
    }



    /**
     * @brief changes the city to the default:
     *        ━━━━━━━
     *        ━⌂━▫━▫▫
     *        ━━━━━~~
     *        ━⌂━▫━░~
     *        ━⌂▫⌂━░~
     *        ━⌂⌂⌂━░~
     *        ━━━━━░~
     */
    public void defaultCity() {
        // add the roads
        defaultStreets();

        // add the water
        defaultWater();

        // add the greenspaces
        defaultGreenspace();

        // add the buildings
        defaultBuildings();

        // add the empty spaces
        defaultEmpty();

        // reset the coordinates
        setCoordinates();
    }



    /**
     * @brief helper function for defaultCity. Handles the empty tile placement
     * for the default city
     */
    private void defaultEmpty(){
        int i = 0;
        int j = 0;

        // 6th and 7th tiles of the 2nd row are empty
        i = 1;
        for ( j = 5; j < cols; j++)
            tiles[i][j] = new Empty();

        // 2nd row, 4th column is empty
        tiles [i][3] = new Empty();

        // 5th row, 3rd column  4th row, 4th column are empty
        tiles[4][2] = new Empty();
        tiles[3][3] = new Empty();
    }



    /**
     * @brief helper function for defaultCity. Handles the greenspace placement
     * for the default city
     */
    private void defaultGreenspace(){
        int i = 0;
        int j = 0;

        // 4th to 7th tile of the 6th column are greenspaces
        j = 5;
        for ( i = 3; i < rows; i++)
            tiles[i][j] = new Greenspace();
    }



    /**
     * @brief helper function for defaultCity. Handles the street placement
     * for the default city
     */
    private void defaultStreets(){
        int i = 0;
        int j = 0;

        // 1st row is just road
        for ( ; j < cols; j++)
            tiles[i][j] = new Street();

        // the 1st and 5th columns are just road
        j = 0;
        for ( i = 0; i < rows; i++)
            tiles[i][j] = new Street();

        j = 4;
        for ( i = 0; i < rows; i++)
            tiles[i][j] = new Street();

        // 3rd and 7th row have 1st 5 tiles as roads
        i = 2;
        for ( j = 1; j < 5; j++)
            tiles[i][j] = new Street();

        i = 6;
        for ( j = 1; j < 5; j++)
            tiles[i][j] = new Street();

        // 1st to 4th tiles of the 4th column are streets
        j = 2;
        for ( i = 1; i < 4; i++)
            tiles[i][j] = new Street();
    }



    /**
     * @brief helper function for defaultCity. Handles the water placement
     * for the default city
     */
    private void defaultWater(){
        int i = 0;
        int j = 0;

        // 3rd tile of 6th row
        tiles[2][5] = new Water();

        // 3rd through final tile of 7th column are water
        j = 6;
        for ( i = 2; i < rows; i++)
            tiles[i][j] = new Water();
    }



    public Tile getTile( int row, int col ) {
        return tiles[row][col];
    }



    /**
     * @brief runs though the city tiles, setting their coordinates.
     */
    private void setCoordinates(){
        // run through the tiles, setting their coordinates as their indices
        for ( int i = 0; i < rows; i++){
            for ( int j = 0; j < cols; j++ ){
                tiles[i][j].setRow(i);
                tiles[i][j].setCol(j);
            }
        }
    }



    /**
     * @brief Outputs the city as a 7 x 7 grid
     *
     * @return the city as a 7 x 7 grid in a String
     */
    @Override
    public String toString() {
        String theString = "\n";

        // run through each tile, getting its output
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                theString += tiles[i][j].toString();
            }
            // new line after each row
            theString += '\n';
        }

        return theString;
    }
}
