//  Program: Android - Herding Cats
//  Author: Zoe Millage
//  Class: CSC-468, 2024s
//  Date Due: 25 April 2024
//  Description: Cats are all over, herd them into the the correct zone
//      before you run out of turns! When there aren't many cats left,
//      use treats to help coax them.
//      This file contains the main activity for the game; initializes
//      all the values and handles button presses
//
//      Last tier completed: ___Tier 4___
//
//
//        __Hopefully__ Pulled the most recent unit tests at submission time, and ensure they still pass
//        __Hopefully__	All grading tags added if the tier was reached
//
//
//        Tier 1: Model		42
//        __Hopefully__	Unit Tests all pass
//
//        Tier 2: Connect Views		16
//        __Hopefully__	Unit Tests all pass
//
//        Tier 3a: State Machine/Event Rules *	36
//        __hopefully__	Framework there
//        __hopefully__	Unit Tests all pass
//
//        Tier 3b: Floating Action		18
//        __Hopefully__	All buttons there
//        __Hopefully__	Icons set and distinguishable
//        __Hopefully__	Opens/closes properly
//        __Hopefully__	Player color updated.
//
//        Tier 3c: Layout **	(-50% each line if fails in on orientation)	26
//        __Hopefully__	Custom’s View’s aspect ratio constant
//        __Hopefully__	Relative size of objects in view maintained
//        __Hopefully__	Works in required screen sizes
//
//        Tier 3d: Rotation		20
//        __Hopefully__	Required state saved on rotation
//
//        Tier 3e: Unit Test		10
//        __Hopefully__	Test there
//        __Hopefully__	Updated all values to support check
//        __Hopefully__	Triggered rotation
//        __Hopefully__	Checked all values after rotation (does NOT require passing since those points are in 3d)
//
//        Tier 4: Extensions		30
//        Extension 1: <1i> <10 points> <make the game not 1:1>: <the game is constantly at 10:8>
//        Extension 2: <id> <5 points> <add 1+ more player appearance options>: <4th option on the
//              floating action button gives a color picker>
//        Extension 3: <1e> <10 points> <implement a color picker to affect something on screen>: <4th option on the
//              floating action button gives a player color picker>
//        Extension 4: <1j> <10 points> <make a custom icon with all sizes>: <minimize the app, look at its icon. Green cat>
//        Extension 5: <1k> <5 points> <put thin red border around game area if treat is active>: <use a treat>
//
//
//

package edu.sdsmt.hcats_millage_zoe;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    // tag for info passed to this activity
    public final static String RETURN_MESSAGE = "edu.sdsmt.HCatsMillageZR.RETURN_COLOR";

    private static String SHOW_COLORS = "showColors";
    private static String SHOW_DIALOGUE = "showDialogue";
    // color picker launcher
    ActivityResultLauncher<Intent> colorLauncher;
    private FloatingActionButton buttonBlue;
    private FloatingActionButton buttonGreen;
    private FloatingActionButton buttonFloat;
    private FloatingActionButton buttonSeafoam;
    private FloatingActionButton buttonSelector;
    private TextView caught;
    private GameView gameView;
    private StateMachine machine;
    private TextView moves;
    private boolean showColors = false;
    private boolean showDialogue = false;
    private Game theGame;
    private TextView treats;


    /**
     * converts a dp value to px
     * @param dp the values in dp
     * @return the value in px
     */
    public float DpToPx(float dp) {
        float pxPerDp = (float) getResources()
                .getDisplayMetrics().densityDpi;

        pxPerDp = pxPerDp / DisplayMetrics.DENSITY_DEFAULT;
        return dp * pxPerDp;
    }



    /**
     * closes the floating action button menu
     */
    private void closeColorMenu() {
        showColors = false;
        buttonBlue.animate().translationX(0);
        buttonSeafoam.animate().translationX(0);
        buttonGreen.animate().translationX(0);
        buttonSelector.animate().translationX(0);
        buttonBlue.animate().alpha(0);
        buttonSeafoam.animate().alpha(0);
        buttonGreen.animate().alpha(0);
        buttonSelector.animate().alpha(0);
    }



    public Game getGame() {
        return theGame;
    }



    public StateMachine getStateMachine() {
        return machine;
    }



    /**
     * loads the instance state, loading the dialog and floating action button states,
     * the game, game view, and state machine
     * @param b the bundle to load from
     */
    private void loadInstanceState(Bundle b) {
        // get values
        showColors = b.getBoolean(SHOW_COLORS);
        showDialogue = b.getBoolean(SHOW_DIALOGUE);

        theGame.loadInstanceState(b);
        gameView.loadInstanceState(b);
        machine.loadInstanceState(b);

        // tint the floating action button to the player color
        buttonFloat.setBackgroundTintList(ColorStateList.valueOf(gameView.getColor()));

        // display the floating menu or dialog if appropriate
        if (showColors)
            showColorMenu();

        if (showDialogue)
            makeDialog();
    }



    /**
     * Creates a dialog on game end
     */
    public void makeDialog() {
        showDialogue = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // set text
        if (theGame.isWon()) {
            builder.setTitle(R.string.you_won);
            builder.setMessage(R.string.won_reason);
        } else {
            builder.setTitle(R.string.you_lose);
            builder.setMessage(R.string.lose_reason);
        }

        // reset the game whenever the dialog closes
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                theGame.reset();
                machine.reset();
                updateUI();
                showDialogue = false;
            }
        });

        // add a close button
        builder.setPositiveButton(R.string.restart, (dialog, which) -> {
            showDialogue = false;
        });

        builder.show();
    }



    /**
     * tints the players blue
     * @param view the triggering button
     */
    public void onBlue(View view) {
        int color = getResources().getColor(R.color.light_blue_400, null);
        gameView.tint(color);
        buttonFloat.setBackgroundTintList(ColorStateList.valueOf(color));
        closeColorMenu();
        gameView.invalidate();
    }



    /**
     * opens or closes the floating action menu
     * @param view the triggering button
     */
    public void onBurst(View view) {
        if (!showColors)
            showColorMenu();
        else
            closeColorMenu();
    }



    /**
     * opens a ColorSelector activity
     * @param view the triggering button
     */
    public void onColorPicker(View view) {
        Intent intent = new Intent(this, ColorSelector.class);

        int selectedColor = gameView.getColor();

        intent.putExtra(ColorSelector.EXTRA_MESSAGE, selectedColor);
        colorLauncher.launch(intent);
    }



    /**
     * initializes all the class values
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // draw on the screen
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // initialize values
        gameView = findViewById(R.id.gameArea);
        theGame = new Game(gameView);
        machine = new StateMachine(this, theGame);
        gameView.setGame(theGame);

        // get all the views
        moves = findViewById(R.id.moves);
        treats = findViewById(R.id.treats);
        caught = findViewById(R.id.caught);
        buttonFloat = findViewById(R.id.floatingMain);
        buttonBlue = findViewById(R.id.floatingBlue);
        buttonGreen = findViewById(R.id.floatingGreen);
        buttonSeafoam = findViewById(R.id.floatingSeafoam);
        buttonSelector = findViewById(R.id.floatingPicker);

        // load from bundle if applicable
        if (savedInstanceState != null)
            loadInstanceState(savedInstanceState);


        // register for the SelectorActivity if needed
        ActivityResultContracts.StartActivityForResult contract =
                new ActivityResultContracts.StartActivityForResult();
        colorLauncher =
                registerForActivityResult(contract, (result) ->
                {
                    // open the activity, return its result, and color the players and button if appropriate
                    int resultCode = result.getResultCode();
                    if (resultCode == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        int value = data.getIntExtra(RETURN_MESSAGE, 0);
                        gameView.tint(value);
                        buttonFloat.setBackgroundTintList(ColorStateList.valueOf(value));
                        closeColorMenu();
                    }
                });

        updateUI();
    }



    /**
     * sweeps cats down
     * @param view the triggering button
     */
    public void onDown(View view) {
        machine.doTask(true);
        updateUI();

        if (theGame.getMoves() < 0) {
            makeDialog();
        }
    }



    /**
     * tints the players green
     * @param view the triggering button
     */
    public void onGreen(View view) {
        int color = getResources().getColor(R.color.green, null);
        gameView.tint(color);
        buttonFloat.setBackgroundTintList(ColorStateList.valueOf(color));
        closeColorMenu();
        gameView.invalidate();
    }



    /**
     * Resets the game
     * @param view the triggering button
     */
    public void onReset(View view) {
        theGame.reset();
        machine.reset();
        updateUI();
    }



    /**
     * sweeps cats right
     * @param view the triggering button
     */
    public void onRight(View view) {
        machine.doTask(false);
        updateUI();

        if (theGame.getMoves() < 0) {
            makeDialog();
        }
    }



    /**
     * saves the state, including the game, game view, machine, and dialogue and floating button states
     * @param outState Bundle in which to place your saved state.
     *
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(SHOW_COLORS, showColors);
        outState.putBoolean(SHOW_DIALOGUE, showDialogue);

        theGame.saveInstanceState(outState);
        gameView.saveInstanceState(outState);
        machine.saveInstanceState(outState);
    }



    /**
     * tints the players seafoam green
     * @param view
     */
    public void onSeafoam(View view) {
        int color = getResources().getColor(R.color.blue_green, null);
        gameView.tint(color);
        buttonFloat.setBackgroundTintList(ColorStateList.valueOf(color));
        closeColorMenu();
        gameView.invalidate();
    }



    /**
     * uses a treat if valid
     * @param view the triggernig button
     */
    public void onTreat(View view) {
        machine.useTreat();
        updateUI();
    }



    /**
     * opens the floating action button menu
     */
    private void showColorMenu() {
        showColors = true;
        buttonBlue.animate().translationX(-DpToPx(50));
        buttonSeafoam.animate().translationX(-DpToPx(90));
        buttonGreen.animate().translationX(-DpToPx(130));
        buttonSelector.animate().translationX(-DpToPx(170));
        buttonBlue.animate().alpha(1f);
        buttonSeafoam.animate().alpha(1f);
        buttonGreen.animate().alpha(1f);
        buttonSelector.animate().alpha(1f);
    }



    /**
     * update the visible values for cats caught, moves, and trest
     */
    private void updateUI() {
        caught.setText(String.valueOf(theGame.getCatsCaught()));
        moves.setText(String.valueOf(theGame.getMoves()));
        treats.setText(String.valueOf(theGame.getTreats()));
    }
}


