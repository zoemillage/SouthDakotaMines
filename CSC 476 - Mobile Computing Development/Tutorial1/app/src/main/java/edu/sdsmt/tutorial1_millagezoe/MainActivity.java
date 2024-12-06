/**
 * Program: Tutorial 1 - Grumpy Button
 * Author: Zoe Millage
 * Class: CSC-476, 2024s
 * Date: Spring 2024
 * Description: a basic program with a single button whose text
 *      changes when pressed once
 */
//
//Tutorial 1 Grading
//
//        Complete the following checklist. If you partially completed an item,
//        put a note how I can check what is working for partial credit.
//
//
//        Hopefully	70pt	T1: Tutorial is completed
//
//        Hopefully	10pt 	T1: Grumpy button working (-5pt each for minor error)
//
//        Hopefully	10pt 	T1: Package is named correctly (e.g. not com.example.puzzle)
//
//        Hopefully	10pt 	T1: API range correct (not com.example.puzzle)
//
//        N/A   	10pt 	T1: CSC 578 ONLY TASK: press count works (-5pt each for minor error)
//
//        The checklist is the starting point for course staff, who reserve the right to change
//        the grade if they disagree with your assessment and to deduct points for other issues
//        they may encounter, such as errors in the submission process, naming issues, etc.

        package edu.sdsmt.tutorial1_millagezoe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button grumpyButton;


    /**
     * loads the main layout with the grumpy button
     * @param savedInstanceState unused in this program
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("My Tag", "OnCreate Done");
        setContentView(R.layout.activity_main);

        grumpyButton = findViewById(R.id.button);
    }


    /***
     * changes the button's text when it is clicked
     * @param view the view containing the button
     */
    public void grumpyButtonClick (View view) {
        this.grumpyButton.setText(R.string.angry_button);
    }
}