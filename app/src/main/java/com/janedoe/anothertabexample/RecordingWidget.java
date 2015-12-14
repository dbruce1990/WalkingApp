package com.janedoe.anothertabexample;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by janedoe on 12/14/2015.
 */
public class RecordingWidget {

    private GoogleApiClient googleApiClient;
    private Handler handler;
    private Button recordBtn;
    private Button stopBtn;
    private MapView mapView;
    private GoogleMap map;
    private Stopwatch stopwatch;
    private Activity activity;
    private TextView timeTextView;
    private LocationRequest locationRequest;
    private LocationManager locationManager;
    private LatLng latlng;
    private Marker marker;
    private MarkerOptions markerOptions;

    private static RecordingWidget recordingWidget;
    private LocationListener locationListener;
    private float zoomLevel = 16;

    public static RecordingWidget initialize(Activity activity) {
        if (recordingWidget == null)
            recordingWidget = new RecordingWidget(activity);
        return recordingWidget;
    }

    private RecordingWidget(Activity activity) {
        this.activity = activity;
        handler = new Handler();
        stopwatch = new Stopwatch();
        initButtons();

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(activity)
                    .addApi(LocationServices.API)
                    .build();
        }
        mapView = (MapView) activity.findViewById(R.id.mapView);
    }

    private void initButtons() {
        recordBtn = (Button) activity.findViewById(R.id.recordBtn);
        stopBtn = (Button) activity.findViewById(R.id.stopBtn);

        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stopwatch.isRecording())
                    pause();
                else
                    record();
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stopwatch.isPaused()) {
                    stop();
                    timeTextView.setText("00:00:00");
                } else
                    stop();
            }
        });
    }

    private void stop() {
        stopwatch.stop();
        cancelLocationUpdates();
        handler.removeCallbacks(run);
        recordBtn.setText("Record");
    }

    private void record() {
        stopwatch.start();
        requestLocationUpdates();
        updateUI();
        recordBtn.setText("Pause");
    }

    private void pause() {
        stopwatch.pause();
        handler.removeCallbacks(run);
        recordBtn.setText("Record");
    }

    private void updateUI() {
        handler.postDelayed(run, 1000);
    }

    private Runnable run = new Runnable() {
        @Override
        public void run() {
            if (timeTextView == null)
                timeTextView = (TextView) activity.findViewById(R.id.time);
            timeTextView.setText(stopwatch.getFormattedElapsedTime());

            handler.postDelayed(this, 1000);
        }
    };

    private void requestLocationUpdates() {
        googleApiClient.connect();
        if (locationManager == null) {
            locationManager = (LocationManager) activity.getSystemService(activity.LOCATION_SERVICE);
        }

        if (locationRequest == null) {
            locationRequest = new LocationRequest();
            locationRequest.setInterval(1000);
            locationRequest.setFastestInterval(0);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }

        if (locationListener == null) {
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    latlng = new LatLng(location.getLatitude(), location.getLongitude());

                    String result = "";
                    result += "Latitude: " + location.getLatitude();
                    result += "\n";
                    result += "Longitude: " + location.getLongitude();
                    result += "\n";
                    result += "Accuracy: " + location.getAccuracy();

                    Log.d("Location", result);

                    if(map == null){
                        mapView = (MapView) activity.findViewById(R.id.mapView);
                        map = mapView.getMap();
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoomLevel));
                    }

                    if(marker == null){
                       marker = map.addMarker(new MarkerOptions()
                               .title("Current Location")
                               .snippet(result)
                               .position(latlng));
                    }else{
                        marker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                        marker.setSnippet(result);
                    }

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
        }

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates("gps", 1000, 0, locationListener);
    }

    private void cancelLocationUpdates() {
        googleApiClient.disconnect();
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(locationListener);
    }
}
