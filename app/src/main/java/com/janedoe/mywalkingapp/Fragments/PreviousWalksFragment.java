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

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.janedoe.mywalkingapp.Handlers.WebRequestHandler;
import com.janedoe.mywalkingapp.Models.WalkModel;
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

import com.janedoe.mywalkingapp.Adapters.*;

import org.json.JSONException;
import org.json.JSONObject;

public class PreviousWalksFragment extends Fragment {

    ArrayList<WalkModel> walks;
    WebRequestHandler req = WebRequestHandler.getInstance();
    View root;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Assign view layout and get reference
        root = inflater.inflate(R.layout.fragment_previous_walks, container, false);

        //Get all walks
        getAllWalks();
        Toast.makeText(root.getContext(), "Swipe to refresh", Toast.LENGTH_SHORT).show();
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllWalks();
            }
        });
        return root;
    }

    private void getAllWalks() {
        req.GET("/walks", responseListener(), errorListener());
    }

    private Response.ErrorListener errorListener() {
        return null;
    }

    private Response.Listener<JSONObject> responseListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("GET /walks", response.getJSONArray("walks").toString(2));


                    walks = new Gson().fromJson(response.getJSONArray("walks").toString(), new TypeToken<ArrayList<WalkModel>>() {
                    }.getType());

                    ListItemAdapter listItemAdapter = new ListItemAdapter(getContext(), R.layout.fragment_previous_walk_item, walks);
                    ListView listView = (ListView) root.findViewById(R.id.list_view);
                    listView.setAdapter(listItemAdapter);
                    swipeRefreshLayout.setRefreshing(false);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private class getWalks extends AsyncTask<ArrayList<WalkModel>, Void, ArrayList<WalkModel>> {

        @Override
        protected ArrayList<WalkModel> doInBackground(ArrayList<WalkModel>... params) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            ArrayList<WalkModel> walks = new ArrayList<>();
            if (networkInfo != null && networkInfo.isConnected()) {
                //connection available
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL("http://walkingapp.herokuapp.com/walks");
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

                    walks = new Gson().fromJson(res, new TypeToken<ArrayList<WalkModel>>() {
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
        protected void onPostExecute(ArrayList<WalkModel> result) {
            super.onPostExecute(result);
            Log.d("Results as POJO", String.valueOf(result));
            ListItemAdapter listAdapter = new ListItemAdapter(getContext(), R.layout.fragment_previous_walk_item, result);
            ListView listView = (ListView) root.findViewById(R.id.list_view);
            listView.setAdapter(listAdapter);
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
