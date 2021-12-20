package com.example.appsuperrace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.LinkedList;

public class ActivityListAdapter extends RecyclerView.Adapter<ActivityListAdapter.ActivityViewHolder>{
    private final LinkedList<Activity> mActivityList;
    private final LayoutInflater mInflater;

    class ActivityViewHolder extends RecyclerView.ViewHolder {
        private final TextView userView, timePostedView, captionView;
        private final TextView timeValueView, distanceValueView, paceValueView;
        private final NetworkImageView trackView;
        public ActivityViewHolder(View view) {
            super(view);
            userView = (TextView) view.findViewById(R.id.activity_user);
            timePostedView = (TextView) view.findViewById(R.id.activity_time_posted);
            captionView = (TextView) view.findViewById(R.id.activity_caption);
            trackView = (NetworkImageView) view.findViewById(R.id.activity_track);
            timeValueView = (TextView) view.findViewById(R.id.activity_value_time);
            distanceValueView = (TextView) view.findViewById(R.id.activity_value_distance);
            paceValueView = (TextView) view.findViewById(R.id.activity_value_pace);
        }
    }

    public ActivityListAdapter(Context context, LinkedList<Activity> activityList) {
        mInflater = LayoutInflater.from(context);
        mActivityList = activityList;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View activityItemView = mInflater.inflate(R.layout.activity_list_item, parent, false);
        return new ActivityViewHolder(activityItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        Activity mCurrent = mActivityList.get(position);
        holder.userView.setText(mCurrent.mUser);
        holder.timePostedView.setText(mCurrent.mTimePosted);
        holder.captionView.setText(mCurrent.mCaption);
        holder.trackView.setImageUrl(mCurrent.mTrackImageUrl, VolleySingleton.getInstance().getImageLoader());

        String displayTime = mCurrent.mTime;
        holder.timeValueView.setText(displayTime);

        String displayDistance = mCurrent.mDistance.toString() + " km";
        holder.distanceValueView.setText(displayDistance);

        String displayPace = mCurrent.mPace.toString() + " km/h";
        holder.paceValueView.setText(displayPace);
    }

    @Override
    public int getItemCount() {
        return mActivityList.size();
    }
}
