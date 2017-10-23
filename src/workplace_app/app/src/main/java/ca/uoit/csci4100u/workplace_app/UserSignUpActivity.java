package ca.uoit.csci4100u.workplace_app;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * The 'UserSignUpActivity' class which is the activity the user sees when needing to create a new
 * account. This handles creating a new account and moving back to the 'LoginActivity'
 */
public class UserSignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    /**
     * The onCreate method for the 'UserSignUpActivity' class. This function initializes the activity
     * and sets the member variables.
     * @param savedInstanceState The saved instance state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * Handles the onClick function for the 'Sign Up' button. This will take the values in the
     * EditText fields and create an account for the new user
     * @param view The view that has been clicked (the button)
     */
    public void handleSignUp(View view) {
        String email = ((EditText) findViewById(R.id.signUpEmail)).getText().toString();
        String password = ((EditText) findViewById(R.id.signUpPass)).getText().toString();

        if (!email.isEmpty() && !password.isEmpty()) {
            createAccount(email, password);
        }
    }

    /**
     * Handles the onClick function for the 'Back' button. This will close the current sub-activity
     * 'UserSignUpActivity'
     * @param view The view that has been clicked (the button)
     */
    public void handleBack(View view) {
        finish();
    }

    /**
     * A helper function used to create a new Firebase account. This function will create a toast
     * if the account was created successfully or not
     * @param email A string representation of the user-specified email address
     * @param password A string representation of the user-specified password
     */
    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sendEmailVerification();
                        } else {
                            // TODO: Check if we need to do our own error messages since it's not in a resource file
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(UserSignUpActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * A helper function to send an email verification to the email given in the sign up information
     */
    private void sendEmailVerification() {
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UserSignUpActivity.this,
                                    getString(R.string.success_verification) + " " +  user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserSignUpActivity.this,
                                    R.string.failed_verification,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}