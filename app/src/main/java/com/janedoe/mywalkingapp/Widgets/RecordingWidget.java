package com.janedoe.mywalkingapp.Widgets;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.janedoe.mywalkingapp.Handlers.WebRequestHandler;
import com.janedoe.mywalkingapp.Models.Walk;
import com.janedoe.mywalkingapp.Models.Waypoint;
import com.janedoe.mywalkingapp.R;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class RecordingWidget {
    private WebRequestHandler req;

    private final TextView distanceTextView;
    private GoogleApiClient googleApiClient;
    private Handler handler;
    private Button recordBtn;
    private Button stopBtn;
    private MapView mapView;
    private GoogleMap map;
    private StopwatchWidget stopwatch;
    private Activity activity;
    private TextView timeTextView;
    private LocationRequest locationRequest;
    private LocationManager locationManager;
    private PolylineOptions polylineOptions;
    private Location lastLocation;
    private Waypoint waypoint;
    private float totalDistance = 0;

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

        recordBtn.setOnClickListener(recordBtnOnCLickListener());
        stopBtn.setOnClickListener(stopBtnOnClickListener());
    }

    private void stop() {
        Log.d("totalDistance", String.valueOf(totalDistance));
        saveWalk();
    }

    private class postData extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            //check network connection
            ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                //connection available
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL("http://walkingapp.herokuapp.com/walks");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setChunkedStreamingMode(0);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestProperty("Content-Type", "application/json");

                    urlConnection.connect();

                    DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
                    outputStream.writeBytes(params[0]);

                    String res = "";
                    InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    while ((line = br.readLine()) != null) {
//                    res += in.read();
                        Log.d("response", String.valueOf(line));
                    }
                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }

            } else {
                //display error
            }

            return null;
        }
    }

    private void saveWalk() {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.description_dialog);

        Button saveDescriptionButton = (Button) dialog.findViewById(R.id.saveDescriptionButton);
        saveDescriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Walk walk = new Walk();

                walk.setElapsedTime(stopwatch.getElapsedTime());
                walk.setWaypoints(waypoints);
                walk.setDistance(totalDistance);

                EditText descriptionTextView = (EditText) dialog.findViewById(R.id.descriptionTextView);
                walk.setDescription(descriptionTextView.getText().toString());

//                String result = gson.toJson(walk);
//                Log.d("Walk", result);
//                Log.d("totalDistance", String.valueOf(totalDistance));

//                new postData().execute(result);
                req.POST("/walks", walk, walkResponseListener(), walkResponseErrorListener());

                dialog.dismiss();

                cancelLocationUpdates();
                handler.removeCallbacks(run);

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
                stopwatch.stopAndReset();
            }
        });
        dialog.show();
    }

    private Response.Listener<JSONObject> walkResponseListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(new Gson().toJson(response));
            }
        };
    }

    private Response.ErrorListener walkResponseErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(new Gson().toJson(error));
            }
        };
    }

    private void record() {
        stopwatch.start();
        requestLocationUpdates();
//        updateUI();
        recordBtn.setText("Pause");
    }

    private void pause() {
        stopwatch.pause();
        handler.removeCallbacks(run);
        recordBtn.setText("Record");
        cancelLocationUpdates();
    }

    private void updateUI() {
//        handler.post(run);
        timeTextView.setText(stopwatch.getFormattedElapsedTime());
//        distanceTextView.setText(String.valueOf(totalDistance));
        distanceTextView.setText(String.valueOf(Math.round((totalDistance * 0.00062137) * 100) / 100) + " mi");

    }


    private Runnable run = new Runnable() {
        @Override
        public void run() {
            timeTextView.setText(stopwatch.getFormattedElapsedTime());
            distanceTextView.setText(String.valueOf(totalDistance));
//            TODO: convert to miles
//            distanceTextView.setText(String.valueOf(Math.round((totalDistance * 0.00062137) * 100) / 100) + " mi");
            handler.post(this);
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

        Log.d("requestLocationUpdates", "GOT HERE!");
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
//                        if (location.getAccuracy() > 1) {
                        waypoint = new Waypoint(location);
                        waypoints.add(waypoint);

                        Log.d("location", gson.toJson(location));
                        Log.d("distanceBetween", String.valueOf(distanceBetween));

                        map.clear();
                        updateCamera(waypoint.getLatLng());
                        updatePolylines(waypoint.getLatLng());
                        updateUI();
//                        }
                        totalDistance += distanceBetween;
                        Log.d("totalDistance", String.valueOf(totalDistance));
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
        map.setMyLocationEnabled(true);
        Log.d("GET", "HERE");
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
        stopwatch = new StopwatchWidget();
        initButtons();

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(activity)
                    .addApi(LocationServices.API)
                    .build();
        }

        gson = new GsonBuilder().setPrettyPrinting().create();
        timeTextView = (TextView) activity.findViewById(R.id.time);
        distanceTextView = (TextView) activity.findViewById(R.id.distance);
        req = WebRequestHandler.getInstance();
    }

    @NonNull
    private View.OnClickListener stopBtnOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stopwatch.isPaused())
                    stop();
                else
                    pause();
            }
        };
    }

    @NonNull
    private View.OnClickListener recordBtnOnCLickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stopwatch.isRecording())
                    pause();
                else {
                    Log.d("inside", "setOnClickListener!");
                    record();
                }
            }
        };
    }
}
