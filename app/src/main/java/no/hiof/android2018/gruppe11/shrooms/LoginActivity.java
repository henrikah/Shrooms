package no.hiof.android2018.gruppe11.shrooms;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getName();

    private EditText email;
    private EditText password;
    private Button btnSignIn;
    private Button btnSignUp;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.emailSignIn);
        password = findViewById(R.id.passwordSignIn);

        btnSignIn = findViewById(R.id.SignInBtn);
        btnSignUp = findViewById(R.id.SignUpntn);
        /*
            Textwatcher for å sjekke at E-post er gyldig og at et passord er skrevet inn
         */
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*
                    Sjekker at E-posten er gyldig og at det skrevet inn et passord før vi skrur på logg inn knappen.
                 */
                if(Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches() && !TextUtils.isEmpty(password.getText())) {
                    btnSignIn.setEnabled(true);
                } else {
                    btnSignIn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        btnSignIn.setEnabled(false);
        email.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
        /*
            Trykkbar knapp for å logge inn
         */
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailTxt = email.getText().toString();
                String passwordTxt = password.getText().toString();

                signIn(emailTxt,passwordTxt);
            }
        });

        /*
            Knapp som sender deg til registrerings activityen
         */
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(LoginActivity.this, CreateUserActivity.class);
                /*
                    Sender e-postadressen som er skrevet inn til registreringsaktiviteten dersom det er en gyldig E-post
                 */
                if(Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
                    myIntent.putExtra("email", email.getText().toString());
                }
                startActivity(myIntent);
            }
        });

    }

    /*
        Metoden som logger deg inn, og sender deg videre til Feed activityen
     */
    public void signIn(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            try
                            {
                                throw task.getException();
                            }
                            catch (FirebaseAuthInvalidUserException | FirebaseAuthInvalidCredentialsException invalidCredentials){
                                Toast.makeText(LoginActivity.this, "Invalid E-mail or password" ,Toast.LENGTH_SHORT).show();
                            }
                            catch (Exception e){
                                Log.d(TAG,"Fikk ikke logget inn");
                                Toast.makeText(LoginActivity.this, "An error occured. Try again" ,Toast.LENGTH_SHORT).show();
                                Log.d(TAG, e.getMessage());
                            }
                        } else {
                            Log.d(TAG,"Fikk logget inn");

                            Intent myIntent = new Intent(LoginActivity.this, bottomNavTest.class);
                            startActivity(myIntent);
                        }
                    }
                });
    }
}
