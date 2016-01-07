package com.janedoe.mywalkingapp.Helpers;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by janedoe on 1/6/2016.
 */
public class WebRequest{
    private RequestQueue requestQueue;
    private String requestTag = "Activity";

    private Gson gson = new Gson();
    private String baseURL = "http://walkingapp.herokuapp.com/";


    public WebRequest(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public void POST(String path, Object obj, Response.Listener<JSONObject> responseListener){
        String url = baseURL + path;
        JSONObject data = toJSONObject(obj);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
                url, data, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = "Message: ";
                if(error.getMessage() != null){
                    message += error.getMessage();
                }
                else
                    message += "no message available.";

                message += "\n";

                if(error.networkResponse != null){
                    message += "Network Response: ";
                    message += error.networkResponse.toString();
                }

                if(error.networkResponse.statusCode == 401){
                    message += "\n Got here!";

                }
                Log.e("MainActivity", message);
            }
        });

        req.setTag(requestTag);
        requestQueue.add(req);
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
