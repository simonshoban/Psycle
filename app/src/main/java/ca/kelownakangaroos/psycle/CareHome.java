package ca.kelownakangaroos.psycle;

import java.util.jar.Attributes;

public class CareHome extends Location {
    String address;

    public CareHome(String name, double latitude, double longitude, String address) {
        super(name, latitude, longitude);
        this.address = address;
    }
    public String getDescription() {
        return address;
    }
}
