package no.hiof.android2018.gruppe11.shrooms;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;

import no.hiof.android2018.gruppe11.shrooms.map.MapLocationPicker;

public class MapLocationSelectFragment extends DialogFragment implements MapLocationPicker.OnLocationSelectedListener {
    public static String TAG = "FullScreenDialog";

    private static final String ARG_LATITUDE = "latitude";
    private static final String ARG_LONGITUDE = "longitude";

    private double latitude;
    private double longitude;

    private OnLocationConfirmed mListener;

    public MapLocationSelectFragment() {
        // Required empty public constructor
    }

    public static MapLocationSelectFragment newInstance(double latitude, double longitude) {
        MapLocationSelectFragment fragment = new MapLocationSelectFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_LATITUDE, latitude);
        args.putDouble(ARG_LONGITUDE, longitude);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
        if (getArguments() != null) {
            latitude = getArguments().getDouble(ARG_LATITUDE);
            longitude = getArguments().getDouble(ARG_LONGITUDE);
        }
    }
    private SupportMapFragment supportMapFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map_location_select, container, false);

        view.findViewById(R.id.confirmLocationButton).setVisibility(View.INVISIBLE);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });
        toolbar.setTitle(getString(R.string.app_name));
        if(supportMapFragment == null) {
            supportMapFragment = SupportMapFragment.newInstance();
            supportMapFragment.getMapAsync(new MapLocationPicker(latitude, longitude, this));
        }
        getChildFragmentManager().beginTransaction().replace(R.id.mmap, supportMapFragment).commit();
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
            if(parent instanceof OnLocationConfirmed) {
                mListener = (OnLocationConfirmed) parent;
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement OnLocationConfirmed");
            }
        } else {
            if (context instanceof OnLocationConfirmed) {
                mListener = (OnLocationConfirmed) context;
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement OnLocationConfirmed");
            }
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override
    public void onLocationSelected(double lat, double lng) {
        latitude = lat;
        longitude = lng;
        getView().findViewById(R.id.confirmLocationButton).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.confirmLocationButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onLocationConfirmed(latitude, longitude);
                getDialog().cancel();
            }
        });
    }

    public interface OnLocationConfirmed {
        void onLocationConfirmed(double lat, double lng);
    }
}
