package com.example.appsuperrace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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


public class LoginActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener{

    private EditText catchUserName;
    private EditText catchPassword;

    private Button loginButt;
    private TextView hintToRegister;
    private TextView loginMess;

    private String userNameBuffer;
    private SharedPreferences mPreferences;
    private String sharedPrefFile;
//    private MainScreen mainScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        catchUserName = findViewById(R.id.editTextEmail);
        catchPassword = findViewById(R.id.editTextPassword);

        loginButt = findViewById(R.id.cirLoginButton);
        hintToRegister = findViewById(R.id.hindToRegister);
        loginMess = findViewById(R.id.loginMess);

        loginButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String inputName = catchUserName.getText().toString();
                final String inputPass  = catchPassword.getText().toString();

                Log.d("Info", "Login Input Name: " + inputName);
                Log.d("Info", "Login Input Pass: " + inputPass);
                loginMess.setText("");

                if(inputName.isEmpty() || inputPass.isEmpty()){
                    loginMess.setText("You have to fill all necessary information!");
                }
                else{
                    //RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    RequestQueue queue = VolleySingleton.getInstance().getRequestQueue();

                    String url = "https://superrace.herokuapp.com/login/";

                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try{
                                        Log.d("Receive", "Response: " + response);
                                        JSONObject obj = new JSONObject(response);
                                        String state = obj.getString("success");
                                        if(state.equals("false")){
                                            loginMess.setText("Incorrect Username or Password!");
                                        }
                                        //else Intent other activity
                                        else if(state.equals("true")){
                                            Toast.makeText(LoginActivity.this, "Login successfully!", Toast.LENGTH_SHORT).show();
                                            userNameBuffer = inputName;

                                            // save username internally
                                            Log.d("Changed", userNameBuffer);
                                            SharedPreferences.Editor editor = mPreferences.edit();
                                            editor.putString("username", userNameBuffer);
                                            editor.apply();
                                            mPreferences.unregisterOnSharedPreferenceChangeListener(LoginActivity.this);
                                            Intent intent = new Intent(LoginActivity.this, BottomBar.class);

                                            catchUserName.setText("");
                                            catchPassword.setText("");

                                            startActivity(intent);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("Receive", "That didn't work!");
                                }
                            }){
                        protected Map<String, String> getParams() {
                            Map<String, String> MyData = new HashMap<String, String>();
                            MyData.put("username", inputName);
                            MyData.put("password", inputPass);

                            return MyData;

                        }
                    };
                    queue.add(stringRequest);
                }
            }
        });

        hintToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        sharedPrefFile = getString(R.string.intenal_data);
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        String content = mPreferences.getString("username", "-1");
        //get text here
        Log.d("SHARE", "SharePre login: " + content);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("username")) {
            Log.d("SHARE", "SharePreferences changed!");
        }
    }
}