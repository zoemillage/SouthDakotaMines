/**
 * Author: Zoe Millage
 * Description: Holds the state for when the cat number is low
 */

package edu.sdsmt.hcats_millage_zoe;

public class LowCats implements State {

    private StateMachine machine;
    private Game theGame;


    public LowCats ( Game g, StateMachine s ) {
        theGame = g;
        machine = s;
    }



    /**
     * does nothing
     */
    @Override
    public void endTask() {

    }



    /**
     * sends low specific sweep parameters
     * @param alt sets whether to sweep down rather than right
     */
    @Override
    public void maintenanceTask(boolean alt) {
        // GRADING: SWEEP
        if (alt) {
            theGame.sweepDown(1, 25);
        }

        else
            theGame.sweepRight(1, 25);
    }



    /**
     * does nothing
     */
    @Override
    public void startTask() {

    }
}
