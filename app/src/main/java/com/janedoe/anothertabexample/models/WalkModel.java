package com.janedoe.anothertabexample.Models;

import android.os.SystemClock;

/**
 * Created by janedoe on 12/10/2015.
 */
public class WalkModel {
    private String description;
    private long date;
    private long elapsedTime;

    public WalkModel() {
        this.date = SystemClock.uptimeMillis();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String name) {
        this.description = name;
    }

    public long getDate() {
        return date;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsed_time(long elapsed_time) {
        this.elapsedTime = elapsed_time;
    }
}
