/**
 * Author: Zoe Millage
 * Description: Holds logic for the state machine and state switching
 */

package edu.sdsmt.hcats_millage_zoe;

import android.os.Bundle;

public class StateMachine {
    private  static String IS_TREAT = "isTreat";

    private boolean isTreat;
    private MainActivity main;
    private StateEnum state = StateEnum.High;
    private State[] stateArray = null;
    private Game theGame;


    public enum StateEnum {High, Mid, Low, Treat, End}



    public StateMachine(MainActivity m, Game g) {
        main = m;
        theGame = g;

        stateArray = new State[] {
                new HighCats(g, this),
                new MidCats(g, this),
                new LowCats(g, this),
                new TreatActive(g, this),
                new EndedState(g, this)
        };
    }



    /**
     * tells the state to do its maintenance task
     * @param alt used to signify a sweep down rather than sweep right
     */
    public void doTask(boolean alt) {
        // GRADING: SWEEPs contained in the maintenance tasks of high/mid/low/treat classes
        stateArray[this.state.ordinal()].maintenanceTask(alt);
        onUpdate();
    }



    public String getCurrentStateName() {
        return stateArray[this.state.ordinal()].getClass().getName();
    }



    /**
     * runs the current state's end task, switches states, and runs the new state's entry task
     * @param state the state to switch to
     */
    public void setState( StateEnum state ) {
        //exit old state
        stateArray[this.state.ordinal()].endTask( );

        // set the state
        this.state = state;

        // start new state
        stateArray[this.state.ordinal()].startTask( );
    }



    /**
     * Specific to moving back to the treat state when rotating and a treat was active,
     * removes some clutter from onUpdate
     * @param state
     * @param wasTreat
     */
    public void setState( StateEnum state, boolean wasTreat ) {
        // set the state that was already started before a rotate
        this.state = state;
    }



    /**
     * handles state transitions related to doing a turn or updating on a rotate not related to
     * transitioning into a treat state or a reset
     */
    public void onUpdate() {
        int caught = theGame.getCatsCaught();

        // check game ends
        // GRADING: DIALOG contained in EndedState.startTask
        if (!getCurrentStateName().equals(EndedState.class.getName()) && (theGame.isLost() || theGame.isWon()))
            setState(StateEnum.End);

        // check for treat state
        //  GRADING: TO_NO_TREAT
        else if (getCurrentStateName().equals(TreatActive.class.getName())) {
            if (caught > 20)
                setState(StateEnum.Low);

            else if (caught > 10)
                setState(StateEnum.Mid);

            else
                setState(StateEnum.High);

            isTreat = false;
        }

        // check high/mid/low thresholds
        else if (getCurrentStateName().equals(HighCats.class.getName()) && caught > 10)
            setState(StateEnum.Mid);

        else if (getCurrentStateName().equals(MidCats.class.getName()) && caught > 20)
            setState(StateEnum.Low);

    }



    /**
     * resets to the high cat state
     */
    public void reset() {
        // GRADING: RESET
        setState(StateEnum.High);
    }



    /**
     * checks if moving to the treat state is valid. Doing the transition only if it is.
     */
    public void useTreat() {
        // GRADING: TO_TREAT
        String currState = getCurrentStateName();

        if(theGame.getTreats() > 0 && !currState.equals(TreatActive.class.getName())
                && (currState.equals(MidCats.class.getName()) ||
                currState.equals(LowCats.class.getName()))) {
            setState(StateEnum.Treat);
            isTreat = true;
        }
    }



    /**
     * sends the signal to make the game end dialog
     */
    public void makeDialog() {
        main.makeDialog();
    }



    /**
     * saves if a treat is currently active. Used for rotation
     * @param b the bundle to save to
     */
    public void saveInstanceState(Bundle b) {
        b.putBoolean(IS_TREAT, isTreat);
    }



    /**
     * loads the treat state
     * @param b the bundle to load from
     */
    public void loadInstanceState(Bundle b) {
        isTreat = b.getBoolean(IS_TREAT);

        if (isTreat)
            setState(StateEnum.Treat, true);

        else
            onUpdate();
    }
}
