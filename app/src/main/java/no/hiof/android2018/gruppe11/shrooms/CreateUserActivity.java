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

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import no.hiof.android2018.gruppe11.shrooms.enumerator.LocationModalEnumerator;
import no.hiof.android2018.gruppe11.shrooms.map.MapLocationPicker;
import no.hiof.android2018.gruppe11.shrooms.map.MapThumbnail;

public class CreateUserActivity extends AppCompatActivity implements ItemListDialogFragment.Listener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private static final String TAG = CreateUserActivity.class.getName();

    private EditText email;
    private EditText password;
    private EditText firstname;
    private EditText lastname;

    private Button btn;
    private Button selectHomeLocationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        firstname = findViewById(R.id.editTextFirstname);
        lastname = findViewById(R.id.editTextLastname);

        btn = findViewById(R.id.button);
        selectHomeLocationButton = findViewById(R.id.selectHomeLocationButton);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        /*
            Viser standard kart uten merke
         */
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new MapThumbnail(59.5953, 10.4000, 10));
        if(getIntent().getExtras() != null) {
            Log.d("register", "getExtra != null");
            if(getIntent().getExtras().getString("email") != null) {
                Log.d("register", "getExtra.getstring != null");
                email.setText(getIntent().getExtras().getString("email"));
            }
        }
        selectHomeLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemListDialogFragment.newInstance(2).show(getSupportFragmentManager(), "LocationTypeSelector");
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


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
                        if (!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            }
                            catch (FirebaseAuthWeakPasswordException weakPassword){
                               // Log.d(TAG, "password is too weak");
                                Toast.makeText(CreateUserActivity.this, "password is too weak" ,Toast.LENGTH_SHORT).show();
                            }
                            catch (FirebaseAuthInvalidCredentialsException malformedEmail){
                             //   Log.d(TAG, " Uncomplete email");
                                Toast.makeText(CreateUserActivity.this, "Uncomplete email" ,Toast.LENGTH_SHORT).show();
                            }
                            catch (FirebaseAuthUserCollisionException existEmail){
                               // Log.d(TAG, "The Email does already exist");
                                Toast.makeText(CreateUserActivity.this, "The Email does already exist" ,Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Log.d(TAG, e.getMessage());
                            }
                        }
                        if (task.isSuccessful()) {
                            saveUser();

                            signIn(emailTxt,passwordTxt);
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


    @Override
    public void onItemClicked(int position) {
        if(position == 0) {
            // Har valgt kart
        } else if(position == 1) {
            // Har valgt å bruke locationservice
        }
    }
}
