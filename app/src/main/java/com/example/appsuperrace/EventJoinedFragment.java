package com.example.appsuperrace;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
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

import java.util.LinkedList;

import static android.content.Context.MODE_PRIVATE;

public class EventJoinedFragment extends Fragment {
    private String sharedPrefFile;
    private String username;
    private RecyclerView rcvEvent;
    private EventAdapter eventAdapter;
    private LinkedList<Event> mEventList;
    public EventJoinedFragment(){
        eventAdapter = new EventAdapter();
        mEventList = new LinkedList<Event>();
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
        View mView;
        mView = inflater.inflate(R.layout.fragment_event_joined, container, false);
        rcvEvent=mView.findViewById(R.id.rcv_user);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
        rcvEvent.setLayoutManager(linearLayoutManager);
        return mView;
    }


    @Override
    public void onResume() {
        super.onResume();
        updateListEvent(false);
    }

    private void updateListEvent(Boolean onCreate){
        /*
        list.add(new Event(R.drawable.image_1, "Bách Khoa vì bạn."));
        list.add(new Event(R.drawable.image_2, "Ngoại thương sống khỏe"));
        list.add(new Event(R.drawable.image_3, "Tự nhiên tự do"));
        list.add(new Event(R.drawable.image_4, "Hutech - Thanh niên khoe"));
        list.add(new Event(R.drawable.image_5, "IU - Hạnh phúc"));
        list.add(new Event(R.drawable.image_6, "Kinh tế khỏe"));
        list.add(new Event(R.drawable.image_7, "UIT - Sức khỏe vô hạn"));
        list.add(new Event(R.drawable.image_1, "BKU - I Love BeKu."));
        list.add(new Event(R.drawable.image_2, "FTU - Mãi iu"));
        list.add(new Event(R.drawable.image_3, "HCMUS - Xu Cà Na"));
        list.add(new Event(R.drawable.image_5, "IU - Em Iu Anh"));
        */
        /*---------------------------*/
        RequestQueue queue = VolleySingleton.getInstance().getRequestQueue(); // okay that was simple, but remember to add Volley to build.gradle file
        // send a GET request
        //String requestUrl = "https://superrace.herokuapp.com/getjoinedevents/18120130/";
        String requestUrl = "https://superrace.herokuapp.com/getjoinedevents/"+ username + "/";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("Receive", "Returned joined event data");
                            LinkedList<Event> newEventList = new LinkedList<Event>();
                            JSONObject responseJSONObject = new JSONObject(response);
                            // receive response, which is a JSON object
                            // parse that shit and put to myActivityHistory list, which is then feed to RecyclerView
                            JSONArray joinedEvents = responseJSONObject.getJSONArray("joined_events");
                            Log.d("Joined", ((Integer) joinedEvents.length()).toString());
                            for (int i = 0; i < joinedEvents.length(); ++i) {
                                // parse each activity
                                JSONObject activity = joinedEvents.getJSONObject(i);
                                String name = (String) activity.get("title");
                                // TODO: get actual icon from server
                                newEventList.add(new Event(R.drawable.event_icon_3,name));

                            }

                            // check if new data is the same. NOTE: need to overwrite Event equals() method
                            if (!mEventList.equals(newEventList)) {
                                // Toast.makeText(getActivity(), "Item refreshed", Toast.LENGTH_SHORT).show();
                                mEventList.clear();
                                mEventList = newEventList;
                                eventAdapter.setData(mEventList);
                                rcvEvent.setAdapter(eventAdapter);
                            }

                        }
                        catch (JSONException error) {
                            error.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Receive", "Error retrieving joined event data.");
            }
        });

        // actually send the request
        queue.add(stringRequest);
    }

}
