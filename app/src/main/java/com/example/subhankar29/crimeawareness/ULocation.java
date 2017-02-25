package com.example.subhankar29.crimeawareness;

/**
 * Created by mayank on 25/2/17.
 */

public class ULocation {
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    String longitude;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    String latitude;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    String provider;

    public ULocation(){}
}
