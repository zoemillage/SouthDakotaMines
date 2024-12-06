/**
 * Holds the interface for callbacks from database calls
 */
package edu.sdsmt.project2team1;

/**
 * Callback interface to know when data has finished
 * being pulled from the database.
 */
public abstract class CloudCallback {
    abstract void finished(boolean done);
}
