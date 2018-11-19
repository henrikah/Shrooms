package no.hiof.android2018.gruppe11.shrooms;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewPostFragment extends Fragment {
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    StorageReference mushroomsRef;
    StorageReference mushroomImagesRef;
    ImageView imageView;
    Bitmap thumbnail;
    View v;
    EditText title;
    EditText description;


    double latitude;
    double longitude;

    private static final String TAG = "FeedFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_newpost,container,false);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mushroomsRef = mStorageRef.child("mushrooms.png");
        mushroomImagesRef = mStorageRef.child("brukerBilder/mushrooms.png");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        openCamera();

    }

    private void openCamera(){
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        startActivity(intent);
        //Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(takePictureIntent,0);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        thumbnail = (Bitmap)data.getExtras().get("data");
        imageView = v.findViewById(R.id.thumbnailview);
        imageView.setImageBitmap(thumbnail);
        title = (EditText) v.findViewById(R.id.tittel);
        description = (EditText) v.findViewById(R.id.description);

        String titleText = title.getText().toString();
        String descText = description.getText().toString();


        mUser = mAuth.getCurrentUser();
        String userUid = mUser.getUid();
        Post p = new Post(titleText,descText,userUid,System.currentTimeMillis(),updateLongLat());


    }


    private void saveImageToDatabase(Bitmap bilde){
        // Get the data from an ImageView as bytes

        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mushroomsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });
    }


    public GeoPoint updateLongLat(){

        SingleShotLocationProvider.requestSingleUpdate(getContext(),
                new SingleShotLocationProvider.LocationCallback() {
                    @Override public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                        latitude = location.latitude;
                        longitude = location.longitude;

                    }
                });
        GeoPoint geoPoint = new GeoPoint(latitude,longitude);
        return geoPoint;

    }

    /* public void uploadLocalFile(){

        Uri file = Uri.fromFile(new File("C:/Users/Marius/Desktop/Shrooms/app/src/main/res/drawable/logo.png"));
        StorageReference riversRef = mStorageRef.child("brukerBilder/"+file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });
    }*/







  //  C:\Users\Marius\Desktop\Shrooms\app\src\main\res\drawable\logo.png
//


    /*// forsøk på å laste opp bilde fra stream
    public void SaveImgToDbFromStream(){
        Log.d(TAG,"TEST: ingorer denne");
        InputStream stream = null;
        try {
            stream = new FileInputStream(new File("C:/Users/Marius/Desktop/Shrooms/app/src/main/res/drawable/logo.png"));
            Toast.makeText(getActivity(), "TEST: Fant filen.", Toast.LENGTH_SHORT).show();
            Log.d(TAG,"TEST: Fant filen");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Finner ikke filen.", Toast.LENGTH_SHORT).show();
            Log.d(TAG,"TEST: Finner ikke filen");
        }

        Log.d(TAG, "TEST: Dette kjører 12345");
        UploadTask uploadTask = mushroomsRef.putStream(stream);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "TEST: Det feilet");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Log.d(TAG, "TEST: Det feilet ikke");
            }
        });
    }*/

    // Kode for å lagre et bilde lokalt *** Start **

     /*String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PNG_" + timeStamp + "_";

        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,  *//* prefix *//*
                ".png",         *//* suffix *//*
                storageDir      *//* directory *//*
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }*/

    // Kode for å lagre et bilde lokalt *** Slutt **
}
