public class Client {
    private String driverLicenseNumber;
    private String fullName;
    private String passportData;
    private String address;
    // Другие поля по необходимости

    public Client(String driverLicenseNumber, String fullName, String passportData, String address) {
        this.driverLicenseNumber = driverLicenseNumber;
        this.fullName = fullName;
        this.passportData = passportData;
        this.address = address;
    }

    public String getDriverLicenseNumber() {
        return driverLicenseNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPassportData() {
        return passportData;
    }

    public String getAddress() {
        return address;
    }
}