package com.example.workouttracker;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class AddEditWorkoutActivity extends AppCompatActivity implements EditExerciseFragment.EditExerciseFragmentListener{

    private LinearLayout fragmentHolder;
    private FragmentManager fragmentManager;
    private EditText etWorkoutName;
    private EditText etTime;
    Button btnAddExercise;
    Button btnAddUpdateWorkout;
    private Workout workout;
    private boolean newWorkout;
    private int originalId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_add_edit_workout);

        // use the custom toolbar as the replacement for the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get back button on the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fragmentManager = getSupportFragmentManager();

        etWorkoutName = findViewById(R.id.etWorkoutName);
        etTime = findViewById(R.id.etEstimatedTime);
        btnAddUpdateWorkout = findViewById(R.id.btnAddUpdateWorkout);

        fragmentHolder = findViewById(R.id.llScrollViewLinearLayout);
        btnAddExercise = findViewById(R.id.btnAddExercise);
        btnAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExerciseFragment("", 0, 0);
            }
        });

        btnAddUpdateWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateWorkout();
            }
        });

        workout = getIntent().getParcelableExtra("workout");

        // if a workout was passed in, .the user is adding a workout, else, the user is editing a workout
        newWorkout = workout == null;

        if (!newWorkout)
        {
            populateUI();
            originalId = workout.getId();
        }
    }

    private void updateWorkout() {

        if (etWorkoutName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please ensure the workout has a name and at least one exercise", Toast.LENGTH_LONG).show();
            return;
        }

        if (fragmentManager.getFragments().size() == 0) {
            Toast.makeText(this, "Please ensure the workout has a name and at least one exercise", Toast.LENGTH_LONG).show();
            return;
        }

        for (Fragment f  : fragmentManager.getFragments()) {
            EditExerciseFragment castFrag = ((EditExerciseFragment) f);
            if (!castFrag.checkIfInputValid()) {
                Toast.makeText(this, "Please ensure each exercise has all fields set", Toast.LENGTH_LONG).show();
                return;
            }
        }

        workout = ConstructWorkoutFromUI();

        if (newWorkout) {
            StoreWorkout myTask = new StoreWorkout(this);
            myTask.execute(workout);
            Toast.makeText(this, "Workout Added", Toast.LENGTH_SHORT).show();
        }
        else {
            UpdateWorkout myTask = new UpdateWorkout(this);
            myTask.execute(workout);
            Toast.makeText(this, "Workout Updated", Toast.LENGTH_SHORT).show();
        }
    }

    private void populateUI() {
        etWorkoutName.setText(workout.getName());
        etTime.setText(Integer.toString(workout.getTime()));
        for (Exercise e: workout.getExercises()) {
            addExerciseFragment(e.getName(), e.getReps(), e.getSets());
        }
        btnAddUpdateWorkout.setText(getString(R.string.update_workout));
    }

    private Workout ConstructWorkoutFromUI() {
        ArrayList<Exercise> exercises = new ArrayList<>();


        for (Fragment f: fragmentManager.getFragments()) {
            exercises.add(((EditExerciseFragment) f).getExerciseFromFragment());
        }

        return new Workout(etWorkoutName.getText().toString(), Integer.parseInt(etTime.getText().toString()), exercises);
    }

    private void addExerciseFragment(String name, int reps, int sets) {
        FrameLayout myFrameLayout = new FrameLayout(AddEditWorkoutActivity.this);
        int myId = ViewCompat.generateViewId();
        myFrameLayout.setId(myId);
        fragmentHolder.addView(myFrameLayout, fragmentHolder.getChildCount() - 1);

        Fragment myFragment = new EditExerciseFragment(name, reps, sets);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(myId, myFragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onDeleteFragmentPressed(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }

    // allows the back arrow to close the activity
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public class StoreWorkout extends AsyncTask<Workout, Integer, Integer> {

        private AddEditWorkoutActivity activity;

        public StoreWorkout(AddEditWorkoutActivity activity) {
            this.activity = activity;
        }

        @Override
        protected Integer doInBackground(Workout... workouts) {
            SQLiteDatabase database = new WorkoutDBHelper(activity).getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put("workout_name", workouts[0].getName());
            cv.put("workout_time", workouts[0].getTime());
            int workoutId = (int)database.insert("workouts", null, cv);

            for (int i = 0; i < workouts[0].getExercises().size(); i++) {
                Exercise e = workouts[0].getExercises().get(i);
                cv = new ContentValues();
                cv.put("workout_id", workoutId);
                cv.put("exercise_name", e.getName());
                cv.put("exercise_reps", e.getReps());
                cv.put("exercise_sets", e.getSets());
                database.insert("exercises", null, cv);
            }

            return workoutId;
        }

        @Override
        protected void onPostExecute(Integer id) {
            super.onPostExecute(id);
            activity.workout.setId(id);
            activity.finish();
        }
    }

    public class UpdateWorkout extends AsyncTask<Workout, Void, Integer> {

        private AddEditWorkoutActivity activity;

        public UpdateWorkout(AddEditWorkoutActivity activity) {
            this.activity = activity;
        }

        @Override
        protected Integer doInBackground(Workout... workouts) {
            SQLiteDatabase database = new WorkoutDBHelper(activity).getWritableDatabase();

            // delete the existing workout information
            database.delete("exercises", "workout_id = " + originalId, null);
            database.delete("workouts", "workout_id = " + originalId, null);

            Log.d("GAY", "doInBackground: to delete" + workouts[0].getId());

            ContentValues cv = new ContentValues();
            cv.put("workout_name", workouts[0].getName());
            cv.put("workout_time", workouts[0].getTime());
            int workoutId = (int)database.insert("workouts", null, cv);

            for (int i = 0; i < workouts[0].getExercises().size(); i++) {
                Exercise e = workouts[0].getExercises().get(i);
                cv = new ContentValues();
                cv.put("workout_id", workoutId);
                cv.put("exercise_name", e.getName());
                cv.put("exercise_reps", e.getReps());
                cv.put("exercise_sets", e.getSets());
                database.insert("exercises", null, cv);
            }

            return workoutId;
        }

        @Override
        protected void onPostExecute(Integer id) {
            super.onPostExecute(id);

            activity.workout.setId(id);

            Intent returnIntent = new Intent();
            returnIntent.putExtra("updated_workout", workout);
            returnIntent.putExtra("index", getIntent().getIntExtra("index", 0));

            activity.setResult(Activity.RESULT_OK, returnIntent);
            activity.finish();
        }
    }

    @Override
    public void onBackPressed() {
        createDialog();
    }

    private void createDialog() {
        AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
        alertDlg.setMessage("Are you sure you want to exit?\nAny unsaved changes will be discarded.");
        alertDlg.setCancelable(false);

        alertDlg.setPositiveButton("Yes, I'm sure", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AddEditWorkoutActivity.super.onBackPressed();
            }
        });

        alertDlg.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        alertDlg.create().show();
    }
}
