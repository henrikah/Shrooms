package no.hiof.android2018.gruppe11.shrooms;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MyProfileFragment extends Fragment {
    private TextView text;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FragmentTransaction fragmentTransaction;
    Fragment fragment;
    ImageButton infoButton;
    ImageButton postsButton;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_my_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //text = getView().findViewById(R.id.myProfileText);
        //text.setText(currentUser.getEmail());

        fragment = new MyInfoFragment();
        fragmentTransaction = this.getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.myLayout, fragment).commit();


       postsButton = getView().findViewById(R.id.infoImageButton);
       postsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new MyPostFragment();
                fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.myLayout, fragment).commit();
            }
        });

        infoButton = getView().findViewById(R.id.postsImageButton);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new MyInfoFragment();
                fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.myLayout, fragment).commit();
            }
        });

    }
}
