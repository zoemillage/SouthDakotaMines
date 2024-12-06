/**
 * Holds the capture activity, which lets the player choose which
 * capture method to use.
 */

package edu.sdsmt.project1team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CaptureActivity extends AppCompatActivity {

    private TextView currPlayerView;
    private TextView currScoreView;

    /**
     * Samantha Kaltved
     * initializes the activity and puts the appropriate values
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        Bundle bundle = getIntent().getExtras();
        currPlayerView = findViewById(R.id.curr_player);
        currScoreView = findViewById(R.id.curr_score);
        assert bundle != null;
        String currPlayer = bundle.getString("currPlayer") + getResources().getString(R.string.their_turn);
        int currScore = bundle.getInt("currScore");
        String scoreString = getResources().getString(R.string.score) + String.valueOf(currScore);
        currPlayerView.setText(currPlayer);
        currScoreView.setText(scoreString);
    }



    /**
     * Samantha Kaltved
     * Moves to the game activity with the dot capture
     * @param view the dot button
     */
    public void onDot(View view){
        Intent i = new Intent();
        i.putExtra("captureType", 0);

        setResult(RESULT_OK, i);

        finish();
    }



    /**
     * Samantha Kaltved
     * Moves to the game activity with the line capture
     * @param view the line button
     */
    public void onLine(View view){
        Intent i = new Intent();
        i.putExtra("captureType", 2);

        setResult(RESULT_OK, i);

        finish();
    }



    /**
     * Samantha Kaltved
     * Moves to the game activity with the rectangle capture
     * @param view the rectangle button
     */
    public void onRect(View view){
        Intent i = new Intent();
        i.putExtra("captureType", 1);

        setResult(RESULT_OK, i);

        finish();
    }
}