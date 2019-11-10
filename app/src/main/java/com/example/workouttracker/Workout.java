package com.example.workouttracker;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Workout implements Parcelable{

    private int id;
    private String name;
    private int time;
    private ArrayList<Exercise> exercises;

    public Workout(String name, int time) {
        this.name = name;
        this.time = time;
        this.exercises = new ArrayList<>();
    }

    public Workout(String name, int time, ArrayList<Exercise> exercises) {
        this.name = name;
        this.time = time;
        this.exercises = exercises;
    }

    public Workout(String name, int time, int id) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.exercises = new ArrayList<>();
    }

    public Workout(Parcel parcel) {
        this.name = parcel.readString();
        this.time = parcel.readInt();
        this.id = parcel.readInt();
        this.exercises = parcel.createTypedArrayList(Exercise.CREATOR);
    }

    public void addExercise(Exercise e) {
        this.exercises.add(e);
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTime() {
        return time;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(time);
        parcel.writeInt(id);
        parcel.writeTypedList(exercises);
    }

    public static final Creator<Workout> CREATOR = new Creator<Workout>() {
        @Override
        public Workout createFromParcel(Parcel in) {
            return new Workout(in);
        }

        @Override
        public Workout[] newArray(int size) {
            return new Workout[size];
        }
    };
}
