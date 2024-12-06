/**
 * contains functionality for the lobby activity, where one
 * logged in user waits for another one to join.
 */

package edu.sdsmt.project2team1;

import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LobbyActivity extends AppCompatActivity {

    private final Cloud cloud = new Cloud();
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 2*1000; //Delay for 2 seconds.  One second = 1000 milliseconds.


    /**
     * initializes the lobby activity
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lobby);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }



    /**
     * Gage Jager
     * Override onResume so it only runs when the activity is focused.
     */
    @Override
    protected void onResume() {
        // Start handler when activity becomes focused.
        handler.postDelayed( runnable = new Runnable() {
            public void run() {
                cloud.playersReady(LobbyActivity.this);
                handler.postDelayed(runnable, delay);
            }
        }, delay);

        super.onResume();
    }



    /**
     * Gage Jager
     * Override onPause so this doesn't run in the background.
     */
    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
    }
}