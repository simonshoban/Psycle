package ca.kelownakangaroos.psycle;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Location implements Serializable {

    private String name;
    private double latitude;
    private double longitude;

    public Location(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public String getName() {
        return name;
    }

    public LatLng getLatLng(){
        return new LatLng(latitude, longitude);
    }

    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
