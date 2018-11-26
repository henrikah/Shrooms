package no.hiof.android2018.gruppe11.shrooms;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.images.Size;

public class CameraFragment extends DialogFragment implements CameraPreviewFragment.OnPictureTaken, CameraPicturePreviewFragment.OnPicturePreview {
    public static String TAG = "FullScreenDialog";

    private OnPictureConfirmed mListener;
    int fragmentView;
    public CameraFragment() {
        // Required empty public constructor
    }

    public static CameraFragment newInstance() {
        return new CameraFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });
        toolbar.setTitle(getString(R.string.app_name));
        fragmentView = R.id.cameraFrame;
        CameraPreview();
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if(dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final android.support.v4.app.Fragment parent = getParentFragment();
        if (parent != null) {
            if(parent instanceof OnPictureConfirmed) {
                mListener = (OnPictureConfirmed) parent;
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement OnPictureTaken");
            }
        } else {
            if (context instanceof OnPictureConfirmed) {
                mListener = (OnPictureConfirmed) context;
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement OnPictureTaken");
            }
        }
    }

    interface OnPictureConfirmed {
        void onPictureConfirmed(byte[] picture, Size size);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public double latitude, longitude;

    private void CameraPreview() {
        CameraPreviewFragment fragment = CameraPreviewFragment.newInstance(this);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(fragmentView, fragment);
        ft.commit();
    }
    private void PicturePreview(byte[] bmp, Size size) {
        CameraPicturePreviewFragment fragment = CameraPicturePreviewFragment.newInstance(this);
        Bundle args = new Bundle();
        args.putByteArray("imageBytes", bmp);
        args.putInt("imageHeight", size.getHeight());
        args.putInt("imageWidth", size.getWidth());
        fragment.setArguments(args);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(fragmentView, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
    public void onPictureTaken(byte[] bmp, Size size) {
        PicturePreview(bmp, size);

    }
    public void onPictureApproved(byte[] bmp, Size size, boolean approved) {
        if(approved) {
            mListener.onPictureConfirmed(bmp, size);
            getDialog().cancel();
        } else {
            CameraPreview();
        }
    }
}
