package edu.sdsmt.hcats_millage_zoe;//TODO add packages

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.widget.Button;
import android.widget.TextView;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class Teir3aStateMachine {

    public static final int MOVE_START = 15;
    public static final int TREAT_START = 3;
    public static final int CAT_START = 40;
    public static final int CAT_START_PER_CELL = 5;
    public static final int GRID_SIZE = 3;

    public static final int CAT_INITIAL_MOVE = 3;
    public static final int  CAT_INITIAL_PERCENT = 60;

    public static final int CAT_MID_MOVE = 2;
    public static final int  CAT_MID_PERCENT =50;

    public static final int CAT_LOW_MOVE = 1;
    public static final int  CAT_LOW_PERCENT =25;

    public static final int CAT_TREAT_MOVE = 5;
    public static final int  CAT_TREAT_PERCENT = 75;

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
    public void a_checkInitialState() {
        init();
        assertEquals(HighCats.class.getName(), sm.getCurrentStateName());
    }

    public void checkNumberMatch(){
        assertEquals(g.getTreats(), Integer.parseInt(treatView.getText().toString()));
        assertEquals(g.getMoves(), Integer.parseInt(moveView.getText().toString()));
        assertEquals(g.getCatsCaught(), Integer.parseInt(caughtView.getText().toString()));
    }

    @Test
    public void b_checkToMid() {
        init();
        //4 right sweeps, on threshold
        onView(withId(R.id.rightBtn)).perform(click());
        onView(withId(R.id.rightBtn)).perform(click());
        onView(withId(R.id.rightBtn)).perform(click());
        onView(withId(R.id.rightBtn)).perform(click());
        assertEquals(HighCats.class.getName(), sm.getCurrentStateName());
        //check values correct
        //row1
        assertEquals(0,  g.getCatsAt(0, 0));
        assertEquals(0,  g.getCatsAt(1, 0));
        assertEquals(CAT_START_PER_CELL*3,  g.getCatsAt(2, 0));
        //row2
        assertEquals(0,  g.getCatsAt(0, 1));
        assertEquals(0,  g.getCatsAt(1, 1));
        assertEquals(CAT_START_PER_CELL*3,  g.getCatsAt(2, 1));
        //row3
        assertEquals(0,  g.getCatsAt(0, 2));
        assertEquals(0,  g.getCatsAt(1, 2));
        //check caught count
        assertEquals(CAT_START_PER_CELL*2,  g.getCatsCaught());
        checkNumberMatch();

        //1 down sweep pushes it over
        onView(withId(R.id.downBtn)).perform(click());
        assertEquals(MidCats.class.getName(), sm.getCurrentStateName());
        assertEquals(MOVE_START-5, g.getMoves());
        //check values correct
        //col3
        int currentTotal = CAT_START_PER_CELL*3;
        int percentageApplied = currentTotal * CAT_MID_PERCENT / 100;
        assertEquals(currentTotal - percentageApplied,  g.getCatsAt(2, 0));
        assertEquals(currentTotal,  g.getCatsAt(2, 1));
        assertEquals(CAT_START_PER_CELL*2 + percentageApplied,  g.getCatsCaught());
        checkNumberMatch();
    }


    @Test
    public void c_checkTreatToMid() {
        init();

        //check mid active and work
        onView(withId(R.id.rightBtn)).perform(click());
        onView(withId(R.id.downBtn)).perform(click());
        onView(withId(R.id.rightBtn)).perform(click());
        assertEquals(MidCats.class.getName(), sm.getCurrentStateName());

        //check values correct
        //row1
        assertEquals(0,  g.getCatsAt(0, 0));
        assertEquals(0,  g.getCatsAt(1, 0));
        assertEquals(6,  g.getCatsAt(2, 0));
        //row2
        assertEquals(0,  g.getCatsAt(0, 1));
        assertEquals(4,  g.getCatsAt(1, 1));
        assertEquals(11,  g.getCatsAt(2, 1));
        //row3
        assertEquals(1,  g.getCatsAt(0, 2));
        assertEquals(7,  g.getCatsAt(1, 2));
        //check caught count
        assertEquals(11,  g.getCatsCaught());
        checkNumberMatch();

        onView(withId(R.id.treatBtn)).perform(click());
        assertEquals(TreatActive.class.getName(), sm.getCurrentStateName());
        assertEquals(MOVE_START-4, g.getMoves());
        assertEquals(TREAT_START-1, g.getTreats());

        //check treats effect
        onView(withId(R.id.rightBtn)).perform(click());
        assertEquals(MidCats.class.getName(), sm.getCurrentStateName());
        //check values correct
        //row1
        assertEquals(0,  g.getCatsAt(0, 0));
        assertEquals(0,  g.getCatsAt(1, 0));
        assertEquals(6,  g.getCatsAt(2, 0));
        //row2
        assertEquals(0,  g.getCatsAt(0, 1));
        assertEquals(0,  g.getCatsAt(1, 1));
        assertEquals(15,  g.getCatsAt(2, 1));
        //row3
        assertEquals(0,  g.getCatsAt(0, 2));
        assertEquals(3,  g.getCatsAt(1, 2));
        //check caught count
        assertEquals(16,  g.getCatsCaught());
        checkNumberMatch();

        //check mid effect is still good
        onView(withId(R.id.rightBtn)).perform(click());
        assertEquals(MidCats.class.getName(), sm.getCurrentStateName());
        onView(withId(R.id.downBtn)).perform(click());

        //check values correct
        //row1
        assertEquals(0,  g.getCatsAt(0, 0));
        assertEquals(0,  g.getCatsAt(1, 0));
        assertEquals(3,  g.getCatsAt(2, 0));
        //row2
        assertEquals(0,  g.getCatsAt(0, 1));
        assertEquals(0,  g.getCatsAt(1, 1));
        assertEquals(11,  g.getCatsAt(2, 1));
        //row3
        assertEquals(0,  g.getCatsAt(0, 2));
        assertEquals(1,  g.getCatsAt(1, 2));
        //check caught count
        assertEquals(25,  g.getCatsCaught());
        checkNumberMatch();
    }

    @Test
    public void d_checkToLow() {
        b_checkToMid();
        onView(withId(R.id.downBtn)).perform(click());
        assertEquals(LowCats.class.getName(), sm.getCurrentStateName());
        assertEquals(MOVE_START-6, g.getMoves());

        //run checks on low
        onView(withId(R.id.downBtn)).perform(click());

        //check values correct
        //row1
        assertEquals(0,  g.getCatsAt(0, 0));
        assertEquals(0,  g.getCatsAt(1, 0));
        assertEquals(3,  g.getCatsAt(2, 0));
        //row2
        assertEquals(0,  g.getCatsAt(0, 1));
        assertEquals(0,  g.getCatsAt(1, 1));
        assertEquals(10,  g.getCatsAt(2, 1));
        //row3
        assertEquals(0,  g.getCatsAt(0, 2));
        assertEquals(0,  g.getCatsAt(1, 2));
        //check caught count
        assertEquals(27,  g.getCatsCaught());
        checkNumberMatch();
    }

    @Test
    public void e_checkLowToTreat() {
        d_checkToLow();
        onView(withId(R.id.treatBtn)).perform(click());
        assertEquals(TreatActive.class.getName(), sm.getCurrentStateName());
        assertEquals(MOVE_START-8, g.getMoves());
        assertEquals(TREAT_START-1, g.getTreats());

        //check it still works
        onView(withId(R.id.downBtn)).perform(click());
        //check values correct
        //col3
        assertEquals(0,  g.getCatsAt(2, 0));
        assertEquals(6,  g.getCatsAt(2, 1));
        assertEquals(34,  g.getCatsCaught());
        checkNumberMatch();

        //back to low
        assertEquals(LowCats.class.getName(), sm.getCurrentStateName());
        assertEquals(MOVE_START-9, g.getMoves());
        assertEquals(TREAT_START-1, g.getTreats());
    }

    @Test
    public void f_checkMidTreatLow() {
        b_checkToMid();
        onView(withId(R.id.treatBtn)).perform(click());
        assertEquals(TreatActive.class.getName(), sm.getCurrentStateName());
        onView(withId(R.id.downBtn)).perform(click());
        assertEquals(LowCats.class.getName(), sm.getCurrentStateName());

        assertEquals(MOVE_START-7, g.getMoves());
        assertEquals(TREAT_START-1, g.getTreats());
        //check values correct
        //col3
        assertEquals(2,  g.getCatsAt(2, 0));
        assertEquals(10,  g.getCatsAt(2, 1));
        assertEquals(28,  g.getCatsCaught());
        checkNumberMatch();
    }

    @Test
    public void g_useUpTreats() {
        b_checkToMid();

        //use them up
        onView(withId(R.id.treatBtn)).perform(click());
        onView(withId(R.id.downBtn)).perform(click());
        onView(withId(R.id.treatBtn)).perform(click());
        onView(withId(R.id.rightBtn)).perform(click()); //down would win, so no effect move
        onView(withId(R.id.treatBtn)).perform(click());
        onView(withId(R.id.downBtn)).perform(click());

        assertEquals(MOVE_START-11, g.getMoves());
        assertEquals(TREAT_START-3, g.getTreats());

        //ensure no extra
        onView(withId(R.id.treatBtn)).perform(click());
        onView(withId(R.id.downBtn)).perform(click());
        assertEquals(MOVE_START-12, g.getMoves());
        assertEquals(TREAT_START-3, g.getTreats());

        //check values correct
        //col3
        assertEquals(0,  g.getCatsAt(2, 0));
        assertEquals(4,  g.getCatsAt(2, 1));
        assertEquals(36,  g.getCatsCaught());
        checkNumberMatch();
    }

    @Test
    public void h_testWin() {
        b_checkToMid();

        //use them up
        onView(withId(R.id.treatBtn)).perform(click());
        onView(withId(R.id.downBtn)).perform(click());
        onView(withId(R.id.treatBtn)).perform(click());
        onView(withId(R.id.downBtn)).perform(click());
        onView(withId(R.id.treatBtn)).perform(click());
        onView(withId(R.id.downBtn)).perform(click());

        assertEquals(MOVE_START-11, g.getMoves());
        assertEquals(TREAT_START-3, g.getTreats());

        //check values correct
        //col3
        assertEquals(0,  g.getCatsAt(2, 0));
        assertEquals(0,  g.getCatsAt(2, 1));
        assertEquals(40,  g.getCatsCaught());
        checkNumberMatch();

        assertTrue( g.isWon());
        assertEquals(EndedState.class.getName(), sm.getCurrentStateName());
    }

    @Test
    public void i_testLoss() {
        b_checkToMid();

        //use up moves
        for(int i = 0; i <=10; i++) {
            onView(withId(R.id.downBtn)).perform(click());
        }
        assertEquals(-1, g.getMoves());
        assertEquals(39, g.getCatsCaught());
        checkNumberMatch();

        assertTrue( g.isLost());
        assertEquals(EndedState.class.getName(), sm.getCurrentStateName());
    }

    @Test
    public void j_resetCorrect() {
        f_checkMidTreatLow();
        onView(withId(R.id.resetBtn)).perform(click());
        
        init(); //repull in case anything was remade with "new"
        helperCheckStartingGameValues();

        onView(withId(R.id.downBtn)).perform(click());
        assertEquals(HighCats.class.getName(), sm.getCurrentStateName());
        checkNumberMatch();
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

}