/**
 * Author: Zoe Millage
 * Description: Holds the model part of the game
 */

package edu.sdsmt.hcats_millage_zoe;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

import android.os.Bundle;

public class Game {
    private static String GRID = "grid";
    private static String HEIGHT = "height";
    private static String MOVES = "moves";
    private static String TREATS = "treats";
    private static String USING_TREAT = "usingATreat";
    private static String WIDTH = "width";

    private int cats = 40;
    private int caught = 0;
    private GameView gameView;
    private int[][] grid;
    private int height = 3;
    private int moves = 15;
    private int treats = 3;
    private boolean usingTreat = false;
    private int width = 3;



    public Game() {
        initializeGrid();
    }



    public Game(GameView view) {
        gameView = view;
        initializeGrid();
    }



    /**
     * convert a 1d array to a 2d for loading the game
     * @param flat the game as a 1d array
     */
    private void expandArr(int[] flat) {
        int count = 0;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                grid[i][j] = flat[count];
                count++;
            }
        }
    }



    /**
     * convert the game to a 1d array for bundle storage purposes
     * @return the game as a 1d array
     */
    private int[] flattenArr() {
        int dim = width * height;
        int count = 0;
        int[] flat = new int[dim];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                flat[count] = grid[i][j];
                count++;
            }
        }

        return flat;

    }


    /**
     * returns the number of cats at the given tile
     * @param i the tile's column
     * @param j the tile's row
     * @return the number of cats here
     */
    public int getCatsAt(int i, int j) {
        return grid[i][j];
    }



    public int getCatsCaught() {
        return caught;
    }



    public int getHeight() {
        return height;
    }



    public int getMoves() {
        return moves;
    }



    public int getTotalCatsLeft() {
        return cats;
    }



    public int getTreats() {
        return treats;
    }



    public int getWidth() {
        return width;
    }



    /**
     * sets everything except the collection to 5
     */
    private void initializeGrid() {
        grid = new int[width][height];

        for ( int i = 0; i < width; i++) {
            for ( int j = 0; j < height; j++) {
                grid[i][j] = 5;
            }
        }

        grid[width - 1][height - 1] = 0;
    }



    /**
     * you've lost if we have no turns left and haven't caught all the cats
     * @return if the player has lost
     */
    public boolean isLost() {
        return (moves <= 0 && caught <= 40);
    }



    public boolean isUsingTreat() {
        return usingTreat;
    }



    /**
     * you've won if you caught all the cats
     * @return if the player has won
     */
    public boolean isWon() {
        return caught >= 40;
    }



    /**
     * loads the game state, including the grid dimensions, moves and treats remaining, treat
     * usage state, and the cats in each grid tile
     * @param b the bundle to load from
     */
    public void loadInstanceState(Bundle b) {
        height = b.getInt(HEIGHT);
        width = b.getInt(WIDTH);
        moves = b.getInt(MOVES);
        treats = b.getInt(TREATS);
        usingTreat = b.getBoolean(USING_TREAT);

        int[] flatGrid = b.getIntArray(GRID);
        expandArr(flatGrid);
        updateTotals();
    }



    /**
     * resets the game state
     */
    public void reset() {
        moves = 15;
        treats = 3;
        initializeGrid();
        updateTotals();

    }



    /**
     * saves the game state, including the grid dimensions, moves and treats remaining, treat
     * usage state, and the cats in each grid tile
     * @param b the bundle to save to
     */
    public void saveInstanceState(Bundle b) {
        b.putInt(HEIGHT, height);
        b.putInt(WIDTH, width);
        b.putInt(MOVES, moves);
        b.putInt(TREATS, treats);
        b.putBoolean(USING_TREAT, usingTreat);

        int[] flatGrid = flattenArr();
        b.putIntArray(GRID, flatGrid);
    }



    public void setUsingTreat(boolean usingTreat) {
        this.usingTreat = usingTreat;
    }



    /**
     * goes through the grid in reverse row order, sweeping down cats based on
     * the larger of the given parameters
     * @param base the static number of cats to sweep
     * @param percent the percentage of cats to sweep
     */
    public void sweepDown(int base, int percent) {
        int movement;

        for ( int i = 0; i < width; i++) {
            for ( int j = height - 2; j >= 0; j--){
                // get whichever movement is higher
                movement = max(base, (grid[i][j] * percent)/100);

                // don't try to move more cats than the grid has
                movement = min(movement, grid[i][j]);

                // do the actual sweeping
                grid[i][j] -= movement;
                grid[i][j + 1] += movement;
            }
        }

        moves -= 1;
        updateTotals();
    }



    /**
     * goes through the grid in reverse columns order, sweeping cats right based on
     * the larger of the given parameters
     * @param base the static number of cats to sweep
     * @param percent the percentage of cats to sweep
     */
    public void sweepRight(int base, int percent) {
        int movement;

        for ( int i = width - 2; i >= 0; i--) {
            for ( int j = 0; j < height; j++){
                // get whichever movement is higher
                movement = max(base, (grid[i][j] * percent)/100);

                // don't try to move more cats than the grid has
                movement = min(movement, grid[i][j]);

                // do the actual sweeping
                grid[i][j] -= movement;
                grid[i + 1][j] += movement;
            }
        }

        moves -= 1;
        updateTotals();
    }



    /**
     * updates the caught and cats remaining values
     */
    private void updateTotals() {
        int count = 0;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (!(i == width - 1 && j == height - 1))
                    count += grid[i][j];
            }
        }

        cats = count;

        caught = grid[ width - 1][height - 1];

        if ( gameView != null)
            gameView.invalidate();
    }



    /**
     * decreases the treat count
     */
    public void useTreat() {
        if ( treats > 0) {
            treats -= 1;
            moves -= 1;

            if ( gameView != null)
                gameView.invalidate();
        }
    }

}
