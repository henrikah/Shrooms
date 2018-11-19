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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
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
import java.util.HashMap;
import java.util.Map;

public class NewPostFragment extends Fragment {
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    private FirebaseFirestore db;

    StorageReference mushroomsRef;
    StorageReference mushroomImagesRef;
    ImageView imageView;
    Bitmap thumbnail;
    View v;
    EditText title;
    EditText description;
    Button postBtn;
    long timeStamp;
    String pictureID;

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
        long timeStamp = System.currentTimeMillis();
        pictureID = "mushrooms_" + timeStamp + "_.png";
        mushroomImagesRef = mStorageRef.child("brukerBilder/"+pictureID);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        hermansCamera();
        //createDummyData();
        //openCamera();

    }

    private void openCamera(){
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        startActivity(intent);
        //Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(takePictureIntent,0);
    }

    private void hermansCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent,0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        thumbnail = (Bitmap)data.getExtras().get("data");
        imageView = v.findViewById(R.id.thumbnailview);
        imageView.setImageBitmap(thumbnail);


        saveImageToDatabase(thumbnail);
        postBtn = (Button) v.findViewById(R.id.button2);
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPost();
            }
        });


    }

    public void createPost(){
        title = (EditText) v.findViewById(R.id.tittel);
        description = (EditText) v.findViewById(R.id.description);

        String titleText = title.getText().toString();
        String descText = description.getText().toString();

        GeoPoint geoPoint = updateLongLat();


        mUser = mAuth.getCurrentUser();
        String userUid = mUser.getUid();

        Post p = new Post(titleText,descText,userUid,timeStamp,geoPoint);
        p.setBildeNavn(pictureID);
        lagrePostIFireStore(p);
    }

    // Kode for å lagre bilde til Firebase Storage
    private void saveImageToDatabase(Bitmap bilde){


        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        // UploadTask uploadTask = mushroomsRef.putBytes(data);
        UploadTask uploadTask = mushroomImagesRef.putBytes(data);

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

    public void lagrePostIFireStore(Post p){

        Map<String, Object> postMap = new HashMap<>();
        postMap.put("Title", p.getTitle());
        postMap.put("Timestamp",p.getTimeStamp());
        postMap.put("Description", p.getDescription());
        postMap.put("UserID", p.getUser());
        postMap.put("Location",p.getLocation());
        postMap.put("bildeNavn",p.getBildeNavn());

        db.collection("Posts").document()
                .set(postMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(),"Post registrert",Toast.LENGTH_SHORT).show();                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Bare for å fylle databasen med dritt
    public void createDummyData(){

        long timeStamp = System.currentTimeMillis();
        GeoPoint geoPoint = updateLongLat();

        Post p1 = new Post("Tittel1","description1","KåreId", timeStamp, geoPoint);
        Post p2 = new Post("Tittel2","description2","KnutId", timeStamp, geoPoint);
        Post p3 = new Post("Tittel3","description3","PerId", timeStamp, geoPoint);
        Post p4 = new Post("Tittel4","description4","EgilId", timeStamp, geoPoint);

        lagrePostIFireStore(p1);
        lagrePostIFireStore(p2);
        lagrePostIFireStore(p3);
        lagrePostIFireStore(p4);
    }
}






