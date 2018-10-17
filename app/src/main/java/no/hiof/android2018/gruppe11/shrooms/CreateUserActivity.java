package no.hiof.android2018.gruppe11.shrooms;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

        mAuth.createUserWithEmailAndPassword(emailTxt, passwordTxt)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("Email", emailTxt);
                            userMap.put("Firstname",firstnameTxt);
                            userMap.put("Lastname", lastnameTxt);
                            userMap.put("Uid", user.getUid());


                            db.collection("shrooms").document("Users").set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Det gikk bra
                                    DocumentReference docRef = db.collection("shrooms").document("Users");
                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                                    Log.d(TAG,"Bruker registrert");
                                                } else {
                                                    Log.d(TAG, "No such document");
                                                }
                                            } else {
                                                Log.d(TAG, "get failed with ", task.getException());
                                            }
                                        }
                                    });





                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Det gikk ikke!
                                }
                            });

                                  //  collection('users').doc(currentUser.uid).set(currentUser)
                            signIn(emailTxt,passwordTxt);
                        } else {

                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            //Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                              //      Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public void signIn(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                           // Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            Intent myIntent = new Intent(CreateUserActivity.this, FeedActivity.class);
                            startActivity(myIntent);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                           // Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                             //       Toast.LENGTH_SHORT).show();
                           // updateUI(null);
                        }

                        // ...
                    }
                });
    }
}
