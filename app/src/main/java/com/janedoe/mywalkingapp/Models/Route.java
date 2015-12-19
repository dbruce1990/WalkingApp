package com.janedoe.mywalkingapp.Models;

import android.os.SystemClock;

import java.util.ArrayList;

/**
 * Created by janedoe on 12/10/2015.
 */
public class Route {
    private String description;
    private long createdAt;
    private long elapsedTime;
    private float distance;
    private ArrayList<Waypoint> waypoints;

    private final String logTag = "Route model: ";

    public void addWaypoint(Waypoint waypoint){
            if(waypoint != null)
                waypoints.add(waypoint);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public void setWaypoints(ArrayList<Waypoint> waypoints) {
        this.waypoints = waypoints;
    }

    public float getDistance() {
        return distance;
    }

    public ArrayList<Waypoint> getWaypoints() {
        return waypoints;
}

    public String getDescription() {
        return description;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public Route() {
        createdAt = SystemClock.uptimeMillis();
        description = "";
        elapsedTime = 0;
        distance = 0;
        waypoints = new ArrayList<>();
    }

}
