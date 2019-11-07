package com.example.workouttracker;

import android.content.Context;
import android.content.Intent;
import android.net.sip.SipSession;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WorkoutViewAdapter extends RecyclerView.Adapter<WorkoutViewAdapter.WorkoutViewHolder> {

    public ArrayList<Workout> workouts;

    public WorkoutViewAdapter(ArrayList<Workout> workouts) {
        this.workouts = workouts;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_display, parent, false);
        return new WorkoutViewHolder(v, new OpenWorkoutListener() {
            @Override
            public void onClick(Context c, int pos) {
                Intent intent = new Intent(c, DetailedWorkoutViewActivity.class);
                intent.putExtra("workout", workouts.get(pos));
                c.startActivity(intent);
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        holder.tvWorkoutName.setText(workouts.get(position).getName());
        holder.tvTime.setText(workouts.get(position).getTime() + " minutes");
    }

    public interface OpenWorkoutListener{
        void onClick(Context c, int pos);
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    public static class WorkoutViewHolder extends RecyclerView.ViewHolder{

        public TextView tvWorkoutName;
        public TextView tvTime;

        public WorkoutViewHolder(@NonNull View itemView, final OpenWorkoutListener listener) {
            super(itemView);

            tvWorkoutName = itemView.findViewById(R.id.tvWorkoutName);
            tvTime = itemView.findViewById(R.id.tvWorkoutTime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(view.getContext(), getAdapterPosition());
                }
            });
        }

    }
}
