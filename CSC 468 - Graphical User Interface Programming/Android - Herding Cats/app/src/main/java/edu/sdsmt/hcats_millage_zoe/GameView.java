/**
 * Author: Zoe Millage
 * Description: Holds the custom view for the game
 */

package edu.sdsmt.hcats_millage_zoe;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.Random;


public class GameView extends View {

    private static String COLOR = "collectionColor";

    private Paint boarderPaint;
    private Paint catPaint;
    private TextView caught;
    private Paint collectionPaint;
    private int color;
    private int height = -1;
    private TextView moves;
    private Random rand = new Random();
    private TextView treats;
    private Game theGame;
    private int width = -1;



    public GameView(Context context) {
        super(context);
        init(null, 0);
    }



    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }



    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }



    /**
     * Zoe Millage
     * draws the boarder of the game view
     * @param canvas - the canvas to draw on
     */
    public void drawBoarder(Canvas canvas) {
        // get dimensions

        if ( width == -1 ) {
            width = this.getWidth();
            height = this.getHeight();
        }

        // draw the boarder
        canvas.drawLine(0, 0, width, 0, boarderPaint);
        canvas.drawLine(0, 0, 0, height, boarderPaint);
        canvas.drawLine(width, height, width, 0, boarderPaint);
        canvas.drawLine(width, height, 0, height, boarderPaint);

    }



    /**
     * draws the collection area, internal borders, cats, and cat totals
     * @param canvas the canvas to draw on
     */
    public void drawInternals(Canvas canvas) {
        int tempWidth = theGame.getWidth();
        int tempHeight = theGame.getHeight();
        int cats;
        int xOffset = width / tempWidth;
        int yOffset = height / tempHeight;
        int intXOffset;
        int intYOffset;
        int i, j, k;
        int margin = max(height / 16, width / 16);
        int catSize = min(height/32, width / 32);

        // draw the collection area
        canvas.drawRect((tempWidth - 1) * xOffset, (tempHeight - 1) * yOffset,
                tempWidth * xOffset - 3f, tempHeight * yOffset - 3f,
                collectionPaint);

        // draw the internal boarders
        for ( i = 1; i < tempWidth; i++ )
            canvas.drawLine(i*xOffset, 0, i*xOffset, height, boarderPaint);

        for ( i = 1; i < tempHeight; i++ )
            canvas.drawLine(0, i*yOffset, width, i*yOffset, boarderPaint);

        // set text size
        catPaint.setTextSize(margin/2);

        // draw the "cats"
        for (i = 0; i < tempWidth; i++) {
            for (j = 0; j < tempHeight; j++) {
                if (!(i == tempWidth - 1 && j == tempHeight - 1)) {
                cats = theGame.getCatsAt(i,j);
                    for ( k = 0; k < cats; k++) {
                        intXOffset = margin + rand.nextInt(xOffset - 3 * margin);
                        intYOffset = margin + rand.nextInt(xOffset - 3 * margin);

                        canvas.drawText(String.valueOf(cats), i * xOffset + margin, j * yOffset + margin, catPaint);

                        canvas.drawOval(i * xOffset + intXOffset, j * yOffset + intYOffset,
                                i * xOffset + intXOffset + catSize, j * yOffset + intYOffset + catSize,
                                catPaint);
                    }
                }
            }
        }

    }



    public int getColor() {
        return color;
    }



    /**
     * initializes the paints and color
     * @param attrs unused, holdover from View
     * @param defStyle unused, holdover from View
     */
    private void init(AttributeSet attrs, int defStyle) {

        boarderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        boarderPaint.setStrokeWidth(10f);
        boarderPaint.setColor(getResources().getColor(R.color.black, null));

        catPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        catPaint.setColor(getResources().getColor(R.color.blue_green, null));

        collectionPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        collectionPaint.setColor(getResources().getColor(R.color.light_blue_400, null));

        color = getResources().getColor(R.color.blue_green, null);
    }



    /**
     * load the player color
     * @param b the bundle to load from
     */
    public void loadInstanceState(Bundle b) {
        color = b.getInt(COLOR);
        tint(color);
        invalidate();
    }



    /**
     * draws the border along with other content if a model is connected
     * @param canvas the canvas on which the background will be drawn
     */
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        // make the border red if a treat is being used
        if (theGame != null ) {
            if (theGame.isUsingTreat()) {
                boarderPaint.setColor(getResources().getColor(R.color.treat_red, null));
            }

            drawBoarder(canvas);

            // reset the color to black for the internals call
            boarderPaint.setColor(getResources().getColor(R.color.black, null));
        }

        else
            drawBoarder(canvas);

        if ( theGame != null )
            drawInternals(canvas);

    }



    /**
     * saves the player color
     * @param b the bundle to save to
     */
    public void saveInstanceState(Bundle b) {
        b.putInt(COLOR, color);
    }



    public void setGame( Game game) {
        theGame = game;
    }



    /**
     * change the player color
     * @param color the color to change to
     */
    public void tint(int color) {
        catPaint.setColor(color);
        this.color = color;
    }

}