package com.example.appsuperrace;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class JoinAdapter extends RecyclerView.Adapter<JoinAdapter.JoinViewHolder> {

    private LinkedList<Join> mListJoin;

    private ViewBinderHelper viewBinderHelper=new ViewBinderHelper();


    public void setData(LinkedList<Join> list) {
        this.mListJoin=list;
        notifyDataSetChanged();
    }
/*
    public JoinAdapter(LinkedList<Join> listJoin, OnItemClickListener listener) {
        this.mListJoin = listJoin;
        this.mListener = listener;
    }*/

    @NonNull
    @Override
    public JoinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_join,parent,false);
        return new JoinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final JoinViewHolder holder, int position) {
        Join join=mListJoin.get(position);
        if(join==null)
        {
            return;
        }

        viewBinderHelper.bind(holder.swipeRevealLayout,String.valueOf(join.getId()));
        holder.tvJoinName.setText(join.getName());

        holder.layoutJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.layoutJoin.setEnabled(false);
                //------------------------
                RequestQueue queue = VolleySingleton.getInstance().getRequestQueue();

                String url = "https://superrace.herokuapp.com/joinevent/";

                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                holder.layoutJoin.setEnabled(true);
                                try {
                                    Log.d("Receive", "Response: " + response);
                                    JSONObject obj = new JSONObject(response);
                                    String state = obj.getString("success");

                                    if (state == "false") {
                                        Toast.makeText(SuperRace.getAppContext(),"Can not join event.",Toast.LENGTH_SHORT).show();
                                    }
                                    else if(state == "true"){
                                        Log.d("Receive", "Join request sent successfully.");
                                        mListJoin.remove(holder.getAdapterPosition());
                                        notifyItemRemoved(holder.getAdapterPosition());
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                holder.layoutJoin.setEnabled(true);
                                Log.d("Receive", "Can not send join event request.");
                            }
                        }){
                    protected Map<String, String> getParams() {
                        Map<String, String> MyData = new HashMap<String, String>();
                        String sharedPrefFile = holder.layoutJoin.getContext().getString(R.string.intenal_data);
                        SharedPreferences mPreferences = holder.layoutJoin.getContext().getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
                        String username = mPreferences.getString("username", "-1");
                        Log.d("Join adapter", username);
                        MyData.put("username", username);
                        MyData.put("event_title", holder.tvJoinName.getText().toString());


                        return MyData;
                    }
                };
                queue.add(stringRequest);
                //-----------------------
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mListJoin!=null)
        {
            return mListJoin.size();
        }
        return 0;
    }

    public class JoinViewHolder extends RecyclerView.ViewHolder{

        private SwipeRevealLayout swipeRevealLayout;
        private LinearLayout layoutJoin;


        private ImageView imgJoin;
        private TextView tvJoinName;


        public JoinViewHolder(@NonNull View itemView) {
            super(itemView);

            swipeRevealLayout = itemView.findViewById(R.id.swipeRevealLayout);
            layoutJoin=itemView.findViewById(R.id.layout_join);
            imgJoin = itemView.findViewById(R.id.img_join);
            tvJoinName=itemView.findViewById(R.id.tv_name);

        }
    }
}