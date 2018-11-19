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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewPostFragment extends Fragment {
    private StorageReference mStorageRef;
    StorageReference mushroomsRef;
    StorageReference mushroomImagesRef;
    ImageView imageView;
    Bitmap thumbnail;
    View v;

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


        mStorageRef = FirebaseStorage.getInstance().getReference();
        mushroomsRef = mStorageRef.child("mushrooms.jpg");
        mushroomImagesRef = mStorageRef.child("images/mountains.jpg");

       // saveImageToDatabase(thumbnail);
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
}
