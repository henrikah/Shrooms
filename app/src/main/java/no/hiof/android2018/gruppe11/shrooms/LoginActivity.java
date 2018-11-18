package no.hiof.android2018.gruppe11.shrooms;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private static final String TAG = LoginActivity.class.getName();
    private FirebaseFirestore db;

    private Button btnSignIn;
    private Button btnSignUp;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        email = (EditText) findViewById(R.id.emailSignIn);
        password = (EditText) findViewById(R.id.passwordSignIn);

        btnSignIn = (Button) findViewById(R.id.SignInBtn);
        btnSignUp = (Button) findViewById(R.id.SignUpntn) ;

        // Knapp for å logge inn
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailTxt = email.getText().toString().trim();
                String passwordTxt = password.getText().toString().trim();

                signIn(emailTxt,passwordTxt);
            }
        });

        // Knapp som sender deg til registrerings activityen
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(LoginActivity.this, CreateUserActivity.class);
                startActivity(myIntent);
            }
        });

    }

    // Metoden som logger deg inn, og sender deg vicdere til Feed activityen
    public void signIn(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG,"Fikk logget inn");
                            FirebaseUser user = mAuth.getCurrentUser();

                             Intent myIntent = new Intent(LoginActivity.this, bottomNavTest.class);
                            startActivity(myIntent);

                        } else {
                            Log.d(TAG,"Fikk ikke logget inn");

                        }
                    }
                });
    }


}
