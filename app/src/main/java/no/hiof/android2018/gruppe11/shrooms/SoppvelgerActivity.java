package no.hiof.android2018.gruppe11.shrooms;

import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SoppvelgerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soppvelger);
        collectSopp();
        initRecyclerView();
    }

    private void collectSopp(){
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        CollectionReference soppcollection = fb.collection("Shrooms");
        Query soppQuery = soppcollection;

        soppQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        Sopp sopp = new Sopp(documentSnapshot.getId(),
                                            documentSnapshot.get("navn").toString(),
                                            documentSnapshot.get("info").toString());
                    }

                }
                else {

                }
            }
        });
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.mainFeed);
        SoppRecyclerAdapter adapter = new SoppRecyclerAdapter(this, Sopp.getSoppListe());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
