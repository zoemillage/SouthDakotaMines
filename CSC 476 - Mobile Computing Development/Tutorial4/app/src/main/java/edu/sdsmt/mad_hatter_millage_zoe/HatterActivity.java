/**
 * Program: Tutorial 4 - Mad Hatter
 * Author: Zoe Millage
 * Class: CSC-476, 2024s
 * Date: Spring 2024
 * Description: Lets the user pick a picture from their gallery and
 *      put a hat on it.
 *      This file contains the main activity, where the user puts
 *      a hat on an image.
 */
//
//Tutorial 4 Grading
//
//        Complete the following checklist.
//
//
//        hopefully	45 	Tutorial completed (points based on percent completed)
//
//        hopefully	15 	Landscape using Constraint\RelativeLayout correct for Pixel C, Pixel 3a XL, and 5.1" FWVGA (-3pt for each minor error)
//
//        hopefully	10 	Scaling works (-5pt for semi working)
//
//        N/A	    10 	CSC 576 ONLY: Scaling keeps the same image point under the touch point (-5pt for semi working)
//
//        hopefully	5 	Feather appears and disappears as required
//
//        hopefully	5 	Color picker opens Color Activity and closes on color tap
//
//        hopefully	5 	Color picker returns a color
//
//        hopefully	5 	Spinner is correct in all cases
//
//        hopefully	5 	Feather checkmark is correct in all cases
//
//        hopefully	5 	Color button disables when not valid
//
//
//
//        The checklist is the starting point for course staff, who reserve the right to change
//        the grade if they disagree with your assessment and to deduct points for other issues
//        they may encounter, such as errors in the submission process, naming issues, etc.
//



package edu.sdsmt.mad_hatter_millage_zoe;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

public class HatterActivity extends AppCompatActivity {
    /**
     * id to identify why permission was requested
     */
    public static final int NEED_PERMISSIONS = 1;

    private static final String PARAMETERS = "parameters";

    //a tag to identify information being sent back this THIS activity
    public final static String RETURN_MESSAGE = "edu.sdsmt.T4MillageZR.RETURN_COLOR";


    /**
     * The color select button
     */
    private Button colorButton = null;

    /**
     * Activity launcher for the color picker
     */
    ActivityResultLauncher<Intent> colorLauncher;

    /**
     * The feather checkbox
     */
    private CheckBox featherCheck = null;

    /**
     * The hatter view object
     */
    private HatterView hatterView = null;

    /**
     * Activity launcher for content
     */
    ActivityResultLauncher<String> resultLauncher;

    /**
     * The hat choice spinner
     */
    private Spinner spinner;



    public class HandleResult implements ActivityResultCallback<Uri> {

        @Override
        public void onActivityResult(Uri result) {
            if(result != null) {
                Log.i("Path", result.toString());
                hatterView.setImageUri(result.toString());
            }
        }
    };



    /**
     * toggles the hat's feather
     * @param view the checkbox clicked
     */
    public void onCheck(View view){
        hatterView.setFeather(featherCheck.isChecked());
        hatterView.invalidate();
    }



    /**
     * starts the color selection activity
     * @param view the button that triggered this
     */
    public void onColorStart ( View view) {
        Intent intent = new Intent(this, ColorSelectActivity.class);

        int selectedColor = hatterView.getColor();

        intent.putExtra(ColorSelectActivity.EXTRA_MESSAGE, selectedColor);
        colorLauncher.launch(intent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * Get some of the views we'll keep around
         */
        hatterView = (HatterView)findViewById(R.id.hatterView);
        colorButton = (Button)findViewById(R.id.buttonColor);
        featherCheck = (CheckBox)findViewById(R.id.checkFeather);
        spinner = (Spinner) findViewById(R.id.spinnerHat);


        resultLauncher =
                registerForActivityResult(new ActivityResultContracts.GetContent(), new HandleResult());

        //any target
        ActivityResultContracts.StartActivityForResult contract =
                new ActivityResultContracts.StartActivityForResult();
        colorLauncher =
                registerForActivityResult(contract, (result)->
                { int resultCode = result.getResultCode();
                    if (resultCode == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        int value = data.getIntExtra(RETURN_MESSAGE, 0);
                        hatterView.setColor(value);
                    }});

        /*
         * Set up the spinner
         */

        // Create an ArrayAdapter using the string array and a default spinner layout
        String[] hats = getResources().getStringArray(R.array.hats_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, hats);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> arg0, View view,
                                       int pos, long id) {
                hatterView.setHat(pos);

                if ( pos != 2)
                    colorButton.setEnabled(false);

                else
                    colorButton.setEnabled(true);
            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });

        /*
         * Restore any state
         */
        if(savedInstanceState != null) {
            hatterView.getFromBundle(PARAMETERS, savedInstanceState);
            featherCheck.setChecked(hatterView.getFeather());
            spinner.setSelection(hatterView.getHat());
        }
    }



    /**
     * handle a picture button press
     * @param view the source view
     */
    public void onPicture(View view) {
        String permission = android.Manifest.permission.READ_EXTERNAL_STORAGE;

        //change in image permission in Tiramisu and later
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            permission = android.Manifest.permission.READ_MEDIA_IMAGES;
        }

        if (ActivityCompat.checkSelfPermission(this,
                permission)  != PackageManager.PERMISSION_GRANTED ){

            //no permission yet, ask for permission and fcancel this image request
            ActivityCompat.requestPermissions(this, new String[]{permission},NEED_PERMISSIONS);
        }else {
            resultLauncher.launch("image/*");
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        hatterView.putToBundle(PARAMETERS, outState);
    }
}