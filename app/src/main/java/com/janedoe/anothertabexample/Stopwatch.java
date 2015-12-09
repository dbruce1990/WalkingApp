package com.janedoe.anothertabexample;

import android.app.Activity;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.TextView;

/**
 * Created by janedoe on 12/8/2015.
 */
public class Stopwatch {
    private final TextView timeText;
    private Handler handler;

    private boolean recording;
    private boolean paused;

    private long startTime;
    private long lastPausedAt;
    private long totalTimePaused;
    private long elapsedTime;

    private int seconds;
    private int minutes;
    private int hours;


    public Stopwatch(Activity activity) {
        timeText = (TextView) activity.findViewById(R.id.time);
        handler = new Handler();
    }

    private Runnable r = new Runnable() {
        @Override
        public void run() {
            elapsedTime = (SystemClock.uptimeMillis() - startTime) - totalTimePaused;
            seconds = (int) (elapsedTime / 1000);
            minutes = (seconds / 60);
            seconds = seconds % 60;
            timeText.setText(String.valueOf(elapsedTime));
            handler.postDelayed(this, 0);
        }
    };

    public void start() {
        if (!recording && !paused) {
            startTime = SystemClock.uptimeMillis();
            record();
        } else if (!recording && paused) {
            totalTimePaused += SystemClock.uptimeMillis() - lastPausedAt;
            record();
        }
    }

    private void record() {
        handler.postDelayed(r, 0);
        recording = true;
        paused = false;
    }

    public void pause() {
        if (!paused && recording) {
            lastPausedAt = SystemClock.uptimeMillis();
            handler.removeCallbacks(r);
            recording = false;
            paused = true;
        }

    }

    public void stop() {
        if (paused)
            reset();
        else {
            pause();
        }

    }

    public void reset() {
        recording = false;
        paused = false;

        startTime = 0;
        lastPausedAt = 0;
        totalTimePaused = 0;
        elapsedTime = 0;

        timeText.setText("00:00:00");
    }

    public boolean isRecording() {
        return recording;
    }

    public boolean isPaused() {
        return paused;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

}
