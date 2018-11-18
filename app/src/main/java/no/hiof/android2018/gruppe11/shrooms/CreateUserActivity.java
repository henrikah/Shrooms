package no.hiof.android2018.gruppe11.shrooms;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateUserActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private static final String TAG = CreateUserActivity.class.getName();

    private EditText email;
    private EditText password;
    private EditText firstname;
    private EditText lastname;

    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        email = (EditText) findViewById(R.id.editTextEmail);
        password = (EditText) findViewById(R.id.editTextPassword);
        firstname = (EditText) findViewById(R.id.editTextFirstname);
        lastname = (EditText) findViewById(R.id.editTextLastname);

        btn = (Button) findViewById(R.id.button);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });

    }

    public void Register(){
       final String emailTxt = email.getText().toString().trim();
       final String passwordTxt = password.getText().toString().trim();
       final String firstnameTxt = firstname.getText().toString().trim();
       final String lastnameTxt = lastname.getText().toString().trim();

       // Metode for å opprette en bruker
        mAuth.createUserWithEmailAndPassword(emailTxt, passwordTxt)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveUser();

                            signIn(emailTxt,passwordTxt);
                        } else {

                        }
                    }
                });
    }


    public void saveUser(){
        final String emailTxt = email.getText().toString().trim();
        final String passwordTxt = password.getText().toString().trim();
        final String firstnameTxt = firstname.getText().toString().trim();
        final String lastnameTxt = lastname.getText().toString().trim();

        // lager et dokument start
        FirebaseUser user = mAuth.getCurrentUser();

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("Email", emailTxt);
        userMap.put("Firstname",firstnameTxt);
        userMap.put("Lastname", lastnameTxt);
        userMap.put("Uid", user.getUid());

        // lager et dokument slutt

        // Legger til et dokument i firestore
        db.collection("Users").document(user.getUid())
                .set(userMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                       Toast.makeText(CreateUserActivity.this,"Bruker Registrert 1",Toast.LENGTH_SHORT).show();                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateUserActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });



    }

    // Tar for seg innlogging
    public void signIn(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            Log.d(TAG, "Innlogging gikk bra.");

                            Intent myIntent = new Intent(CreateUserActivity.this, bottomNavTest.class);
                            startActivity(myIntent);

                        } else {
                           Log.d(TAG, "Innlogging feilet.");
                        }
                    }
                });
    }


}
