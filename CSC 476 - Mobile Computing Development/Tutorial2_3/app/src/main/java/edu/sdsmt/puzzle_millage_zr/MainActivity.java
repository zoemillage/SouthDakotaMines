/**
 * Program: Tutorial 2/3 - Jigsaw Puzzle
 * Author: Zoe Millage
 * Class: CSC-476, 2024s
 * Date: Spring 2024
 * Description: A basic game that gets a player name and lets the user play
 *      a 6-piece jigsaw puzzle.
 *      This file handles the welcome activity.
 */
//
//        Tutorial 2 Grading
//
//        Complete the following checklist.
//
//
//        ____hopefully__	10pt 	T2: Package is named correctly (not com.example.puzzle)
//
//        ___hopefully___	10pt 	T2: The layout items are named correctly (grubbyImage, buttonStart, textWelcome)
//
//        __hopefully____	50pt	T2: Tutorial is completed
//
//        ___hopefully___	15pt 	T2: TASK: Custom view covered entire screen (-5pt each for minor error)
//
//        ___hopefully___	15pt 	T2: TASK: Landscape task (-3pt each for minor error)
//
//        ___N/A___	15pt 	T2: CSC 578 ONLY TASK: Border around the board area (-5pt each for minor error)
//
//
//
//        The checklist is the starting point for course staff, who reserve the right to change
//        the grade if they disagree with your assessment and to deduct points for other
//        issues they may encounter, such as errors in the submission process, naming issues, etc.
//
//
//
//        Tutorial 3 Grading
//
//        Complete the following checklist.
//
//
//        ___hopefully___	55 	T3: Tutorial completed (Graded on percent done)
//
//        ___hopefully___	15 	T3: TASK: Pieces can be dragged around (-3pt each for minor error)
//
//        ___hopefully___	10 	T3: TASK: Snapped piece is on bottom (-5pt for sometimes works)
//
//        ___hopefully___	10 	T3: TASK: Refreshes immediately after shuffle menu option (-5pt each for minor error)
//
//        ___hopefully___	10	T3: TASK: Refreshes immediately after dialog shuffle button (-5pt each for minor error)
//
//        ___NA___	10 	T3: CSC 576 ONLY: Displays solved version of the puzzle
//
//
//
//        The checklist is the starting point for course staff, who reserve the right to change
//        the grade if they disagree with your assessment and to deduct points for other issues
//        they may encounter, such as errors in the submission process, naming issues, etc.
//

package edu.sdsmt.puzzle_millage_zr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    /**
     * starts the welcome activity
     * @param savedInstanceState restores the current name input
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    /**
     * Starts the puzzle activity
     * @param view the button pressed to start the puzzle
     */
    public void onStartPuzzle(View view) {
        Intent intent = new Intent(this, PuzzleActivity.class);
        EditText name = findViewById(R.id.name);
        intent.putExtra(PuzzleActivity.PLAYER_NAME, name.getText().toString());
        startActivity(intent);
    }
}