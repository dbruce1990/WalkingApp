package com.janedoe.mywalkingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.janedoe.mywalkingapp.Controllers.UserController;
import com.janedoe.mywalkingapp.Handler.WebRequest;
import com.janedoe.mywalkingapp.Models.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by janedoe on 1/26/2016.
 */
public class LoginActivity extends AppCompatActivity {
    UserController userController;
    WebRequest req;

    Button registerBtn;
    Button loginBtn;
    EditText usernameInput;
    EditText passwordInput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        registerBtn = (Button) findViewById(R.id.login_registration_btn);
        loginBtn = (Button) findViewById(R.id.login_login_btn);
        usernameInput = (EditText) findViewById(R.id.login_username_input);
        passwordInput = (EditText) findViewById(R.id.login_password_input);

        registerBtn.setOnClickListener(registerBtnOnClickListener());
        loginBtn.setOnClickListener(loginBtnOnClickListener());

        userController = UserController.getInstance(this);
        req = new WebRequest(this);
    }


    private View.OnClickListener loginBtnOnClickListener() {
        final Activity activity = this;

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get user input
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();
                //Create user model
                UserModel user = new UserModel();
                user.setUsername(username);
                user.setPassword(password);
                //attempt to post to server
                req.POST("/login", user,
                        //handle response
                        new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            Log.d("response from server", new Gson().toJson(response));
                            if(success){
                                activity.finish();
                            }else{
                                System.out.println(response);
                            }
                            //TODO inform user of incorrect credentials
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                        //Handle Errors
                        new Response.ErrorListener() {
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

            }
        };
    }

    private View.OnClickListener registerBtnOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        };
    }
}
