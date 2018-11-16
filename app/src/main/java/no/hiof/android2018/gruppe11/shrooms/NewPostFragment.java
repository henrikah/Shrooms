package no.hiof.android2018.gruppe11.shrooms;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class NewPostFragment extends Fragment {

    private StorageReference msStorageRef;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    public int distance = 10;
    ImageView imageView;
    Bitmap thumbnail;
    TextView tittel;
    TextView description;
    String userToken;
    View v;

    StorageReference bildeRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_newpost,container,false);
        return v;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openCamera();
        tittel = v.findViewById(R.id.title);
        description = v.findViewById(R.id.description);
        db = FirebaseFirestore.getInstance();

        msStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference bilderRef = msStorageRef.child("postPictures.png");
        StorageReference postImagesRef = msStorageRef.child("images/postPictures.png");

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            userToken = task.getResult().getToken();

                            // ...
                        } else {
                            // Handle error -> task.getException();
                        }
                    }
                });

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
        String tittelText = tittel.getText().toString();
        String descriptionText = description.getText().toString();



        //Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, baos);

        byte[] dataArr = baos.toByteArray();

        UploadTask uploadTask = bildeRef.putBytes(dataArr);
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


        Post p = new Post(tittelText , distance, userToken, descriptionText ,thumbnail);
        savePostToDatabase(p);


    }


    public  void savePostToDatabase(Post post){
        Map<String, Object> postMap = new HashMap<>();
        postMap.put("Title",post.getTitle());
        postMap.put("Distance", post.getDistance());
        postMap.put("UserID",post.getUser());
        postMap.put("Timestamp", post.getTimeStamp());
        postMap.put("Description", post.getDescription());


        db.collection("Posts").document().set(postMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // det gikk bra
            }
        });
    }

}
