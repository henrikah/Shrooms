package no.hiof.android2018.gruppe11.shrooms.model;

import no.hiof.android2018.gruppe11.shrooms.Post;

public class MapPostMarker {
    private double lat, lng;
    private Post post;

    public MapPostMarker(double lat, double lng, Post post) {
        this.lat = lat;
        this.lng = lng;
        this.post = post;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
