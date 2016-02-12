package com.janedoe.mywalkingapp.Singletons;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.janedoe.mywalkingapp.Application.MyApplication;

import java.net.CookieHandler;
import java.net.CookieManager;

/**
 * Created by janedoe on 2/4/2016.
 */
public class VolleySingleton {
    private static VolleySingleton singleton;
    private RequestQueue requestQueue;

    private VolleySingleton(){
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
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
