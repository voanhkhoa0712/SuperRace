package com.example.appsuperrace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class EventAddFragment extends Fragment {
    private String sharedPrefFile;
    private String username;
    private EditText catchTitle;
    private EditText catchDescription;
    private EditText catchStartdate;
    private EditText catchEnddate;
    private EditText catchMilestone;

    private Button add;

    private TextView registerMess;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_add,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get username
        String sharedPrefFile = getParentFragment().getActivity().getString(R.string.intenal_data);
        SharedPreferences mPreferences = getParentFragment().getActivity().getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        username = mPreferences.getString("username", "-1");

        catchTitle = getView().findViewById(R.id.addevent_title);
        catchDescription = getView().findViewById(R.id.addevent_description);
        catchStartdate = getView().findViewById(R.id.addevent_startdate);
        catchEnddate = getView().findViewById(R.id.addevent_enddate);
        catchMilestone = getView().findViewById(R.id.addevent_milestone);

        add = getView().findViewById(R.id.addButton);

        registerMess = getView().findViewById(R.id.registerMess);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Button disabled until called back, prevent multiple clicks
                add.setEnabled(false);

                final String inputTitle = catchTitle.getText().toString();
                final String inputDescription = catchDescription.getText().toString();
                final String inputStartdate = catchStartdate.getText().toString();
                final String inputEnddate = catchEnddate.getText().toString();
                final String inputMilestone = catchMilestone.getText().toString();


                registerMess.setText("");

                if(inputTitle.isEmpty() || inputDescription.isEmpty() || inputStartdate.isEmpty() || inputEnddate.isEmpty() || inputMilestone.isEmpty()){
                    registerMess.setText("You have to fill all of information!");
                }
                else{
                    RequestQueue queue = VolleySingleton.getInstance().getRequestQueue();

                    String url = "https://superrace.herokuapp.com/createevent/";

                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    add.setEnabled(true);
                                    try {
                                        Log.d("Receive", "Response: " + response);
                                        JSONObject obj = new JSONObject(response);
                                        String state = obj.getString("success");

                                        if (state == "false") {
                                            registerMess.setText("Event title already exist!");
                                        }

                                        else if(state == "true"){
                                            Toast.makeText(getActivity(), "Event registered successfully!", Toast.LENGTH_SHORT).show();
                                            catchTitle.setText("");
                                            catchDescription.setText("");
                                            catchStartdate.setText("");
                                            catchEnddate.setText("");
                                            catchMilestone.setText("");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    add.setEnabled(true);
                                    Log.d("Receive", "Cannot send or receive add event data.");
                                    registerMess.setText("Incorrect format or wrong info.");
                                }
                            }){
                        protected Map<String, String> getParams() {
                            Map<String, String> MyData = new HashMap<String, String>();
                            MyData.put("username",username);
                            MyData.put("title", inputTitle);
                            MyData.put("description", inputDescription);
                            MyData.put("start_date", inputStartdate);
                            MyData.put("end_date", inputEnddate);
                            MyData.put("milestone", inputMilestone);

                            return MyData;
                        }
                    };
                    queue.add(stringRequest);
                }
            }
        });

        /*
        hintToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddFragment.this, LoginActivity.class);
                startActivity(intent);
            }
        });

         */
    }

}
