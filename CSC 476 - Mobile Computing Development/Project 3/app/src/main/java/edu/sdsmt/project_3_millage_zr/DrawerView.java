/**
 * Author: Zoe Millage
 * Description: Holds the view which lets the user draw
 */

package edu.sdsmt.project_3_millage_zr;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class DrawerView extends View {

    private int width = -1;
    private int height = -1;
    private float drawWidth = 15f;
    private Paint borderPaint;
    private Paint drawPaint;
    private MainActivity main;
    private ArrayList<Float> widthList;


    public DrawerView(Context context) {
        super(context);
        init(null, 0);
    }



    public DrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }



    public DrawerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }



    /**
     * adds the width of the newest point
     */
    public void appendWidthList() {
        widthList.add(drawWidth);
    }



    /**
     * draws the boarder around the draw area
     * @param canvas
     */
    private void drawBorders(Canvas canvas) {
        if (height == -1) {
            height = getHeight();
            width = getWidth();
        }

        canvas.drawLine(0, 0, width, 0, borderPaint);
        canvas.drawLine(width, 0, width, height, borderPaint);
        canvas.drawLine(0, 0, 0, height, borderPaint);
        canvas.drawLine(0, height, width, height, borderPaint);
    }



    /**
     * draws a set of circles whose sizes are based on the location
     * and device angle at the time of being stored
     * @param canvas the canvas to draw on
     */
    private void drawPoints(Canvas canvas) {
        float currX = 0.5f;
        float currY = 0.5f;

        int pxX;
        int pxY;
        int radius;

        ArrayList<Float> tempX = main.getDrawX();
        ArrayList<Float> tempY = main.getDrawY();

        for ( int i = 0; i < widthList.size(); i++) {
            currX += tempX.get(i);
            currY -= tempY.get(i);
            radius = widthList.get(i).intValue();

            pxX = (int)(currX * width);
            pxY = (int)(currY * height);

            canvas.drawOval(pxX - radius, pxY - radius, pxX + radius, pxY + radius, drawPaint);
        }
    }



    /**
     * initialize the paints and draw sizes
     * @param attrs unused in this program
     * @param defStyle unused in this program
     */
    private void init(AttributeSet attrs, int defStyle) {
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setColor(getResources().getColor(R.color.black, null));
        borderPaint.setStrokeWidth(10f);

        drawPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        drawPaint.setColor(getResources().getColor(R.color.light_blue_600, null));

        widthList = new ArrayList<>();
    }



    /**
     * draws the canvas and the user's current drawing
     * @param canvas the canvas on which the background will be drawn
     */
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        drawBorders(canvas);

        if ( main != null)
            drawPoints(canvas);
    }



    public void setActivity(MainActivity m) {
        main = m;
    }



    public void setDrawWidth(float f) {
        drawWidth = f;
    }

}