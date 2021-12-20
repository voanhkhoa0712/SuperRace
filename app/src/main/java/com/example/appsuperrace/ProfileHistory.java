package com.example.appsuperrace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class ProfileHistory extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ActivityListAdapter mAdapter;
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.login";
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_history);

        // make a request queue (singleton?)
        RequestQueue queue = VolleySingleton.getInstance().getRequestQueue(); // okay that was simple, but remember to add Volley to build.gradle file

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        username = mPreferences.getString("username", "-1");

        // send a request GET /getactivities/<username>
        //String requestUrl = "https://superrace.herokuapp.com/getactivities/18120130/";
        String requestUrl = "https://superrace.herokuapp.com/getactivities/" + username + "/";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            LinkedList<Activity> myActivityHistory = new LinkedList<Activity>();

                            // receive response, which is a JSON object
                            JSONObject responseJSONObject = new JSONObject(response);
                            if (responseJSONObject.getBoolean("success")) {

                                // parse that shit and put to myActivityHistory list, which is then feed to RecyclerView
                                String user = (String) responseJSONObject.get("display_name");
                                JSONArray activities = responseJSONObject.getJSONArray("activities");

                                if (activities.length() == 0){
                                    Toast toast = Toast.makeText(ProfileHistory.this, "You haven't had any activities yet!", Toast.LENGTH_LONG);
                                    toast.show();
                                }

                                for (int i = 0; i < activities.length(); ++i) {
                                    // parse each activity
                                    JSONObject activity = activities.getJSONObject(i);
                                    String timePosted = (String) activity.get("start_time");
                                    String caption = (String) activity.get("caption");
                                    String time = (String) activity.get("duration");
                                    // IMPORTANT: Android 9.0 and above only accepts https connections not http
                                    String trackImageUrl = (String) activity.get("image_url");
                                    trackImageUrl = trackImageUrl.substring(0, 4) + 's' + trackImageUrl.substring(4);
                                    Double distance = (Double) activity.get("distance");
                                    Double pace = (Double) activity.get("pace");

                                    // add it to myActivityHistory list
                                    Activity myActivity = new Activity(user, timePosted, caption, trackImageUrl, time, distance, pace);
                                    myActivityHistory.add(myActivity);
                                }

                                // Render RecyclerView
                                mRecyclerView = findViewById(R.id.profile_history);
                                mAdapter = new ActivityListAdapter(ProfileHistory.this, myActivityHistory);
                                mRecyclerView.setAdapter(mAdapter);
                                mRecyclerView.setLayoutManager(new LinearLayoutManager(ProfileHistory.this));
                            }
                            else {
                                Toast toast = Toast.makeText(ProfileHistory.this, "Can not find user's activity history. Please try again later.", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                        catch (JSONException error) {
                            error.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Receive", "Error retrieving activity history data.");
            }
        });

        // actually send the request
        queue.add(stringRequest);

    }
}