package com.janedoe.mywalkingapp.Handlers;

import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.janedoe.mywalkingapp.Singletons.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by janedoe on 1/6/2016.
 */
public class WebRequestHandler {
    private static WebRequestHandler webRequest;
    private RequestQueue requestQueue;
    private String requestTag = "Activity";

    private Gson gson = new Gson();
    private String baseURL = "http://walkingapp.herokuapp.com";

    public static WebRequestHandler getInstance(){
        if(webRequest == null)
            webRequest = new WebRequestHandler();
        return webRequest;
    }

    private WebRequestHandler() {
        requestQueue = VolleySingleton.getInstance().getRequestQueue();
    }

    public void POST(String path, Object obj, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        String url = baseURL + path;
        JSONObject data = toJSONObject(obj);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
                url, data, responseListener, errorListener);

        req.setTag(requestTag);
        requestQueue.add(req);
    }

    public void GET(String path, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        String url = baseURL + path;

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null, responseListener, errorListener);

        req.setTag(requestTag);
        requestQueue.add(req);
    }

    public void isLoggedIn(Response.Listener<JSONObject> responseListener){
        String url = baseURL + "/isLoggedIn";

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null, responseListener, isLoggedInErrorListener());

        req.setTag(requestTag);
        requestQueue.add(req);
    }

    @NonNull
    private Response.ErrorListener isLoggedInErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = "Message: ";
                if(error.getMessage() != null){
                    message += error.getMessage().toString();
                }
                else
                    message += "no message available.";

                message += "\n";

                if(error.networkResponse != null){
                    message += "Network Response: ";
                    message += error.networkResponse.toString();

                    if(error.networkResponse.statusCode == 401){
                        message += "\n Got here!";

                    }
                }
                Log.e("MainActivity", message);
            }
        };
    }

    private JSONObject toJSONObject(Object obj){
        try{
            return new JSONObject(gson.toJson(obj));
        }catch (JSONException e) {
            Log.d("toJSONObject", "got here");
            e.printStackTrace();
        }

        return null;
    }
}
