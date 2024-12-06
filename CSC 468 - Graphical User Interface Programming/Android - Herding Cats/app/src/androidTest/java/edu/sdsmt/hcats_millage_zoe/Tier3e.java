/**
 * Author: Zoe Millage
 * Description: Holds the model part of the game
 */

package edu.sdsmt.hcats_millage_zoe;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;

import android.content.pm.ActivityInfo;
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
 * The entry point of the test
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class Tier3e {

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

    /**
     * Initialize to have access to underlying game, state machine, and buttons
     */
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


    /**
     * makes sure model and view agree
     */
    public void checkNumberMatch(){
        assertEquals(g.getTreats(), Integer.parseInt(treatView.getText().toString()));
        assertEquals(g.getMoves(), Integer.parseInt(moveView.getText().toString()));
        assertEquals(g.getCatsCaught(), Integer.parseInt(caughtView.getText().toString()));
    }


    /**
     * makes sure that values are saved on rotation
     */
    @Test
    public void testRotate () {
        int i, j;

        init();

        // get to mid cats state
        onView(withId(R.id.downBtn)).perform(click());
        onView(withId(R.id.downBtn)).perform(click());
        onView(withId(R.id.downBtn)).perform(click());
        onView(withId(R.id.downBtn)).perform(click());
        onView(withId(R.id.rightBtn)).perform(click());

        // use a treat
        onView(withId(R.id.treatBtn)).perform(click());

        // check values
        assertEquals(TreatActive.class.getName(), sm.getCurrentStateName());
        assertEquals(MOVE_START-6, g.getMoves());
        assertEquals(TREAT_START-1, g.getTreats());
        assertEquals(17, g.getCatsCaught());

            // top 2 rows are all 0
        for ( i = 0; i < 2; i++) {
            for (j = 0; j < 2; j++) {
                assertEquals(0, g.getCatsAt(i, j));
            }
        }

            // check the bottom row
        assertEquals(8, g.getCatsAt(0, 2));
        assertEquals(15, g.getCatsAt(1, 2));

        checkNumberMatch();

        // rotate
        mainAct.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getInstrumentation().waitForIdleSync();

        // check values
        assertEquals(TreatActive.class.getName(), sm.getCurrentStateName());
        assertEquals(MOVE_START-6, g.getMoves());
        assertEquals(TREAT_START-1, g.getTreats());
        assertEquals(17, g.getCatsCaught());

        // top 2 rows are all 0
        for ( i = 0; i < 2; i++) {
            for (j = 0; j < 2; j++) {
                assertEquals(0, g.getCatsAt(i, j));
            }
        }

        // check the bottom row
        assertEquals(8, g.getCatsAt(0, 2));
        assertEquals(15, g.getCatsAt(1, 2));

        checkNumberMatch();

    }

}