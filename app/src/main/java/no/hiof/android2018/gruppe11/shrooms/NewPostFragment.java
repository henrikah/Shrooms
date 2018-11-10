package no.hiof.android2018.gruppe11.shrooms;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class NewPostFragment extends Fragment {

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
    }
}
