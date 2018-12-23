package com.example.pruthvi.carride;

import android.net.Uri;

public class Passenger {
    public String destination;
    public String pickUpTime;
    public String passengerName;
    public String date;

    /**
     *
     */
    public Passenger() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }


    /**
     *
     * @param destination
     * @param pickUpTime
     * @param passengerName
     * @param date
     */
    public Passenger(String destination, String pickUpTime, String passengerName, String date) {
        this.destination = destination;
        this.pickUpTime = pickUpTime;
        this.passengerName = passengerName;
        this.date = date;
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
     * @return PickUpTime
     */
    public String getPickUpTime() {
        return pickUpTime;
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
     * @return PassengerName
     */
    public String getPassengerName() {
        return passengerName;
    }
}
