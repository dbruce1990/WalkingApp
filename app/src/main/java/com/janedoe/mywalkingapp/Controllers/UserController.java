package com.janedoe.mywalkingapp.Controllers;

import android.content.Context;

import com.janedoe.mywalkingapp.Handlers.DatabaseHandler;
import com.janedoe.mywalkingapp.Handlers.WebRequestHandler;
import com.janedoe.mywalkingapp.Models.UserModel;

/**
 * Created by janedoe on 1/26/2016.
 */
public class UserController {
    DatabaseHandler db;
    WebRequestHandler req;

    private static UserController controller;
    public static UserController getInstance(Context context){
        if(controller == null)
            controller = new UserController(context);
        return controller;
    }
    private UserController(Context context){
        db = DatabaseHandler.getInstance(context);
//        req = WebRequestHandler.getInstance(context);
    }

    public UserModel login(UserModel user){
        //attempt to log user into server
        user.setIsLoggedIn(1);
        return user;
    }

}
