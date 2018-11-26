package no.hiof.android2018.gruppe11.shrooms.firebase.schema;

import java.util.HashMap;

public class ShroomSchema {
    private String uid;
    private String name;

    private String description;
    private boolean poisonous;
    public ShroomSchema(String uid, String name, String description, boolean poisonous) {
        this.name = name;
        this.description = description;
        this.poisonous = poisonous;
        this.uid = uid;
    }
    public ShroomSchema(String name, String description, boolean poisonous) {
        this.name = name;
        this.description = description;
        this.poisonous = poisonous;
    }
    public HashMap<String, Object> getFull() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Name", name);
        hashMap.put("Description", description);
        hashMap.put("poisonous", poisonous);
        hashMap.put("Uid", uid);
        return hashMap;
    }
    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPoisonous() {
        return poisonous;
    }

}