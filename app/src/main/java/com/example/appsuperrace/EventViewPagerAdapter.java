package com.example.appsuperrace;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class EventViewPagerAdapter extends FragmentStatePagerAdapter {
    public EventViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return new EventJoinFragment();
            case 1:
                return new EventJoinedFragment();

            case 2:
                return new EventAddFragment();
            default:
                return new EventJoinFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title="";
        switch(position)
        {
            case 0:
                title = "Join Event";
                break;

            case 1:
                title = "Event";
                break;

            case 2:
                title = "Add Event";
                break;
        }
        return title;
    }
}
