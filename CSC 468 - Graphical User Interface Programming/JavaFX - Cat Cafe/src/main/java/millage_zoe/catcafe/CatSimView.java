/**
 * Author: Zoe Millage
 * Description: This file contains the container view for
 *      all the tiles in the catfe
 */

package millage_zoe.catcafe;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.util.ArrayList;

public class CatSimView extends GridPane{
    private int cols = 3;
    private int rows = 3;
    private CafeSim theSim;
    private ArrayList<TileView> tiles;


    public CatSimView() {
        tiles = new ArrayList<TileView>();
    }



    /**
     * clear the current tiles so we can restart fresh
     */
    public void clearArea() {
        this.getChildren().clear();
        this.getRowConstraints().clear();
        this.getColumnConstraints().clear();
    }



    /**
     * change the number of tiles in the catfe
     * @param col the new number of columns
     * @param row the new number of rows
     */
    public void resize( int col, int row){
        clearArea();
        rows = row;
        cols = col;

        theSim.resize(col, row);
        setModel(theSim);
    }



    /**
     * attached a model to the view and initializes
     * the tile views based on the tiles
     * @param model the model to connect
     */
    public void setModel(CafeSim model){
        int i, j;
        int tileNum = 0;

        tiles.clear();
        theSim = model;

        // initialize the tile views
        ArrayList<Tile> modelTiles = theSim.getTiles();

        TileView temp;

        for ( i = 0; i < cols; i++) {
            for ( j = 0; j < rows; j++) {
                temp = new TileView(modelTiles.get(tileNum));
                tiles.add(temp);

                temp.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                CatSimView.setHgrow(temp, Priority.ALWAYS);
                CatSimView.setVgrow(temp, Priority.ALWAYS);
                GridPane.setHalignment(temp, HPos.CENTER);
                this.add(temp, j, i);
                tileNum++;
            }
        }

        String tempy = "";

    }



    /**
     * gets the cafe's data as a string: the current week,
     * the number of non-empty tiles, the current funds, and the total
     * number of cats adopted.
     * @return the cafe's data
     */
    @Override
    public String toString() {
        return theSim.toString();
    }
}
