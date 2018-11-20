package no.hiof.android2018.gruppe11.shrooms;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MyInfoFragment extends Fragment {
    private TextView name;
    private TextView mail;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    public MyInfoFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_myinfo, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        name = getView().findViewById(R.id.nameText);
        name.setText(currentUser.getUid());

        mail = getView().findViewById(R.id.mailText);
        mail.setText(currentUser.getEmail());

        }


    }
