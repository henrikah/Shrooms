package no.hiof.android2018.gruppe11.shrooms;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private static final String TAG = "FeedActivity";

    private ArrayList<Post> posts = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
       setnames();
        fillSoppList();

        Log.d(TAG, "PostLength:" + posts.size());
    }
    private void setnames(){
        Post d1 = new Post("Brunsopp",2,"Herman",System.currentTimeMillis());
        Post d2 = new Post("Blåsopp",4,"Herman",System.currentTimeMillis());
        Post d3 = new Post("Kurdersopp",8,"Herman",System.currentTimeMillis());
        Post d4 = new Post("Kinesersopp",10,"Herman",System.currentTimeMillis());


       // initRecyclerView();


    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.mainFeed);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(posts, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void fillSoppList(){
        Log.d(TAG, "Nå er vi her 1");
        db.collection("Posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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



        Log.d(TAG, "Kode: kjørte ferdig");
    }

    public  void savePostToDatabase(Post post){
        Map<String, Object> postMap = new HashMap<>();
        postMap.put("Title",post.getTitle());
        postMap.put("Distance", post.getDistance());
        postMap.put("userID",post.getUser());
        postMap.put("Timestamp", post.getTimeStamp());

        db.collection("Posts").document().set(postMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // det gikk bra
            }
        });
    }



}

