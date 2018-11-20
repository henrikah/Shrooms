package no.hiof.android2018.gruppe11.shrooms.map;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.maps.SupportMapFragment;
import no.hiof.android2018.gruppe11.shrooms.R;

public class MapTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_map_test);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new MapLocationPicker(59.5953, 10.4000));

    }

}
