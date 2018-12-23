package com.example.pruthvi.carride;

import android.net.Uri;

public class Ride {

    private String photo;
    private String driverName;
    private String date;
    private String time;
    private String fare;
    private String phoneNo;

    /**
     *
     * @param photo
     * @param driverName
     * @param date
     * @param time
     * @param fare
     * @param phoneNo
     */
    public Ride(String photo, String driverName, String date, String time, String fare, String phoneNo) {
        this.photo = photo;
        this.driverName = driverName;
        this.date = date;
        this.time = time;
        this.fare = fare;
        this.phoneNo = phoneNo;
    }


    /**
     *
     * @param photo
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     *
     * @param driverName
     */
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    /**
     *
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     *
     * @param time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     *
     * @return Photo
     */
    public String getPhoto() {

        return photo;
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

    /**
     *
     * @return Fare
     */
    public String getFare() {
        return fare;
    }

    /**
     *
     * @return PhoneNo
     */
    public String getPhoneNo() {
        return phoneNo;
    }
}
