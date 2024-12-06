/**
 * Holds functions related to uploading/downloading with the Firebase database
 */

package edu.sdsmt.project2team1;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cloud {
    private final static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final static DatabaseReference gameRef = database.getReference("Game");
    private final static DatabaseReference lobbyRef = database.getReference("Lobby");
    private int player;
    private String player1Name = "";
    private String player2Name = "";
    private final static String GAME_OVER = "gameOver";
    private final static String PLAYER_1 = "player1";
    private final static String PLAYER_2 = "player2";
    private final static String PLAYER_1_NAME = "player1Name";
    private final static String PLAYER_2_NAME = "player2Name";
    private final static String PLAYER_1_TURN = "player1Turn";
    private final static String WAITING = "waiting";
    private final static String READY = "ready";
    private final static String TURN_OVER = "turnOver";

    /**
     * the game in a serializable form
     */
    private static class GameState implements Serializable {
        public String player1Name;
        public String player2Name;
        public int player1Score;
        public int player2Score;
        public int roundNumber;
        public int playerTurn;
        List<List<Integer>> board;
        public int totalNumberOfRounds;
        public boolean player1Turn;
        public boolean gameEnded;
    }



    /**
     * Gage Jager
     * This one's an edge case for when the off-turn player disconnects
     *      during the active player's turn. We needed a way to prevent
     *      the remaining player from becoming both players and go to the end screen instead,
     *      and this boolean is that way.
     * @param cloudCallback the callback to use with the database check
     */
    public void checkTurnOver(CloudCallback cloudCallback) {
        gameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            /**
             * Check if the active player's turn is over
             * @param snapshot The current data at the location
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean stillOver = Boolean.parseBoolean(snapshot.child(TURN_OVER).getValue().toString());
                cloudCallback.finished(stillOver);
            }


            /**
             * Gage Jager
             * handle a database checking error
             * @param error A description of the error that occurred
             */
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }



    /**
     * Marcus Kane
     * converts a 2D integer array to a 2D integer list
     * @param array the array to convert
     * @return the array as a list
     */
    private static List<List<Integer>> convertIntArrayToList(int[][] array) {
        List<List<Integer>> list = new ArrayList<>();
        for (int[] row : array) {
            List<Integer> rowList = new ArrayList<>();
            for (int element : row) {
                rowList.add(element);
            }
            list.add(rowList);
        }
        return list;
    }



    /**
     * Zoe Millage
     * Converts a given 2d list of integers into a 2d array of integers
     * @param list - the list to convert
     * @return the list as a 2d array
     */
    private static int[][] convertIntListToArray (List<List<Integer>> list) {
        int size = list.size();
        int[][] arr = new int[size][size];

        for (int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                arr[i][j] = list.get(i).get(j);
            }
        }

        return arr;
    }



    /**
     * Gage Jager
     * reset the database to default values
     * @param view the button that caused this
     */
    public void deleteGameData(GameView view) {
        Game emptyGame = new Game(view);
        int [][] emptyBoard = new int [0][0];
        emptyGame.setPlayer1Name("");
        emptyGame.setPlayer2Name("");
        emptyGame.setPlayer1Score(0);
        emptyGame.setPlayer2Score(0);
        emptyGame.setRoundNumber(0);
        emptyGame.setPlayerTurn(0);
        emptyGame.setBoard(emptyBoard);
        emptyGame.setTotalNumberOfRounds(0);
        upload(emptyGame, view, new CloudCallback() {

            @Override
            void finished(boolean done) {
                // We don't need to worry about anything here.
                // Just required to include a cloud callback because I'm calling upload.
            }
        });
    }



    /**
     * Zoe Millage
     * Downloads the current gameRef's data into the game
     * @param game - the current game, the game to insert data into
     * @param view - the view to show possible errors
     */
    public void download (Game game, GameView view, CloudCallback cloudCallback) {
        // check the database
        gameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // insert the values
                // player names
                game.setPlayer1Name(snapshot.child("player1Name").getValue().toString());
                game.setPlayer2Name(snapshot.child("player2Name").getValue().toString());
                player1Name = game.getPlayer1Name();
                player2Name = game.getPlayer2Name();

                // player scores
                game.setPlayer1Score(Integer.parseInt
                        (snapshot.child("player1Score").getValue().toString()));
                game.setPlayer2Score(Integer.parseInt
                        (snapshot.child("player2Score").getValue().toString()));

                // round/turn info
                game.setRoundNumber(Integer.parseInt
                        (snapshot.child("roundNumber").getValue().toString()));
                game.setPlayerTurn(Integer.parseInt
                        (snapshot.child("playerTurn").getValue().toString()));
                game.setTotalNumberOfRounds(Integer.parseInt
                        (snapshot.child("totalNumberOfRounds").getValue().toString()));

                // board
                int size = (int) snapshot.child("board").getChildrenCount();

                ArrayList<ArrayList<Integer>> tempList = new ArrayList<>();
                // run through each row of the board, adding it to the full list
                for ( int i = 0; i < size; i++) {
                    ArrayList<Integer> tempRow = new ArrayList<>();

                    for(DataSnapshot snappy : snapshot.child("board").child(String.valueOf(i)).getChildren()) {
                        tempRow.add(Integer.parseInt(snappy.getValue().toString()));
                    }

                    tempList.add(tempRow);
                }

                // convert to the proper list type, then set it as the new board
                List<List<Integer>> intList = new ArrayList<>(tempList);
                int[][] theBoard = convertIntListToArray(intList);
                game.setBoard(theBoard);

                cloudCallback.finished(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // do an error toast
                view.post(() -> Toast.makeText(view.getContext(), R.string.fetch_failed, Toast.LENGTH_SHORT).show());
            }
        });
    }


    /**
     * Zoe Millage
     * see if the game has ended
     * @param view the button that triggered this
     * @return
     */
    public boolean downloadEnd (View view) {
        final boolean[] turnBool = new boolean[1];

        // check the database
        gameRef.addListenerForSingleValueEvent(new ValueEventListener() {

            /**
             * Zoe Millage
             * get the turn boolean
             * @param snapshot The current data at the location
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                turnBool[0] = Boolean.parseBoolean(snapshot.child(GAME_OVER)
                        .getValue().toString());
            }

            /**
             * Zoe Millage
             * do an error toast
             * @param error A description of the error that occurred
             */
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                view.post(() -> Toast.makeText(view.getContext(), R.string.fetch_failed, Toast.LENGTH_SHORT).show());
            }
        });

        return turnBool[0];
    }


    /**
     * Zoe Millage
     * get whose turn it is
     * @param view the button that caused this
     * @return true if it's player 1's turn, false otherwise
     */
    public boolean downloadTurn (View view) {
        final boolean[] turnBool = new boolean[1];

        // check the database
        gameRef.addListenerForSingleValueEvent(new ValueEventListener() {

            /**
             * get whose turn it is
             * @param snapshot The current data at the location
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                turnBool[0] = Boolean.parseBoolean(snapshot.child(PLAYER_1_TURN)
                        .getValue().toString());
            }

            /**
             * do an error toast
             * @param error A description of the error that occurred
             */
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                view.post(() -> Toast.makeText(view.getContext(), R.string.fetch_failed, Toast.LENGTH_SHORT).show());
            }
        });

        return turnBool[0];
    }



    /**
     * Gage Jager
     * checks if both players have joined
     * @param view the button that eventually moved a player to the lobby
     * @param name the most recently joined player's name
     * @param context used for moving to specific activities
     */
    public void findPlayers(View view, String name, Context context) {
        lobbyRef.child(PLAYER_1).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    view.post(() -> Toast.makeText(view.getContext(), R.string.fetch_failed, Toast.LENGTH_SHORT).show());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    if (String.valueOf(task.getResult().getValue()).equals(WAITING)) {
                        // you are player 1
                        player1Name = name;
                        lobbyRef.child(PLAYER_1).setValue(READY);
                        gameRef.child(PLAYER_1_NAME).setValue(player1Name);
                        player = 1;
                        // must wait for player 2 to join
                        Intent intent = new Intent(context, LobbyActivity.class);
                        startActivity(context, intent, null);
                    } else {
                        // player 1 is already taken and you are player 2
                        player2Name = name;
                        lobbyRef.child(PLAYER_2).setValue(READY);
                        gameRef.child(PLAYER_2_NAME).setValue(player2Name);
                        player = 2;
                        // we are ready to start the game
                        // This section is what gets Player 2 into the game proper.
                        // They always start in the off turn activity.
                        Intent intent = new Intent(context, OffTurnActivity.class);
                        startActivity(context, intent, null);

                    }
                }
            }
        });
    }



    /**
     * Gage Jager
     * sets the game to over when a player quits
     * @param view the quit button
     */
    public void playerQuit(GameView view) {
        gameRef.child(GAME_OVER).setValue(true);
        resetPlayers();
    }



    /**
     * Zoe Millage
     * checks if both players are ready to play
     */
    public void playersReady(Context context) {
        final boolean[] bothReady = {false};
        lobbyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String p1State = snapshot.child(PLAYER_1).getValue().toString();
                String p2State = snapshot.child(PLAYER_2).getValue().toString();

                bothReady[0] = ( p1State.equals(READY) & p2State.equals(READY) );

                if (bothReady[0]) {
                    // This section is what gets Player 1 into the game proper.
                    // We need a new game. This one's scuffed, since we don't have a
                    // GameActivity, GameView, or Game at this point.
                    int [][] emptyBoard = new int[5][5];
                    gameRef.child("board").setValue(convertIntArrayToList(emptyBoard));
                    gameRef.child("gameOver").setValue(false);
                    gameRef.child("player1Score").setValue(0);
                    gameRef.child("player2Score").setValue(0);
                    gameRef.child("player1Turn").setValue(true);
                    gameRef.child("playerTurn").setValue(1);
                    gameRef.child("roundNumber").setValue(1);
                    gameRef.child("totalNumberOfRounds").setValue(3);
                    Intent intent = new Intent(context, GameActivity.class);
                    startActivity(context, intent, null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }



    /**
     * Gage Jager
     * resets the player statuses in the database to waiting
     */
    public void resetPlayers() {
        lobbyRef.child(PLAYER_1).setValue(WAITING);
        lobbyRef.child(PLAYER_2).setValue(WAITING);
        gameRef.child(TURN_OVER).setValue(false);
    }



    /**
     * Marcus Kane
     * puts the game into a serializable state
     * @param game the game to serialize
     * @return the game, serialized
     */
    private GameState serializeGame(Game game) {
        GameState gameState = new GameState();
        gameState.player1Name = game.getPlayer1Name();
        gameState.player1Score = game.getPlayer1Score();
        gameState.player2Name = game.getPlayer2Name();
        gameState.player2Score = game.getPlayer2Score();
        gameState.roundNumber = game.getRoundNumber();
        gameState.playerTurn = game.getPlayerTurn();
        gameState.board = convertIntArrayToList(game.getBoard());
        gameState.totalNumberOfRounds = game.getTotalNumberOfRounds();
        gameState.player1Turn = game.getPlayerTurn() == 1;
        gameState.gameEnded = game.getRoundNumber() > game.getTotalNumberOfRounds();


        return gameState;
    }



    /**
     * Marcus Kane
     * maps a serializable game state for database upload
     * @param gameState the game state
     * @return a mapping of the game state
     */
    private Map<String, Object> serializeGameState(GameState gameState) {
        Map<String, Object> gameMap = new HashMap<>();
        gameMap.put("player1Name", gameState.player1Name);
        gameMap.put("player1Score", gameState.player1Score);
        gameMap.put("player2Name", gameState.player2Name);
        gameMap.put("player2Score", gameState.player2Score);
        gameMap.put("roundNumber", gameState.roundNumber);
        gameMap.put("playerTurn", gameState.playerTurn);
        gameMap.put("player1Turn", gameState.player1Turn);
        gameMap.put("board", gameState.board);
        gameMap.put("totalNumberOfRounds", gameState.totalNumberOfRounds);
        gameMap.put(GAME_OVER, gameState.gameEnded);
        return gameMap;
    }



    /**
     * Marcus Kane
     * send the data necessary to change whose turn it is
     * @param context used to change activity
     */
    public void turnOver(Context context) {
        final boolean[] turnOver = {false};
        final boolean[] gameOver = {false};
        gameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // get turnOver and gameOver
                turnOver[0] = Boolean.parseBoolean(snapshot.child(TURN_OVER)
                        .getValue().toString());
                gameOver[0] = Boolean.parseBoolean(snapshot.child(GAME_OVER)
                        .getValue().toString());
                // If the turn is over, quickly reset it to be not over, then launch the GameActivity,
                // unless the game is over.
                if (gameOver[0]) {
                    // Head to the end of the game.
                    String p1Name = snapshot.child(PLAYER_1_NAME).getValue().toString();
                    String p2Name = snapshot.child(PLAYER_2_NAME).getValue().toString();
                    Integer p1Score = Integer.parseInt(snapshot.child("player1Score").getValue().toString());
                    Integer p2Score = Integer.parseInt(snapshot.child("player2Score").getValue().toString());
                    Intent intent = new Intent(context, EndActivity.class);
                    if (p2Score > p1Score) {
                        intent.putExtra("winnerName", p2Name);
                        intent.putExtra("winnerScore", p2Score);
                        intent.putExtra("loserName", p1Name);
                        intent.putExtra("loserScore", p1Score);
                    } else {
                        intent.putExtra("winnerName", p1Name);
                        intent.putExtra("winnerScore", p1Score);
                        intent.putExtra("loserName", p2Name);
                        intent.putExtra("loserScore", p2Score);
                    }
                    resetPlayers();
                    startActivity(context, intent, null);
                }
                else{
                    if (turnOver[0]) {
                        gameRef.child(TURN_OVER).setValue(false);
                        Intent intent = new Intent(context, GameActivity.class);
                        startActivity(context, intent, null);
                    }
                }
            }

            /**
             * handle an error
             * @param error A description of the error that occurred
             */
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }



    /**
     * Marcus Kane
     * sends the serialized and mapped game to the database
     * @param game the game
     * @param view the button that eventually caused the upload
     * @param cloudCallback the result of the attempted upload
     */
    public void upload(Game game, GameView view, CloudCallback cloudCallback) {
        GameState gameState = serializeGame(game);
        Map<String, Object> gameMap = serializeGameState(gameState);
        gameRef.setValue(gameMap, (databaseError, databaseReference) -> {
            if (databaseError != null) {
                view.post(() -> Toast.makeText(view.getContext(), R.string.upload_failed, Toast.LENGTH_SHORT).show());
            }
        });
        gameRef.child(TURN_OVER).setValue(true);
        cloudCallback.finished(true);
    }



    /**
     * Marcus Kane
     * send the end of the game flag to the database
     * @param game the game
     * @param view the button that caused this
     */
    public void uploadEnd(Game game, GameView view) {
        boolean tempBool = game.getRoundNumber() > game.getTotalNumberOfRounds();
        gameRef.child(GAME_OVER).setValue(tempBool, (databaseError, databaseReference) -> {
            if (databaseError != null) {
                view.post(() -> Toast.makeText(view.getContext(), R.string.upload_failed, Toast.LENGTH_SHORT).show());
            }
        });
    }



    /**
     * Marcus Kane
     * send the end of the game flag to the database
     * @param game the game
     * @param view the button that caused this
     * @param ended whether or not the game ended
     */
    public void uploadEnd(Game game, GameView view, Boolean ended) {
        gameRef.child(GAME_OVER).setValue(ended, (databaseError, databaseReference) -> {
            if (databaseError != null) {
                view.post(() -> Toast.makeText(view.getContext(), R.string.upload_failed, Toast.LENGTH_SHORT).show());
            }
        });
    }



    /**
     * Marcus Kane
     * upload whose turn it is to the database
     * @param game the game
     * @param view the button that caused this
     */
    public void uploadTurn(Game game, GameView view) {
        boolean tempBool = game.getPlayerTurn() == 1;
        gameRef.child(PLAYER_1_TURN).setValue(tempBool, (databaseError, databaseReference) -> {
            if (databaseError != null) {
                view.post(() -> Toast.makeText(view.getContext(), R.string.upload_failed, Toast.LENGTH_SHORT).show());
            }
        });
    }

}
