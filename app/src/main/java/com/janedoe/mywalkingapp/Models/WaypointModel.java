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
    private LatLng latlng;

    public double getLatitude() {
        return latlng.latitude;
    }

    public double getLongitude() {
        return latlng.longitude;
    }

    public double getCreatedAt() {
        return createdAt;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public LatLng getLatLng() {
        return latlng;
    }

    public WaypointModel(Location location){
        createdAt = SystemClock.uptimeMillis();
        accuracy = location.getAccuracy();
        latlng = new LatLng(location.getLatitude(), location.getLongitude());
    }
}
