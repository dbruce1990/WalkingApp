package com.janedoe.mywalkingapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.janedoe.mywalkingapp.R;

public class GoogleMapsFragment extends Fragment {

    private MapView mapView;
    private GoogleMap map;
    private GoogleApiClient googleApiClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map_view, container, false);
        mapView = (MapView) v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

//        if (googleApiClient == null) {
//            googleApiClient = new GoogleApiClient.Builder(v.getContext())
//                    .addApi(LocationServices.API)
//                    .build();
//        }
//
//        map = mapView.getMap();
//        map.setMyLocationEnabled(true);
//        Gson gson = new Gson();
//        Log.d("MapFragment.location: ", gson.toJson(LocationServices.FusedLocationApi.getLastLocation(googleApiClient)));
//        Location location = map.getMyLocation();
//        if(location != null){
//            LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
//            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 17.5f));
//            Log.d("location", "not null!");
//        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
