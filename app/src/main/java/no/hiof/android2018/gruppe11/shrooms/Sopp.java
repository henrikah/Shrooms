package no.hiof.android2018.gruppe11.shrooms;


import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class Sopp {

    private static ArrayList<Sopp> soppListe = new ArrayList<Sopp>();
    private String name;
    private String soppId;
    private String beskrivelse;
    private Bitmap bilde;
    private boolean giftig;

    public Sopp(String name, String soppId, String beskrivelse, Bitmap bilde, boolean giftig) {
        this.name = name;
        this.soppId = soppId;
        this.beskrivelse = beskrivelse;
        this.bilde = bilde;
        this.giftig = giftig;
        soppListe.add(this);
    }

    public static ArrayList<Sopp> getSoppListe() {
        return soppListe;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSoppId() {
        return soppId;
    }

    public void setSoppId(String soppId) {
        this.soppId = soppId;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public Bitmap getBilde() {
        return bilde;
    }

    public void setBilde(Bitmap bilde) {
        this.bilde = bilde;
    }

    public boolean isGiftig() {
        return giftig;
    }

    public void setGiftig(boolean giftig) {
        this.giftig = giftig;
    }
}