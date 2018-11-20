package no.hiof.android2018.gruppe11.shrooms.map;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.UrlTileProvider;
import java.net.MalformedURLException;
import java.net.URL;

public class MapLocationPicker implements OnMapReadyCallback {

    private LatLng mPosition;
    private int mZoom = 5;
    private Marker mMarker;

    private double lat, lng;
    MapLocationPicker(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }
    // Med egen zoom
    MapLocationPicker(double lat, double lng, int zoom) {
        mPosition = new LatLng(lat, lng);
        mZoom = zoom;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // Skrur av rotasjon og tilt
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        googleMap.getUiSettings().setTiltGesturesEnabled(false);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(mMarker == null) {
                    mMarker = googleMap.addMarker(new MarkerOptions().position(latLng));
                } else {
                    mMarker.remove();
                    mMarker = googleMap.addMarker(new MarkerOptions().position(latLng));
                }
                lat = latLng.latitude;
                lng = latLng.longitude;
            }
        });

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
