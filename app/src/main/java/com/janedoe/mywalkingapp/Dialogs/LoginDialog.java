package com.janedoe.mywalkingapp.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.janedoe.mywalkingapp.Handler.WebRequest;
import com.janedoe.mywalkingapp.Models.UserModel;
import com.janedoe.mywalkingapp.R;

import org.json.JSONObject;

/**
 * Created by janedoe on 1/7/2016.
 */
public class LoginDialog extends Dialog{
    WebRequest request;

    Button loginBtn;
    EditText usernameEditText;
    EditText passwordEditText;

    public LoginDialog(Context context) {
        super(context);

        this.setContentView(R.layout.login_dialog);
        request = new WebRequest(context);

        loginBtn = (Button) findViewById(R.id.login_button);
        usernameEditText = (EditText) findViewById(R.id.username_login_input);
        passwordEditText = (EditText) findViewById(R.id.password_login_input);

        final Dialog dialog = this;
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("loginDialog", "got here!");

                UserModel user = new UserModel();
                user.setUsername(usernameEditText.getText().toString());
                user.setPassword(passwordEditText.getText().toString());

                request.POST("/login", user, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        dialog.dismiss();
                    }
                },  new Response.ErrorListener() {
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
        });
    }

}
