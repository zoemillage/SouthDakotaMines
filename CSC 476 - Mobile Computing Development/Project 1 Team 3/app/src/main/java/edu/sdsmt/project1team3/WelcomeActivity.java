/**
 * Program: Project 1 - Risk
 * Authors: Gabe Jerome, Samantha Kaltved, Zoe Millage
 * Class: CSC-476, 2024s
 * Date: Spring 2024
 * Description: A game of risk where 2 players try to capture areas,
 * but options with higher potential point captures have lower success
 * chances. Two players play by handing around the same device.
 * This file contains the welcome activity.
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

package edu.sdsmt.project1team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class WelcomeActivity extends AppCompatActivity {
    private Spinner spinner;
    private int rounds;

    /**
     * Samantha Kaltved
     * Initiates the welcome screen
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.splashScreenTheme);
        setTheme(R.style.Base_Theme_Project1Team3);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = (Spinner) findViewById(R.id.selectRoundsSpinner);

        String[] hats = getResources().getStringArray(R.array.round_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, hats);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int pos, long id) {
                if(pos == 0){
                    rounds = 1;
                }
                else if(pos == 1){
                    rounds = 3;
                }
                else{
                    rounds = 5;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
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
     * Samantha Kaltved
     * Moves to the game activity
     * @param view the button triggering the transition
     */
    public void onNext(View view){
        Intent intent = new Intent(this, GameActivity.class);
        EditText name1 = findViewById(R.id.player1NameInput);
        EditText name2 = findViewById(R.id.player2NameInput);
        intent.putExtra("player1Name", name1.getText().toString());
        intent.putExtra("player2Name", name2.getText().toString());
        intent.putExtra("rounds", rounds);
        startActivity(intent);
    }

}