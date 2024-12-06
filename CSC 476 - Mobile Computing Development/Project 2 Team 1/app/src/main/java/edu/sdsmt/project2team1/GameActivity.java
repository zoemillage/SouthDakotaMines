/**
 * Holds the game activity, which has the main gameplay
 */

package edu.sdsmt.project2team1;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class GameActivity extends AppCompatActivity {
    public static int rounds;
    public GameView gameView;
    private Cloud cloud;


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
     * Samantha Kaltved and Marcus Kane
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
            gameView.uploadGame(new CloudCallback() {

                @Override
                void finished(boolean done) {
                    Intent intent = new Intent(GameActivity.this, EndActivity.class);
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
                    gameView.uploadEnd(true);
                    startActivity(intent);
                }
            });

        }
        else {
            gameView.uploadGame(new CloudCallback() {

                @Override
                public void finished(boolean done) {
                    if (done) {
                        // Finished uploading data, go to the off turn activity.
                        Intent intent = new Intent(GameActivity.this, OffTurnActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }



    /**
     * Samantha Kaltved and Zoe Millage
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
        cloud = new Cloud();

        if ( savedInstanceState != null )
            gameView.loadInstanceState(savedInstanceState);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            /**
             * Gage Jager
             * prevents the back button from undoing turns
             */
            @Override
            public void handleOnBackPressed() {
                // We don't want players to go back to their last turn...
                // Really messes up keeping each device be the right player.
            }
        });

        gameView.downloadGame(new CloudCallback() {
            @Override
            public void finished(boolean done) {
                if (done) {
                    // Finish loading the data, now that we have gotten it from the database.
                    TextView name1 = findViewById(R.id.player_1_name_game);
                    String player1 = gameView.getPlayer1Name();
                    if(Objects.equals(player1, "")){
                        name1.setText(R.string.player1);
                        gameView.setPlayer1Name("Player 1");
                    }
                    else {
                        name1.setText(player1);
                        gameView.setPlayer1Name(player1);
                    }
                    TextView name2 = findViewById(R.id.player_2_name_game);
                    String player2 = gameView.getPlayer2Name();
                    if(Objects.equals(player2, "")){
                        name2.setText(R.string.player2);
                        gameView.setPlayer2Name("Player 2");
                    }
                    else {
                        name2.setText(player2);
                        gameView.setPlayer2Name(player2);
                    }
                    rounds = gameView.getTotalNumberOfRounds();
                    gameView.setTotalNumberOfRounds(rounds);
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
                else {
                    Toast.makeText(GameActivity.this, "Please check your internet connection.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }



    /**
     * Gage Jager
     * pulls up the quit menu
     * @param menu The options menu in which you place your items.
     * @return true once the menu has been created
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game_menu, menu);
        return true;
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
     * Gage Jager
     * handles the player actively quitting the game
     * @param item The menu item that was selected.
     * @return true once the menu has been handled
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.menu_quit) {
            // This will get the other player to the end screen.
            cloud.playerQuit(gameView);

            // This will get this player to the end screen.
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
            return true;
        }
        return super.onOptionsItemSelected(item);
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



    /**
     * Gage Jager
     * returns the player to the welcome activity
     */
    public void playerQuit() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }

}