package com.example.pruthvi.carride;

public class UserProfile {

    private String name;
    private String phoneNo;
    public String location;

    /**
     *
     */
    public UserProfile() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    /**
     *
     * @param name
     * @param phoneNo
     * @param location
     */
    public UserProfile(String name, String phoneNo,String location) {
        this.name = name;
        this.phoneNo = phoneNo;
        this.location=location;
    }

    /**
     *
     * @return Name
     */
    public String getName() {
        return name;
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
     * @return Location
     */
    public String getLocation() {
        return location;
    }
}
