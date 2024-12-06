/**
 * Author: Zoe Millage
 * Description: Holds the view the puzzle is drawn on
 */

package edu.sdsmt.puzzle_millage_zr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;


/**
 * Custom view class for our Puzzle.
 */
public class PuzzleView extends View {

    /**
     * The actual puzzle
     */
    private Puzzle puzzle;

    public PuzzleView(Context context) {
        super(context);
        init(null, 0);
    }



    public PuzzleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }



    public PuzzleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }



    public Puzzle getPuzzle() {
        return puzzle;
    }



    private void init(AttributeSet attrs, int defStyle) {
        puzzle = new Puzzle(getContext(), this);
    }



    /**
     * Load the puzzle from a bundle
     * @param bundle The bundle we save to
     */
    public void loadInstanceState(Bundle bundle) {
        puzzle.loadInstanceState(bundle);
    }



    /**
     * draws the canvas and calls for the puzzle to be drawn
     * @param canvas the canvas on which the background will be drawn
     */
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        puzzle.draw(canvas);
    }



    /**
     * forwards touches to the puzzle
     * @param event The motion event.
     * @return true when the event is handled
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return puzzle.onTouchEvent(this, event);
    }



    /**
     * Save the puzzle to a bundle
     * @param bundle The bundle we save to
     */
    public void saveInstanceState(Bundle bundle) {
        puzzle.saveInstanceState(bundle);
    }
}