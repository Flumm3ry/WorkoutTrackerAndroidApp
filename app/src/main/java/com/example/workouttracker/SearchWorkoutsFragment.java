package com.example.workouttracker;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class SearchWorkoutsFragment extends Fragment implements IHasWorkoutList{

    private ArrayList<Workout> workouts = new ArrayList<>();

    private RecyclerView rv;
    private AutoCompleteTextView actvSearchBar;

    public SearchWorkoutsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_workouts, container, false);

        rv = v.findViewById(R.id.rvSearchResults);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(new WorkoutViewAdapter(new ArrayList<Workout>()));

        actvSearchBar = v.findViewById(R.id.actvSearchBar);

        actvSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                UpdateResults(String.valueOf(charSequence));
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        return v;
    }

    private void UpdateResults(String searchTerm) {
        UpdateRecyclerView updateRecyclerView = new UpdateRecyclerView(this);
        updateRecyclerView.execute(searchTerm);
    }

    @Override
    public void setWorkouts(ArrayList<Workout> workouts) {
        this.workouts = workouts;
        SetupAutocomplete setupAutocomplete = new SetupAutocomplete(this);
        setupAutocomplete.execute(workouts);
    }

    @Override
    public ProgressDialog getProgressDialog() {
        return null;
    }

    @Override
    public void setProgressDialog(ProgressDialog progressDialog) {

    }

    public static class SetupAutocomplete extends AsyncTask<ArrayList<Workout>, Void, String[]> {

        private WeakReference<SearchWorkoutsFragment> fragmentWeakReference;

        public SetupAutocomplete(SearchWorkoutsFragment fragmentWeakReference) {
            this.fragmentWeakReference = new WeakReference<>(fragmentWeakReference);
        }

        @Override
        protected String[] doInBackground(ArrayList<Workout>... arrayLists) {
            String[] array = new String[arrayLists[0].size()];

            for (int i = 0; i < arrayLists[0].size(); i++) {
                array[i] = arrayLists[0].get(i).getName();
            }

            return array;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);

            SearchWorkoutsFragment fragment = fragmentWeakReference.get();

            ArrayAdapter<String> adapter = new ArrayAdapter<>(fragment.getContext(),
                    android.R.layout.simple_list_item_1, strings);

            fragment.actvSearchBar.setAdapter(adapter);
        }
    }

    public class UpdateRecyclerView extends AsyncTask<String, Void, ArrayList<Workout>> {

        private WeakReference<SearchWorkoutsFragment> fragmentWeakReference;

        public UpdateRecyclerView(SearchWorkoutsFragment fragmentWeakReference) {
            this.fragmentWeakReference = new WeakReference<>(fragmentWeakReference);
        }

        @Override
        protected ArrayList<Workout> doInBackground(String... strings) {

            ArrayList<Workout> result = new ArrayList<>();

            if (strings[0].isEmpty())
                return result;

            for (Workout workout: workouts) {
                if (workout.getName().toLowerCase().contains(strings[0].toLowerCase()))
                    result.add(workout);
            }

            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<Workout> workouts) {
            super.onPostExecute(workouts);

            SearchWorkoutsFragment fragment = fragmentWeakReference.get();

            ((WorkoutViewAdapter) fragment.rv.getAdapter()).workouts = workouts;

            fragment.rv.getAdapter().notifyDataSetChanged();
        }
    }
}
