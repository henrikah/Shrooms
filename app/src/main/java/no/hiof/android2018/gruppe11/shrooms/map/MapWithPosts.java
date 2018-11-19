package no.hiof.android2018.gruppe11.shrooms.map;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.UrlTileProvider;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import no.hiof.android2018.gruppe11.shrooms.model.MapPostMarker;


public class MapWithPosts implements OnMapReadyCallback {

    private LatLng mPosition;
    private ArrayList<MapPostMarker> mapPostMarkers;
    private int mZoom = 10;
    MapWithPosts(double lat, double lng, ArrayList<MapPostMarker> posts) {
        mPosition = new LatLng(lat, lng);
        mapPostMarkers = posts;
    }
    // Med egen zoom
    MapWithPosts(double lat, double lng, ArrayList<MapPostMarker> posts, int zoom) {
        mPosition = new LatLng(lat, lng);
        mapPostMarkers = posts;
        mZoom = zoom;
    }
    // Med kun en post uten zoom
    MapWithPosts(double lat, double lng, MapPostMarker post) {
        mPosition = new LatLng(lat, lng);
        mapPostMarkers = new ArrayList<>();
        mapPostMarkers.add(post);
    }
    // Med kun en post med zoom
    MapWithPosts(double lat, double lng, MapPostMarker post, int zoom) {
        mPosition = new LatLng(lat, lng);
        mapPostMarkers = new ArrayList<>();
        mapPostMarkers.add(post);
        mZoom = zoom;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // Skrur av rotasjon og tilt
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        googleMap.getUiSettings().setTiltGesturesEnabled(false);

        for (MapPostMarker post : mapPostMarkers) {
            googleMap.addMarker(new MarkerOptions().position(new LatLng(post.getLat(), post.getLng())).title(post.getPost().getTitle()).snippet(post.getPost().getTimeStamp().toString()));
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mPosition, mZoom));
        googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(new UrlTileProvider(256, 256) {
            @Override
            public synchronized URL getTileUrl(int x, int y, int zoom) {
                URL url = null;
                try {
                    url = new URL("https://opencache.statkart.no/gatekeeper/gk/gk.open_gmaps?layers=topo4&tilematrixset=WGS84&Format=image%2Fpng&zoom=" + zoom + "&x=" + x + "&y=" + y);
                } catch (MalformedURLException e) {
                    throw new AssertionError(e);
                }
                return url;
            }
        }));
    }
}
