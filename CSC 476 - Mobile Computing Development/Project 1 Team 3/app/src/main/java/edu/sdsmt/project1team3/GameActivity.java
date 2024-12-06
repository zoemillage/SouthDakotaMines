/**
 * Holds the game activity, which has the main gameplay
 */

package edu.sdsmt.project1team3;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Objects;

public class GameActivity extends AppCompatActivity {
    private static int rounds;
    public GameView gameView;


    /**
     * launches capture activity to change capture type
     */
    ActivityResultLauncher<Intent> captureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {

                /**
                 * Samantha Kaltved
                 * changes capture if the result was valid
                 * @param result the result of the capture activity's end
                 */
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        assert data != null;
                        int method = data.getIntExtra("captureType", -1);
                        gameView.setCaptureMethod(method);
                        TextView currCapture = findViewById(R.id.current_capture_text);
                        if(method == 0){
                            currCapture.setText(R.string.current_capture_dot_capture);
                        }
                        else if (method == 1) {
                            currCapture.setText(R.string.current_capture_rectangle_capture);
                        }
                        else if (method == 2){
                            currCapture.setText(R.string.current_capture_line_capture);
                        }
                    }
                }
            });


    /**
     * Samantha Kaltved
     * Process the current player's capture. Move onto the other players turn
     * if there's still a round to complete. Move to the end screen otherwise.
     * @param view the activity's view
     */
    public void onCollect(View view) {
        int currRound = gameView.getRoundNumber();
        if (currRound <= rounds) {
            //switch players and increment round if needed
            int currPlayerNum = gameView.getPlayerTurn();
            if (currPlayerNum == 1) {
                //call a gameView function to capture
                //in game class need x and y touch returns score
                int newScore = gameView.capture();
                gameView.addToPlayer1Score(newScore);

                //change current player and set text
                gameView.setPlayerTurn(2);
                String player2 = gameView.getPlayer2Name();
                TextView currPlayer = findViewById(R.id.whose_turn_text);
                String playerTurn = player2 + getResources().getString(R.string.their_turn);
                currPlayer.setText(playerTurn);
                //get player 1's new score
                TextView player1Score = findViewById(R.id.player_1_score_game);
                String player1ScoreText = getResources().getString(R.string.score) + gameView.getPlayer1Score();
                player1Score.setText(player1ScoreText);
                gameView.invalidate();
            } else {
                //call a gameView function to capture
                //in game class need x and y touch returns score
                int newScore = gameView.capture();
                gameView.addToPlayer2Score(newScore);

                //change current player and set text
                gameView.setPlayerTurn(1);
                String player1 = gameView.getPlayer1Name();
                TextView currPlayer = findViewById(R.id.whose_turn_text);
                String playerTurn = player1 + getResources().getString(R.string.their_turn);
                currPlayer.setText(playerTurn);
                //get the new round and set text
                currRound++;
                gameView.setRoundNumber(currRound);
                TextView currRoundText = findViewById(R.id.current_round_text);
                String roundText = getResources().getString(R.string.round_number) + " " + (currRound);
                currRoundText.setText(roundText);
                //get player 2's new score
                TextView player2Score = findViewById(R.id.player_2_score_game);
                String player2ScoreText = getResources().getString(R.string.score) + gameView.getPlayer2Score();
                player2Score.setText(player2ScoreText);
                gameView.invalidate();
            }
        }
        //now check if that was the last turn
        if (currRound > rounds) {
            Intent intent = new Intent(this, EndActivity.class);
            if (gameView.getPlayer2Score() > gameView.getPlayer1Score()) {
                intent.putExtra("winnerName", gameView.getPlayer2Name());
                intent.putExtra("winnerScore", gameView.getPlayer2Score());
                intent.putExtra("loserName", gameView.getPlayer1Name());
                intent.putExtra("loserScore", gameView.getPlayer1Score());
            } else {
                intent.putExtra("winnerName", gameView.getPlayer1Name());
                intent.putExtra("winnerScore", gameView.getPlayer1Score());
                intent.putExtra("loserName", gameView.getPlayer2Name());
                intent.putExtra("loserScore", gameView.getPlayer2Score());
            }
            startActivity(intent);
        }
    }


    /**
     * Samantha Kaltved
     * initializes the activity and load the appropriate values and strings
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameView = findViewById(R.id.game_view);

        if ( savedInstanceState != null )
            gameView.loadInstanceState(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        TextView name1 = findViewById(R.id.player_1_name_game);
        assert bundle != null;
        String player1 = bundle.getString("player1Name", "Player 1");
        if(Objects.equals(player1, "")){
            name1.setText(R.string.player1);
            gameView.setPlayer1Name("Player 1");
        }
        else {
            name1.setText(player1);
            gameView.setPlayer1Name(player1);
        }
        TextView name2 = findViewById(R.id.player_2_name_game);
        String player2 = bundle.getString("player2Name", "Player 2");
        if(Objects.equals(player2, "")){
            name2.setText(R.string.player2);
            gameView.setPlayer2Name("Player 2");
        }
        else {
            name2.setText(player2);
            gameView.setPlayer2Name(player2);
        }
        rounds = bundle.getInt("rounds");

        TextView currPlayer = findViewById(R.id.whose_turn_text);

        int whoseTurn = gameView.getPlayerTurn();
        String playerTurn;

        if (whoseTurn == 1)
            playerTurn = gameView.getPlayer1Name() + getResources().getString(R.string.their_turn);

        else
            playerTurn = gameView.getPlayer2Name() + getResources().getString(R.string.their_turn);

        currPlayer.setText(playerTurn);

        TextView currRoundText = findViewById(R.id.current_round_text);
        String roundText = getResources().getString(R.string.round_number) + " " + gameView.getRoundNumber();
        currRoundText.setText(roundText);

        TextView player1Score = findViewById(R.id.player_1_score_game);
        String player1ScoreText = getResources().getString(R.string.score) + gameView.getPlayer1Score();
        player1Score.setText(player1ScoreText);
        TextView player2Score = findViewById(R.id.player_2_score_game);
        String player2ScoreText = getResources().getString(R.string.score) + gameView.getPlayer2Score();
        player2Score.setText(player2ScoreText);

        TextView currCapture = findViewById(R.id.current_capture_text);
        int method = gameView.getCaptureMethod();
        if(method == 0){
            currCapture.setText(R.string.current_capture_dot_capture);
        }
        else if (method == 1) {
            currCapture.setText(R.string.current_capture_rectangle_capture);
        }
        else if (method == 2){
            currCapture.setText(R.string.current_capture_line_capture);
        }

    }


    /**
     * Samantha Kaltved
     * move to the capture activity when the player requests it
     * @param view the change capture button
     */
    public void onNext(View view){
        Intent intent = new Intent(this, CaptureActivity.class);
        int currScore;
        String currPlayer;
        int currPlayerNum = gameView.getPlayerTurn();
        if(currPlayerNum == 1){
            currScore = gameView.getPlayer1Score();
            currPlayer = gameView.getPlayer1Name();
        }
        else{
            currScore = gameView.getPlayer2Score();
            currPlayer = gameView.getPlayer2Name();
        }
        intent.putExtra("currPlayer", currPlayer);
        intent.putExtra("currScore", currScore);

        captureLauncher.launch(intent);
    }


    /**
     * Samantha Kaltved
     * saves the game's visual state
     * @param bundle Bundle in which to place your saved state.
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);

        gameView.saveInstanceState(bundle);
    }
}