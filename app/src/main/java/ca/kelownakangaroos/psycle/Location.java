package ca.kelownakangaroos.psycle;

public class Location {

    private double latitude;
    private double longitude;
    private String name;

    public Location(double latitude, double longitude, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "Location{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", name='" + name + '\'' +
                '}';
    }

    public double getLatitude() {
        return latitude;
    }

    public String getName() {
        return name;
    }

}
