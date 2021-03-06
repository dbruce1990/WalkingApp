package com.janedoe.mywalkingapp.Models;

import java.util.ArrayList;

/**
 * Created by janedoe on 12/10/2015.
 */
public class WalkModel {
    private String description;
    private long createdAt;
    private long elapsedTime;
    private float distance;
    private ArrayList<WaypointModel> waypoints;

    public void addWaypoint(WaypointModel waypoint){
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

    public void setWaypoints(ArrayList<WaypointModel> waypoints) {
        this.waypoints = waypoints;
    }

    public float getDistance() {
        return distance;
    }

    public ArrayList<WaypointModel> getWaypoints() {
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

    public WalkModel() {
        createdAt = System.currentTimeMillis();
        description = "";
        elapsedTime = 0;
        distance = 0;
        waypoints = new ArrayList<>();
    }

}
