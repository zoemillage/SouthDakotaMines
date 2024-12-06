/**
 * Author: Zoe Millage
 * Description: Holds the base class for all the states
 */

package edu.sdsmt.hcats_millage_zoe;

public interface State {

    /**
     * the task to run when a state is left
     */
    public void endTask();



    /**
     * the main task of a state
     * @param alt used to signify sweep down rather than sweep right
     */
    public void maintenanceTask(boolean alt);



    /**
     * the state to run when a state is entered
     */
    public void startTask();
}
