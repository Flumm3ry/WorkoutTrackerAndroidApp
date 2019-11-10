package com.example.workouttracker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChooseEditWorkoutActivity extends AppCompatActivity implements IHasWorkoutList{

    RecyclerView rv;
    private ProgressDialog progressDialog;

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public void setProgressDialog(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_choose_edit_workout);

        // use the custom toolbar as the replacement for the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rv = findViewById(R.id.rvEditWorkout);

        rv.setAdapter(new AlterWorkoutRowAdapter(new ArrayList<Workout>()));
        rv.setLayoutManager(new LinearLayoutManager(this));

        GetAllWorkoutsAsyncTask myTask = new GetAllWorkoutsAsyncTask(this);
        myTask.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0)
            if (resultCode == Activity.RESULT_OK) {
                int index = data.getIntExtra("index", 0);
                Workout workout = data.getParcelableExtra("updated_workout");
                ((AlterWorkoutRowAdapter) rv.getAdapter()).workouts.set(index, workout);
                rv.getAdapter().notifyItemChanged(index);
            }
    }

    @Override
    public void setWorkouts(ArrayList<Workout> workouts) {
        ((AlterWorkoutRowAdapter) rv.getAdapter()).workouts = workouts;
        rv.getAdapter().notifyDataSetChanged();
        Log.d("GAY", "setWorkouts: adapter has been set");
    }
}
