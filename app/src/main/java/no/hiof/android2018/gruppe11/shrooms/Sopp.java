package no.hiof.android2018.gruppe11.shrooms;



import java.util.ArrayList;
import java.util.List;

public class Sopp {
    private static List<Sopp> soppListe;
    private String navn;
    private String id;
    private String info;
    private int imageId;

    public Sopp(String id, String navn, String info) {
        this.navn = navn;
        this.id = id;
        this.info = info;
        this.imageId = imageId;
        soppListe.add(this);
    }

    public static List<Sopp> getSoppListe() {

        return soppListe;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}