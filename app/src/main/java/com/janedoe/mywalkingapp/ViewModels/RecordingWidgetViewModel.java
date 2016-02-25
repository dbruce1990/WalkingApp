package com.janedoe.mywalkingapp.ViewModels;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.janedoe.mywalkingapp.R;

/**
 * Created by janedoe on 2/12/2016.
 */
public class RecordingWidgetViewModel {
    //Attributes
    private TextView distanceTextView;
    private Button recordBtn;
    private Button stopBtn;
    private TextView timeTextView;

    //Dependencies
//    Stopwatch stopwatch;

    public RecordingWidgetViewModel(Activity activity) {
//        stopwatch = Stopwatch.getInstance();

        timeTextView = (TextView) activity.findViewById(R.id.time);
        distanceTextView = (TextView) activity.findViewById(R.id.distance);

        recordBtn = (Button) activity.findViewById(R.id.recordBtn);
        stopBtn = (Button) activity.findViewById(R.id.stopBtn);

//        recordBtn.setOnClickListener(recordBtnOnCLickListener());
//        stopBtn.setOnClickListener(stopBtnOnClickListener());
    }

    public void setRecordBtnOnClickListener(View.OnClickListener listener){
        recordBtn.setOnClickListener(listener);
    }

    public void setStopBtnOnClickListener(View.OnClickListener listener){
        stopBtn.setOnClickListener(listener);
    }

    public TextView getDistanceTextView() {
        return distanceTextView;
    }

    public Button getRecordBtn() {
        return recordBtn;
    }

    public Button getStopBtn() {
        return stopBtn;
    }

    public TextView getTimeTextView() {
        return timeTextView;
    }

    public void setDistanceTextView(TextView distanceTextView) {
        this.distanceTextView = distanceTextView;
    }

    public void setRecordBtn(Button recordBtn) {
        this.recordBtn = recordBtn;
    }

    public void setStopBtn(Button stopBtn) {
        this.stopBtn = stopBtn;
    }

    public void setTimeTextView(TextView timeTextView) {
        this.timeTextView = timeTextView;
    }

}
