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

public class RegisterActivity extends AppCompatActivity {

    private EditText catchName;
    private EditText catchEmail;
    private EditText catchPass;
    private EditText catchFirstName;
    private EditText catchLastName;
    private EditText catchDOB;
    private EditText catchWeight;
    private EditText catchHeight;
    private EditText catchGender;

    private Button registerButt;

    private TextView hintToLogin;
    private TextView registerMess;
/*
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.login";
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        catchName = findViewById(R.id.registerName);
        catchEmail = findViewById(R.id.registerEmail);
        catchPass = findViewById(R.id.registerPass);
        catchFirstName = findViewById(R.id.registerFirstName);
        catchLastName = findViewById(R.id.registerLastName);
        catchDOB = findViewById(R.id.registerDOB);
        catchWeight = findViewById(R.id.registerWeight);
        catchHeight = findViewById(R.id.registerHeight);
        catchGender = findViewById(R.id.registerGender);

        registerButt = findViewById(R.id.registerButton);
        hintToLogin = findViewById(R.id.hintToLogin);
        registerMess = findViewById(R.id.registerMess);

        registerButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String inputName = catchName.getText().toString();
                final String inputEmail = catchEmail.getText().toString();
                final String inputPass = catchPass.getText().toString();
                final String inputFN = catchFirstName.getText().toString();
                final String inputLN = catchLastName.getText().toString();
                final String inputDOB = catchDOB.getText().toString();
                final String inputWeight = catchWeight.getText().toString();
                final String inputHeight = catchHeight.getText().toString();
                final String inputGender;

                if (catchGender.getText().toString().equals("Male"))
                    inputGender = "True";
                else
                    inputGender = "False";

                Log.d("Info", "Register Input Name: " + inputName);
                Log.d("Info", "Register Input Pass: " + inputPass);

                registerMess.setText("");

                if(inputName.isEmpty() || inputPass.isEmpty() || inputEmail.isEmpty() || inputFN.isEmpty() || inputLN.isEmpty() || inputDOB.isEmpty()
                        || inputHeight.isEmpty() || inputWeight.isEmpty() || inputGender.isEmpty()){
                    registerMess.setText("You have to fill in all the information!");
                }
                else{
                    //RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                    RequestQueue queue = VolleySingleton.getInstance().getRequestQueue();

                    String url = "https://superrace.herokuapp.com/register/";

                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try{
                                        Log.d("Receive", "Response: " + response);
                                        JSONObject obj = new JSONObject(response);
                                        String state = obj.getString("success");

                                        if(state == "false"){
                                            String mes = obj.getString("message");
                                            if( mes.equals("Username already exists.")){
                                                registerMess.setText("Username already exist!");
                                            }else if(mes.equals("Email already exists.")) {
                                                registerMess.setText("Email already exist!");
                                            }
                                        }
                                        else if(state == "true"){
                                            Toast.makeText(RegisterActivity.this, "Account registered successfully!", Toast.LENGTH_SHORT).show();
                                            finish();
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
                                    registerMess.setText("Incorrect Format!");
                                }
                            }){
                        protected Map<String, String> getParams() {
                            Map<String, String> MyData = new HashMap<String, String>();
                            MyData.put("username", inputName);
                            MyData.put("email", inputEmail);
                            MyData.put("password", inputPass);
                            MyData.put("first_name", inputFN);
                            MyData.put("last_name", inputLN);
                            MyData.put("dob", inputDOB);
                            MyData.put("weight", inputWeight);
                            MyData.put("height", inputHeight);
                            MyData.put("gender", inputGender);

                            return MyData;
                        }
                    };
                    queue.add(stringRequest);
                }
            }
        });

        hintToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
/*
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        String content = mPreferences.getString("username", "-1");
        //get text here
        Log.d("SHARE", "SharePre register: " + content);

 */
    }
}