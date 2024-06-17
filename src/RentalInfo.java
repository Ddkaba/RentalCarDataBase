
public class RentalInfo {

    private String Number;
    private String driversLicense;
    private String dateOfIssue;
    private String returnDate;


    public RentalInfo(String number, String driversLicense, String dateOfIssue, String returnDate) {
        Number = number;
        this.driversLicense = driversLicense;
        this.dateOfIssue = dateOfIssue;
        this.returnDate = returnDate;
    }

    public String getNumber() {
        return Number;
    }

    public String getDriversLicense() {
        return driversLicense;
    }

    public String getDateOfIssue() {
        return dateOfIssue;
    }

    public String getReturnDate() {
        return returnDate;
    }
}
