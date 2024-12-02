package com.example.binsos;

public class LocationItem {
    private String userID;
    private double latitude;
    private double longitude;
    private String timestamp;

    public LocationItem(String userID, double latitude, double longitude, String timestamp) {
        this.userID = userID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    public String getUserID() {
        return userID;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
