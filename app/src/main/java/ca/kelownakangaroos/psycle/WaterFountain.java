package ca.kelownakangaroos.psycle;

import com.google.android.gms.maps.model.LatLng;



public class WaterFountain extends Location {

    private String park;

    public WaterFountain(String name, double latitude, double longitude, String park) {
        super(name, latitude, longitude);
        this.park = park;
    }

    @Override
    public String getDescription() {
        return park;
    }
}
