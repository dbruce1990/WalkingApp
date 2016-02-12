package com.janedoe.mywalkingapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.janedoe.mywalkingapp.Adapters.PagerAdapter;
import com.janedoe.mywalkingapp.Handlers.WebRequestHandler;
import com.janedoe.mywalkingapp.R;
import com.janedoe.mywalkingapp.Widgets.RecordingWidget;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    WebRequestHandler req;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Assign view layout
        setContentView(R.layout.activity_main);

        //Add toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Instantiate dependencies
        req = WebRequestHandler.getInstance();

        initTabLayout();
        RecordingWidget.initialize(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        login();
    }

    private void login() {
        //Get reference to this Activity
        final Activity activity = this;
        //Ping server to see if logged in
        req.isLoggedIn(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //if not logged in, direct user to LoginActivity
                try {
                    System.out.println("Login response: " + response.toString(2));
                    boolean success = response.getBoolean("success");
                    if (!success) {
                        Intent intent = new Intent(activity, LoginActivity.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(item.getTitle().equals("Logout")){
            logout();
            return true;
        }

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        //attempt to log user out
        req.GET("/logout", logoutResponseListener(), logoutErrorListener());
    }

    private Response.ErrorListener logoutErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = "Message: ";
                if(error.getMessage() != null){
                    message += error.getMessage().toString();
                }
                else
                    message += "no message available.";

                message += "\n";

                if(error.networkResponse != null){
                    message += "Network Response: ";
                    message += error.networkResponse.toString();

                    if(error.networkResponse.statusCode == 401){
                        message += "\n Got here!";

                    }
                }
                Log.e("MainActivity", message);
            }
        };
    }

    private Response.Listener<JSONObject> logoutResponseListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());

                try {
                    boolean success = response.getBoolean("success");

                    if(success) {
                        //redirect user back to login activity
                        login();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        };
    }

    private void initTabLayout() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Previous"));
        tabLayout.addTab(tabLayout.newTab().setText("Map"));
        tabLayout.addTab(tabLayout.newTab().setText("Stats"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        final PagerAdapter adapter = new PagerAdapter(
                getSupportFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem((tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}
