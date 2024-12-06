package edu.sdsmt.project2team1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class WelcomeActivity extends AppCompatActivity {
    private final int rounds = 3;
    private final Cloud cloud = new Cloud();

    /**
     * Samantha Kaltved and Gage Jager
     * initializes the welcome activity
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.splashScreenTheme);
        setTheme(R.style.Base_Theme_Project2Team1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    /**
     * Gabe Jerome
     * Opens the how to play dialogue
     * @param view the how to play button
     */
    public void onHowToPlay(View view){
        HowToPlayDialog howToPlayDialog = new HowToPlayDialog();
        howToPlayDialog.show(getSupportFragmentManager(), "HowToPlayDialogFragment");
    }



    /**
     * Gage Jager
     * forwards player name to the cloud, which will move onto the game
     *      or lobby activity, depending on how many players have joined
     * @param view the button clicked
     */
    public void onNext(View view){
        EditText name = findViewById(R.id.playerNameInput);
        cloud.findPlayers(view, name.getText().toString(), this);
    }



    /**
     * Gage Jager
     * resets the player status in the cloud
     * @param view the reset button clicked
     */
    public void onReset(View view){
        cloud.resetPlayers();
    }

}