package com.janedoe.mywalkingapp.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by janedoe on 1/7/2016.
 */
public class UserModel {
    long id;
    String username;
    String password;
    int isLoggedIn;

    public long getId(){
        return this.id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isLoggedIn() {
        if(isLoggedIn == 1) {
            return true;
        }
        return false;
    }

    public void setIsLoggedIn(int status){
        if(status == 1)
            isLoggedIn = status;
        if(status == 0)
            isLoggedIn = status;
    }
}
