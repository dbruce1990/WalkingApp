package com.janedoe.mywalkingapp.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.janedoe.mywalkingapp.Helpers.WebRequest;
import com.janedoe.mywalkingapp.Models.User;
import com.janedoe.mywalkingapp.R;

import org.json.JSONException;
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

        SharedPreferences settings = context.getSharedPreferences("WalkingAppSettings", 0);
        final SharedPreferences.Editor editor = settings.edit();

        loginBtn = (Button) findViewById(R.id.login_button);
        usernameEditText = (EditText) findViewById(R.id.username_login_input);
        passwordEditText = (EditText) findViewById(R.id.password_login_input);

        final Dialog dialog = this;
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("loginDialog", "got here!");

                User user = new User();
                user.username = usernameEditText.getText().toString();
                user.password = passwordEditText.getText().toString();

                request.POST("login", user, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        boolean isLoggedIn = false;
                        try {
                            isLoggedIn = response.getBoolean("success");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        editor.putBoolean("isLoggedIn", isLoggedIn);
                        dialog.dismiss();
                    }
                });
            }
        });
    }

}
