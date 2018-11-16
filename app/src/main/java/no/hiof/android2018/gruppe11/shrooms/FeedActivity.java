package no.hiof.android2018.gruppe11.shrooms;

import android.content.Intent;
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
        Intent myIntent = new Intent(FeedActivity.this, bottomNavTest.class);
        startActivity(myIntent);
        setContentView(R.layout.activity_feed);
      // setnames();
        //fillSoppList();

        Log.d(TAG, "PostLength:" + posts.size());
    }
    private void setnames(){
        Post d1 = new Post("Brunsopp",2,"Herman","Blablablabla");
        Post d2 = new Post("Blåsopp",4,"Herman","Blablablabla");
        Post d3 = new Post("Kurdersopp",8,"Herman","Blablablabla");
        Post d4 = new Post("Kinesersopp",10,"Herman","Blablablabla");

        savePostToDatabase(d1);
        savePostToDatabase(d2);
        savePostToDatabase(d3);
        savePostToDatabase(d4);

       //initRecyclerView();


    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.mainFeed);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(posts, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    public  void savePostToDatabase(Post post){
        Map<String, Object> postMap = new HashMap<>();
        postMap.put("Title",post.getTitle());
        postMap.put("Distance", post.getDistance());
        postMap.put("UserID",post.getUser());
        postMap.put("Timestamp", post.getTimeStamp());
        postMap.put("Description", post.getDescription());


        db.collection("Posts").document().set(postMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // det gikk bra
            }
        });
    }



}

