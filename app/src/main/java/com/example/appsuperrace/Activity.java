package com.example.appsuperrace;

public class Activity {
    public String mUser, mTimePosted, mCaption;
    public String mTrackImageUrl;
    public String mTime;
    public Double mDistance, mPace;

    public Activity(String user, String timePosted, String caption, String trackImageUrl, String time, Double distance, Double pace) {
        mUser = user;
        mTimePosted = timePosted;
        mCaption = caption;
        mTrackImageUrl = trackImageUrl;
        mTime = time;
        mDistance = distance;
        mPace = pace;
    }
}
