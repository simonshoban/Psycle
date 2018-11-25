package ca.kelownakangaroos.psycle;

import com.google.android.gms.maps.model.LatLng;



public class WaterFountain extends Location {

    private String park;

    public WaterFountain(LatLng coordinates, String name, double latitude, double longitude) {
        super(coordinates, name, latitude, longitude);
    }

    @Override
    public String getDescription() {
        return name + "/n" + park;
    }
}
