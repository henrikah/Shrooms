package no.hiof.android2018.gruppe11.shrooms;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.MapFragment;
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

public class CreateUserActivity extends AppCompatActivity implements ItemListDialogFragment.Listener, MapLocationSelectFragment.OnFragmentInteractionListener, MapLocationPicker.OnLocationSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private static final String TAG = CreateUserActivity.class.getName();
    private final int GET_LOCATION_REQUEST_CODE = 42;
    private EditText email;
    private EditText password;
    private EditText firstname;
    private EditText lastname;
    private SupportMapFragment mapFragment;
    private double lat = 59.5953;
    private double lng = 10.4000;
    private int zoom = 12;

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
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new MapThumbnail(lat, lng, zoom));
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
            MapLocationSelectFragment dialog = new MapLocationSelectFragment();
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
            dialog.show(getSupportFragmentManager(), MapLocationSelectFragment.TAG);
        } else if(position == 1) {
            Log.d("kart", "vi trykket på knappen");
            // Har valgt å bruke locationservice
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, GET_LOCATION_REQUEST_CODE);
            } else {
                updateLocation();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d("kart", "vi kommer ihvertfall til onrequestpermissionsresult");
        if(requestCode == GET_LOCATION_REQUEST_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("kart", "vi har tilgang til location");
                updateLocation();
            } else {
                Toast.makeText(getApplicationContext(), "You need to allow location access to use current location as your home location.", Toast.LENGTH_LONG).show();
            }
        }
    }
    public void updateLocation() {
        Log.d("kart", "updateLocation kjøres");
        SingleShotLocationProvider.requestSingleUpdate(getApplicationContext(),
                new SingleShotLocationProvider.LocationCallback() {
                    @Override public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                        lat = location.latitude;
                        lng = location.longitude;
                        mapFragment.getMapAsync(new MapThumbnail(lat, lng, zoom));
                        Log.d("kart", location.toString());
                        Log.d("kart", lat + " " + lng);
                    }
                });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onLocationSelected(double lat, double lng) {

    }
}
