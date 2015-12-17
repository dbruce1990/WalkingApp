package com.janedoe.anothertabexample.Models;

import android.location.Location;
import android.os.SystemClock;

/**
 * Created by janedoe on 12/17/2015.
 */
public class Waypoint {
    double latitude;
    double longitude;
    float createdAt;
    float accuracy;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public float getCreatedAt() {
        return createdAt;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public Waypoint(Location location){
        createdAt = SystemClock.uptimeMillis();
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        accuracy = location.getAccuracy();
    }

}
