package no.hiof.android2018.gruppe11.shrooms;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.net.URL;
import java.util.concurrent.CompletableFuture;

import static com.google.android.gms.tasks.Tasks.await;
import static java.lang.Thread.sleep;
import static java.util.concurrent.CompletableFuture.completedFuture;

public class Location_client {


    private static String retLocation = "Balle";
    private static Activity act;



    //Henter og returnerer et Location-objekt
    public static String getLocation(Context context, Activity activity) {
        act = activity;
        LocationAsync locationAsync = new LocationAsync();
        locationAsync.execute(activity);
        return retLocation;
        }


    public float getDistance(Location location1, Location location2){

        return location1.distanceTo(location2);
    }


    private static class LocationAsync extends AsyncTask<Activity, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            retLocation = "pre";
            if (ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(act,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(act,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {

                    } else {

                        ActivityCompat.requestPermissions(act,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                0);

                    }
                } else {

                }
            }
        }

        @SuppressLint("MissingPermission")
        protected String doInBackground(Activity... activity) {
            FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(act);

                            if (mFusedLocationClient.getLastLocation() != null) {
                                retLocation = "Yupp";
                            }
                            else {
                                retLocation = "Nope";
                            }
            return retLocation;
            }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }





    //----------------------------TEST----------------------------------------//
    @SuppressLint("MissingPermission")
    public static void getRetLocation(Context context, Activity activity){
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if (location != null) {
                            retLocation = "Yupp";
                        }
                        else {
                            retLocation = "Nope";
                        }
                    }
                });
    }

    public static void permission(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {

                } else {

                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            0);

                }
            } else {

            }
        }
    }

    }


