package com.example.workouttracker;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

public class DetailedWorkoutViewActivity extends AppCompatActivity {

    private Workout workout;
    private RecyclerView rv;
    private TextView tvProgress;
    private ProgressBar pbProgress;
    private TextView tvWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_detailed_workout_view);

        // use the custom toolbar as the replacement for the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        workout = getIntent().getParcelableExtra("workout");

        tvWorkout = findViewById(R.id.tvViewWorkoutName);
        tvWorkout.setText(workout.getName());

        rv = findViewById(R.id.rvDetailedWorkout);
        tvProgress = findViewById(R.id.tvWorkoutProgress);
        pbProgress = findViewById(R.id.pbWorkoutProgress);

        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rv.setAdapter(new DetailedWorkoutAdapter(workout.getExercises()));

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rv);

        rv.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                updateProgress();
            }
        });
    }

    private void updateProgress() {
        int position = ((LinearLayoutManager) rv.getLayoutManager()).findLastVisibleItemPosition() + 1;
        int total = workout.getExercises().size();
        float progress = (float)position/total*100;
        tvProgress.setText(position + "/" + total);
        pbProgress.setProgress((int) progress, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.workout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.icShare:
                shareWorkout();
                break;
            case R.id.icEdit:
                Intent intent = new Intent(this, AddEditWorkoutActivity.class);

                intent.putExtra("workout", workout);

                this.startActivityForResult(intent, 1);
                break;
        }

        return true;
    }

    private void shareWorkout() {
        String shareData = "Hey! Have a look at this awesome workout!\n" +
                "Name: " + workout.getName() + "\n" +
                "Estimated time: " + workout.getTime() + "minutes\n" +
                "This workout consists of:" + "\n";

        for (Exercise e:
                workout.getExercises()) {
            shareData += e.getName() + ": " + e.getSets() + " sets of " + e.getReps() + "reps\n";
        }

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareData);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1)
            if (resultCode == Activity.RESULT_OK) {
                workout = data.getParcelableExtra("updated_workout");
                tvWorkout.setText(workout.getName());
                ((DetailedWorkoutAdapter) rv.getAdapter()).setExercises(workout.getExercises());
                rv.getAdapter().notifyDataSetChanged();
                Toast.makeText(this, "Workout Updated", Toast.LENGTH_SHORT).show();
            }
    }
}
