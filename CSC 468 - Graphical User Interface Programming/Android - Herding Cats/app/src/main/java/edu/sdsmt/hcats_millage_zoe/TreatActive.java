/**
 * Author: Zoe Millage
 * Description: Holds the state for when a treat is active
 */

package edu.sdsmt.hcats_millage_zoe;

public class TreatActive implements State {

    private StateMachine machine;
    private Game theGame;



    public TreatActive ( Game g, StateMachine s ) {
        theGame = g;
        machine = s;
    }



    /**
     * resets the using treats flag used by the view
     */
    @Override
    public void endTask() {
        theGame.setUsingTreat(false);
    }



    /**
     * sends treat specific sweep parameters
     * @param alt sets whether to sweep down rather than right
     */
    @Override
    public void maintenanceTask(boolean alt) {
        // GRADING: SWEEP
        if (alt) {
            theGame.sweepDown(5, 75);
        }

        else
            theGame.sweepRight(5,75);
    }



    /**
     * sets the using treat flag used by the view and decreases the number of treats
     */
    @Override
    public void startTask() {
        theGame.useTreat();
        theGame.setUsingTreat(true);
    }

}
