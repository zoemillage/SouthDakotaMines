/**
 * Author: Zoe Millage
 * Description: Holds the state for when the cat number is high
 */

package edu.sdsmt.hcats_millage_zoe;

public class HighCats implements State {

    private StateMachine machine;
    private Game theGame;



    public HighCats ( Game g, StateMachine s ) {
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
     * sends high specific sweep parameters
     * @param alt sets whether to sweep down rather than right
     */
    @Override
    public void maintenanceTask(boolean alt) {
        // GRADING: SWEEP
        if (alt) {
            theGame.sweepDown(3, 50);
        }

        else
            theGame.sweepRight(3, 50);
    }



    /**
     * does nothing
     */
    @Override
    public void startTask() {

    }
}
