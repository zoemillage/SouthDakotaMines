/**
 * Author: Zoe Millage
 * Description: Holds the puzzle class, which holds
 * pieces and the overall state of the puzzle
 */

package edu.sdsmt.puzzle_millage_zr;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class Puzzle {

    /**
     * Percentage of the display width or height that
     * is occupied by the puzzle
     */
    final static float SCALE_IN_VIEW = 0.9f;


    /**
     * This variable is set to a piece we are dragging. If
     * we are not dragging, the variable is null.
     */
    private PuzzlePiece dragging = null;

    /**
     * Most recent relative X touch when dragging
     */
    private float lastRelX;

    /**
     * Most recent relative Y touch when dragging
     */
    private float lastRelY;

    /**
     * Left margin in pixels
     */
    private int marginX;


    /**
     * Top margin in pixels
     */
    private int marginY;

    /**
     * Paint for filling the area the puzzle's in
     */
    private final Paint fillPaint;

    /**
     * Collection of puzzle pieces
     */
    public ArrayList<PuzzlePiece> pieces = new ArrayList<>();

    /**
     * Completed puzzle bitmap
     */
    private final Bitmap puzzleComplete;

    /**
     * The size of the puzzle in pixels
     */
    private int puzzleSize;

    /**
     * The view containing this puzzle
     */
    private PuzzleView puzzleView;

    /**
     * Random number generator
     */
    private final static Random random = new Random();

    /**
     * How much we scale the puzzle pieces
     */
    private float scaleFactor;

    /**
     * The name of the bundle keys to save the puzzle
     */
    private final static String LOCATIONS = "Puzzle.locations";
    private final static String IDS = "Puzzle.ids";


    public Puzzle(Context context, PuzzleView puzzleView) {
        this.puzzleView = puzzleView;

        // Create paint for filling the area the puzzle will
        // be solved in
        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(0xffcccccc);

        // Load the solved puzzle image
        puzzleComplete = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.grubby_done);

        // Load the puzzle pieces
        pieces.add(new PuzzlePiece(context, R.drawable.grubby1, 0.264f, 0.175f));
        pieces.add(new PuzzlePiece(context, R.drawable.grubby2, 0.701f, 0.239f));
        pieces.add(new PuzzlePiece(context, R.drawable.grubby3, 0.751f, 0.522f));
        pieces.add(new PuzzlePiece(context, R.drawable.grubby4, 0.335f, 0.477f));
        pieces.add(new PuzzlePiece(context, R.drawable.grubby5, 0.662f, 0.792f));
        pieces.add(new PuzzlePiece(context, R.drawable.grubby6, 0.254f, 0.818f));

        shuffle();
    }


    /**
     *  Draws the puzzle area and calls the pieces to draw themselves.
     * @param canvas the canvas to draw on
     */
    public void draw(Canvas canvas){
        int wid = canvas.getWidth();
        int hit = canvas.getHeight();

        // Determine the minimum of the two dimensions
        int minDim = Math.min(wid, hit);

        puzzleSize = (int)(minDim * SCALE_IN_VIEW);

        // Compute the margins so we center the puzzle
        marginX = (wid - puzzleSize) / 2;
        marginY = (hit - puzzleSize) / 2;

        // Draw the outline of the puzzle
        canvas.drawRect(marginX, marginY, marginX + puzzleSize,
                marginY + puzzleSize, fillPaint);

        scaleFactor = (float)puzzleSize / (float)puzzleComplete.getWidth();

        canvas.save();
        canvas.translate(marginX, marginY);
        canvas.scale(scaleFactor, scaleFactor);
        canvas.restore();

        for(PuzzlePiece piece : pieces) {
            piece.draw(canvas, marginX, marginY, puzzleSize, scaleFactor);
        }
    }



    /**
     * Determine if the puzzle is done!
     * @return true if puzzle is done
     */
    public boolean isDone() {
        for(PuzzlePiece piece : pieces) {
            if(!piece.isSnapped()) {
                return false;
            }

        }

        return true;
    }



    /**
     * Read the puzzle from a bundle
     * @param bundle The bundle we save to
     */
    public void loadInstanceState(Bundle bundle) {
        float [] locations = bundle.getFloatArray(LOCATIONS);
        int [] ids = bundle.getIntArray(IDS);

        for(int i=0; i<ids.length-1; i++) {

            // Find the corresponding piece
            // We don't have to test if the piece is at i already,
            // since the loop below will fall out without it moving anything
            for(int j=i+1;  j<ids.length;  j++) {
                if(ids[i] == pieces.get(j).getId()) {
                    // We found it
                    // Yay...
                    // Swap the pieces
                    PuzzlePiece t = pieces.get(i);
                    pieces.set(i, pieces.get(j));
                    pieces.set(j, t);
                }
            }
        }

        for(int i=0;  i<pieces.size(); i++) {
            PuzzlePiece piece = pieces.get(i);
            piece.setX(locations[i*2]);
            piece.setY(locations[i*2+1]);
        }
    }



    /**
     * Handle a release of a touch message.
     * @param x x location for the touch release, relative to the puzzle - 0 to 1 over the puzzle
     * @param y y location for the touch release, relative to the puzzle - 0 to 1 over the puzzle
     * @return true if the touch is handled
     */
    private boolean onReleased(View view, float x, float y) {

        if(dragging != null) {
            if (dragging.maybeSnap()){

                if(isDone()) {
                    // The puzzle is done
                    // Instantiate a dialog box builder
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                    ShuffleListener listener = new ShuffleListener();

                    // Parameterize the builder
                    builder.setTitle(R.string.hurrah);
                    builder.setMessage(R.string.completed_puzzle);
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setNegativeButton(R.string.shuffle, listener);

                    // Create the dialog box and show it
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

                // force snapped pieces to render below
                pieces.remove(dragging);
                pieces.add(0, dragging);

                view.invalidate();
            }

            dragging = null;
            return true;
        }

        return false;
    }



    /**
     *  a dialogue with the ability to shuffle the puzzle
     */
    private class ShuffleListener implements DialogInterface.OnClickListener {

        /**
         * Causes the puzzle pieces to get shuffled
         * @param dialog the dialog that received the click
         * @param which the button that was clicked
         */
        @Override
        public void onClick(DialogInterface dialog, int which) {
            shuffle();
            puzzleView.invalidate();
        }
    }



    /**
     * Handle a touch message. This is when we get an initial touch
     * @param x x location for the touch, relative to the puzzle - 0 to 1 over the puzzle
     * @param y y location for the touch, relative to the puzzle - 0 to 1 over the puzzle
     * @return true if the touch is handled
     */
    private boolean onTouched(float x, float y) {

        // Check each piece to see if it has been hit
        // We do this in reverse order so we find the pieces in front
        for(int p=pieces.size()-1; p>=0;  p--) {
            if(pieces.get(p).hit(x, y, puzzleSize, scaleFactor)) {
                // We hit a piece!
                dragging = pieces.get(p);
                lastRelX = x;
                lastRelY = y;

                pieces.remove(p);
                pieces.add(dragging);
                return true;
            }
        }

        return false;
    }



    /**
     * Handle a touch event from the view.
     * @param view The view that is the source of the touch
     * @param event The motion event describing the touch
     * @return true if the touch is handled.
     */
    public boolean onTouchEvent(View view, MotionEvent event) {
        // Convert an x,y location to a relative location in the
        // puzzle.
        float relX = (event.getX() - marginX) / puzzleSize;
        float relY = (event.getY() - marginY) / puzzleSize;


        switch(event.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
                return onTouched(relX, relY);

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                return onReleased(view, relX, relY);

            case MotionEvent.ACTION_MOVE:
                // If we are dragging, move the piece
                if(dragging != null) {

                    // move to the new location and force a redraw
                    dragging.move(relX - lastRelX, relY - lastRelY);
                    view.invalidate();

                    lastRelX = relX;
                    lastRelY = relY;
                    return true;
                }
                break;
        }

        return false;
    }



    /**
     * Save the puzzle to a bundle
     * @param bundle The bundle we save to
     */
    public void saveInstanceState(Bundle bundle) {
        float [] locations = new float[pieces.size() * 2];
        int [] ids = new int[pieces.size()];

        for(int i=0;  i<pieces.size(); i++) {
            PuzzlePiece piece = pieces.get(i);
            locations[i*2] = piece.getX();
            locations[i*2+1] = piece.getY();
            ids[i] = piece.getId();
        }

        bundle.putFloatArray(LOCATIONS, locations);
        bundle.putIntArray(IDS,  ids);
    }



    /**
     * Shuffle the puzzle pieces
     */
    public void shuffle() {
        for(PuzzlePiece piece : pieces) {
            piece.shuffle(random);
        }
    }

}
