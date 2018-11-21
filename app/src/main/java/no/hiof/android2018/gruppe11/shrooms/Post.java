package no.hiof.android2018.gruppe11.shrooms;

import android.graphics.Bitmap;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;

public class Post {
    public String title;
    public String description;
    public String user;
    public long timeStamp;
    public String bildeNavn;
    public GeoPoint location;
    

    //public Bitmap image;
    public Post(String title, String description, String user, Long timeStamp, GeoPoint location){


        this.title = title;
        this.description = description;
        this.user = user;
        this.timeStamp = timeStamp ;
        this.location = location;

    }
    public Post(String title, String description, String user, Long timeStamp, GeoPoint location, String bildeNavn){


        this.title = title;
        this.description = description;
        this.user = user;
        this.timeStamp = timeStamp ;
        this.location = location;
        this.bildeNavn = bildeNavn;

    }
    /*LITEN ENDRING FOR Å SE OM NOE ENDRET SEG*/

    public String getBildeNavn() {
        return bildeNavn;
    }

    public void setBildeNavn(String bildeURL) {
        this.bildeNavn = bildeURL;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }


    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
/*
    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }*/


}
