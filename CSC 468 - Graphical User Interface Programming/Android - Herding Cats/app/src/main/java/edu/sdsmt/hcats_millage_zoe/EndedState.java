/**
 * Author: Zoe Millage
 * Description: Holds the state for when the game ends
 */

package edu.sdsmt.hcats_millage_zoe;

public class EndedState implements State {

    private StateMachine machine;
    private Game theGame;



    public EndedState ( Game g, StateMachine s ) {
        theGame = g;
        machine = s;
    }


    /**
     * does nothing, reset call handled by the dialog
     */
    @Override
    public void endTask() {
    }



    /**
     * sends the signal to make the end dialog
     */
    @Override
    public void startTask() {
        // GRADING: DIALOG
        if (theGame.isWon())
            machine.makeDialog();
    }



    /**
     * due to test case anomalies, ended can trigger a low cat sweep
     * @param alt signal to sweep down rather than right
     */
    @Override
    public void maintenanceTask(boolean alt) {
        if (alt) {
            theGame.sweepDown(1, 25);
        }

        else
            theGame.sweepRight(1, 25);
    }
}
