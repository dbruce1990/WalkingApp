package com.janedoe.mywalkingapp.Controllers;

import android.content.Context;

import com.android.volley.Response;
import com.janedoe.mywalkingapp.Handler.DatabaseHandler;
import com.janedoe.mywalkingapp.Handler.WebRequest;
import com.janedoe.mywalkingapp.Models.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by janedoe on 1/26/2016.
 */
public class UserController {
    DatabaseHandler db;
    WebRequest req;

    private static UserController controller;
    public static UserController getInstance(Context context){
        if(controller == null)
            controller = new UserController(context);
        return controller;
    }
    private UserController(Context context){
        db = DatabaseHandler.getInstance(context);
//        req = WebRequest.getInstance(context);
    }

    public UserModel login(UserModel user){
        //attempt to log user into server
        user.setIsLoggedIn(1);
        return user;
    }

}
