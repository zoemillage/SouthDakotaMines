/**
 * @Author Zoe Millage
 * @Brief The FixRoads visitor, which, when visiting a street, gets the tiles
 * to the direct left, top, bottom, and right, visits those tiles with the
 * IsRoad visitor, and changes the road's symbol to "connect" to the
 * surrounding roads.
 */

package millage_zoe;

public class FixRoads implements CityVisitor{

    /**
     * @brief this intentionally does nothing
     * @param b - the visited building
     * @param c - the current city
     */
    @Override
    public void acceptBuilding(Building b, City c) {

    }



    /**
     * @brief this intentionally does nothing
     * @param e - the visited empty
     * @param c - the current city
     */
    @Override
    public void acceptEmpty(Empty e, City c) {

    }



    /**
     * @brief this intentionally does nothing
     * @param g - the visited greenspace
     * @param c - the current city
     */
    @Override
    public void acceptGreenspace(Greenspace g, City c) {

    }



    /**
     * @brief changes the tile character based on nearby streets
     * @param s - the visited street
     * @param c - the current city
     */
    @Override
    public void acceptStreet(Street s, City c) {
        IsRoad isRoadVisitor = new IsRoad();

        Tile[] area = new Tile[4];
        boolean[] exists = new boolean[4];
        boolean[] isARoad = new boolean[4];

        int row = s.getRow();
        int col = s.getCol();
        int maxRow = c.rows - 1;
        int maxCol = c.cols - 1;
        int i;

        for ( i = 0; i < 4; i++ ) {
            exists[i] = false;
            isARoad[i] = false;
        }

        // check if tiles to the left, top, bottom, and right exist
        if ( col > 0 ) {
            // add them to the lists if they exist
            exists[0] = true;
            area[0] = c.getTile(row, col - 1);
        }

        if ( row > 0 ) {
            exists[1] = true;
            area[1] = c.getTile(row - 1, col );
        }

        if ( row < maxRow ) {
            exists[2] = true;
            area[2] = c.getTile(row + 1, col );
        }

        if ( col < maxCol ) {
            exists[3] = true;
            area[3] = c.getTile(row, col + 1 );
        }

        //GRADING: NESTED.
        // check if the left, top, bottom, and right tiles are roads
        for ( i = 0; i < 4; i++ ) {
            if ( exists[i] ){
                area[i].accept( isRoadVisitor );
                isARoad[i] = isRoadVisitor.getIsRoad();
            }
        }

        // fix this road
        s.setAdjacencies( isARoad[0], isARoad[1], isARoad[2], isARoad[3] );
    }



    /**
     * @brief this intentionally does nothing
     * @param w - the visited water
     * @param c - the current city
     */
    @Override
    public void acceptWater(Water w, City c) {

    }
}