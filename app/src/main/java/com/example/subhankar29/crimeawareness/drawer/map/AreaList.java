package com.example.subhankar29.crimeawareness.drawer.map;


import android.location.Location;

import java.util.ArrayList;

/**
 * Created by mayank on 11/3/17.
 */

public class AreaList {
    public ArrayList<Location> getMembers() {
        return locationList;
    }

    public void setMembers(ArrayList<Location> locationList) {
        this.locationList = locationList;
    }

    private ArrayList<Location> locationList;

    public AreaList(){
        locationList = new ArrayList<>();
    }



}
