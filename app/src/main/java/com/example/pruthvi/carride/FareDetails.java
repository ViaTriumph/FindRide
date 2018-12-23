package com.example.pruthvi.carride;

public class FareDetails {

    public String driverName;
    public String photoUrl;



    public String fare;

    public String destination;

    public String phoneNo;
    public String date;
    public String time;


    /**
     *
     */
    public FareDetails() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    /**
     *
     * @param driverName
     * @param photoUrl
     * @param fare
     * @param destination
     * @param phoneNo
     * @param date
     * @param time
     */
    public FareDetails(String driverName, String photoUrl, String fare, String destination, String phoneNo, String date, String time) {
        this.driverName = driverName;
        this.photoUrl = photoUrl;
        this.fare = fare;
        this.destination = destination;
        this.phoneNo = phoneNo;
        this.date = date;
        this.time = time;
    }

    /**
     *
     * @return Fare
     */
    public String getFare() {
        return fare;
    }

    /**
     *
     * @return Destination
     */
    public String getDestination() {
        return destination;
    }


    /**
     *
     * @return DriverName
     */
    public String getDriverName() {
        return driverName;
    }

    /**
     *
     * @return PhotoUrl
     */
    public String getPhotoUrl() {
        return photoUrl;
    }

    /**
     *
     * @return PhoneNo
     */
    public String getPhoneNo() {
        return phoneNo;
    }

    /**
     *
     * @return Date
     */
    public String getDate() {
        return date;
    }

    /**
     *
     * @return Time
     */
    public String getTime() {
        return time;
    }
}
