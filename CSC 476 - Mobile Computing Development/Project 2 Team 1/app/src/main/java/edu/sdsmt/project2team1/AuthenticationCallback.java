/**
 * contains the callback for login authentication
 */

package edu.sdsmt.project2team1;

/**
 * Callback interface to know if creating a user/logging in succeeded,
 * since you cannot return things from the OnCompleteListener,
 * but I can give something to an overridden authSucceed.
 */
public abstract class AuthenticationCallback {
    abstract void authSucceed(boolean success);
}
