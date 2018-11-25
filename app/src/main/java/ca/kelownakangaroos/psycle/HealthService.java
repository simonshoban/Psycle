package ca.kelownakangaroos.psycle;

public class HealthService extends Location {
    public String address;
    public String hours;

    public HealthService(String name, double latitude, double longitude, String address, String hours) {
        super(name, latitude, longitude);
        this.address = address;
        this.hours = hours;
    }

    @Override
    public String getDescription() {
        return address + "\nHours: " + hours;
    }
}
