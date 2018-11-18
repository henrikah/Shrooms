package no.hiof.android2018.gruppe11.shrooms;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class NewPostFragment extends Fragment {

    ImageView imageView;
    Bitmap thumbnail;
    View v;
    byte[] mParam1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_newpost,container,false);
        imageView = v.findViewById(R.id.thumbnailview);
        if(getArguments() == null) {
            openCamera();
        } else {
            mParam1 = getArguments().getByteArray("image");
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(mParam1, 0, mParam1.length));
        }
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void openCamera(){
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        intent.putExtra("callbackClass", bottomNavTest.class);
        startActivity(intent);
        //Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(takePictureIntent,0);|
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        thumbnail = (Bitmap)data.getExtras().get("data");
        imageView.setImageBitmap(thumbnail);
    }
}
