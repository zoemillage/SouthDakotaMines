/**
 * Author: Zoe Millage
 * Description: the activity that lets the user pick a custom hat color
 */

package edu.sdsmt.mad_hatter_millage_zoe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ColorSelectActivity extends AppCompatActivity {
    //tag to identify information being passed to THIS activity
    public final static String EXTRA_MESSAGE = "edu.sdsmt.T4MillageZR.COLOR";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_select);
    }



    /**
     * sends the given color back to the hatter activity
     * @param color the color as an integer
     */
    public void selectColor(int color) {
        Intent resultIntent = new Intent();

        // get the value to add
        resultIntent.putExtra(HatterActivity.RETURN_MESSAGE, color);

        // say everything went ok
        setResult(Activity.RESULT_OK, resultIntent);

        // return from and stop activity
        finish();
    }

}