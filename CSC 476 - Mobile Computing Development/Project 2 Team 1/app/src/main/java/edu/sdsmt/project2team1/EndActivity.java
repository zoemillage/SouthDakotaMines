/**
 * the ending activity, which shows who won and the scores.
 */

package edu.sdsmt.project2team1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class EndActivity extends AppCompatActivity {

    public static final String WINNER_NAME = "winner_name";
    public static final String WINNER_SCORE = "winner_score";
    public static final String LOSER_NAME = "loser_name";
    public static final String LOSER_SCORE = "loser_score";

    public String winnerName = "";
    public String loserName = "";
    public int winnerScore = 0;
    public int loserScore = 0;

    private final Cloud cloud = new Cloud();


    /**
     * Gabe Jerome
     * gets scores and names from first initialization
     * @param intent the intent to start this activity
     */
    private void getIntentData(Intent intent){
        winnerName = intent.getStringExtra("winnerName");
        winnerScore = intent.getIntExtra("winnerScore", 0);
        loserName = intent.getStringExtra("loserName");
        loserScore = intent.getIntExtra("loserScore", 0);
    }



    /**
     * Gabe Jerome
     * loads the player names and score so they aren't null on reinitialization
     * @param bundle holds the names and scores
     */
    private void loadInstanceState(Bundle bundle){
        winnerName = bundle.getString(WINNER_NAME);
        winnerScore = bundle.getInt(WINNER_SCORE);
        loserName = bundle.getString(LOSER_NAME);
        loserScore = bundle.getInt(LOSER_SCORE);
    }



    /**
     * Gabe Jerome and Marcus Kane
     * initializes the activity and puts the appropriate values
     * @param bundle If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_end);

        cloud.resetPlayers();

        Intent intent = getIntent();
        // Load from instance state if new
        if(bundle != null){
            loadInstanceState(bundle);
        }
        // Load from bundle if rotating
        else if( intent != null ){
            getIntentData(intent);
        }

        setWinnerName();
        setWinnerScore();
        setLoserMessage();
    }



    /**
     * Gabe Jerome
     * returns to the welcome activity
     * @param view the button causing the transition
     */
    public void onRestart(View view){
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }



    /**
     * Gabe Jerome
     * saves names and scores
     * @param bundle Bundle in which to place your saved state.
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle bundle){
        super.onSaveInstanceState(bundle);

        bundle.putString(WINNER_NAME, winnerName);
        bundle.putString(LOSER_NAME, loserName);
        bundle.putInt(WINNER_SCORE, winnerScore);
        bundle.putInt(LOSER_SCORE, loserScore);
    }



    /**
     * Gabe Jerome
     * sets the losing player's name and score in the appropriate area
     */
    private void setLoserMessage(){
        TextView view = findViewById(R.id.loserScore);
        String message = loserName + getString(R.string.end_loser_score) + loserScore;
        view.setText(message);
    }



    /**
     * Gabe Jerome
     * sets the winning player's name onto the congratulation message
     */
    private void setWinnerName(){
        TextView view = findViewById(R.id.winnerCongrats);
        String message = getString(R.string.congratulations) + winnerName;
        view.setText(message);
    }



    /**
     * Gabe Jerome
     * sets the winner's score in the appropriate area
     */
    private void setWinnerScore(){
        TextView view = findViewById(R.id.winnerScore);
        String message = getString(R.string.end_winner_score) + winnerScore;
        view.setText(message);
    }

}