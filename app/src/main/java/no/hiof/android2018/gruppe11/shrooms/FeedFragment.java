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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FeedFragment extends Fragment {

    private FirebaseFirestore db;
    private static final String TAG = "FeedActivity";
    private ArrayList<Post> posts = new ArrayList<>();
    View v;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        fillSoppList();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_feed,container,false);
        return v;
    }

    public void fillSoppList(){
        Log.d(TAG, "Nå er vi her 1");
        db.collection("Posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d(TAG, "Nå er vi her 2");
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, "Nå er vi her 3");
                                String title = document.getString("Title");
                                Integer distance = (document.getLong("Distance").intValue());

                                String userName = document.getString("UserID");
                                Long timeStamp = document.getLong("Timestamp");
                                Post p = new Post(title,distance,userName,timeStamp);
                                posts.add(p);

                                // det gikk fint
                                //Toast.makeText(FeedActivity.this, "funket fint",Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "DokumentID: "+document.getId());
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


}
