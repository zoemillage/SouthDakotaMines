package edu.sdsmt.hcats_millage_zoe;//TODO add packages

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.widget.Button;
import android.widget.TextView;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class Teir2AllViewsThere {

    public static final int MOVE_START = 15;
    public static final int TREAT_START = 3;
    public static final int CAT_START = 40;
    public static final int CAT_START_PER_CELL = 5;
    public static final int GRID_SIZE = 3;

    public static final int CAT_INITIAL_MOVE = 3;
    public static final int  CAT_INITIAL_PERCENT = 60;

    //thresholds

    @Rule
    public ActivityScenarioRule<MainActivity> act = new ActivityScenarioRule<>(MainActivity.class);

    private Game g;
    private StateMachine sm;
    private MainActivity mainAct;
    private Button treatBtn;
    private Button resetBtn;
    private Button downBtn;
    private Button rightBtn;
    private TextView treatView;
    private TextView moveView;
    private TextView caughtView;

    //Initialize to have access to underlying game, state machine, and buttons
    private void init(){
        AtomicReference<Game> gameAtom = new AtomicReference<>();
        AtomicReference<StateMachine> smAtom = new AtomicReference<>();
        AtomicReference<Button> treatBtnAtom = new AtomicReference<>();
        AtomicReference<Button> resetBtnAtom = new AtomicReference<>();
        AtomicReference<Button> downBtnAtom = new AtomicReference<>();
        AtomicReference<Button> rightBtnAtom = new AtomicReference<>();
        AtomicReference<TextView> treatAtom = new AtomicReference<>();
        AtomicReference<TextView> moveAtom = new AtomicReference<>();
        AtomicReference<TextView> caughtAtom = new AtomicReference<>();
        act.getScenario().onActivity(act -> {
            mainAct = act;
            gameAtom.set(act.getGame());
            smAtom.set(act.getStateMachine());

            treatBtnAtom.set(act.findViewById(R.id.treatBtn));
            resetBtnAtom.set(act.findViewById(R.id.resetBtn));
            downBtnAtom.set(act.findViewById(R.id.downBtn));
            rightBtnAtom.set(act.findViewById(R.id.rightBtn));

            treatAtom.set(act.findViewById(R.id.treats));
            moveAtom.set(act.findViewById(R.id.moves));
            caughtAtom.set(act.findViewById(R.id.caught));
        });

        g = gameAtom.get();
        sm = smAtom.get();
        treatBtn=treatBtnAtom.get();
        resetBtn=resetBtnAtom.get();
        downBtn=downBtnAtom.get();
        rightBtn=rightBtnAtom.get();

        treatView = treatAtom.get();
        moveView = moveAtom.get();
        caughtView = caughtAtom.get();
    }

    @Test
    public void a_allCorrectButtonsAndIdsThere() {
        init();
        boolean notSet = g == null;
        notSet |= treatBtn == null;
        notSet |= resetBtn == null;
        notSet |= downBtn == null;
        notSet |= rightBtn == null;

        notSet |= treatView == null;
        notSet |= moveView == null;
        notSet |= caughtView == null;

        assertFalse(notSet);
    }


    @Test
    public void b_allStartingValuesStillCorrect() {
        init();
        helperCheckStartingGameValues();

        helperCheckNumberMatch();
    }

    @Test
    public void c_sweepsButtonsConnected() {
        init();

        //check right sweep button
        onView(withId(R.id.rightBtn)).perform(click());

        //check all counts to ensure they updated
        assertEquals(MOVE_START-1, g.getMoves() );
        //row 1
        assertEquals(CAT_START_PER_CELL -CAT_INITIAL_MOVE,  g.getCatsAt(0, 0));
        assertEquals(CAT_START_PER_CELL,  g.getCatsAt(1, 0));
        assertEquals(CAT_START_PER_CELL+CAT_INITIAL_MOVE,  g.getCatsAt(2, 0));
        //row2
        assertEquals(CAT_START_PER_CELL -CAT_INITIAL_MOVE,  g.getCatsAt(0, 1));
        assertEquals(CAT_START_PER_CELL,  g.getCatsAt(1, 1));
        assertEquals(CAT_START_PER_CELL +CAT_INITIAL_MOVE,  g.getCatsAt(2, 1));
        //row 3
        assertEquals(CAT_START_PER_CELL-CAT_INITIAL_MOVE,  g.getCatsAt(0, 2));
        assertEquals(CAT_START_PER_CELL,  g.getCatsAt(1, 2));
        assertEquals(CAT_INITIAL_MOVE,  g.getCatsCaught());
        //check every view updated
        helperCheckNumberMatch();



        //check down button
        onView(withId(R.id.downBtn)).perform(click());
        helperCheckGameStateAfterRightAndDown();
    }
    @Test
    public void d_treatButtonDoesNothingYet() {
        init();

        //shouldn't do anything yet, but should exist
        onView(withId(R.id.treatBtn)).perform(click());
        assertEquals(TREAT_START,  g.getTreats());
        assertEquals(MOVE_START, g.getMoves() );

        helperCheckNumberMatch();
    }

    @Test
    public void e_resetCorrect() {
        init();

        //make two moves
        onView(withId(R.id.rightBtn)).perform(click());
        onView(withId(R.id.downBtn)).perform(click());

        //should be the same state as end of sweep test
        helperCheckGameStateAfterRightAndDown();

        //shouldn't do anything yet, but should exist
        onView(withId(R.id.treatBtn)).perform(click());
        assertEquals(TREAT_START,  g.getTreats());
        assertEquals(MOVE_START-2, g.getMoves() );

        helperCheckNumberMatch();

        onView(withId(R.id.resetBtn)).perform(click());
        helperCheckStartingGameValues();
        helperCheckNumberMatch();
    }

    private void helperCheckStartingGameValues(){
        assertEquals(MOVE_START, g.getMoves());
        assertEquals(TREAT_START, g.getTreats());
        assertEquals(CAT_START, g.getTotalCatsLeft());
        assertEquals(0, g.getCatsCaught());
        assertEquals(MOVE_START, g.getMoves());
        assertEquals(GRID_SIZE, g.getWidth());
        assertEquals(GRID_SIZE, g.getHeight());
    }

    private void helperCheckNumberMatch(){
        assertEquals(g.getTreats(), Integer.parseInt(treatView.getText().toString()));
        assertEquals(g.getMoves(), Integer.parseInt(moveView.getText().toString()));
        assertEquals(g.getCatsCaught(), Integer.parseInt(caughtView.getText().toString()));
    }

    private void helperCheckGameStateAfterRightAndDown(){
        //check all counts to ensure they updated
        assertEquals(MOVE_START-2, g.getMoves() );
        //row 1
        assertEquals(0,  g.getCatsAt(0, 0));
        assertEquals(CAT_START_PER_CELL -CAT_INITIAL_MOVE,  g.getCatsAt(1, 0));
        int currentTotal = CAT_START_PER_CELL + CAT_INITIAL_MOVE;
        int percentApplied = currentTotal - currentTotal * CAT_INITIAL_PERCENT / 100;
        assertEquals(percentApplied,  g.getCatsAt(2, 0));
        //row2
        assertEquals(CAT_START_PER_CELL-CAT_INITIAL_MOVE,  g.getCatsAt(0, 1));
        assertEquals(CAT_START_PER_CELL,  g.getCatsAt(1, 1));
        assertEquals( currentTotal,  g.getCatsAt(2, 1));
        //row 3
        assertEquals(percentApplied,  g.getCatsAt(0, 2));
        assertEquals(CAT_START_PER_CELL+CAT_INITIAL_MOVE,  g.getCatsAt(1, 2));
        percentApplied = CAT_INITIAL_MOVE + currentTotal * CAT_INITIAL_PERCENT / 100;
        assertEquals(percentApplied,  g.getCatsCaught());
        //check every view updated
        helperCheckNumberMatch();
    }

}