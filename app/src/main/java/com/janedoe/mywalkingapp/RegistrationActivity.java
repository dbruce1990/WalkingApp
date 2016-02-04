package com.janedoe.mywalkingapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.janedoe.mywalkingapp.Handler.WebRequest;
import com.janedoe.mywalkingapp.Models.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by janedoe on 1/26/2016.
 */
public class RegistrationActivity extends AppCompatActivity {
    WebRequest req;

    Button signupBtn;
    EditText usernameInput;
    EditText password1Input;
    EditText password2Input;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        signupBtn = (Button) findViewById(R.id.registration_signup_btn);
        usernameInput = (EditText) findViewById(R.id.registration_username_input);
        password1Input = (EditText) findViewById(R.id.registration_password1_input);
        password2Input = (EditText) findViewById(R.id.registration_password2_input);

        signupBtn.setOnClickListener(signupBtnOnClickListener());

        req = WebRequest.getInstance();
    }

    private View.OnClickListener signupBtnOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get input
                String username = usernameInput.getText().toString();
                String pass1 = password1Input.getText().toString();
                String pass2 = password2Input.getText().toString();

                //Check if passwords match and for null values
                if(pass1.equals(pass2) && !pass1.isEmpty() && !pass2.isEmpty() && !username.isEmpty()){
                    //Form data is not null and passwords match, assign input to new UserModel
                    UserModel newUser = new UserModel();
                    newUser.setUsername(username);
                    newUser.setPassword(pass1);

                    //Attempt to create a new user
                    System.out.println("Posting to server...");
                    req.POST("/signup", newUser, responseListener(), errorListener());
                }
                else{
                    //Null value or passwords didn't match
                    System.out.println("Missing credentials!");
                }

            }
        };
    }

    private Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(new Gson().toJson(error));
            }
        };
    }

    private Response.Listener<JSONObject> responseListener() {
        final Activity activity = this;

        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(new Gson().toJson(response));
                try {
                    //check to see if registration was successful
                    boolean success = response.getBoolean("success");
                    if(success){
                        //if registration was successful close activity
                        activity.finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
