package com.janedoe.mywalkingapp.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.janedoe.mywalkingapp.Models.Walk;
import com.janedoe.mywalkingapp.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.janedoe.mywalkingapp.Adapters.*;

public class PreviousWalksTab extends Fragment {

    View root;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_previous_walks, container, false);
        new getWalks().execute();
        Toast.makeText(root.getContext(), "Swipe to refresh", Toast.LENGTH_SHORT).show();
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new getWalks().execute();
            }
        });

        return root;
    }

    private class getWalks extends AsyncTask<ArrayList<Walk>, Void, ArrayList<Walk>> {

        @Override
        protected ArrayList<Walk> doInBackground(ArrayList<Walk>... params) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            ArrayList<Walk> walks = new ArrayList<>();
            if (networkInfo != null && networkInfo.isConnected()) {
                //connection available
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL("http://192.168.2.2:3000/walks");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setChunkedStreamingMode(0);
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    String res = "";
                    InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    while ((line = br.readLine()) != null) {
                        res += line;
                        Log.d("response", String.valueOf(line));
                    }


                    walks = new Gson().fromJson(res, new TypeToken<ArrayList<Walk>>() {
                    }.getType());

                    inputStream.close();
                    urlConnection.disconnect();
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
            return walks;
        }

        @Override
        protected void onPostExecute(ArrayList<Walk> result) {
            super.onPostExecute(result);
            Log.d("Results as POJO", String.valueOf(result));
            ListItemAdapter listAdapter = new ListItemAdapter(getContext(), R.layout.fragment_previous_walk_item, result);
            ListView listView = (ListView) root.findViewById(R.id.list_view);
            listView.setAdapter(listAdapter);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

//    private ArrayList<Walk> getRoutes() {
//        ArrayList<Walk> walks = new ArrayList<>();
//        Walk walk1 = new Walk();
//        walk1.setDescription("Walked around the block.");
//        walk1.setElapsedTime(SystemClock.uptimeMillis() - 99999999);
//        walk1.setElapsedTime(100000000);
//
//        Walk walk2 = new Walk();
//        walk2.setDescription("Walked to store.");
//        walk2.setElapsedTime(SystemClock.uptimeMillis() - 98999000);
//        walk2.setElapsedTime(100000000);
//
//        Walk walk3 = new Walk();
//        walk3.setDescription("Ran a million miles.");
//        walk3.setElapsedTime(SystemClock.uptimeMillis() - 97999999);
//        walk3.setElapsedTime(18222214);
//
//        Walk walk4 = new Walk();
//        walk4.setDescription("Biked");
//        walk4.setElapsedTime(SystemClock.uptimeMillis() - 96999999);
//        walk4.setElapsedTime(4322342);
//
//        walks.add(walk1);
//        walks.add(walk2);
//        walks.add(walk3);
//        walks.add(walk4);
//        return walks;
//    }
}
