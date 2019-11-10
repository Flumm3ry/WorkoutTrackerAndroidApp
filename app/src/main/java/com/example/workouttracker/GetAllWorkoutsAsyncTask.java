package com.example.workouttracker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class GetAllWorkoutsAsyncTask extends AsyncTask<Void, Integer, ArrayList<Workout>> {

    private WeakReference<Activity> activityWeakReference;

    public GetAllWorkoutsAsyncTask(Activity activity) {
        this.activityWeakReference = new WeakReference<>(activity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Activity activity = activityWeakReference.get();

        if (activity == null || activity.isFinishing())
            return;

        IHasWorkoutList dialogHolder = (IHasWorkoutList) activity;

        dialogHolder.setProgressDialog(new ProgressDialog(activity));
        dialogHolder.getProgressDialog().setMessage(activity.getString(R.string.fetching_workouts_progress));
        dialogHolder.getProgressDialog().setCancelable(false);
        dialogHolder.getProgressDialog().show();
    }

    @Override
    protected ArrayList<Workout> doInBackground(Void... voids) {
        ArrayList<Workout> result = new ArrayList<>();

        SQLiteDatabase database = new WorkoutDBHelper(activityWeakReference.get()).getReadableDatabase();

        Cursor cursor = database.query(
                "workout_exercise",
                null,
                null,
                null,
                null,
                null,
                null);

        // used to see which workout is currently being read in
        int currentId = -1;

        int count = cursor.getCount();

        cursor.moveToFirst();

        // read workouts loop
        for (int i = 0; i < count; i++) {

            if (currentId != cursor.getInt(cursor.getColumnIndex("workout_id"))) {

                currentId = cursor.getInt(cursor.getColumnIndex("workout_id"));
                String workoutName = cursor.getString(cursor.getColumnIndex("workout_name"));
                int time = cursor.getInt(cursor.getColumnIndex("workout_time"));

                result.add(new Workout(workoutName, time, currentId));
            }

            String exerciseName = cursor.getString(cursor.getColumnIndex("exercise_name"));
            int reps = cursor.getInt(cursor.getColumnIndex("exercise_reps"));
            int sets = cursor.getInt(cursor.getColumnIndex("exercise_sets"));

            result.get(result.size()-1).addExercise(new Exercise(exerciseName, reps, sets));

            publishProgress(1/count * 100);

            cursor.moveToNext();
        }

        cursor.close();

        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<Workout> workouts) {
        super.onPostExecute(workouts);
        Activity activity = activityWeakReference.get();

        if (activity == null || activity.isFinishing())
            return;

        IHasWorkoutList myActivity = (IHasWorkoutList) activity;

        myActivity.setWorkouts(workouts);

        myActivity.getProgressDialog().dismiss();
    }
}
