package no.hiof.android2018.gruppe11.shrooms;

import android.graphics.Bitmap;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Post {
    public String title;
    public int distance;
    public String user;
    public long timeStamp;
    public Bitmap bilde;
    

    //public Bitmap image;
    public Post(String title, int distance, String user, Long timeStamp/*,Bitmap bilde*/){


        this.title = title;
        this.distance = distance;
        this.user = user;
        this.timeStamp = timeStamp ;
        //this.bilde = bilde;



       // this.image = image;


    }
    /*LITEN ENDRING FOR Å SE OM NOE ENDRET SEG*/

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Bitmap getBilde() {
        return bilde;
    }

    public void setBilde(Bitmap bilde) {
        this.bilde = bilde;
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

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
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
