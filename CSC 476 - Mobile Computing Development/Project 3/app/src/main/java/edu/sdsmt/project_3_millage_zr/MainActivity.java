/**
 * Program: Project 3 - Draw by Moving
 * Author: Zoe Millage
 * Class: CSC-476, 2024s
 * Date: Spring 2024
 * Description: Lets the user draw on a canvas by walking around
 *      and tilting their device.
 *      Holds the main activity.
 */

package edu.sdsmt.project_3_millage_zr;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.location.provider.ProviderProperties;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.location.LocationListenerCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int NEED_PERMISSIONS = 1;

    private final ActiveListener activeListener = new ActiveListener();
    private double latitude = -1;
    private LocationManager locationManager = null;
    private double longitude = -1;
    private DrawerView drawView;
    private ArrayList<Float> drawX;
    private ArrayList<Float> drawY;
    private static final float NS2S = 1.0f / 1000000000.0f;
    private long timestamp = 0;
    SensorManager sensorManager;
    Sensor accelSensor;
    accelListener accelListener;

    /**
     * handles changes in location or provider
     */
    private class ActiveListener implements LocationListenerCompat {

        /**
         * use an updated location
         * @param location the updated location
         */
        @Override
        public void onLocationChanged(@NonNull Location location) {
            onLocation(location);
        }


        /**
         * register new location listener
         * @param provider the name of the location provider
         */
        @Override
        public void onProviderEnabled(@NonNull String provider) {
            registerListeners();
        }


        /**
         * register a different location listener
         * @param provider the name of the location provider
         */
        @Override
        public void onProviderDisabled(@NonNull String provider) {
            registerListeners();
        }
    }



    /**
     * listener so the program can figure out the angle of the device
     */
    private class accelListener implements SensorEventListener {

        /**
         * changes the draw size when the accelerometer detects a change in angle
         * @param event the {@link android.hardware.SensorEvent SensorEvent}.
         */
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (timestamp != 0) {
                final float dT = (event.timestamp - timestamp) * NS2S;       // Delta time – how much time as passed

                float accX = event.values[1];

                float accMag;

                float value;

                // make the draw size larger when horizontal
                // and smaller when vertical, with constraints
                if (accX < 0)
                    accX *= -1;

                if ( accX != 0) {
                    accMag = 9.8f/accX;
                    value = 15 * (accMag);

                    if (value > 40f)
                        value = 40f;

                    else if (accMag < 1.3)
                        value = 0;

                    else if (accMag < 1.5)
                        value = 15 / accMag;
                }


                else
                    value = 40f;


                drawView.setDrawWidth(value);


            }
            timestamp = event.timestamp;
        }


        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }



    public ArrayList getDrawX() {
        return drawX;
    }



    public ArrayList getDrawY() {
        return drawY;
    }



    /**
     * gets or initializing the list of points to draw, and tries to use location permissions
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        drawX = new ArrayList<>();
        drawY = new ArrayList<>();

        drawView = findViewById(R.id.drawerView);
        drawView.setActivity(this);

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Force the screen to say on and bright
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }



    /**
     * Handle when a new location if found by updating the latitude and longitude
     * @param location the current device location
     */
    private void onLocation(Location location) {
        if (location == null) {
            return;
        }

        double tempLat = location.getLatitude();
        double tempLong = location.getLongitude();

        if (longitude != -1) {
            drawX.add((float) ((tempLong - longitude) * 1000));
            drawY.add((float) ((tempLat - latitude) * 1000));
            drawView.appendWidthList();
        }
        latitude = tempLat;
        longitude = tempLong;

        setUI();
    }



    /**
     * stop using listeners and sensors when paused
     */
    public void onPause() {
        super.onPause();
        if (accelSensor != null) {
            sensorManager.unregisterListener(accelListener);
            accelListener = null;
            accelSensor = null;
        }
    }



    /**
     * try to get location permissions
     * @param requestCode The request code passed in
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     *
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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
     * Reinitialize sensors and listeners
     */
    public void onResume() {
        super.onResume();
        SensorManager sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);

        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(accelSensor != null) {
            accelListener = new accelListener();
            sensorManager.registerListener(accelListener, accelSensor, SensorManager.SENSOR_DELAY_GAME);
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
     * register a location listener, or ask for
     * permissions if they aren't granted.
     */
    private void registerListeners() {
        unregisterListeners();


        //register if permitted, and request permission if not
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, NEED_PERMISSIONS);
        }

    }



    /**
     * Set all user interface components to the current state
     */
    private void setUI() {
        drawView.invalidate();
    }



    /**
     * unregister the location listener
     */
    private void unregisterListeners() {
        locationManager.removeUpdates(activeListener);
    }


}