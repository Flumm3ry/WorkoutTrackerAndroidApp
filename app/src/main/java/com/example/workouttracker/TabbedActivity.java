package com.example.workouttracker;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.workouttracker.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class TabbedActivity extends AppCompatActivity implements IHasWorkoutList{

    SectionsPagerAdapter sectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_tabbed);
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        // use the custom toolbar as the replacement for the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GetAllWorkoutsAsyncTask myTask = new GetAllWorkoutsAsyncTask(this);
        myTask.execute();
    }

    @Override
    public void setWorkouts(ArrayList<Workout> workouts) {

        for (int i = 0; i < 2; i++) {
            Fragment f = sectionsPagerAdapter.fragmentReferences.get(i).get();
            ((IHasWorkoutList) f).setWorkouts(workouts);
        }
    }
}