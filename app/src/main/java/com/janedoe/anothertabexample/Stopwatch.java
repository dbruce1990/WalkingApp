package com.janedoe.anothertabexample;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
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
            startTime = SystemClock.uptimeMillis();
            record();
        } else if (!recording && paused) {
            totalTimePaused += SystemClock.uptimeMillis() - lastPausedAt;
            record();
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

    public void stop() {
        if (paused)
            reset();
        else
            pause();
    }

    public void reset() {
        recording = false;
        paused = false;

        startTime = 0;
        lastPausedAt = 0;
        totalTimePaused = 0;
        elapsedTime = 0;
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

    public String getFormattedElapsedTime(){
        return formattedTime(elapsedTime);
    }

    private void record() {
        handler.postDelayed(run, 1000);
        recording = true;
        paused = false;
    }

    private Runnable run = new Runnable() {
        @Override
        public void run() {
            elapsedTime = (SystemClock.uptimeMillis() - startTime) - totalTimePaused;
            handler.postDelayed(this, 1000);
        }
    };
}
