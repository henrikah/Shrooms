package no.hiof.android2018.gruppe11.shrooms;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.images.Size;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.net.SocketException;

import no.hiof.android2018.gruppe11.shrooms.global.Permission;

public class CameraPreviewFragment extends Fragment {
    CameraSource cs;
    SurfaceView sv;
    FloatingActionButton cb;
    private static Object parentContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private OnPictureTaken mListener;

    public CameraPreviewFragment() {
        // Required empty public constructor
    }


    public static CameraPreviewFragment newInstance(Object context) {
        CameraPreviewFragment fragment = new CameraPreviewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        parentContext = context;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_camera_preview, container, false);

        sv = view.findViewById(R.id.cameraPreview);
        cb = view.findViewById(R.id.cameraTakePictureButton);
        final TextRecognizer textRecognizer = new TextRecognizer.Builder(getContext()).build();
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cs.takePicture(null, new CameraSource.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] bytes) {
                        Size size = cs.getPreviewSize();
                        mListener.onPictureTaken(bytes, size);
                    }
                });
            }
        });
        cs = new CameraSource.Builder(getContext(),textRecognizer).setRequestedFps(30f).build();

        sv.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, Permission.GET_CAMERA_REQUEST_CODE);
                        return;
                    }
                    cs.start(sv.getHolder());
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cs.stop();
            }
        });
        textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<TextBlock> detections) {

            }

        });
        return view;
    }
    View view;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == Permission.GET_CAMERA_REQUEST_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    cs.start(sv.getHolder());
                } catch(SecurityException | IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.allow_access_to_camera_error), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (parentContext instanceof OnPictureTaken) {
            mListener = (OnPictureTaken) parentContext;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPictureTaken");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnPictureTaken {
        // TODO: Update argument type and name
        void onPictureTaken(byte[] bmp, Size size);
    }
}