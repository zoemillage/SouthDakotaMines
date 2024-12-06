/**
 * The activity for creating a new user
 */

package edu.sdsmt.project2team1;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CreateUserActivity extends AppCompatActivity {

    private static final String EMAIL = "email";
    private static final String PASSWORD = "pass1";
    private static final String REPASSWORD = "pass2";
    private final Authenticating auth = Authenticating.INSTANCE;


    /**
     * Gage Jager
     * initialize the activity and replace values if there's any saved
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        if(savedInstanceState != null) {
            TextView email = findViewById(R.id.EmailField);
            TextView password = findViewById(R.id.PasswordField);
            TextView repassword = findViewById(R.id.RePasswordField);
            String savedEmail = savedInstanceState.getString(EMAIL);
            String savedPassword = savedInstanceState.getString(PASSWORD);
            String savedRePassword = savedInstanceState.getString(REPASSWORD);
            // Do not set fields if the strings are null.
            if (savedEmail != null) {
                email.setText(savedEmail);
            }
            if (savedPassword != null) {
                password.setText(savedPassword);
            }
            if (savedRePassword != null) {
                repassword.setText(savedRePassword);
            }
        }

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }



    /**
     * Gage Jager
     * attempts to create a new user
     * @param view the register button
     */
    public void onRegister(View view) {
        TextView email = findViewById(R.id.EmailField);
        TextView password = findViewById(R.id.PasswordField);
        TextView repassword = findViewById(R.id.RePasswordField);
        String enteredEmail = email.getText().toString();
        String enteredPassword = password.getText().toString();
        String enteredRePassword = repassword.getText().toString();

        // First check: All fields are not null.
        // Second check: Entered passwords match.
        if (enteredEmail.matches("") || enteredPassword.matches("") || enteredRePassword.matches("")) {
            Toast.makeText(CreateUserActivity.this, "Please fill in all fields.",
                    Toast.LENGTH_LONG).show();
        }
        else {
            if (enteredPassword.matches(enteredRePassword)) {
                if (enteredPassword.length() >= 6) {
                    auth.createUser(enteredEmail, enteredPassword, new AuthenticationCallback() {
                        @Override
                        public void authSucceed(boolean success) {
                            if (success) {
                                Toast.makeText(CreateUserActivity.this, "Success! Please log in now.",
                                        Toast.LENGTH_LONG).show();
                                finish();
                            }
                            else {
                                Toast.makeText(CreateUserActivity.this, "Something went wrong while processing your registration.",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(CreateUserActivity.this, "Password must be 6+ characters.",
                            Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(CreateUserActivity.this, "Passwords entered do not match.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }



    /**
     * moves back to the login page
     * @param view the button used to move back
     */
    public void onReturnToLogin(View view) {
        finish();
    }



    /**
     * Gage Jager
     * saves input text to a bundle
     * @param outState Bundle in which to place your saved state.
     *
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        TextView email = findViewById(R.id.EmailField);
        TextView password = findViewById(R.id.PasswordField);
        TextView repassword = findViewById(R.id.RePasswordField);
        outState.putString(EMAIL, email.getText().toString());
        outState.putString(PASSWORD, password.getText().toString());
        outState.putString(REPASSWORD, repassword.getText().toString());
    }
}