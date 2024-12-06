/**
 * Holds the activity a player is moved to after their turn.
 */

package edu.sdsmt.project2team1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OffTurnActivity extends AppCompatActivity {

    private final Cloud cloud = new Cloud();
    private int timeout;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 2*1000; //Delay for 2 seconds.  One second = 1000 milliseconds.

    /**
     * Gage Jager
     * initializes the off turn activity
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_off_turn);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        timeout = 0;

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            /**
             * Gage Jager
             * makes the back button do nothing in this activity
             */
            @Override
            public void handleOnBackPressed() {
                // We don't want players to go back to their last turn...
                // Really messes up keeping each device be the right player.
            }
        });
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



    /**
     * Gage Jager
     * Override onResume so it only runs when the activity is focused.
     */
    @Override
    protected void onResume() {
        // Start handler when activity becomes focused.
        handler.postDelayed( runnable = new Runnable() {
            public void run() {
                // GRADING: TIMEOUT

                // If this loops 30 times (60 seconds),
                // we will assume either we have lost connection,
                // or the player taking their turn lost connection, or just took too long to play.
                if (timeout > 30) {
                    // Assume the worst. The worst implies we disconnected (no way to get info for
                    // the end activity) or the other player disconnected/stopped playing (could try
                    // to get info, but won't on the assumption that we disconnected).
                    Toast.makeText(OffTurnActivity.this, "Catastrophic error, ensure you have constant internet access.",
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(OffTurnActivity.this, EndActivity.class);
                    startActivity(intent);
                }
                // Edge case for when the other player disconnected during our last turn,
                // and therefore didn't get turnOver set back to false.
                // We will make the sketchy assumption that if turnOver is still true
                // on our first loop (after 2 seconds since our turn ended), then
                // the passive exit occurred.
                if (timeout == 0) {
                    cloud.checkTurnOver(new CloudCallback() {
                        /**
                         * check if the other player ended their turn or disconnected
                         * @param done if the game is going to be "done"
                         */
                        @Override
                        void finished(boolean done) {
                            if (done) {
                                // Passive exit, go to EndActivity.
                                Toast.makeText(OffTurnActivity.this, "The other player lost connection or closed the game. Sorry!",
                                        Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(OffTurnActivity.this, EndActivity.class);
                                startActivity(intent);
                            }
                            else {
                                // Check if the other player ended their turn, as normal.
                                cloud.turnOver(OffTurnActivity.this);
                                timeout += 1;
                                handler.postDelayed(runnable, delay);
                            }
                        }
                    });
                }
                else {
                    // Check if the other player ended their turn.
                    cloud.turnOver(OffTurnActivity.this);
                    timeout += 1;
                    handler.postDelayed(runnable, delay);
                }
            }
        }, delay);

        super.onResume();
    }

}