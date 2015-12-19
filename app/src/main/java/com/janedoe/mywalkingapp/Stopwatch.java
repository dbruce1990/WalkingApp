package com.janedoe.mywalkingapp;

import android.os.Handler;
import android.os.SystemClock;

import java.util.concurrent.TimeUnit;

/**
 * Created by janedoe on 12/8/2015.
 */
public class Stopwatch {
    private Handler handler;

    private boolean recording;
    private boolean paused;

    private long startTime;
    private long lastPausedAt;
    private long totalTimePaused;
    private long elapsedTime;

    private long sec;
    private long min;
    private long hr;

    public Stopwatch() {
        handler = new Handler();
    }

    public void start() {
        if (!recording && !paused) {
            record();
        } else if (!recording && paused) {
            resume();
        }
    }

    public void pause() {
        if (!paused && recording) {
            lastPausedAt = SystemClock.uptimeMillis();
            handler.removeCallbacks(run);
            recording = false;
            paused = true;
        }
    }

    private void resume() {
        totalTimePaused += SystemClock.uptimeMillis() - lastPausedAt;
        handler.postDelayed(run, 1000);
        recording = true;
        paused = false;
    }

    private void record() {
        if (confirmNewRecording()){
            elapsedTime = 0;
            lastPausedAt = 0;
            totalTimePaused = 0;

            startTime = SystemClock.uptimeMillis();
            recording = true;
            paused = false;
            handler.postDelayed(run, 1000);
        }
    }

    public void stopAndReset(){
        startTime = 0;
        lastPausedAt = 0;
        totalTimePaused = 0;
        elapsedTime = 0;
    }

    private boolean confirmNewRecording() {
        //TODO: implement confirmation dialogue if previous recording has not yet been saved
        return true;
    }

    public boolean isRecording() {
        return recording;
    }

    public boolean isPaused() {
        return paused;
    }

    private String formattedTime(long millis) {
        hr = TimeUnit.MILLISECONDS.toHours(millis);
        min = TimeUnit.MILLISECONDS.toMinutes(millis - TimeUnit.HOURS.toMillis(hr));
        sec = TimeUnit.MILLISECONDS.toSeconds(millis - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min));
        return String.format("%02d:%02d:%02d", hr, min, sec);
    }

    public String getFormattedElapsedTime() {
        return formattedTime(elapsedTime);
    }

    private Runnable run = new Runnable() {
        @Override
        public void run() {
            elapsedTime = (SystemClock.uptimeMillis() - startTime) - totalTimePaused;
            handler.postDelayed(this, 1000);
        }
    };

    public long getElapsedTime() {
        return elapsedTime;
    }
}
