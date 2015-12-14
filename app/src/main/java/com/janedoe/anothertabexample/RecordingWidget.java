package com.janedoe.anothertabexample;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;

/**
 * Created by janedoe on 12/14/2015.
 */
public class RecordingWidget {
    private Handler handler;
    private Button recordBtn;
    private Button stopBtn;
    private MapView mapView;
    private Stopwatch stopwatch;
    private Activity activity;

    private TextView timeTextView;

    private static RecordingWidget recordingWidget;

    public static RecordingWidget initialize(Activity activity){
        if(recordingWidget == null)
            recordingWidget = new RecordingWidget(activity);
        return  recordingWidget;
    }

    private RecordingWidget(Activity activity) {
        this.activity = activity;
        handler = new Handler();
        stopwatch = new Stopwatch();
        initButtons();
    }

    private void initButtons() {
        recordBtn = (Button) activity.findViewById(R.id.recordBtn);
        stopBtn = (Button) activity.findViewById(R.id.stopBtn);

        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stopwatch.isRecording()) {
                    stopwatch.pause();
                    recordBtn.setText("Record");
                    handler.removeCallbacks(run);
                } else {
                    stopwatch.start();
                    recordBtn.setText("Pause");
                    updateUI();
                }
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopwatch.stop();
                recordBtn.setText("Record");
                handler.removeCallbacks(run);

                if(stopwatch.isPaused()){

                }
            }
        });
    }

    private void updateUI(){
        handler.postDelayed(run, 1000);
    }

    private Runnable run = new Runnable(){
        @Override
        public void run() {
            if(timeTextView == null)
                timeTextView = (TextView) activity.findViewById(R.id.time);
            timeTextView.setText(stopwatch.getFormattedElapsedTime());
            Log.d("RecordingWidget", "Formatted Elapsed Time: " + stopwatch.getFormattedElapsedTime());

            handler.postDelayed(this, 1000);
        }
    };

}
