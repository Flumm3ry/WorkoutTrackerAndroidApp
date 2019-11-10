package com.example.workouttracker;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AllWorkoutsFragment extends Fragment implements IHasWorkoutList{

    private RecyclerView rv;

    private ArrayList<Workout> workouts = new ArrayList<>();

    public AllWorkoutsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_workouts, container, false);

        rv = v.findViewById(R.id.rvAllWorkouts);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(new WorkoutViewAdapter(workouts));

        return v;
    }

    @Override
    public void setWorkouts(ArrayList<Workout> workouts) {
        ((WorkoutViewAdapter) rv.getAdapter()).workouts = workouts;
        rv.getAdapter().notifyDataSetChanged();
    }

    @Override
    public ProgressDialog getProgressDialog() {
        return null;
    }

    @Override
    public void setProgressDialog(ProgressDialog progressDialog) {
    }
}
