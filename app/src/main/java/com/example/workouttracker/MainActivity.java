package com.example.workouttracker;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // use the custom toolbar as the replacement for the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (!databaseExists("workout.db")) {
            performFirstTimeSetup myTask = new performFirstTimeSetup(this);
            myTask.execute();
        }
    }

    public void FindAWorkout(View view) {
        Intent intent = new Intent(MainActivity.this, TabbedActivity.class);
        startActivity(intent);
    }

    public void AddAWorkout(View view) {
        Intent intent = new Intent(MainActivity.this, AddEditWorkoutActivity.class);
        startActivity(intent);
    }

    public void EditWorkouts(View view) {
        Intent intent = new Intent(MainActivity.this, ChooseEditWorkoutActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        createDialog();
    }

    private boolean databaseExists(String databaseName) {
        return getDatabasePath(databaseName).exists();
    }

    private void createDialog() {
        AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
        alertDlg.setMessage("Are you sure you want to exit the app?");
        alertDlg.setCancelable(false);

        alertDlg.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.super.onBackPressed();
            }
        });

        alertDlg.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        alertDlg.create().show();
    }

    public static class performFirstTimeSetup extends AsyncTask<Void, Void, Void> {

        private WeakReference<MainActivity> activityWeakReference;

        public performFirstTimeSetup(MainActivity activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing())
                return;

            activity.dialog = new ProgressDialog(activity);
            activity.dialog.setMessage(activity.getString(R.string.setup_message));
            activity.dialog.setCancelable(false);
            activity.dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            MainActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing())
                return null;

            new WorkoutDBHelper(activity).getReadableDatabase();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            MainActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing())
                return;

            activity.dialog.dismiss();
        }
    }
}
