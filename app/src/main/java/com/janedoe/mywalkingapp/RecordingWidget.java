package com.janedoe.mywalkingapp;

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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.janedoe.mywalkingapp.Models.Route;
import com.janedoe.mywalkingapp.Models.Waypoint;

import java.util.ArrayList;

/**
 * Created by janedoe on 12/14/2015.
 */
public class RecordingWidget {

    private final TextView distanceTextView;
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
    private PolylineOptions polylineOptions;
    private Location lastLocation;
    private Waypoint waypoint;
    private float totalDistance= 0;

    private static RecordingWidget recordingWidget;
    private LocationListener locationListener;
    private float zoomLevel = 18.5f;
    private boolean cameraInMotion = false;

    private Gson gson;
//    private Polyline polyline;
    private ArrayList<Waypoint> waypoints = new ArrayList<>();

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
                if (stopwatch.isPaused())
                    stop();
                else
                    pause();
            }
        });
    }

    private void stop() {
        cancelLocationUpdates();
        handler.removeCallbacks(run);
        saveWalk();

        timeTextView.setText("00:00:00");
        recordBtn.setText("Record");
        map.clear();
        map.setMyLocationEnabled(false);

        Log.d("PolylineOptions: ", gson.toJson(polylineOptions));
        Log.d("Waypoints: ", gson.toJson(waypoints));
        lastLocation = null;
        polylineOptions = null;
        waypoints.clear();
        totalDistance = 0;
    }

    private void saveWalk() {
        Route route = new Route();

        route.setElapsedTime(stopwatch.getElapsedTime());
        route.setWaypoints(waypoints);
        route.setDescription("Test Description!");
        route.setDistance(totalDistance);

        String result = gson.toJson(route);
        Log.d("Route", result);
    }

    private void record() {
        stopwatch.start();
        requestLocationUpdates();
        updateUI();
        recordBtn.setText("Pause");
        map.setMyLocationEnabled(true);
    }

    private void pause() {
        stopwatch.pause();
        handler.removeCallbacks(run);
        recordBtn.setText("Record");
        cancelLocationUpdates();
    }

    private void updateUI() {
        handler.postDelayed(run, 1000);
    }

    private Runnable run = new Runnable() {
        @Override
        public void run() {
            timeTextView.setText(stopwatch.getFormattedElapsedTime());
            //TODO: convert to miles
            distanceTextView.setText(String.valueOf(Math.round((totalDistance * 0.00062137) * 100) / 100) + " mi");
            handler.postDelayed(this, 1000);
        }
    };

    private void requestLocationUpdates() {

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

        googleApiClient.connect();
        initMap();
        initLocationManager();
        initLocationRequest();
        initLocationListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
    }

    private void cancelLocationUpdates() {
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
        googleApiClient.disconnect();
        locationManager.removeUpdates(locationListener);
    }

    private void initLocationListener() {
        if (locationListener == null) {
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (lastLocation != null) {
                        float distanceBetween = lastLocation.distanceTo(location);
                        if (location.getAccuracy() > 15) {
                            waypoint = new Waypoint(location);
                            waypoints.add(waypoint);

                            Log.d("location", gson.toJson(location));
                            Log.d("distanceBetween", String.valueOf(distanceBetween));
                                    map.clear();
                            updateCamera(waypoint.getLatLng());
                            updatePolylines(waypoint.getLatLng());
                        }
                        totalDistance += distanceBetween;
                    }
                    lastLocation = location;
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
    }

    private void initLocationManager() {
        if (locationManager == null) {
            locationManager = (LocationManager) activity.getSystemService(activity.LOCATION_SERVICE);
        }
    }

    private void initLocationRequest() {
        if (locationRequest == null) {
            locationRequest = new LocationRequest();
            locationRequest.setInterval(1000);
            locationRequest.setFastestInterval(0);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }
    }

    private void updateCamera(LatLng latlng) {
        if (!cameraInMotion) {
            cameraInMotion = true;
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoomLevel), new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    cameraInMotion = false;
                }

                @Override
                public void onCancel() {
                    cameraInMotion = false;
                }
            });
        }
    }

    private void updatePolylines(LatLng latlng) {
        if (polylineOptions == null) {
            polylineOptions = new PolylineOptions().add(latlng);
        }
        polylineOptions.add(latlng);
        map.addPolyline(polylineOptions);
    }

    private void initMap() {
        mapView = (MapView) activity.findViewById(R.id.mapView);
        map = mapView.getMap();

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    private LatLng meanLatLngOfLocation(ArrayList<Location> locations) {
        double lat = 0f, lng = 0f;
        LatLng latlng;
        String logLocations = "Locations size = " + String.valueOf(locations.size());
        logLocations += "\n locations {";

        for (Location location : locations) {
            String str = "";
            lat += location.getLatitude();
            lng += location.getLongitude();
            str += "\n Latitude: " + lat + ", Longitude: " + lng;
            logLocations += str;
        }
        logLocations += "\n}";

        double meanLat = lat / locations.size();
        double meanLng = lng / locations.size();
        latlng = new LatLng(meanLat, meanLng);
        logLocations += "\n Mean: " + meanLat + ", " + meanLng;

        Log.d("Locations: ", logLocations);
        return latlng;
    }

    public static RecordingWidget initialize(Activity activity) {
        if (recordingWidget == null) {
            if (activity == null) {
                Log.d("RecordingWidget: ", "activity NULL in initialize()");
            }
            recordingWidget = new RecordingWidget(activity);
        }
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

        gson = new GsonBuilder().setPrettyPrinting().create();
        timeTextView = (TextView) activity.findViewById(R.id.time);
        distanceTextView = (TextView) activity.findViewById(R.id.distance);
    }
}
