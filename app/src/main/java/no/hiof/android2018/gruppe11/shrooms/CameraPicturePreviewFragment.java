package no.hiof.android2018.gruppe11.shrooms;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;


/**
     * A simple {@link Fragment} subclass.
     * Activities that contain this fragment must implement the
     * {@link CameraPicturePreviewFragment.OnFragmentInteractionListener} interface
     * to handle interaction events.
     * Use the {@link CameraPicturePreviewFragment#newInstance} factory method to
     * create an instance of this fragment.
     */
    public class CameraPicturePreviewFragment extends Fragment {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private static final String ARG_PARAM1 = "param1";
        private static final String ARG_PARAM2 = "param2";

        // TODO: Rename and change types of parameters
        private byte[] mParam1;
        private String mParam2;

        public View view;

        private OnFragmentInteractionListener mListener;
    private int height;
    private int width;
    private Bitmap bmp;
    private byte[] data;

    public CameraPicturePreviewFragment() {
            // Required empty public constructor
        }

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CameraPicturePreviewFragment.
         */
        // TODO: Rename and change types and number of parameters
        public static CameraPicturePreviewFragment newInstance(String param1, String param2) {
            CameraPicturePreviewFragment fragment = new CameraPicturePreviewFragment();
            Bundle args = new Bundle();
            args.putString(ARG_PARAM1, param1);
            args.putString(ARG_PARAM2, param2);
            fragment.setArguments(args);
            return fragment;
        }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getByteArray("imageBytes");
            height = getArguments().getInt("imageHeight");
            width = getArguments().getInt("imageWidth");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_camera_picture_preview, container, false);
        Button ok = view.findViewById(R.id.pictureOkButton);
        Button cancel = view.findViewById(R.id.pictureCancelButton);

        ImageView pictureHolder = view.findViewById(R.id.cameraPicturePreview);
        bmp = BitmapFactory.decodeByteArray(mParam1, 0, mParam1.length);
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
                mListener.onPictureApproved(data, true);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPictureApproved(data, false);
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(byte[] uri) {
        if (mListener != null) {
            mListener.onPictureApproved(uri, true);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onPictureApproved(byte[] bmp, boolean approved);
    }
}
