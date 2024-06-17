public class Car {
    private String registrationNumber;
    private String make;
    private String color;
    private int year;
    private boolean availability;

    public Car(String registrationNumber, String make, String color, int year, boolean availability) {
        this.registrationNumber = registrationNumber;
        this.make = make;
        this.color = color;
        this.year = year;
        this.availability = availability;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getMake() {
        return make;
    }

    public String getColor() {
        return color;
    }

    public int getYear() {
        return year;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }
}
