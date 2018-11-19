package no.hiof.android2018.gruppe11.shrooms;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class GPSLocation {
    /*private LocationManager locationManager;
    private LocationListener locationListener;
    double latitude;
    double longitude;

    public interface CallBack(){
        public void iamdone(double lat, double lon);
    }
    private static CallBack callback;
    public void getLocation(Context context){
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        locationManager.requestSingleUpdate(criteria, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                callback
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            @Override public void onStatusChanged(String provider, int status, Bundle extras) { }
            @Override public void onProviderEnabled(String provider) { }
            @Override public void onProviderDisabled(String provider) { }
        }, null);
    }
*/
}
