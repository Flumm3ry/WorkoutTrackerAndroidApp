package com.example.workouttracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WorkoutDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "workout.db";
    public static final int DATABASE_VERSION = 1;

    public WorkoutDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_WORKOUT_TABLE = "CREATE TABLE workouts (" +
                "workout_id INTEGER PRIMARY KEY," +
                "workout_name TEXT NOT NULL," +
                "workout_time INT" +
                ");";

        final String SQL_CREATE_EXERCISE_TABLE = "CREATE TABLE exercises (" +
                "exercise_id INTEGER PRIMARY KEY," +
                "workout_id INTEGER NOT NULL," +
                "exercise_name TEXT NOT NULL," +
                "exercise_reps INT NOT NULL," +
                "exercise_sets INT NOT NULL" +
                ");";

        final String SQL_CREATE_JOIN_VIEW = "CREATE VIEW workout_exercise " +
                "AS " +
                "SELECT * FROM workouts NATURAL JOIN exercises;";

        sqLiteDatabase.execSQL(SQL_CREATE_WORKOUT_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_EXERCISE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_JOIN_VIEW);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS exercises");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS workouts");
        sqLiteDatabase.execSQL("DROP VIEW IF EXISTS workout_exercise");


        onCreate(sqLiteDatabase);
    }
}
