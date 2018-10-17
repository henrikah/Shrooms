package no.hiof.android2018.gruppe11.shrooms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity {

    private ArrayList<String> titles = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        setnames();
    }
    private void setnames(){
        titles.add("Hallo");
        titles.add("Hallo2");
        titles.add("Hallo3");
        titles.add("Hallo4");
        titles.add("Hallo5");
        titles.add("Hallo5");
        titles.add("Hallo5");
        titles.add("Hallo5");
        titles.add("Hallo5");

        initRecyclerView();

    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.mainFeed);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(titles, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
