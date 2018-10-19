package no.hiof.android2018.gruppe11.shrooms;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class SoppVelgerActivity extends Activity {
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sopp_velger);


    new Sopp("Fluesopp", "fff", "Fin og rød", null, true);
    new Sopp("Fotsopp", "ffddf", "Klør veldig", null, true);
    new Sopp("Underlivsopp", "ffddf", "Lukter vondt", null, true);
    initSoppRecycler();
    }


    private void initSoppRecycler() {
        recyclerView = findViewById(R.id.soppRecycler);
        recyclerView.setAdapter(new SoppRecyclerAdapter(this, Sopp.getSoppListe()));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
}
