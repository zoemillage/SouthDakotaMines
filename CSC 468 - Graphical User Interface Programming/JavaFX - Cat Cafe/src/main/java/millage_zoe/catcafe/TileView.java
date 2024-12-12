/**
 * Author: Zoe Millage
 * Description: This file contains the view for an individual tile
 */

package millage_zoe.catcafe;

import javafx.scene.control.Button;
import javafx.scene.text.TextAlignment;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class TileView extends Button implements PropertyChangeListener {
    private Tile theTile;


    public TileView(Tile tile) {
        theTile = tile;
        theTile.setTileView(this);
        this.setText(theTile.toString());
        this.setTextAlignment(TextAlignment.CENTER);
        theTile.addObserver(this);
    }



    /**
     * handles changes in the model from a next week call
     * @param evt A PropertyChangeEvent object describing the event source
     *          and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // GRADING: OBSERVE
        update();
    }



    /**
     * updates the tile's quick details on the button
     */
    public void update() {
        this.setText(theTile.toString());
        this.setTextAlignment(TextAlignment.CENTER);
    }

}
