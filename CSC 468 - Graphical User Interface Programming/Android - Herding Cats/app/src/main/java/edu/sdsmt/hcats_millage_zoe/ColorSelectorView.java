/**
 * Author: Zoe Millage
 * Description: The color selector view, which returns the
 * color of the pixel the users clicks.
 * HEAVILY based on mobile's tutorial 4
 */

package edu.sdsmt.hcats_millage_zoe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ColorSelectorView extends View {

    private Bitmap imageBitmap = null;

    private float imageScale = 1;

    private float marginLeft = 0;

    private float marginTop = 0;



    public ColorSelectorView(Context context) {
        super(context);
        init();
    }



    public ColorSelectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }



    public ColorSelectorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }



    /**
     * initializes the color selector image
     */
    public void init() {
        imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.colors);
    }



    /**
     * draws the color selector image, scaling based on the orientation
     * @param canvas the canvas on which the background will be drawn
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(imageBitmap == null) {
            return;
        }

        float wid = getWidth();
        float hit = getHeight();

        // Scale image to fit either screen orientation
        float scaleH = wid / imageBitmap.getWidth();
        float scaleV = hit / imageBitmap.getHeight();

        // Use the smaller scale
        imageScale = Math.min(scaleH, scaleV);

        // get pixel dimensions
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



    /**
     * sends the touch coordinates to
     * @param event The motion event.
     * @return if a type of touch event was handled
     */
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {

        // only handle a single touch
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            touched(event.getX(0), event.getY(0));
        }

        return super.onTouchEvent(event);
    }



    /**
     * sends the color of the pixel at the given coordinates and send it
     * eventually back to the main activity
     * @param x the x coordinate
     * @param y the y corrdinate
     */
    private void touched(float x, float y) {
        // account for margins
        y -= marginTop;
        x -= marginLeft;
        x /= imageScale;
        y /= imageScale;

        // get the color at the touched pixel
        if(x >= 0 && x < imageBitmap.getWidth() &&
                y >= 0 && y < imageBitmap.getHeight()) {
            int color = imageBitmap.getPixel((int)x, (int)y);
            ColorSelector activity = (ColorSelector)getContext();
            activity.selectColor(color);
        }
    }
}