/**
 * Author: Zoe Millage
 * Description: Holds the view for the color selector activity
 */

package edu.sdsmt.mad_hatter_millage_zoe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ColorSelectView extends View {
    /**
     * The image bitmap.
     */
    private Bitmap imageBitmap = null;

    private float imageScale = 1;

    private float marginLeft = 0;

    private float marginTop = 0;

    public ColorSelectView(Context context) {
        super(context);
        init();
    }



    public ColorSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }



    public ColorSelectView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }



    public void init() {
        imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.colors);
    }



    /* (non-Javadoc)
     * @see android.view.View#onDraw(android.graphics.Canvas)
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(imageBitmap == null) {
            return;
        }

        float wid = getWidth();
        float hit = getHeight();

        // What would be the scale to draw the where it fits both
        // horizontally and vertically?
        float scaleH = wid / imageBitmap.getWidth();
        float scaleV = hit / imageBitmap.getHeight();

        // Use the lesser of the two
        imageScale = scaleH < scaleV ? scaleH : scaleV;

        // What is the scaled image size?
        float iWid = imageScale * imageBitmap.getWidth();
        float iHit = imageScale * imageBitmap.getHeight();

        // Determine the top and left margins to center
        marginLeft = (wid - iWid) / 2;
        marginTop = (hit - iHit) / 2;

        // And draw the bitmap
        canvas.save();
        canvas.translate(marginLeft,  marginTop);
        canvas.scale(imageScale, imageScale);
        canvas.drawBitmap(imageBitmap, 0, 0, null);
        canvas.restore();

    }



    /* (non-Javadoc)
     * @see android.view.View#onTouchEvent(android.view.MotionEvent)
     */
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            touched(event.getX(0), event.getY(0));
        }

        return super.onTouchEvent(event);
    }



    /**
     * gets the color of the pixel touched and sends it to the hatter activity
     * @param x the x position of the touch
     * @param y the y position of the touch
     */
    private void touched(float x, float y) {
        y -= marginTop;
        x -= marginLeft;
        x /= imageScale;
        y /= imageScale;

        if(x >= 0 && x < imageBitmap.getWidth() &&
                y >= 0 && y < imageBitmap.getHeight()) {
            int color = imageBitmap.getPixel((int)x, (int)y);
            ColorSelectActivity activity = (ColorSelectActivity)getContext();
            activity.selectColor(color);
        }
    }
}