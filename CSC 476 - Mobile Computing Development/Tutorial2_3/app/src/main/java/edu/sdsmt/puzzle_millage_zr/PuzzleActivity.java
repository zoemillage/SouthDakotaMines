/**
 * Author: Zoe Millage
 * Description: Handles the activity containing the puzzle and its gameplay
 */

package edu.sdsmt.puzzle_millage_zr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class PuzzleActivity extends AppCompatActivity {

    public static final String  PLAYER_NAME = "user_name";

    /**
     * The puzzle view in this activity's view
     */
    private PuzzleView puzzleView;

    /**
     * starts the puzzle activity
     * @param bundle holds the puzzle state if reinitializing
     *
     */
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_puzzle);

        if( getIntent() != null){
            String name = getIntent().getStringExtra(PLAYER_NAME);
            TextView view = findViewById(R.id.player_name);
            String message = getString(R.string.player) + ": " + name;
            view.setText(message);
        }

        puzzleView = this.findViewById(R.id.puzzleView);

        if(bundle != null) {
            // We have saved state
            puzzleView.loadInstanceState(bundle);
        }

    }



    /**
     * opens the menu to shuffle the puzzle midway through
     * @param menu The options menu in which you place your items
     * @return true when finished opening
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_puzzle, menu);
        return true;

    }



    /**
     * shuffles the puzzle if that option was selected
     * @param item The menu item that was selected
     * @return true when finished
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_shuffle) {
            puzzleView.getPuzzle().shuffle();
            puzzleView.invalidate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    /**
     * saves the puzzle state
     * @param bundle Bundle in which to place your saved state
     *
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);

        puzzleView.saveInstanceState(bundle);
    }
}