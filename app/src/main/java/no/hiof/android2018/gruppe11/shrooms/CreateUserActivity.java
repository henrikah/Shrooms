package no.hiof.android2018.gruppe11.shrooms;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.images.Size;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import no.hiof.android2018.gruppe11.shrooms.enumerator.BottomSheetItemType;
import no.hiof.android2018.gruppe11.shrooms.firebase.schema.UserSchema;
import no.hiof.android2018.gruppe11.shrooms.global.Permission;
import no.hiof.android2018.gruppe11.shrooms.map.MapThumbnail;

public class CreateUserActivity extends AppCompatActivity implements BottomSheetFragment.Listener, MapLocationSelectFragment.OnLocationConfirmed, CameraFragment.OnPictureConfirmed {

    /*
        Class statics
     */

    private static final String TAG = CreateUserActivity.class.getName();

    /*
        Firebase
     */

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private UploadTask uploadTask;

    /*
        Layout fields
     */

    private EditText email;
    private EditText password;
    private EditText firstName;
    private EditText lastName;
    private ImageView profilePic;
    private SupportMapFragment mapFragment;
    private Button submitButton;

    /*
        User data
     */

    private double lat = 59.5953;
    private double lng = 10.4000;
    private int zoom = 12;
    private byte[] profilePicBytes;
    private Uri downloadUri;

    /*
        Helpers
     */

    private boolean locationConfirmed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        firstName = findViewById(R.id.editTextFirstname);
        lastName = findViewById(R.id.editTextLastname);
        profilePic = findViewById(R.id.profilePic);
        submitButton = findViewById(R.id.button);

        Button selectHomeLocationButton = findViewById(R.id.selectHomeLocationButton);

        /*
            Om brukeren har skrevet inn E-post for å logge inn og heller valgt å registrere seg så henter vi ut den
         */

        if(getIntent().getExtras() != null) {
            if(getIntent().getExtras().getString("email") != null) {
                email.setText(getIntent().getExtras().getString("email"));
            }
        }

        /*
            Viser standard kart uten merke
         */
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new MapThumbnail(lat, lng, zoom));

        selectHomeLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add(getString(R.string.select_on_map));
                arrayList.add(getString(R.string.use_current_location));
                BottomSheetFragment.newInstance(arrayList, BottomSheetItemType.MAP).show(getSupportFragmentManager(), "LocationTypeSelector");
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add(getString(R.string.take_picture_with_camera));
                arrayList.add(getString(R.string.get_picture_from_storage));
                BottomSheetFragment.newInstance(arrayList, BottomSheetItemType.PICTURE).show(getSupportFragmentManager(), "PictureTypeSelector");
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitButton.setEnabled(false);
                if(!locationConfirmed) {
                    lat = lng = Double.NaN;
                }
                Register();
            }
        });

        submitButton.setEnabled(false);
        email.addTextChangedListener(textWatcher());
        password.addTextChangedListener(textWatcher());
        firstName.addTextChangedListener(textWatcher());
        lastName.addTextChangedListener(textWatcher());
    }
    public TextWatcher textWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                 /*
                    Sjekker at E-posten er gyldig, at det skrevet inn et passord, og at det er skrevet inn fornavn og etternavn før vi skrur på registreringsknappen.
                 */
                if(Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches() && !TextUtils.isEmpty(password.getText()) && !TextUtils.isEmpty(firstName.getText()) && !TextUtils.isEmpty(lastName.getText())) {
                    submitButton.setEnabled(true);
                } else {
                    submitButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }
    @Override
    protected void onStart() {
        super.onStart();


    }

    public void Register(){
       final String emailTxt = email.getText().toString().trim();
       final String passwordTxt = password.getText().toString();

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
                                Toast.makeText(CreateUserActivity.this, getString(R.string.password_is_too_weak) ,Toast.LENGTH_SHORT).show();
                            }
                            catch (FirebaseAuthInvalidCredentialsException malformedEmail){
                                Toast.makeText(CreateUserActivity.this, getString(R.string.invalid_email) ,Toast.LENGTH_SHORT).show();
                            }
                            catch (FirebaseAuthUserCollisionException existEmail){
                                Toast.makeText(CreateUserActivity.this, getString(R.string.email_already_exist) ,Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Log.d(TAG, e.getMessage());
                            }
                            submitButton.setEnabled(true);
                        } else {
                            uploadProfilePicture();
                        }
                    }
                });
    }
    public void uploadProfilePicture() {
        // Om brukeren ikke har valgt et profilbilde sender vi en tom uri
        if(profilePicBytes == null) {
            downloadUri = Uri.parse("");
            saveUser();
            signIn(email.getText().toString().trim(), password.getText().toString().trim());
        } else {
            FirebaseUser user = mAuth.getCurrentUser();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            final StorageReference imageReference = storage.getReference().child("profilePicture/" + user.getUid() + ".png");

            uploadTask = imageReference.putBytes(profilePicBytes);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    /*
                        Om opplastningen ikke fullførte så fullfører vi uansett registreringen men med tomt bilde.
                     */
                    downloadUri = Uri.parse("");
                    saveUser();
                    signIn(email.getText().toString().trim(), password.getText().toString().trim());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                return imageReference.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    downloadUri = task.getResult();
                                    saveUser();
                                    signIn(email.getText().toString().trim(), password.getText().toString());

                                }
                            }
                        });
                    }
            });
        }
    }
    public void saveUser(){
        final String emailTxt = email.getText().toString().trim();
        final String firstNameTxt = firstName.getText().toString().trim();
        final String lastNameTxt = lastName.getText().toString().trim();

        // lager et dokument start
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(firstNameTxt + " " + lastNameTxt).setPhotoUri(downloadUri).build());

            // lager et dokument slutt
            final UserSchema userSchema = new UserSchema(emailTxt, firstNameTxt, lastNameTxt, downloadUri.toString(), lat, lng, user.getUid());


            // Legger til et dokument i firestore
            db.collection("Users").document(user.getUid())
                    .set(userSchema.getFull())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(CreateUserActivity.this, getString(R.string.user_registered), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateUserActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }



    }

    // Tar for seg innlogging
    public void signIn(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent myIntent = new Intent(CreateUserActivity.this, bottomNavTest.class);
                            startActivity(myIntent);
                        }
                    }
                });
    }


    @Override
    public void onItemClicked(int position, BottomSheetItemType type) {
        if(type == BottomSheetItemType.MAP) {
            if(position == 0) {
                /*
                    Har valgt kart
                  */
                Bundle args = new Bundle();
                args.putDouble("latitude", lat);
                args.putDouble("longitude", lng);
                MapLocationSelectFragment dialog = new MapLocationSelectFragment();
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(), MapLocationSelectFragment.TAG);
            } else if(position == 1) {
                /*
                    Har valgt å bruke locationservice
                  */
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, Permission.GET_LOCATION_REQUEST_CODE);
                } else {
                    updateLocation();
                }
            }
        } else if(type == BottomSheetItemType.PICTURE) {
            if(position == 0) {
                /*
                    Har valgt kamera
                 */
                CameraFragment dialog = new CameraFragment();
                dialog.show(getSupportFragmentManager(), CameraFragment.TAG);
            } else if(position == 1) {
                /*
                    Har valgt filepicker
                 */
                FilePick.getPhoto(this);
            }
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri;
            Bitmap bm;
            if (resultData != null) {
                uri = resultData.getData();
                ImageView iv = findViewById(R.id.profilePic);
                try {
                    bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    profilePicBytes = stream.toByteArray();
                    iv.setImageBitmap(bm);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(requestCode == Permission.GET_LOCATION_REQUEST_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateLocation();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.allow_access_to_location_error), Toast.LENGTH_LONG).show();
            }
        }
    }
    public void updateLocation() {
        SingleShotLocationProvider.requestSingleUpdate(getApplicationContext(),
                new SingleShotLocationProvider.LocationCallback() {
                    @Override public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                        lat = location.latitude;
                        lng = location.longitude;
                        mapFragment.getMapAsync(new MapThumbnail(lat, lng, zoom));
                    }
                });
    }

    @Override
    public void onLocationConfirmed(double latitude, double longitude) {
        lat = latitude;
        lng = longitude;
        mapFragment.getMapAsync(new MapThumbnail(lat, lng, zoom));
        locationConfirmed = true;
    }

    @Override
    public void onPictureConfirmed(byte[] picture, Size size) {
        profilePicBytes = picture;
        profilePic.setImageBitmap(BitmapFactory.decodeByteArray(picture, 0, picture.length));
    }
}
