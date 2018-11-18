package no.hiof.android2018.gruppe11.shrooms;

import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SoppVelgerActivity extends Activity {
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sopp_velger);

    retrieveSopp();

    }


    private void initSoppRecycler() {
        recyclerView = findViewById(R.id.soppRecycler);
        recyclerView.setAdapter(new SoppRecyclerAdapter(this, Sopp.getSoppListe()));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    private void retrieveSopp(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference soppColletionRef = db.collection("Shrooms");

        Query soppQuery = soppColletionRef;

        soppQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for(QueryDocumentSnapshot document: task.getResult()){
                        //Toast.makeText(SoppVelgerActivity.this, (String)document.get("navn"), Toast.LENGTH_SHORT).show();
                        Sopp sopp = new Sopp((String) document.get("navn"), "fff", (String) document.get("info"), null, false);
                    }
                }
                else {
                    Toast.makeText(SoppVelgerActivity.this, "Nei", Toast.LENGTH_SHORT).show();
                }

                // Starter sopprecycleren etter at soppene er instansierte
                initSoppRecycler();
            }
        });
    }
}
