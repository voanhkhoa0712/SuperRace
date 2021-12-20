package com.example.appsuperrace;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

public class EventAdapter extends  RecyclerView.Adapter<EventAdapter.UserViewHolder> {

    private LinkedList<Event> mListUser;

    public void setData(LinkedList<Event> list) {
        this.mListUser=list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

        Event user=mListUser.get(position);
        if(user==null){
            return;
        }
        holder.imgUser.setImageResource(user.getResourceid());
        holder.tvEvenName.setText(user.getName());

    }

    @Override
    public int getItemCount() {
        if(mListUser!=null) {
            return mListUser.size();
        }
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgUser;
        private TextView tvEvenName;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.img_user);
            tvEvenName=itemView.findViewById(R.id.tv_name);
        }
    }
}
