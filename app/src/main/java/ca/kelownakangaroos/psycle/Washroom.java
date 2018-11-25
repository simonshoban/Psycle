package ca.kelownakangaroos.psycle;

public class Washroom extends Location {
    public String address;
    public String hours;


    public Washroom(String name, double latitude, double longitude, String address, String hours) {
        super(name, latitude, longitude);
        this.address = address;
        this.hours = hours;

    }
    public String getDescription() {
        return address + "\nHours: " + hours;
    }
}
