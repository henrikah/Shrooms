package no.hiof.android2018.gruppe11.shrooms.firebase.schema;

import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;

public class PostSchema {
    private String title, description, mushroom, mushroomPic, uid;
    private GeoPoint geoPoint;
    private long timestamp;
    public PostSchema(String title, String description, String mushroom, String mushroomPic, GeoPoint geoPoint, long timestamp, String uid) {
        this.title = title;
        this.description = description;
        this.mushroom = mushroom;
        this.mushroomPic = mushroomPic;
        this.geoPoint = geoPoint;
        this.timestamp = timestamp;
        this.uid = uid;
    }
    public HashMap<String, Object> getFull() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Title", title);
        hashMap.put("Description", description);
        hashMap.put("Mushroom", mushroom);
        hashMap.put("MushroomPic", mushroomPic);
        hashMap.put("Location", geoPoint);
        hashMap.put("Timestamp", timestamp);
        hashMap.put("UserID", uid);
        return hashMap;
    }
}
