/**
 * Program: Tutorial 7 - Are We There Yet
 * Author: Zoe Millage
 * Class: CSC-476, 2024s
 * Date: Spring 2024
 * Description: Calculates the distance from the given
 * location and the user's current location
 */
//
//        Tutorial 7 Grading
//
//        Complete the following checklist. If you partially completed an item, put a note how I can check what is working for partial credit.
//
//
//        ___hopefully___  35 	Tutorial complete
//
//        ___hopefully___	20 	Layout (-3pt for each minor error)
//
//        ___hopefully___	10 	setUI() function (-5pt for each minor error)
//
//        ___hopefully___	10 	Computing distance between two coordinates (-5pt for each minor error)
//
//        ___hopefully___	10 	Saving new address to preferences (-5pt for each minor error)
//
//        ___hopefully___	5 	Adding your home coordinates to the program
//
//        ___hopefully___	10 	Getting the call to newLocation() to work (-5pt for each minor error)
//
//        ___N/A___	10 	CSC 576 ONLY:  "is GPS active?" task
//
//
//
//        The grade you compute is the starting point for course staff, who reserve the right to
//        change the grade if they disagree with your assessment and to deduct points for other
//        issues they may encounter, such as errors in the submission process, naming issues, etc.


package edu.sdsmt.there_yet_millage_zr;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.location.provider.ProviderProperties;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.location.LocationListenerCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int NEED_PERMISSIONS = 1;
    private final static String TO = "to";
    private final static String TOLAT = "tolat";
    private final static String TOLONG = "tolong";

    private final ActiveListener activeListener = new ActiveListener();
    private float distance = 0;
    private double latitude = 0;
    private LocationManager locationManager = null;
    private double longitude = 0;
    private SharedPreferences settings = null;
    private String to = "";
    private double toLatitude = 0;
    private double toLongitude = 0;
    private boolean valid = false;
    private TextView viewDistance;
    private TextView viewLatitude;
    private TextView viewLongitude;
    private TextView viewTo;

    /**
     * listens for location and provider status
     */
    private class ActiveListener implements LocationListenerCompat {


        @Override
        public void onLocationChanged(@NonNull Location location) {
            onLocation(location);
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            registerListeners();
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            registerListeners();
        }
    }



    /**
     * Look up the provided address. This works in a thread!
     * @param address Address we are looking up
     */
    private void lookupAddress(String address) {
        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.US);
        boolean exception = false;
        List<Address> locations;
        try {
            locations = geocoder.getFromLocationName(address, 1);
        } catch(IOException ex) {
            // Failed due to I/O exception
            locations = null;
            exception = true;
        }

        if (!exception) {
            final boolean tempException = exception;
            final List<Address> tempLoc = locations;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    newLocation(address, tempException, tempLoc);
                }
            });
        }
    }



    /**
     * Spins up the thread to find an address
     * @param address the address
     */
    private void newAddress(final String address) {
        if(address.isEmpty()) {
            // Don't do anything if the address is blank
            return;
        }

        new Thread(() -> lookupAddress(address)).start();
    }



    /**
     * Takes an user input address, and the location address associated with the address, and
     * processes them to display it. If there was an exception, it should toast a failure message
     * @param address the user input address
     * @param exception was there an exception when trying to find the GPS address
     * @param locations the location address than had the latitude and longitude
     */
    private void newLocation(String address, boolean exception, List<Address> locations) {

        if(exception) {
            Toast.makeText(MainActivity.this, R.string.exception, Toast.LENGTH_SHORT).show();
        } else {
            if(locations == null || locations.isEmpty()) {
                Toast.makeText(this, R.string.couldnotfind, Toast.LENGTH_SHORT).show();
                return;
            }

            EditText location = findViewById(R.id.editLocation);
            location.setText("");

            // We have a valid new location
            Address a = locations.get(0);
            newTo(address, a.getLatitude(), a.getLongitude());

        }
    }



    /**
     * Handle setting a new "to" location.
     * @param address Address to display
     * @param lat latitude
     * @param lon longitude
     */
    private void newTo(String address, double lat, double lon) {
        to = address;
        toLatitude = lat;
        toLongitude = lon;

        setUI();

        SharedPreferences.Editor editor = settings.edit();
        editor.putString(TO, to);
        editor.putString(TOLAT, Double.toString(toLatitude));
        editor.putString(TOLONG, Double.toString(toLongitude));
        editor.apply();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        settings = getSharedPreferences( getPackageName() + "_preferences", Context.MODE_PRIVATE);
        to = settings.getString(TO, "McLaury Building");
        toLatitude = Double.parseDouble(settings.getString(TOLAT, "44.075104"));
        toLongitude = Double.parseDouble(settings.getString(TOLONG, "-103.206819"));

        viewDistance = findViewById(R.id.textDistance);
        viewLatitude = findViewById(R.id.textLatitude);
        viewLongitude = findViewById(R.id.textLongitude);
        viewTo = findViewById(R.id.textTo);

        // Get the location manager
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        // Force the screen to say on and bright
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;

    }



    /**
     * Handle when a new location if found by updating the latitude and longitude
     * @param location the current device location
     */
    private void onLocation(Location location) {
        if(location == null) {
            return;
        }

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        valid = true;

        setUI();
    }



    /**
     * Starts the process of finding the coordinates of a new a dress
     * @param view not used
     */
    public void onNew(View view) {
        EditText location = findViewById(R.id.editLocation);
        final String address = location.getText().toString().trim();
        newAddress(address);
    }



    /**
     * Handle an options menu selection
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.itemGrubby) {
            newTo(getString(R.string.grubby), 44.074834, -103.207562);
            return true;

        } else if (id == R.id.itemHome) {
            newTo(getString(R.string.home), 42.4518917, -83.1421233);
            return true;

        } else if (id == R.id.itemMcLaury) {
            newTo(getString(R.string.mclaury), 44.075104, -103.206819);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,   String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case NEED_PERMISSIONS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Try registering again
                    registerListeners();

                } else {

                    // permission denied, boo! Tell the users the app won't work now
                    Toast.makeText(getApplicationContext(), R.string.denied, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }



    /**
     * Called when this application becomes foreground again.
     */
    @Override
    protected void onStart() {
        super.onStart();

        TextView viewProvider = findViewById(R.id.textProvider);
        viewProvider.setText("");

        setUI();
        registerListeners();
    }



    /**
     * Called when this application is no longer the foreground application.
     */
    @Override
    protected void onStop() {
        unregisterListeners();
        super.onStop();
    }



    /**
     * registers listeners for getting/having permission, location provider, and location
     */
    private void registerListeners() {
        unregisterListeners();


        //register if permitted, and request permission if not
        if ( ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            List<String> providers = locationManager.getProviders(true);
            String bestAvailable = providers.get(0);

            //base selection on best  accuracy,  and nothing else
            for (int i = 1; i < providers.size(); i++) {

                //use LocationProvider is under android S, and provider properties if over to check accuracy
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S) {
                    LocationProvider loc = locationManager.getProvider(providers.get(i));
                    if (loc.getAccuracy() < locationManager.getProvider(bestAvailable).getAccuracy()) {
                        bestAvailable = providers.get(i);
                    }
                } else {
                    ProviderProperties loc = locationManager.getProviderProperties(providers.get(i));

                    //fix a bug with the emulator that in later APIs "passive" has a accuracy of 1.
                    if (bestAvailable.equals("passive") || loc.getAccuracy() < locationManager
                            .getProviderProperties(bestAvailable).getAccuracy()) {
                        bestAvailable = providers.get(i);
                    }
                }
            }


            //if location is available, request location updates
            if (!bestAvailable.equals(LocationManager.PASSIVE_PROVIDER)) {
                locationManager.requestLocationUpdates(bestAvailable, 500, 1, activeListener);
                TextView viewProvider = findViewById(R.id.textProvider);
                viewProvider.setText(bestAvailable);
                Location location = locationManager.getLastKnownLocation(bestAvailable);
                onLocation(location);
            }
        }
        else{
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},NEED_PERMISSIONS);
        }

    }



    /**
     * Set all user interface components to the current state
     */
    private void setUI() {
        viewTo.setText(to);

        if (valid) {
            viewLatitude.setText(String.format(Locale.getDefault(),"%.7f", latitude));
            viewLongitude.setText(String.format(Locale.getDefault(),"%.7f", longitude));

            // compute and display distance
            float[] temp = new float[3];
            Location.distanceBetween(latitude, longitude, toLatitude, toLongitude, temp);
            distance = temp[0];
            viewDistance.setText(String.format(Locale.getDefault(),"%1$6.1fm", distance));

        }

        else {
            viewDistance.setText("");
            viewLatitude.setText("");
            viewLongitude.setText("");
        }


    }


    /**
     * unregister the location listener
     */
    private void unregisterListeners() {
        locationManager.removeUpdates(activeListener);
    }

}