package com.janedoe.anothertabexample;

import android.app.Activity;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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

    private long sec;
    private long min;
    private long hr;

    public Stopwatch(Activity activity) {
        timeText = (TextView) activity.findViewById(R.id.time);
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

    private String formattedTime() {
        hr = TimeUnit.MILLISECONDS.toHours(elapsedTime);
        min = TimeUnit.MILLISECONDS.toMinutes(elapsedTime);
        sec = TimeUnit.MILLISECONDS.toSeconds(elapsedTime);
        return String.format("%02d:%02d:%02d", hr, min, sec);
    }

    private void record() {
        handler.postDelayed(run, 0);
        recording = true;
        paused = false;
    }

    private Runnable run = new Runnable() {
        @Override
        public void run() {
            elapsedTime = (SystemClock.uptimeMillis() - startTime) - totalTimePaused;
            timeText.setText(formattedTime());
            handler.postDelayed(this, 0);
        }
    };
}
