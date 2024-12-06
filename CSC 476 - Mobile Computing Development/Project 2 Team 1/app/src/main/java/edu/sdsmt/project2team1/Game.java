/**
 * holds the game class and functions for scoring
 */

package edu.sdsmt.project2team1;

import android.os.Bundle;

import java.util.Random;

public class Game {
    /* game settings and variables */
    private final float LINE_COLLECT_PROBABILITY = .75f;
    private final float MIN_RECTANGLE_PROBABILITY = .15f;
    private final float MAX_RECTANGLE_PROBABILITY = .5f;
    private final int BOARD_SIZE = 5;

    /* Bundle Keys */
    private final String PLAYER_1_NAME  = "player1Name";
    private final String PLAYER_2_NAME  = "player2Name";
    private final String PLAYER_1_SCORE  = "player1Score";
    private final String PLAYER_2_SCORE  = "player2Score";
    private final String ROUND_NUMBER  = "roundNumber";
    private final String PLAYER_TURN  = "playerTurn";
    private final String BOARD = "board";

    /* Reference to the view */
    private final GameView gameView;

    /* Game state information */
    private String player1Name;
    private String player2Name;
    private int player1Score = 0;
    private int player2Score = 0;
    private int roundNumber = 1;
    private int playerTurn = 1;
    private int totalNumberOfRounds = 0;
    private int[][] board = new int[BOARD_SIZE][BOARD_SIZE];

    /* Collectable and collection sizes  */
    private float circleDiameter = 50;
    private float collectableRadius = 10;
    private float lineThickness = 50;
    private float lineCollectThreshold = collectableRadius;

    /* Locations of the collectables */
    private final float[][][] collectableLocations = new float[BOARD_SIZE][BOARD_SIZE][2];

    /**
     * Gabe Jerome
     * Set the game view and initialize game board at all uncaptured
     * @param view - reference to the game view
     */
    public Game(GameView view){
        int i;
        int j;

        gameView = view;

        for (i = 0; i < BOARD_SIZE; i++) {
            for (j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = 0;
            }
        }
    }



    /**
     * Gabe Jerome
     * Calculate the distance to the closest point on the line
     * @param x1 - x coordinate of end 1 of the line
     * @param y1 - y coordinate of end 1 of the line
     * @param x2 - x coordinate of end 2 of the line
     * @param y2 - y coordinate of end 2 of the line
     * @param collectableX - x coordinate of the center of the collectable
     * @param collectableY - y coordinate of the center of the collectable
     * @return the distance between the collectable and the closest point on the line
     */
    private float calculateDistanceToLine(float x1, float y1, float x2, float y2,
                                          float collectableX, float collectableY) {
        // Calculate the length of the line segment
        float length = (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));

        float dotProduct = dotProduct(x1, y1, x2, y2, collectableX, collectableY, length);

        // Clamp the dot product to be within the bounds of the line segment
        dotProduct = Math.max(0, Math.min(dotProduct, length));

        // Calculate the nearest point on the line segment
        float nearestX = x1 + (dotProduct * (x2 - x1) / length);
        float nearestY = y1 + (dotProduct * (y2 - y1) / length);

        // Calculate the distance between the point and the nearest point on the line segment
        return (float) Math.sqrt(Math.pow(collectableX - nearestX, 2) + Math.pow(collectableY - nearestY, 2));
    }



    /**
     * Gabe Jerome
     * Capture the closest collectable (dot capture)
     * @param x - x coordinate for center of capture circle
     * @param y - y coordinate for center of capture circle
     * @return the earned points of the collectable. 0 if board is empty, else 1
     */
    public int captureClosestCollectable(float x, float y){
        int i, j;
        int[] minIndex = {-1, -1};
        float minDistance = Integer.MAX_VALUE;
        float distance;
        float collectX;
        float collectY;

        if(isBoardEmpty())
            return 0;

        for(i = 0; i < BOARD_SIZE; i++){
            for(j = 0; j < BOARD_SIZE; j++){
                if(board[i][j] == 0) {
                    collectX = collectableLocations[i][j][0];
                    collectY = collectableLocations[i][j][1];
                    distance = (float) Math.sqrt(Math.pow(x - collectX, 2) + Math.pow(y - collectY, 2));

                    if (distance < minDistance) {
                        minDistance = distance;
                        minIndex[0] = i;
                        minIndex[1] = j;
                    }
                }
            }
        }

        board[minIndex[0]][minIndex[1]] = playerTurn;
        return 1;
    }



    /**
     * Gabe Jerome
     * Determine if the dot capture and the collectable are touching.
     * @param collectX - x coordinate for center of collectable
     * @param collectY - y coordinate for center of collectable
     * @param collectR - radius of the collectable
     * @param captureX - x coordinate for center of capture circle
     * @param captureY - y coordinate for center of capture circle
     * @param captureR - radius of the capture circle
     * @return true if the circles are touching, else false
     */
    public boolean circlesAreTouching(float collectX, float collectY, float collectR,
                                      float captureX, float captureY, float captureR) {
        // Calculate the distance between the centers of the circles
        float distance = (float) Math.sqrt(Math.pow(captureX - collectX, 2) + Math.pow(captureY - collectY, 2));

        // Check if the distance is less than or equal to the sum of the radii
        return distance <= (collectR + captureR);
    }



    /**
     * Gabe Jerome
     * Loop through collectibles and determine what collectables
     * should be captured by the circle.
     * @param x - x coordinate for center of circle
     * @param y - y coordinate for center of circle
     * @return number of earned points
     */
    public int circleCapture(float x, float y){
        int earnedScore = 0;
        int i, j;
        float collectableX;
        float collectableY;

        setCollectableLocations();

        for(i = 0; i < BOARD_SIZE; i++){
            for(j = 0; j < BOARD_SIZE; j++){
                collectableX = collectableLocations[i][j][0];
                collectableY = collectableLocations[i][j][1];

                // Check if circle and collectible are touching
                if(board[i][j] == 0 && circlesAreTouching(collectableX, collectableY, collectableRadius, x, y, circleDiameter / 2)){
                    board[i][j] = playerTurn;
                    earnedScore++;
                }
            }
        }

        // Capture closest collectable if none were under the circle
        if(earnedScore == 0){
            earnedScore = captureClosestCollectable(x, y);
        }

        return earnedScore;
    }



    /**
     * Gabe Jerome
     * Calculate the dot product between a line and a point
     * @param x1 - x coordinate of end 1 of the line
     * @param y1 - y coordinate of end 1 of the line
     * @param x2 - x coordinate of end 2 of the line
     * @param y2 - y coordinate of end 2 of the line
     * @param collectableX - x coordinate of the center of the collectable
     * @param collectableY - y coordinate of the center of the collectable
     * @param length - length of the line
     * @return the dot product between the line and the point
     */
    private float dotProduct(
            float x1,
            float y1,
            float x2,
            float y2,
            float collectableX,
            float collectableY,
            float length
    ){
        return ((collectableX - x1) * (x2 - x1) + (collectableY - y1) * (y2 - y1)) / length;
    }



    /**
     * Gabe Jerome
     * Flatten board array for bundling
     * @param array - 2D game board
     * @return the flattened game board
     */
    private int[] flattenArray(int[][] array){
        int i, j;
        int ctr = 0;
        int[] flatArray = new int[BOARD_SIZE * BOARD_SIZE];

        for (i = 0; i < BOARD_SIZE; i++){
            for (j = 0; j < BOARD_SIZE; j++){
                flatArray[ctr] = array[i][j];
                ctr++;
            }
        }

        return flatArray;
    }



    public int[][] getBoard() {return board;}



    public int getBoardSize() {
        return BOARD_SIZE;
    }



    public float getCircleDiameter() {
        return circleDiameter;
    }



    public float[][][] getCollectableLocations() {
        return collectableLocations;
    }



    public float getCollectableRadius() {
        return collectableRadius;
    }



    public String getPlayer1Name() {
        return player1Name;
    }



    public int getPlayer1Score() {
        return player1Score;
    }



    public String getPlayer2Name() {
        return player2Name;
    }



    public int getPlayer2Score() {
        return player2Score;
    }



    public int getPlayerTurn() {
        return playerTurn;
    }



    /**
     * Gabe Jerome
     * gets a random number
     * @return a random float
     */
    private float getRandomNumber(){
        Random random = new Random();
        return random.nextFloat();
    }



    /**
     * Gabe Jerome
     * Generate a probability for the rectangle capture odds
     * @param area- area of the rectangle
     * @return the probability that the rectangle should capture
     * (between MIN_RECTANGLE_PROBABILITY and MAX_RECTANGLE_PROBABILITY)
     */
    private double getRectangleProbability(float area){
        float boardSize = gameView.getHeight() * gameView.getWidth();
        float probabilityAreaThreshold = .2f * boardSize;
        if (area <= probabilityAreaThreshold) {
            return MAX_RECTANGLE_PROBABILITY;
        } else {
            // Scale the probability linearly based on the area
            double scaleFactor = (MAX_RECTANGLE_PROBABILITY - MIN_RECTANGLE_PROBABILITY) / (boardSize - probabilityAreaThreshold);
            double probability = MAX_RECTANGLE_PROBABILITY - scaleFactor * (area - probabilityAreaThreshold);
            // Ensure probability is within the specified range
            return Math.max(MIN_RECTANGLE_PROBABILITY, Math.min(MAX_RECTANGLE_PROBABILITY, probability));
        }
    }



    public int getRoundNumber() {
        return roundNumber;
    }



    public int getTotalNumberOfRounds() {
        return totalNumberOfRounds;
    }



    /**
     * Gabe Jerome
     * Loop through the board and check for uncaptured collectables
     * @return true if the board is empty, else false
     */
    public boolean isBoardEmpty(){
        int i, j;

        for(i = 0; i < BOARD_SIZE; i++){
            for(j = 0; j < BOARD_SIZE; j++){
                if(board[i][j] == 0)
                    return false;
            }
        }

        return true;
    }



    /**
     * Gabe Jerome
     * Determine if a collectable is in the range of the line to be collected
     * @param x1 - x coordinate of end 1 of the line
     * @param y1 - y coordinate of end 1 of the line
     * @param x2 - x coordinate of end 2 of the line
     * @param y2 - y coordinate of end 2 of the line
     * @param collectableX - x coordinate of the center of the collectable
     * @param collectableY - y coordinate of the center of the collectable
     * @return true if the center of the collectable is within the range of the line, else false
     */
    public boolean isCollectableInLineThreshold(float x1, float y1, float x2, float y2, float collectableX, float collectableY) {
        float distance = calculateDistanceToLine(x1, y1, x2, y2, collectableX, collectableY);

        float requiredDistanceToLine = lineThickness / 2 + collectableRadius + lineCollectThreshold;

        return distance <= requiredDistanceToLine;
    }



    /**
     * Gabe Jerome
     * Determine the collectable point is within the bounds of the rectangle.
     * @param x1 - x coordinate of corner 1 of the rectangle
     * @param y1 - y coordinate of corner 1 of the rectangle
     * @param x2 - x coordinate of corner 2 of the rectangle
     * @param y2 - y coordinate of corner 2 of the rectangle
     * @param collectableX - x coordinate of the center of the collectable
     * @param collectableY - y coordinate of the center of the collectable
     * @return true if the center of the collectable is in the bounds of the rectangle, else false
     */
    private boolean isInRectangle(
            float x1,
            float y1,
            float x2,
            float y2,
            float collectableX,
            float collectableY
    ){
        float maxX = Math.max(x1, x2);
        float minX = Math.min(x1, x2);
        float maxY = Math.max(y1, y2);
        float minY = Math.min(y1, y2);
        return (collectableX <= maxX && collectableX >= minX && collectableY <= maxY && collectableY >= minY);
    }



    /**
     * Gabe Jerome
     * Loop through collectibles and determine what collectables
     * should be captured by the line.
     * @param x1 - x coordinate of end 1 of the line
     * @param y1 - y coordinate of end 1 of the line
     * @param x2 - x coordinate of end 2 of the line
     * @param y2 - y coordinate of end 2 of the line
     * @return number of earned points
     */
    public int lineCapture(float x1, float y1, float x2, float y2){
        int earnedScore = 0;
        int i, j;
        float collectableX;
        float collectableY;

        setCollectableLocations();

        for(i = 0; i < BOARD_SIZE; i++){
            for(j = 0; j < BOARD_SIZE; j++){
                collectableX = collectableLocations[i][j][0];
                collectableY = collectableLocations[i][j][1];

                if(
                        board[i][j] == 0 &&
                                isCollectableInLineThreshold(x1, y1, x2, y2, collectableX, collectableY) &&
                                shouldLineCapture()
                ){
                    board[i][j] = playerTurn;
                    earnedScore++;
                }
            }
        }

        return earnedScore;
    }



    /**
     * Gabe Jerome
     * load all data from bundle
     * @param bundle - the data to load
     */
    public void loadInstanceState(Bundle bundle) {
        int[] flatBoard;

        player1Name = bundle.getString(PLAYER_1_NAME);
        player2Name = bundle.getString(PLAYER_2_NAME);
        player1Score = bundle.getInt(PLAYER_1_SCORE);
        player2Score = bundle.getInt(PLAYER_2_SCORE);
        roundNumber = bundle.getInt(ROUND_NUMBER);
        playerTurn = bundle.getInt(PLAYER_TURN);
        flatBoard = bundle.getIntArray(BOARD);

        board = unflattenArray(flatBoard);
    }



    /**
     * Gabe Jerome
     * Loop through collectibles and determine what collectables
     * should be captured by the rectangle.
     * @param x1 - x coordinate of corner 1 of the rectangle
     * @param y1 - y coordinate of corner 1 of the rectangle
     * @param x2 - x coordinate of corner 2 of the rectangle
     * @param y2 - y coordinate of corner 2 of the rectangle
     * @return number of earned points
     */
    public int rectangleCapture(float x1, float y1, float x2, float y2){
        int earnedScore = 0;
        int i, j;
        float collectableX;
        float collectableY;

        setCollectableLocations();

        for(i = 0; i < BOARD_SIZE; i++){
            for(j = 0; j < BOARD_SIZE; j++){
                collectableX = collectableLocations[i][j][0];
                collectableY = collectableLocations[i][j][1];

                // collectable is inside the bounds of the rectangle
                if(
                        board[i][j] == 0 &&
                                isInRectangle(x1, y1, x2, y2, collectableX, collectableY) &&
                                shouldRectangleCapture(x1, y1, x2, y2)
                ){
                    board[i][j] = playerTurn;
                    earnedScore++;
                }
            }
        }

        return earnedScore;
    }



    /**
     * Gabe Jerome
     * Bundle all game data that needs to stay persistent
     * @param bundle - holds all the saved data
     */
    public void saveInstanceState(Bundle bundle) {
        int[] flatBoard = flattenArray(board);

        bundle.putString(PLAYER_1_NAME, player1Name);
        bundle.putString(PLAYER_2_NAME, player2Name);
        bundle.putInt(PLAYER_1_SCORE, player1Score);
        bundle.putInt(PLAYER_2_SCORE, player2Score);
        bundle.putInt(ROUND_NUMBER, roundNumber);
        bundle.putInt(PLAYER_TURN, playerTurn);
        bundle.putIntArray(BOARD, flatBoard);
    }



    public void setBoard(int[][] board) {
        this.board = board;
    }



    public void setCircleDiameter(float circleDiameter) {
        this.circleDiameter = circleDiameter;
    }



    /**
     * Gabe Jerome
     * Reset the locations of the collectables in case of adjustment in
     * the view or the screen is rotated.
     */
    public void setCollectableLocations(){
        int i, j;
        float xLocation;
        float yLocation;
        float width = gameView.getWidth();
        float height = gameView.getHeight();

        // The margins between the outside collectables and the edge of the view
        float xMargin = width / (BOARD_SIZE * 2);
        float yMargin = height / (BOARD_SIZE * 2);

        /* Loop through points and set collectable locations as the midpoint of
         * each grid space.
         */
        for (i = 0; i < BOARD_SIZE; i++) {
            for (j = 0; j < BOARD_SIZE; j++) {
                xLocation = xMargin + (width / BOARD_SIZE) * i;
                yLocation = yMargin + (height / BOARD_SIZE) * j;

                collectableLocations[i][j][0] = xLocation;
                collectableLocations[i][j][1] = yLocation;
            }
        }
    }



    public void setCollectableRadius(float collectableRadius) {
        this.collectableRadius = collectableRadius;
    }



    public void setLineCollectThreshold(float lineCollectThreshold) {
        this.lineCollectThreshold = lineCollectThreshold;
    }



    public void setLineThickness(float lineThickness) {
        this.lineThickness = lineThickness;
    }



    public void setPlayer1Name(String name){
        player1Name = name;
    }



    public void setPlayer1Score(int player1Score) {
        this.player1Score = player1Score;
    }



    public void setPlayer2Name(String name){
        player2Name = name;
    }



    public void setPlayer2Score(int newScore){
        player2Score = newScore;
    }



    public void setPlayerTurn(int currPlayer){
        playerTurn = currPlayer;
    }



    public void setRoundNumber(int round){
        roundNumber = round;
    }



    public void setTotalNumberOfRounds(int rounds){totalNumberOfRounds = rounds;}



    /**
     * Gabe Jerome
     * Determine if a line should capture based on set line capture probability
     * @return true if line should capture, else false
     */
    private boolean shouldLineCapture(){
        return LINE_COLLECT_PROBABILITY > getRandomNumber();
    }



    /**
     * Gabe Jerome
     * Determine if a rectangle should capture based on its size
     * @param x1 - x coordinate of corner 1 of the rectangle
     * @param y1 - y coordinate of corner 1 of the rectangle
     * @param x2 - x coordinate of corner 2 of the rectangle
     * @param y2 - y coordinate of corner 2 of the rectangle
     * @return true if the rectangle should capture, else false
     */
    private boolean shouldRectangleCapture(
            float x1,
            float y1,
            float x2,
            float y2
    ){
        float area = Math.abs((x1 - x2) * (y1 - y2));

        double prob = getRectangleProbability(area);
        float rand = getRandomNumber();
        return prob > rand;
    }



    /**
     * Gabe Jerome
     * Unflatten game board for loading from bundle
     * @param flatArray - 1D game board
     * @return Unflattened 2D game board
     */
    private int[][] unflattenArray(int[] flatArray){
        int i, j;
        int ctr = 0;
        int[][] array2D = new int[BOARD_SIZE][BOARD_SIZE];

        for (i = 0; i < BOARD_SIZE; i++){
            for (j = 0; j < BOARD_SIZE; j++){
                array2D[i][j] = flatArray[ctr];
                ctr++;
            }
        }

        return array2D;
    }

}
