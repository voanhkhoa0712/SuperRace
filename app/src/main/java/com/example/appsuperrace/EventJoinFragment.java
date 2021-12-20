package com.example.appsuperrace;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class EventJoinFragment extends Fragment {
    private String sharedPrefFile;
    private String username;
    private RecyclerView rcvJoin;
    private JoinAdapter joinAdapter;
    private LinkedList<Join> mEventList;
    public EventJoinFragment(){
        mEventList = new LinkedList<Join>();
        joinAdapter = new JoinAdapter();

    }

    @Override
    public void onStart() {
        super.onStart();

        // get username
        sharedPrefFile = getParentFragment().getActivity().getString(R.string.intenal_data);
        SharedPreferences mPreferences = getParentFragment().getActivity().getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        username = mPreferences.getString("username", "-1");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_event_join, container, false);

        rcvJoin=mView.findViewById(R.id.rcv_join);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        rcvJoin.setLayoutManager(linearLayoutManager);

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateListJoin();
    }

    private void updateListJoin(){
        /*
        List<Join> list = new ArrayList<>();
        list.add(new Join(R.drawable.image_3, "Run For Dream"));
        list.add(new Join(R.drawable.image_4, "HCMUS - Sống tự nhiên "));
        list.add(new Join(R.drawable.image_5, "Sức bền tuổi trẻ"));
        list.add(new Join(R.drawable.image_3, "HCMUS - ABC...XYZ"));
        list.add(new Join(R.drawable.image_5, "Sức khỏe vô hạn"));
        return list;

         */
        /*---------------------------*/
        RequestQueue queue = VolleySingleton.getInstance().getRequestQueue(); // okay that was simple, but remember to add Volley to build.gradle file
        // send a GET request
        //String requestUrl = "https://superrace.herokuapp.com/getnewevents/18120130/";
        Log.d("Join", username);
        String requestUrl = "https://superrace.herokuapp.com/getnewevents/" + username + "/";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("Receive", "Returned new events data");
                            LinkedList<Join> newJoinList = new LinkedList<Join>();
                            JSONObject responseJSONObject = new JSONObject(response);
                            // receive response, which is a JSON object
                            // parse that shit and put to myActivityHistory list, which is then feed to RecyclerView
                            JSONArray joinedEvents = responseJSONObject.getJSONArray("new_events");
                            for (int i = 0; i < joinedEvents.length(); ++i) {
                                // parse each activity
                                JSONObject activity = joinedEvents.getJSONObject(i);
                                String name = (String) activity.get("title");
                                // TODO: get actual icon from server
                                newJoinList.add(new Join(R.drawable.event_icon_1,name));

                            }
                            // check if new data is the same. NOTE: need to overwrite Event equals() method
                            if (!mEventList.equals(newJoinList)) {
                                // Toast.makeText(getActivity(), "Item refreshed", Toast.LENGTH_SHORT).show();
                                mEventList.clear();
                                mEventList = newJoinList;
                                joinAdapter.setData(mEventList);
                                rcvJoin.setAdapter(joinAdapter);
                            }

                        }
                        catch (JSONException error) {
                            error.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Receive", "Error retrieving new events data.");
            }
        });

        // actually send the request
        queue.add(stringRequest);
    }

}