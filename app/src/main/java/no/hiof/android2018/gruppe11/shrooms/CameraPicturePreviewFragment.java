package no.hiof.android2018.gruppe11.shrooms;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.common.images.Size;

import java.io.ByteArrayOutputStream;

public class CameraPicturePreviewFragment extends Fragment {

    private static final String ARG_IMAGE_BYTES = "imageBytes";
    private static final String ARG_IMAGE_HEIGHT = "imageHeight";
    private static final String ARG_IMAGE_WIDTH = "imageWidth";

    private byte[] imageBytes;

    public View view;

    private OnPicturePreview mListener;
    private int height;
    private int width;
    private Bitmap bmp;
    private byte[] data;

    public CameraPicturePreviewFragment() {
        // Required empty public constructor
    }

    private static Object parentContext;
    public static CameraPicturePreviewFragment newInstance(Object context) {
        parentContext = context;
        CameraPicturePreviewFragment fragment = new CameraPicturePreviewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageBytes = getArguments().getByteArray(ARG_IMAGE_BYTES);
            height = getArguments().getInt(ARG_IMAGE_HEIGHT);
            width = getArguments().getInt(ARG_IMAGE_WIDTH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_camera_picture_preview, container, false);
        Button ok = view.findViewById(R.id.pictureOkButton);
        Button cancel = view.findViewById(R.id.pictureCancelButton);

        ImageView pictureHolder = view.findViewById(R.id.cameraPicturePreview);
        bmp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        int w = width;
        int h = height;

        Matrix mtx = new Matrix();
        //       mtx.postRotate(degree);
        mtx.setRotate(90);

        Bitmap newBmp = Bitmap.createBitmap(bmp, 0, 0, w, h, mtx, false);
        pictureHolder.setImageBitmap(newBmp);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        newBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data = baos.toByteArray();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPictureApproved(data, new Size(width, height), true);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPictureApproved(data, new Size(width, height), false);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (parentContext instanceof OnPicturePreview) {
            mListener = (OnPicturePreview) parentContext;
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

    public interface OnPicturePreview {
        void onPictureApproved(byte[] bmp, Size size, boolean approved);
    }
}