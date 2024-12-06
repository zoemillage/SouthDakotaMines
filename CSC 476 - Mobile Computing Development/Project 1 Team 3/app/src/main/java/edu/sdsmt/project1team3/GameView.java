/**
 * Holds info related to the game view, including resizing and drawing
 */

package edu.sdsmt.project1team3;

import static java.lang.Math.max;
import static java.lang.Math.min;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;


public class GameView extends View {
    // helps to keep the view at 4:3 aspect ratio
    final static float ASPECT_RATIO = 1.1f;
    final static int DOT = 0;
    final static int RECTANGLE = 1;

    // save/load tags
    private final String ANGLE_X = "angleX";
    private final String ANGLE_Y = "angleY";
    private final String CAPTURE_METHOD = "captureMethod";
    private final String DISTANCE = "distance";
    private final String ROUND = "round";
    private final String SCALE = "scale";
    private final String X = "x";
    private final String Y = "y";


    // paint used for the game boarder
    private Paint boarderPaint;

    private int captureMethod;

    // semi-transparent in the current player's color for moving the capture
    private Paint capturePaint;

    // default fill for collectables
    private Paint defaultPaint;

    // dot capture method state
    private Dot theDot;

    // If we haven't drawn before, we'll need to initialize some values for the game/drawings
    private boolean drew = false;

    // the game with its states
    private Game theGame;

    // height of the view
    private int height = -1;

    // max possible height of the view
    private int heightBounds;

    // line capture method state
    private Line theLine;

    // colors for each player
    private int player1Color;

    private int player1ColorTrans;

    private int player2Color;

    private int player2ColorTrans;

    // blue fill for player 1
    private Paint player1Paint;

    // red fill for player 2
    private Paint player2Paint;

    // rectangle capture method state
    private Rectangle theRect;

    // touch statuses
    private Touch touch1 = new Touch();
    private Touch touch2 = new Touch();

    // width of the view
    private int width = -1;

    // max possible width of the view
    private int widthBounds;


    /**
     * parameters for the dot capture
     */
    private static class Dot {
        public float x = 0;
        public float y = 0;
    }

    /**
     * parameters for the line capture
     */
    private static class Line {
        public float angleX = 1f;
        public float angleY = 0;
        //distance between the two points
        public float distance = 0;
        public float x = 0;
        public float y = 0;
    }


    /**
     * parameters for the rectangle capture
     */
    private static class Rectangle {
        public float scale = 1f;
        //distance from center point
        public float distance = 0;
        public float x = 0;
        public float y = 0;
    }


    /**
     * holds info used to handle touch(es)
     */
    private static class Touch {
        public float dX = 0;
        public float dY = 0;
        public int id = -1;
        public float lastX = 0;
        public float lastY = 0;
        public float x = 0;
        public float y = 0;


        /**
         * Zoe Millage
         * gets delta x and delta y
         */
        public void computeDeltas() {
            dX = x - lastX;
            dY = y - lastY;
        }


        /**
         * Zoe Millage
         * copies the current x/y values into lastX/Y
         */
        public void updateLast() {
            lastX = x;
            lastY = y;
        }
    }



    public GameView(Context context) {
        super(context);
        init(context, null, 0);
    }



    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }



    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }



    /**
     * Gabe Jerome
     * Adds the given points to player 1's score
     * @param earnedPoints the number of points to add
     */
    public void addToPlayer1Score(int earnedPoints) {
        theGame.setPlayer1Score(theGame.getPlayer1Score() + earnedPoints);
    }



    /**
     * Gabe Jerome
     * Adds the given points to player 2's score
     * @param earnedPoints the number of points to add
     */
    public void addToPlayer2Score(int earnedPoints) {
        theGame.setPlayer2Score(theGame.getPlayer2Score() + earnedPoints);
    }



    /**
     * Gabe Jerome
     * initiate a capture
     * @return the number of points gained from the capture
     */
    public int capture() {
        int newScore = 0;
        float[] dotTouchTemp;
        float[][] touchTemp;

        // capture with a dot
        if (captureMethod == 0) {
            dotTouchTemp = getDotCenter();
            newScore = theGame.circleCapture(dotTouchTemp[0], dotTouchTemp[1]);

        // capture with a rectangle
        } else if (captureMethod == 1) {
            touchTemp = getRectangleCorners();
            newScore = theGame.rectangleCapture(touchTemp[0][0], touchTemp[0][1], touchTemp[1][0], touchTemp[1][1]);

        // capture with a line
        } else if (captureMethod == 2) {
            touchTemp = getLinePoints();
            newScore = theGame.lineCapture(touchTemp[0][0], touchTemp[0][1], touchTemp[1][0], touchTemp[1][1]);
        }
        return newScore;
    }



    /**
     * Zoe Millage
     * get the angle between the two touches
     */
    public float computeAngle(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.toDegrees(Math.atan2(dy, dx));
    }



    /**
     * Zoe Millage
     * draws the boarder of the game view
     * @param canvas - the canvas to draw on
     */
    public void drawBoarder(Canvas canvas) {
        // check if we're using the right player's color
        if (theGame.getPlayerTurn() == 1) {
            if (boarderPaint.getColor() != player1Color)
                boarderPaint.setColor(player1Color);
        } else {
            if (boarderPaint.getColor() != player2Color)
                boarderPaint.setColor(player2Color);
        }

        // draw the boarder
        canvas.drawLine(0, 0, width, 0, boarderPaint);
        canvas.drawLine(0, 0, 0, height, boarderPaint);
        canvas.drawLine(width, height, width, 0, boarderPaint);
        canvas.drawLine(width, height, 0, height, boarderPaint);
    }



    /**
     * Zoe Millage
     * draws the selected capture option
     * @param canvas - the canvas to draw on
     */
    public void drawCapture(Canvas canvas) {
        // change the color based on whose turn it is
        if (theGame.getPlayerTurn() == 1) {
            if (capturePaint.getColor() != player1ColorTrans)
                capturePaint.setColor(player1ColorTrans);
        } else {
            if (capturePaint.getColor() != player2ColorTrans)
                capturePaint.setColor(player2ColorTrans);
        }


        if (captureMethod == DOT) {
            // draw the dot
            canvas.drawOval(theDot.x, theDot.y, theDot.x + theGame.getCircleDiameter(),
                    theDot.y + theGame.getCircleDiameter(), capturePaint);
        } else if (captureMethod == RECTANGLE) {
            // draw the rectangle
            float distance = theRect.distance * theRect.scale;
            canvas.drawRect(theRect.x - distance, theRect.y - distance,
                    theRect.x + distance, theRect.y + distance,
                    capturePaint);
        } else {
            // draw the line
            canvas.drawLine(theLine.x,
                    theLine.y,
                    theLine.x + theLine.distance * theLine.angleX,
                    theLine.y + theLine.distance * theLine.angleY, capturePaint);

        }
    }



    /**
     * Zoe Millage
     * draws the collectables as a grid
     * @param canvas - the canvas to draw on
     */
    public void drawCollectables(Canvas canvas) {
        int i, j;
        float x, y;

        // set collectable size and position if they haven't been already
        if (!drew) {
            theGame.setCollectableLocations();
            setCollectionSizes();
            drew = true;
        }

        // get game board info
        float[][][] collectables = theGame.getCollectableLocations();
        int[][] boardClaims = theGame.getBoard();
        int boardSize = theGame.getBoardSize();
        float collectableSize = theGame.getCollectableRadius();

        // draw the collectables
        for (i = 0; i < boardSize; i++) {
            for (j = 0; j < boardSize; j++) {
                x = collectables[i][j][0];
                y = collectables[i][j][1];


                // draw the collectable in the correct color
                if (boardClaims[i][j] == 1)
                    canvas.drawOval(x - collectableSize, y - collectableSize,
                            x + collectableSize, y + collectableSize, player1Paint);

                else if (boardClaims[i][j] == 2)
                    canvas.drawOval(x - collectableSize, y - collectableSize,
                            x + collectableSize, y + collectableSize, player2Paint);

                else
                    canvas.drawOval(x - collectableSize, y - collectableSize,
                            x + collectableSize, y + collectableSize, defaultPaint);
            }
        }
    }



    public int getCaptureMethod() {
        return captureMethod;
    }


    /**
     * Gabe Jerome
     * gets the center of the dot capture
     * @return the dot capture's current center
     */
    public float[] getDotCenter() {
        float[] temp = new float[2];
        temp[0] = theDot.x + theGame.getCircleDiameter() / 2;
        temp[1] = theDot.y + theGame.getCircleDiameter() / 2;
        return temp;
    }



    /**
     * Gabe Jerome
     * gets the boundary points of the line
     * @return the points of the line
     */
    public float[][] getLinePoints() {
        float[][] temp = new float[2][2];
        temp[0][0] = theLine.x;
        temp[0][1] = theLine.y;
        temp[1][0] = theLine.x + theLine.distance * theLine.angleX;
        temp[1][1] = theLine.y + theLine.distance * theLine.angleY;
        return temp;
    }



    public String getPlayer1Name() {
        return theGame.getPlayer1Name();
    }



    public String getPlayer2Name() {
        return theGame.getPlayer2Name();
    }



    public int getPlayer1Score() {
        return theGame.getPlayer1Score();
    }



    public int getPlayer2Score() {
        return theGame.getPlayer2Score();
    }



    public int getPlayerTurn() {
        return theGame.getPlayerTurn();
    }



    /**
     * Gabe Jerome
     * gets the corners of the rectangle based on its center and proportions
     * @return the rectangle's corners
     */
    public float[][] getRectangleCorners() {
        float[][] temp = new float[2][2];
        float distance = theRect.distance * theRect.scale;
        temp[0][0] = theRect.x - distance;
        temp[0][1] = theRect.y - distance;
        temp[1][0] = theRect.x + distance;
        temp[1][1] = theRect.y + distance;
        return temp;
    }



    public int getRoundNumber() {
        return theGame.getRoundNumber();
    }



    /**
     * Zoe Millage
     * initialize most values needed by this view
     * @param context  - used to get the screen size
     * @param attrs    - attributes of the view
     * @param defStyle - defines the color theme possibly
     */
    private void init(Context context, AttributeSet attrs, int defStyle) {
        // get screen dimensions, view takes up ~50% of screen
        heightBounds = context.getResources().getDisplayMetrics().heightPixels;
        widthBounds = context.getResources().getDisplayMetrics().widthPixels;

        // get colors for later
        player1Color = getResources().getColor(R.color.dark_blue, null);
        player1ColorTrans = getResources().getColor(R.color.dark_blue_trans, null);
        player2Color = getResources().getColor(R.color.dark_red, null);
        player2ColorTrans = getResources().getColor(R.color.dark_red_trans, null);

        // initialize the paints
        boarderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        boarderPaint.setStrokeWidth(10f);
        boarderPaint.setColor(player1Color);

        defaultPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        defaultPaint.setColor(getResources().getColor(R.color.gray_400, null));

        player1Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        player1Paint.setColor(player1Color);

        player2Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        player2Paint.setColor(player2Color);

        capturePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        capturePaint.setColor(player1ColorTrans);

        // initialize the capture method
        captureMethod = DOT;

        // initialize the game and captures
        theGame = new Game(this);
        theDot = new Dot();
        theLine = new Line();
        theRect = new Rectangle();
    }



    /**
     * Zoe Millage
     * loads the Game's instance state
     * @param bundle - data to load
     */
    public void loadInstanceState(Bundle bundle) {
        theGame.loadInstanceState(bundle);

        theLine.angleX = bundle.getFloat(ANGLE_X);
        theLine.angleY = bundle.getFloat(ANGLE_Y);
        captureMethod = bundle.getInt(CAPTURE_METHOD);
        theLine.distance = bundle.getFloat(DISTANCE);
        theRect.distance = bundle.getFloat(DISTANCE);
        theGame.setRoundNumber(bundle.getInt(ROUND));
        theRect.scale = bundle.getFloat(SCALE);
        theDot.x = bundle.getFloat(X);
        theDot.y = bundle.getFloat(Y);
        theRect.x = bundle.getFloat(X);
        theRect.y = bundle.getFloat(Y);
        theLine.x = bundle.getFloat(X);
        theLine.y = bundle.getFloat(Y);
    }



    /**
     * Zoe Millage
     * resizes the game view once it's attached to a window. Used to maintain a relatively
     * stable size and a constant aspect ratio
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        // resize the view if it's the first time it's being used or drawn
        // takes up ~50% of the screen
        if (height == -1) {
            int viewHeight = heightBounds / 2;
            int viewWidth = widthBounds / 2;

            // use smaller of two values as width, make the view a 1.1:1 aspect ratio
            if (viewHeight > viewWidth) {
                viewWidth = (int) (viewHeight * ASPECT_RATIO);

                // make sure we don't break past bounds, width may have grown
                while (viewWidth >= widthBounds) {
                    viewWidth *= 0.95;
                    viewHeight *= 0.95;
                }

                // resize
                ConstraintLayout.LayoutParams newParams = (ConstraintLayout.LayoutParams) getLayoutParams();
                newParams.height = viewHeight;
                newParams.width = viewWidth;
                this.setLayoutParams(newParams);
            } else {
                viewHeight = (int) (viewWidth / ASPECT_RATIO);

                // in landscape, the width bounds is half of the screen width
                widthBounds *= 0.5;

                // make sure we don't break past the bounds we want
                while (viewWidth >= widthBounds | viewHeight >= heightBounds) {
                    viewWidth *= 0.95;
                    viewHeight *= 0.95;
                }

                // resize
                this.setLayoutParams(new LinearLayout.LayoutParams(viewWidth, viewHeight));
                LinearLayout.LayoutParams newParams = (LinearLayout.LayoutParams) getLayoutParams();
                newParams.height = viewHeight;
                newParams.width = viewWidth;
                this.setLayoutParams(newParams);
            }

            height = viewHeight;
            width = viewWidth;
        }
    }



    /**
     * Zoe Millage
     * draws the game
     * @param canvas the canvas on which the background will be drawn
     */
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        drawBoarder(canvas);

        drawCollectables(canvas);

        drawCapture(canvas);

        invalidate();

    }



    /**
     * Zoe Millage
     * allows captures to be moved with touches
     * @param event The motion event.
     * @return if a touch event was handled
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int id = event.getPointerId(event.getActionIndex());


        switch (event.getActionMasked()) {
            // handle a touch
            case MotionEvent.ACTION_DOWN:
                touch1.id = id;
                touch2.id = -1;
                pointPositions(event);
                touch1.updateLast();
                return true;

            // handle a second touch
            case MotionEvent.ACTION_POINTER_DOWN:
                if (touch1.id >= 0 && touch2.id < 0) {
                    touch2.id = id;
                    pointPositions(event);
                    touch2.updateLast();
                    return true;
                }
                break;

            // handle a touch stop or final touch lift
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                touch1.id = -1;
                touch2.id = -1;
                invalidate();
                return true;

            // handle the 1st but not 2nd touch lift
            case MotionEvent.ACTION_POINTER_UP:
                if (id == touch2.id) {
                    touch2.id = -1;
                } else if (id == touch1.id) {
                    // Make what was touch2 now be touch1 by
                    // swapping the objects.
                    Touch t = touch1;
                    touch1 = touch2;
                    touch2 = t;
                    touch2.id = -1;
                }
                invalidate();
                return true;

            // handle moves/scaling/rotating motions
            case MotionEvent.ACTION_MOVE:
                pointPositions(event);

                if (captureMethod == DOT)
                    pointMoveDot();

                if (captureMethod == RECTANGLE)
                    pointMoveRect();

                else
                    pointMoveLine();

                return true;
        }


        return super.onTouchEvent(event);
    }



    /**
     * Zoe Millage
     * handles the dot's movement
     */
    private void pointMoveDot() {
        // If no touch1, do nothing
        // shouldn't happen, but it's good to check.
        if (touch1.id < 0) {
            return;
        } else {
            // At least one touch
            // We are moving
            touch1.computeDeltas();

            // move dot
            theDot.x += touch1.dX;
            theDot.y += touch1.dY;

            // keep within game bounds
            if (theDot.x > width)
                theDot.x = width;

            else if (theDot.x < 0)
                theDot.x = 0;

            if (theDot.y > height)
                theDot.y = height;

            else if (theDot.y < 0)
                theDot.y = 0;
        }
    }



    /**
     * Zoe Millage
     * handles the line's movement and rotation
     */
    private void pointMoveLine() {
        // If no touch1, do nothing
        // shouldn't happen, but it's good to check.
        if (touch1.id < 0) {
            return;
        } else {
            // At least one touch
            // We are moving
            touch1.computeDeltas();

            // move line
            theLine.x += touch1.dX;
            theLine.y += touch1.dY;

            // keep within game bounds
            if (theLine.x > width)
                theLine.x = width;

            else if (theLine.x < 0)
                theLine.x = 0;

            if (theLine.y > height)
                theLine.y = height;

            else if (theLine.y < 0)
                theLine.y = 0;
        }

        // two touches
        if (touch2.id >= 0) {
            /*
             * Rotation
             */
            //float angle1 = computeAngle(touch1.lastX, touch1.lastY, touch2.lastX, touch2.lastY);
            float angle2 = computeAngle(touch1.x, touch1.y, touch2.x, touch2.y);
            //float da = angle2 - angle1;
            pointRotate(angle2, touch1.x, touch1.y);
        }
    }



    /**
     * Zoe Millage
     * handles the rectangle's movement and scaling
     */
    private void pointMoveRect() {
        // If no touch1, do nothing
        // shouldn't happen, but it's good to check.
        if (touch1.id < 0) {
            return;
        } else {
            // At least one touch
            // We are moving
            touch1.computeDeltas();

            // move rectangle
            theRect.x += touch1.dX;
            theRect.y += touch1.dY;

            // keep within game bounds
            if (theRect.x > width)
                theRect.x = width;

            else if (theRect.x < 0)
                theRect.x = 0;

            if (theRect.y > height)
                theRect.y = height;

            else if (theRect.y < 0)
                theRect.y = 0;
        }

        if (touch2.id >= 0) {
            // Two touches

            // scaling
            touch1.computeDeltas();
            pointScale(touch1.x, touch2.x, touch1.lastX, touch2.lastX);
        }
    }



    /**
     * Zoe Millage
     * updates the touch coordinates
     * @param event the motion event that occurred
     */
    private void pointPositions(MotionEvent event) {
        for (int i = 0; i < event.getPointerCount(); i++) {

            // Get the pointer id
            int id = event.getPointerId(i);

            // Convert to view coordinates
            float x = (event.getX(i));
            float y = (event.getY(i));

            // updates the current touch coordinates
            if (id == touch1.id) {
                touch1.updateLast();
                touch1.x = x;
                touch1.y = y;
            } else if (id == touch2.id) {
                touch2.updateLast();
                touch2.x = x;
                touch2.y = y;
            }
        }

        invalidate();
    }



    /**
     * Zoe Millage
     * updates the line's parameters to rotate while staying
     * relatively under the user's fingers
     * @param dAngle - and angle change
     * @param x1     - the touch x coordinate
     * @param y1     - the touch y coordinate
     */
    public void pointRotate(float dAngle, float x1, float y1) {
        // Compute the radians angle
        double rAngle = Math.toRadians(dAngle);
        float ca = (float) Math.cos(rAngle);
        float sa = (float) Math.sin(rAngle);
        theLine.angleX = ca;
        theLine.angleY = sa;

        // set the start of the line to the 1st touch so the line stays in an expected place
        theLine.x = x1;
        theLine.y = y1;
    }



    /**
     * Zoe Millage
     * updates the rectangle's parameters to resize
     * @param x1    - touch 1's x
     * @param x2    - touch 2's x
     * @param oldX1 - touch1's old x
     * @param oldX2 - touch 2's old x
     */
    public void pointScale(float x1, float x2, float oldX1, float oldX2) {
        // get the old and new distance
        float dist1 = x2 - x1;
        float dist2 = oldX2 - oldX1;
        float scale;

        // get a distance ratio, avoid /0
        // > 1 = scale up
        // < 1 = scale down
        if (dist1 > 0.01 | dist1 < -0.01) {
            if (dist2 > 0.01 | dist2 < -0.01)
                scale = dist1 / dist2;

            else
                scale = dist1;
        } else
            scale = dist2;

        // multiply ratio onto the current hat scale
        scale = theRect.scale * scale;

        // constrain the size
        if (scale < 1f)
            scale = 1f;

        else if (scale > 1.5)
            scale = 1.5f;

        // modify hat scale
        theRect.scale = scale;
    }



    /**
     * Zoe Millage
     * saves the game's state
     *
     * @param bundle - the data to save
     */
    public void saveInstanceState(Bundle bundle) {
        theGame.saveInstanceState(bundle);

        bundle.putFloat(ANGLE_X, theLine.angleX);
        bundle.putFloat(ANGLE_Y, theLine.angleY);
        bundle.putInt(CAPTURE_METHOD, captureMethod);
        bundle.putInt(ROUND, theGame.getRoundNumber());
        bundle.putFloat(SCALE, theRect.scale);


        if ( captureMethod == DOT ) {
            bundle.putFloat(X, theDot.x);
            bundle.putFloat(Y, theDot.y);
            bundle.putFloat(DISTANCE, theRect.distance);
        }

        else if (captureMethod == RECTANGLE) {
            bundle.putFloat(X, theRect.x);
            bundle.putFloat(Y, theRect.y);
            bundle.putFloat(DISTANCE, theRect.distance);
        }

        else {
            bundle.putFloat(X, theLine.x);
            bundle.putFloat(Y, theLine.y);
            bundle.putFloat(DISTANCE, theLine.distance);
        }
    }



    /**
     * Zoe Millage
     * sets the sizes of the collectables and captures
     * relative to the size of the view
     */
    public void setCollectionSizes() {
        // get a base size for everything
        float size = (float) min(height, width) / 15;

        // line and dot captures' thickness should be larger than the collectable
        size *= 2;
        theGame.setCircleDiameter((float) (size * 1.5));
        theGame.setLineThickness(size);
        capturePaint.setStrokeWidth(size);

        // collectable and line collection threshold are the same size
        size /= 4;
        theGame.setCollectableRadius(size);
        theGame.setLineCollectThreshold(size);

        // the size of a tile in the game's "grid", useful for line and rectangle scaling
        // size of one tile in the grid, used for default rectangle size
        float tileSizeX = (float) width / theGame.getBoardSize();
        float tileSizeY = (float) height / theGame.getBoardSize();

        float temp = 2 * max(tileSizeX, tileSizeY);
        theLine.distance = temp * 2;
        theRect.distance = temp;

        if ( theDot.x == 0 & theLine.x == 0 & theRect.x == 0 ) {
            // set initial coordinates for the captures, 0 0 looks bad
            theDot.x = tileSizeX;
            theDot.y = tileSizeY;

            theLine.y = tileSizeY;

            theRect.x = 2 * tileSizeX;
            theRect.y = 3 * tileSizeY;
        }
    }



    public void setCaptureMethod(int method) {
        captureMethod = method;
    }



    public void setPlayer1Name(String name) {
        theGame.setPlayer1Name(name);
    }



    public void setPlayer2Name(String name) {
        theGame.setPlayer2Name(name);
    }



    public void setPlayerTurn(int currPlayer) {
        theGame.setPlayerTurn(currPlayer);
    }



    public void setRoundNumber(int round) {
        theGame.setRoundNumber(round);
    }

}