package no.hiof.android2018.gruppe11.shrooms;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FeedFragment extends Fragment {
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private FirebaseFirestore db;
    private static final String TAG = "FeedActivity";
    private ArrayList<Post> posts = new ArrayList<>();
    View v;
    double latitude;
    double longitude;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        super.onCreate(savedInstanceState);
        SingleShotLocationProvider.requestSingleUpdate(getContext(),
                new SingleShotLocationProvider.LocationCallback() {
                    @Override public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                        latitude = location.latitude;
                        longitude = location.longitude;

                        fillSoppList();
                    }
        });






    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_feed,container,false);
        return v;
    }

    public void fillSoppList(){

        db.collection("Posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String title = document.getString("Title");
                                String desc = document.getString("Description");
                                GeoPoint loc = document.getGeoPoint("Location");
                                String userName = document.getString("UserID");
                                Long timeStamp = document.getLong("Timestamp");
                                Post p = new Post(title,desc,userName,timeStamp,loc);
                                posts.add(p);
                                // det gikk fint
                                //Toast.makeText(FeedActivity.this, "funket fint",Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                        initRecyclerView();
                    }
                });

    }
    private void initRecyclerView(){
        RecyclerView recyclerView = v.findViewById(R.id.mainFeed);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(posts,getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    // forsøk på å få vist TokenID til bruker
    public void showTokenId(){
        String uId;
        mUser = mAuth.getCurrentUser();
        if(mUser != null){
            uId = mUser.getUid();
            Toast.makeText(getActivity(), "bruker-token er : " + uId  ,Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getActivity(), "bruker-token er : fant ikke "   ,Toast.LENGTH_SHORT).show();
        }


    }

}
