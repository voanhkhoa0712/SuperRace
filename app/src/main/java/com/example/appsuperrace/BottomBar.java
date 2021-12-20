package com.example.appsuperrace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomBar extends AppCompatActivity {

    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_bar);

        toolbar = getSupportActionBar();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_nav);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar.setTitle("Event");
        loadFragment(new event_fragment());
    }

    @Override
    public void onBackPressed() {
        /*


        // allow back pressed fragments, but not back to login screen
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 1) {
            // Do nothing
        } else {
            getSupportFragmentManager().popBackStack();
        }


         */
        // Do nothing
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.action_event:
                    toolbar.setTitle("Event");
                    fragment = new event_fragment();
                    loadFragment(fragment);
                    return true;
                case R.id.action_record:
                    /*
                    toolbar.setTitle("Record");
                    fragment = new record_fragment();
                    loadFragment(fragment);
                     */
                    Intent intent = new Intent(BottomBar.this, RecordActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.action_personal:
                    toolbar.setTitle("Profile");
                    fragment = new personal_fragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.bottom_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}