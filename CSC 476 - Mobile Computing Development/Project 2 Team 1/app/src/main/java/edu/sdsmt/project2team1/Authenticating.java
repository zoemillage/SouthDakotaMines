/**
 * holds functionality related to authenticating a user login or creation
 */

package edu.sdsmt.project2team1;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class Authenticating {
    public final static Authenticating INSTANCE = new Authenticating();
    private final FirebaseAuth userAuth;
    private boolean authenticated = false;

    private Authenticating() {
        this.userAuth = FirebaseAuth.getInstance();
    }

    public boolean isAuthenticated() {
        return authenticated;
    }


    /**
     * Gage Jager
     * attempt to create a new user in the database
     * @param email the user's email
     * @param password the user's password
     * @param authCallback callback used if creation is successful
     */
    public void createUser(String email, String password, AuthenticationCallback authCallback) {
        Task<AuthResult> result = userAuth.createUserWithEmailAndPassword(email, password);
        result.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            /**
             * Gage Jager
             * sends a callback that user creation succeeded
             * @param task the attempted creation
             */
            @Override
            public void onComplete(@NonNull Task task) {
                authCallback.authSucceed(task.isSuccessful());
            }
        });
        result.addOnFailureListener(new OnFailureListener() {

            /**
             * Gage Jager
             * get the error code if signup fails
             * @param e the exception caused by a failed singup
             */
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthException) {
                    ((FirebaseAuthException) e).getErrorCode();
                }
            }
        });
    }



    /**
     * Gage Jager
     * attempt to login, giving an error if failed
     * @param email
     * @param password
     * @param authCallback
     */
    public void signIn(String email, String password, AuthenticationCallback authCallback) {
        Task<AuthResult> result = userAuth.signInWithEmailAndPassword(email, password);
        result.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    authenticated = true;
                    authCallback.authSucceed(true);

                } else {
                    authenticated = false;
                    authCallback.authSucceed(false);
                }
            }
        });
    }
}