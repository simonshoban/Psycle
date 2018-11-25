package ca.kelownakangaroos.psycle;

public class Hospital extends Location {
    String address;
    public Hospital(String name, double latitude, double longitude, String address) {
        super(name, latitude, longitude);
        this.address = address;
    }
    public String getDescription() {
        return address;
    }
}
