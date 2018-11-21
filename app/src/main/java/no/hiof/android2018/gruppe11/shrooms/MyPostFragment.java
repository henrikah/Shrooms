package no.hiof.android2018.gruppe11.shrooms;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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

public class MyPostFragment extends Fragment {
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private FirebaseFirestore db;
    private static final String TAG = "FeedActivity";
    private ArrayList<Post> posts = new ArrayList<>();
    View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        super.onCreate(savedInstanceState);
        fillSoppList();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_myposts,container,false);
        return view;
    }

    public void fillSoppList(){

        db.collection("Posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getString("UserID").contentEquals(mAuth.getCurrentUser().getUid())) {
                            String title = document.getString("Title");
                            String desc = document.getString("Description");
                            GeoPoint loc = document.getGeoPoint("Location");
                            String userName = document.getString("UserID");
                            Long timeStamp = document.getLong("Timestamp");
                            String bildeNavn = document.getString("bildenNavn");
                            Post p = new Post(title, desc, userName, timeStamp, loc);
                            p.setBildeNavn(bildeNavn);
                            posts.add(p);
                        }
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }

                initRecyclerView();
            }
        });

    }
    private void initRecyclerView(){
        RecyclerView recyclerView = view.findViewById(R.id.myPostsRecycler);
        MyPostsRecycler adapter = new MyPostsRecycler(posts,getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
    }


}
