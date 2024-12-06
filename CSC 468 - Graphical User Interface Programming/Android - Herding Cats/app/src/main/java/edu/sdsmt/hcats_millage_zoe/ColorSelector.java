/**
 * Author: Zoe Millage
 * Description: Holds the logic for the color selector. HEAVILY based on mobile's tutorial 4
 */

package edu.sdsmt.hcats_millage_zoe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ColorSelector extends AppCompatActivity {

    // tag for info passed to this activity
    public final static String EXTRA_MESSAGE = "edu.sdsmt.HCatsMillageZR.COLOR";

    /**
     * sets the view for the color selector
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.color_selector);
    }



    /**
     * returns the selected color to the main activity
     * @param color the color to end to the main activity
     */
    public void selectColor(int color) {
        Intent resultIntent = new Intent();

        // get the value to add
        resultIntent.putExtra(MainActivity.RETURN_MESSAGE, color);

        // say everything went ok
        setResult(Activity.RESULT_OK, resultIntent);

        // return from and stop activity
        finish();
    }
}
