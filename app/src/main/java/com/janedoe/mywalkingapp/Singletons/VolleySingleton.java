package com.janedoe.mywalkingapp.Singletons;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.janedoe.mywalkingapp.Application.MyApplication;

/**
 * Created by janedoe on 2/4/2016.
 */
public class VolleySingleton {
    private static VolleySingleton singleton;
    private RequestQueue requestQueue;

    private VolleySingleton(){
        requestQueue = Volley.newRequestQueue(MyApplication.getAppContext());
    }

    public static VolleySingleton getInstance(){
        if(singleton == null)
            singleton = new VolleySingleton();
        return singleton;
    }

    public RequestQueue getRequestQueue(){
        return requestQueue;
    }
}
