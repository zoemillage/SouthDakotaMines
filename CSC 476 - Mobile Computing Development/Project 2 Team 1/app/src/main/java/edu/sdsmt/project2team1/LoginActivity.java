/**
 * Program: Project 2 - Risk, Online
 * Authors: Gage Jager, Marcus Kane, Zoe Millage;
 *      builds on code by Gabe Jerome, Samantha Kaltved, Zoe Millage
 * Class: CSC-476, 2024s
 * Date: Spring 2024
 * Description: A game of risk where 2 players try to capture areas,
 * but options with higher potential point captures have lower success
 * chances. Two players play by logging in with an email, and a database
 * is used to determine whose turn it is.
 * This file contains the login activity and its functionality.
 *
 * NOTE
 * the database has been inactive for months, so it may be inaccessible and cause the app to
 * not work
 */
/*
 *
 Project 2 Grading

 firebase editor access given: yes

 Time out period: 60 seconds, check every 2 seconds.

 How to reset database (file or button): Most issues should be fixed by an "Experiencing Issues?" button
    in WelcomeActivity, after you have logged in. If you need a full reset though, a json file was
    included in the main project folder, named project2team1-default-rtdb-export.json.

 Reminder: Mark where the timeout period is set with GRADING: TIMEOUT
    Marked in OffTurnActivity.java


 Group:

 __yes__ 6pt Game still works and Database setup
 __yes__ 8pt Database setup\reset
 __yes__ 8pt New user activity
 __yes__ 18pt Opening\login activity
 __yes__ 5pt rotation


 Individual:

 Sequencing
 __yes__ 4pt Registration sequence
 __yes__ 9pt Login Sequence
 __yes__ 18pt Play Sequence
 __yes__ 9pt Exiting menu, and handlers
 __yes__ 5pt rotation


 Upload

 __yes__ 6pt intial setup
 __yes__ 6pt waiting
 __yes__ 17pt store game state
 __yes__ 11pt notify end/early exits
 __yes__ 5pt rotation


 Download

 __yes__ 6pt intial setup
 __yes__ 6pt waiting
 __yes__ 17pt store game state
 __yes__ 11pt grab and forward end/early exits
 __yes__ 5pt rotation


 Monitor Waiting
 __N/A__ 10pt inital setup
 __N/A__ 12pt Uploading the 3 state
 __N/A__ 12pt Downloading the 3 state
 __N/A__ 6pt UI update
 __N/A__ 5pt rotation

 Please list any additional rules that may be needed to properly grade your project:

 Due to detecting the passive exit where the off-turn player loses internet, closes the app,
 or otherwise leaves the game early, it is possible for a player to accidentally trigger the
 passive end if they make their turn in less than 2 seconds, approximately.  The timing differs
 based upon when the game data is uploaded in relation to the 2 second delay before the other player
 checks for the uploaded data.  Either way, the game may behave incorrectly if you simply
 spam the collect button as quickly as possible.

 If both players disconnect from the internet, when the game ends, neither player will be able to
 reset the READY/WAITING status in the database.  In this case, please hit the "Experiencing Issues?"
 button in the Welcome activity before starting another game.
 */
/*
Project 1 Grading

Group:
_Done_ 6pt No redundant activities
_Done_ 6pt How to play dialog
_Done_ 6pt Icons
_Done_ 6pt End activity
_Done_ 6pt Back button handled
How to open the "how to play dialog": _Button on Welcome Activity_

Individual:

	Play activity and custom view

		_Done_ 15pt Activity appearance\layout
		_Done_ 12pt Static Custom View
		_Done_ 23pt Dynamic part of the Custom View
		_Done_ 10pt Rotation

	Welcome activity and Game Class

		_Done_ 8pt Welcome activity appearance\layout
		_Done_ 25pt Applying capture rules
		_Done_ 12pt Game state
		_Done_ 15pt Rotation
		What is the probability of the rectangle capture:
		    _It is a linear regression between 15% and 50%. It is clamped by the ratio of the
		    board size and the rectangle size (1:1 would have a 50% chance of capture)_

	Capture activity and activity sequencing

		_Done_ 8pt Capture activity appearance\layout
		_Done_ 20pt Player round sequencing
		_Done_ 27pt Move to next activity
		_Done_ 5pt Rotation
		_Done_     Splash Screen

	Timer

		_N/A_ 16pt Timer activity\appearance\layout
		_N/A_ 20pt Graphic
		_N/A_ 14pt Player turn end
		_N/A_ 10pt Rotation


Please list any additional rules that may be needed to properly grade your project:
    Samantha Kaltved is 500 level and worked on Controller and Activity Sequencing and completed a splash screen
 */

package edu.sdsmt.project2team1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String CHECKED = "checked";
    private final Authenticating auth = Authenticating.INSTANCE;
    private CheckBox remember;
    private SharedPreferences sharedPref;


    /**
     * Gage Jager
     * initializes the login activity. lets the user login, and save a
     * preference to be remembered
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPref = getPreferences(Context.MODE_PRIVATE);

        remember = findViewById(R.id.rememberMe);

        // Load the email and password, if the checkbox was saved as checked.
        boolean defaultChecked = false;
        boolean wasChecked = sharedPref.getBoolean(CHECKED, defaultChecked);
        if (wasChecked) {
            TextView email = findViewById(R.id.LoginEmailField);
            TextView password = findViewById(R.id.LoginPasswordField);
            String savedEmail = sharedPref.getString(EMAIL, "");
            String savedPassword = sharedPref.getString(PASSWORD, "");
            remember.setChecked(true);
            email.setText(savedEmail);
            password.setText(savedPassword);
        }

        // While the above will always run, even on rotations,
        // if we did rotate the device, this would set everything the way it was
        // before the rotation, negating the above.
        if(savedInstanceState != null) {
            TextView email = findViewById(R.id.LoginEmailField);
            TextView password = findViewById(R.id.LoginPasswordField);
            String savedEmail = savedInstanceState.getString(EMAIL);
            String savedPassword = savedInstanceState.getString(PASSWORD);
            boolean isChecked = savedInstanceState.getBoolean(CHECKED);
            // Do not set fields if the Strings are null.
            if (savedEmail != null) {
                email.setText(savedEmail);
            }
            if (savedPassword != null) {
                password.setText(savedPassword);
            }
            remember.setChecked(isChecked);
        }

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }



    /**
     * Gage Jager
     * moves to the sign up activity
     * @param view the sign up transition button
     */
    public void onGoToSignUp(View view) {
        Intent intent = new Intent(this, CreateUserActivity.class);
        startActivity(intent);
    }



    /**
     * Gage Jager
     * tries to login and move to the welcome activity
     * @param view the login button
     */
    public void onLogin(View view) {
        TextView email = findViewById(R.id.LoginEmailField);
        TextView password = findViewById(R.id.LoginPasswordField);
        String enteredEmail = email.getText().toString();
        String enteredPassword = password.getText().toString();

        // First check: All fields are not null.
        // Second check (through Firebase): Is the login valid.
        if (enteredEmail.matches("") || enteredPassword.matches("")) {
            Toast.makeText(LoginActivity.this, "Please fill in all fields.",
                    Toast.LENGTH_LONG).show();
        }
        else {
            auth.signIn(enteredEmail, enteredPassword, new AuthenticationCallback() {
                @Override
                public void authSucceed(boolean success) {
                    if (success) {
                        // First, save the login info if the box is checked.
                        if (remember.isChecked()) {
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(EMAIL, enteredEmail);
                            editor.putString(PASSWORD, enteredPassword);
                            editor.putBoolean(CHECKED, true);
                            editor.apply();
                        }
                        // Go to next activity.
                        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Please check your email, password, and internet connection.",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }



    /**
     * Gage Jager
     * saves states and input text
     * @param outState Bundle in which to place your saved state.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        TextView email = findViewById(R.id.LoginEmailField);
        TextView password = findViewById(R.id.LoginPasswordField);
        outState.putString(EMAIL, email.getText().toString());
        outState.putString(PASSWORD, password.getText().toString());
        outState.putBoolean(CHECKED, remember.isChecked());
    }

}