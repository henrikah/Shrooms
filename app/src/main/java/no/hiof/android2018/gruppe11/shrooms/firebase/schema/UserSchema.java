package no.hiof.android2018.gruppe11.shrooms.firebase.schema;

import java.util.HashMap;

public class UserSchema {
    private String email, firstName, lastName, profilePic, uid;
    private double latitude, longitude;
    public UserSchema(String email, String firstName, String lastName, String profilePic, double latitude, double longitude, String uid) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePic = profilePic;
        this.latitude = latitude;
        this.longitude = longitude;
        this.uid = uid;
    }
    public HashMap<String, Object> getFull() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Email", email);
        hashMap.put("FirstName", firstName);
        hashMap.put("LastName", lastName);
        hashMap.put("ProfilePic", profilePic);
        hashMap.put("Latitude", latitude);
        hashMap.put("Longitude", longitude);
        hashMap.put("Uid", uid);
        return hashMap;
    }
}
