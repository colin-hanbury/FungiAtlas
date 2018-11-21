package com.nuig.colin.fungiatlas;

public class LocationData {
    private double longitude;
    private double latitude;

    public LocationData(double longitude, double latitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLongitude(){
        return this.longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}