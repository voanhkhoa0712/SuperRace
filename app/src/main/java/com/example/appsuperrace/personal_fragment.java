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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class personal_fragment extends Fragment {
    private EditText email, firstName, lastName, dateOfBirth, gender, height, weight;
    private Button history, logout;
    private String sharedPrefFile;
    private String username;

    // Retrieve user profile from server and display them
    private void displayProfile() {
        email = getView().findViewById(R.id.edt_profile_email);
        firstName = getView().findViewById(R.id.edt_profile_first_name);
        lastName = getView().findViewById(R.id.edt_profile_last_name);
        dateOfBirth = getView().findViewById(R.id.edt_profile_date_of_birth);
        gender = getView().findViewById(R.id.edt_profile_gender);
        height = getView().findViewById(R.id.edt_profile_height);
        weight = getView().findViewById(R.id.edt_profile_weight);

        history = getView().findViewById(R.id.btn_profile_history);
        logout = getView().findViewById(R.id.btn_profile_logout);

        // request profile data
        RequestQueue queue = VolleySingleton.getInstance().getRequestQueue();
        Log.d("Get", username);
        String requestUrl = "https://superrace.herokuapp.com/getuser/" + username + "/";
        StringRequest stringRequestProfile = new StringRequest(Request.Method.GET, requestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // receive response, which is a JSON object
                            JSONObject responseJSONObject = new JSONObject(response);

                            // parse them
                            if (responseJSONObject.getBoolean("success")) {
                                email.setText(responseJSONObject.getString("Email"));
                                firstName.setText(responseJSONObject.getString("First name"));
                                lastName.setText(responseJSONObject.getString("Last name"));
                                dateOfBirth.setText(responseJSONObject.getString("D.O.B"));
                                gender.setText(responseJSONObject.getString("Gender"));

                                height.setText(String.valueOf(Integer.valueOf(responseJSONObject.getInt("Height"))));
                                weight.setText(String.valueOf(responseJSONObject.getDouble("Weight")));
                            } else {
                                Toast toast = Toast.makeText(getActivity(), "Can not find user's profile data. Please try again later.", Toast.LENGTH_SHORT);
                                toast.show();
                            }

                        } catch (JSONException error) {
                            error.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Receive", "Error retrieving activity history data.");
            }
        });
        queue.add(stringRequestProfile); // send request

        // For now these info are not editable
        email.setEnabled(false);
        firstName.setEnabled(false);
        lastName.setEnabled(false);
        dateOfBirth.setEnabled(false);
        gender.setEnabled(false);
        height.setEnabled(false);
        weight.setEnabled(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get username
        sharedPrefFile = getActivity().getString(R.string.intenal_data);
        SharedPreferences mPreferences = getActivity().getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        username = mPreferences.getString("username", "-1");

        // Put data into the Edit Texts
        displayProfile();

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileHistory = new Intent(getActivity(), ProfileHistory.class);
                getActivity().startActivity(profileHistory);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Account logged out", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });

    }
}
