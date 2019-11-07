package com.example.workouttracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DetailedWorkoutAdapter extends RecyclerView.Adapter<DetailedWorkoutAdapter.DetailedWorkoutViewHolder> {

    private ArrayList<Exercise> exercises;

    public DetailedWorkoutAdapter(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }

    @NonNull
    @Override
    public DetailedWorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DetailedWorkoutViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DetailedWorkoutViewHolder holder, int position) {
        //TODO: Get reps and sets from string file
        holder.tvExerciseName.setText(exercises.get(position).getName());
        holder.tvReps.setText("REPS: " + exercises.get(position).getReps());
        holder.tvSets.setText("SETS: " + exercises.get(position).getSets());
    }

    public void setExercises(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public class DetailedWorkoutViewHolder extends RecyclerView.ViewHolder {

        public TextView tvExerciseName;
        public TextView tvReps;
        public TextView tvSets;

        public DetailedWorkoutViewHolder(@NonNull View itemView) {
            super(itemView);

            tvExerciseName = itemView.findViewById(R.id.tvExerciseRowName);
            tvReps = itemView.findViewById(R.id.tvExerciseRowReps);
            tvSets = itemView.findViewById(R.id.tvExerciseRowSets);
        }
    }
}
