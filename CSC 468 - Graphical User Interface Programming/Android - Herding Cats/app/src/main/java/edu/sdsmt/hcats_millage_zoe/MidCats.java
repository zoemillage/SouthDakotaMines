/**
 * Author: Zoe Millage
 * Description: Holds the state for when the cat number is medium
 */

package edu.sdsmt.hcats_millage_zoe;

public class MidCats implements State {

    private StateMachine machine;
    private Game theGame;



    public MidCats ( Game g, StateMachine s ) {
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
     * sends mid specific sweep parameters
     * @param alt sets whether to sweep down rather than right
     */
    @Override
    public void maintenanceTask(boolean alt) {
        // GRADING: SWEEP
        if (alt) {
            theGame.sweepDown(2, 50);
        }

        else
            theGame.sweepRight(2, 50);
    }



    /**
     * does nothing
     */
    @Override
    public void startTask() {

    }
}
