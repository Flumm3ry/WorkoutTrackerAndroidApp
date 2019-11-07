package com.example.workouttracker;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditExerciseFragment extends Fragment {
    private EditExerciseFragmentListener listener;
    private EditText etExerciseName;
    private EditText etReps;
    private EditText etSets;

    private String exerciseName;
    private int reps;
    private int sets;

    public EditExerciseFragment() {
        // Required empty public constructor
    }

    public EditExerciseFragment(String name, int reps, int sets) {
        this.exerciseName = name;
        this.reps = reps;
        this.sets = sets;
    }

    public interface EditExerciseFragmentListener {
        void onDeleteFragmentPressed(Fragment fragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit_exercise, container, false);

        etExerciseName = v.findViewById(R.id.etExerciseName);
        etReps = v.findViewById(R.id.etReps);
        etSets = v.findViewById(R.id.etSets);

        if (!exerciseName.isEmpty())
        {
            etExerciseName.setText(exerciseName);
            etReps.setText(Integer.toString(reps));
            etSets.setText(Integer.toString(sets));
        }

        Button btnDelete = v.findViewById(R.id.btnDeleteExercise);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteBtnClicked();
            }
        });

        return v;
    }

    private void deleteBtnClicked() {
        listener.onDeleteFragmentPressed(this);
    }

    public boolean checkIfInputValid() {
        if (etExerciseName.getText().toString().isEmpty() || etReps.getText().toString().isEmpty() || etSets.getText().toString().isEmpty())
            return false;
        return true;
    }

    public Exercise getExerciseFromFragment() {
        return new Exercise(etExerciseName.getText().toString(),
                Integer.parseInt(etReps.getText().toString()),
                Integer.parseInt(etSets.getText().toString()));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // if the activity implements this interface
        if (context instanceof EditExerciseFragmentListener) {
            listener = (EditExerciseFragmentListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
            + " must implement EditExerciseFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // remove the reference to the activity
        listener = null;
    }
}
