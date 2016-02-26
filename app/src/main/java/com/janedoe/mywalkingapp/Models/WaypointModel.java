package com.janedoe.mywalkingapp.Models;

import android.location.Location;
import android.os.SystemClock;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by janedoe on 12/17/2015.
 */
public class WaypointModel {
    double createdAt;
    float accuracy;
    double longitude;
    double latitude;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getCreatedAt() {
        return createdAt;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public LatLng getLatLng() {
        return new LatLng(getLatitude(), getLongitude());
    }

    public WaypointModel(Location location){
        createdAt = SystemClock.uptimeMillis();
        accuracy = location.getAccuracy();
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }
}
