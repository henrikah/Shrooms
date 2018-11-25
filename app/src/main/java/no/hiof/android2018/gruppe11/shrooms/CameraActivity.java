package no.hiof.android2018.gruppe11.shrooms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.common.images.Size;

import android.util.Log;

public class CameraActivity extends AppCompatActivity {

    int fragmentView;
    Class cbc;
    private void CameraPreview() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(fragmentView, new CameraPreviewFragment());
        ft.commit();
    }
    private void PicturePreview(byte[] bmp, Size size) {
        CameraPicturePreviewFragment fragment = new CameraPicturePreviewFragment();
        Bundle args = new Bundle();
        args.putByteArray("imageBytes", bmp);
        args.putInt("imageHeight", size.getHeight());
        args.putInt("imageWidth", size.getWidth());
        Log.d("TAG", size.getHeight() + " : " + size.getWidth());
        fragment.setArguments(args);
        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
// Replace the contents of the container with the new fragment
        ft.replace(fragmentView, fragment);
        ft.addToBackStack(null);
// or ft.add(R.id.your_placeholder, new FooFragment());
// Complete the changes added above
        ft.commit();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent().getExtras() != null) {
            if(getIntent().getExtras().get("callbackClass") != null) {
                cbc = (Class) getIntent().getExtras().get("callbackClass");
            }
        }
        setContentView(R.layout.activity_camera);
        fragmentView = R.id.cameraFragment;
        CameraPreview();
    }

    public void onPictureApproved(byte[] bmp, boolean approved) {
        if(approved) {
            Log.d("hei", getIntent().getClass().toString());
            Intent i = new Intent(CameraActivity.this, cbc);
            i.putExtra("image", bmp);
            startActivity(i);
        } else {
            CameraPreview();
        }
    }
    public void onPictureTaken(byte[] bmp, Size size) {
        PicturePreview(bmp, size);

    }

}