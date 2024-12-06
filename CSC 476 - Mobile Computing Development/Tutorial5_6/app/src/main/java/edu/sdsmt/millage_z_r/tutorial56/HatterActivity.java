/**
 * Program: Tutorial 5/6 - Mad Hatter Online
 * Author: Zoe Millage
 * Class: CSC-476, 2024s
 * Date: Spring 2024
 * Description: Lets the user pick a picture from their gallery and
 *      put a hat on it. Also allows saving to a Firebase database.
 *      This file contains the main activity, where the user puts
 *      a hat on an image.
 */
//        Tutorial 5 Grading
//
//        Complete the following checklist. If you partially completed an item, put a note how I
//        can check what is working for partial credit.
//
//        _Hopefully_  Gave Editor-level firebase access to course staff
//
//
//        _Hopefully_	45 	T5: Tutorial completed  (points based on percent completed)
//
//        _Hopefully_	10 	T5: Catalog item layout correct (-3pt for each minor error)
//
//        _Hopefully_	10 	T5: Function to get an item from the catalog adapter (-5pt for each error)
//
//        _Hopefully_	15 	T5: The Save to Cloud dialog box. (-5pt for each error)
//
//        _Hopefully_	5 	T5: Toast if name is empty
//
//        _Hopefully_	10 	T5: Toast if server save fails (e.g. permission  denied which is how
//                          I'll be testing it) (-5pt for each error)
//
//        _N/A_	10 	T5 CSC 576 ONLY: overwrite a hatting name
//
//        _N/A_	15 	T5 CSC 576 ONLY: Checking internet access for load, save, and picture access (5pt each)
//
//
//        The grade you compute is the starting point for course staff, who reserve the
//        right to change the grade if they disagree with your assessment and to deduct
//        points for other issues they may encounter, such as errors in the submission
//        process, naming issues, etc.
//
//
//
//        Tutorial 6 Grading
//        ----------------------------------------------------------------
//
//        ___hopefully___  Editor-level firebase access to course staff still available
//
//        Complete the following checklist. If you partially completed an item, put a note how
//        I can check what is working for partial credit.
//
//        ___hopefully___	40 	T6: Tutorial completed (points based on percent completed)
//
//        ___hopefully___  5 	T6: Can login in a new user and existing user
//
//        ___hopefully___	10 	T6: getCatalog/load/save still work correctly with new tree structure (-5pt for each error)
//
//        ___hopefully___	10 	T6: hatting rules work properly (-3pt for each minor error)
//
//        ___hopefully___	10 	T6: Deletion Dialog (-3pt for each minor error)
//
//        ___hopefully___	10 	T6: Deletion works (-3pt for each minor error)
//
//        ___hopefully___	5 	T6: Deletion error caught
//
//        ___N/A___	10 	T6 CSC 576 ONLY: Validate is hatting creation is allowed (-5pt for each error)
//
//
//        The grade you compute is the starting point for course staff, who reserve the right to
//        change the grade if they disagree with your assessment and to deduct points for other
//        issues they may encounter, such as errors in the submission process, naming issues, etc.




package edu.sdsmt.millage_z_r.tutorial56;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

public class HatterActivity extends AppCompatActivity {

    /**
     * Key for the parameters in the bundle
     */
    private static final String PARAMETERS = "parameters";

    /**
     * Request code for storage permission
     */
    private static final int NEED_PERMISSIONS = 1;

    /**
     * flags for a out of sequence event where the activities saves it state, but hasn't fully reloaded
     */
    private boolean alreadySaved = false;
    private  boolean pendingConfirmation = false;
    /**
     * Activity launchers for color selection
     */
    private ActivityResultLauncher<Intent> colorResultLauncher;

    /**
     * Activity launcher for getting an image from the gallery
     */
    private ActivityResultLauncher<String> imageResultLauncher;

    /**
     * The hatter view object
     */
    private HatterView hatterView = null;
    /**
     * The color select button
     */
    private Button colorButton = null;
    /**
     * The feather checkbox
     */
    private CheckBox featherCheck = null;
    /**
     * The hat choice spinner
     */
    private Spinner spinner;


    /**
     * method to setup and show picture dialog
     */
    private void getUserPicture() {
        if (alreadySaved) {
            pendingConfirmation = true;
        } else {
            // Bring up the picture selection dialog box
            PictureDlg dialog = new PictureDlg(imageResultLauncher);
            dialog.show(getSupportFragmentManager(), null);
        }
    }



    /**
     * Start up the activity to choose a color
     *
     * @param view the view
     */
    public void onColor(View view) {
        //any target
        Intent intent = new Intent(this, ColorSelectActivity.class);
        colorResultLauncher.launch(intent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final MonitorFirebase monitor = MonitorFirebase.INSTANCE;

        setContentView(R.layout.activity_hatter);

        /*
         * Get some of the views we'll keep around
         */
        hatterView = findViewById(R.id.hatterView);
        colorButton = findViewById(R.id.buttonColor);
        featherCheck = findViewById(R.id.checkFeather);
        spinner = findViewById(R.id.spinnerHat);

        /*
         *register activity launchers
         */
        ActivityResultContracts.StartActivityForResult contract = new ActivityResultContracts.StartActivityForResult();
        colorResultLauncher = registerForActivityResult(contract, (result) -> {
            int resultCode = result.getResultCode();
            if (resultCode == Activity.RESULT_OK) {
                Intent data = result.getData();
                hatterView.setColor(data.getIntExtra(ColorSelectActivity.SELECTOR_COLOR, 0));
            }
        });

        imageResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), (result)->{
            if(result != null) {
                setUri(result);
            }
        });


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
                updateUI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });

        /*
         * Restore any state
         */
        if (savedInstanceState != null) {
            hatterView.getFromBundle(PARAMETERS, savedInstanceState);
            spinner.setSelection(hatterView.getHat());
        }

        /*
         * Ensure the user interface is up to date
         */
        updateUI();
    }



    /**
     * Called when it is time to create the options menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hatter, menu);
        return true;
    }



    /**
     * Handle the feather checkmark press
     * @param view the view
     */
    public void onFeather(View view) {
        hatterView.setFeather(featherCheck.isChecked());
        updateUI();
    }



    /**
     * Handle options menu selections
     *
     * @param item Menu item selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.menu_reset) {
            hatterView.reset();
            return true;

        }

        else if (itemId == R.id.menu_load) {
            LoadDlg dlg2 = new LoadDlg();
            dlg2.show(getSupportFragmentManager(), "load");
            return true;
        }

        else if (itemId == R.id.menu_save) {
            SaveDlg dlg3 = new SaveDlg();
            dlg3.show(getSupportFragmentManager(), "save");
            return true;
        }

        else if (itemId == R.id.menu_delete) {
            DeleteDlg dlg4 = new DeleteDlg();
            dlg4.show(getSupportFragmentManager(), "delete");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    /**
     * Handle a picture button press
     * @param view the button that causes the event
     */
    public void onPicture(View view) {
        String permission;

        //change in image permission in Tiramisu and later
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            permission = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        //still checking for both just in case
        boolean permissionNotGranted =ActivityCompat.checkSelfPermission(this,
                permission)  != PackageManager.PERMISSION_GRANTED ;

        if (permissionNotGranted) {

            AlertDialog.Builder builder = new AlertDialog.Builder((HatterActivity.this));
            builder.setMessage(R.string.permissionRational);

            //make the OK button ask for permission
            builder.setPositiveButton(android.R.string.ok, (dialog, id) -> ActivityCompat.requestPermissions(
                    this,
                    new String[]{permission}, NEED_PERMISSIONS));

            builder.create().show();

        } else {
            getUserPicture();
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case NEED_PERMISSIONS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getUserPicture();

                } else {
                    // permission denied, boo! Tell the users the no images can be loaded
                    Toast.makeText(getApplicationContext(), R.string.denied,
                            Toast.LENGTH_SHORT).show();                        //make the dialog box
                }
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        alreadySaved = false;

        //awaiting a confirmation dialog bos after saving the state?
        if (pendingConfirmation) {
            getUserPicture();
        }
    }



    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        hatterView.putToBundle(PARAMETERS, outState);

        alreadySaved = true;
    }



    public void setUri(Uri uri) {
        hatterView.setImageUri(uri);
    }



    /**
     * Ensure the user interface components match the current state
     */
    public void updateUI() {
        spinner.setSelection(hatterView.getHat());
        featherCheck.setChecked(hatterView.getFeather());
        colorButton.setEnabled(hatterView.getHat() == HatterView.HAT_CUSTOM);
    }
}