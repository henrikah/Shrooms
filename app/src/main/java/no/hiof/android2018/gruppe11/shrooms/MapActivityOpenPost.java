package no.hiof.android2018.gruppe11.shrooms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.firestore.GeoPoint;

import no.hiof.android2018.gruppe11.shrooms.map.MapLocationPicker;
import no.hiof.android2018.gruppe11.shrooms.map.MapWithPosts;
import no.hiof.android2018.gruppe11.shrooms.model.MapPostMarker;

public class MapActivityOpenPost extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_test);
        double lan = getIntent().getExtras().getDouble("lat");
        double lon = getIntent().getExtras().getDouble("lon");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        Post kek = new Post("Null","Null","Null",1479249799770L,new GeoPoint(lan,lon));
        MapPostMarker mpm = new MapPostMarker(lan,lon, kek);
        mapFragment.getMapAsync(new MapWithPosts(lan,lon, mpm));
    }

}
