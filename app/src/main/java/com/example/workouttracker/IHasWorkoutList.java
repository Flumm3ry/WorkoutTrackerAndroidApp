package com.example.workouttracker;

import android.app.ProgressDialog;

import java.util.ArrayList;

public interface IHasWorkoutList {
    void setWorkouts(ArrayList<Workout> workouts);
    ProgressDialog getProgressDialog();
    void setProgressDialog(ProgressDialog progressDialog);
}
