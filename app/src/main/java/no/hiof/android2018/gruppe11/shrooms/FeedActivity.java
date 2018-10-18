package no.hiof.android2018.gruppe11.shrooms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity {

    private ArrayList<Post> posts = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        setnames();
    }
    private void setnames(){
        posts.add(new Post("Brunsopp",2,"Herman"));
        posts.add(new Post("Blåsopp",4,"Herman"));
        posts.add(new Post("Kurdersopp",8,"Herman"));
        posts.add(new Post("Kinesersopp",10,"Herman"));
        initRecyclerView();

    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.mainFeed);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(posts, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
