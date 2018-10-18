package no.hiof.android2018.gruppe11.shrooms;

import android.graphics.Bitmap;

public class Post {
    public String title;
    public int distance;
    public String user;
    //public Bitmap image;
    public Post(String title, int distance, String user){
        this.title = title;
        this.distance = distance;
        this.user = user;
       // this.image = image;
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
