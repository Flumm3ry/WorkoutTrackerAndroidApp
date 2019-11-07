package com.example.workouttracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AlterWorkoutRowAdapter extends RecyclerView.Adapter<AlterWorkoutRowAdapter.AlterWorkoutViewHolder> {

    public List<Workout> workouts;

    public AlterWorkoutRowAdapter(List<Workout> workouts) {
        this.workouts = workouts;
    }

    /* VIEW HOLDER */
    public static class AlterWorkoutViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private EditDeleteWorkoutListener listener;

        public TextView tvName;
        public Button btnEditWorkout;
        public Button btnDeleteWorkout;

        public AlterWorkoutViewHolder(@NonNull View itemView, EditDeleteWorkoutListener listener) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvEditWorkoutname);
            btnEditWorkout = itemView.findViewById(R.id.btnEditWorkout);
            btnDeleteWorkout = itemView.findViewById(R.id.btnDeleteWorkout);

            this.listener = listener;

            btnEditWorkout.setOnClickListener(this);
            btnDeleteWorkout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnEditWorkout:
                    listener.onEdit(view.getContext(), this.getLayoutPosition());
                    break;
                case R.id.btnDeleteWorkout:
                    createDeletConfirmDialog(view.getContext(), this.getLayoutPosition());
                    break;
                default:
                    break;
            }
        }

        public interface EditDeleteWorkoutListener {
            void onEdit(Context c, int i);
            void onDelete(Context c, int i);
        }

        private void createDeletConfirmDialog(final Context c, final int pos) {
            AlertDialog.Builder alertDlg = new AlertDialog.Builder(c);
            alertDlg.setMessage("Are you sure you want to delete this workout?\nThis cannot be undone.");
            alertDlg.setCancelable(false);

            alertDlg.setPositiveButton("Yes, delete this", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    listener.onDelete(c, pos);
                }
            });

            alertDlg.setNegativeButton("No, I still want this workout", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {}
            });

            alertDlg.create().show();
        }

    }

    @NonNull
    @Override
    public AlterWorkoutViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_delete_workout_row, parent, false);
        return new AlterWorkoutViewHolder(v, new AlterWorkoutViewHolder.EditDeleteWorkoutListener() {
            @Override
            public void onEdit(Context c, int i) {
                Intent intent = new Intent(c, AddEditWorkoutActivity.class);

                intent.putExtra("workout", workouts.get(i));
                intent.putExtra("index", i);

                ((Activity) c).startActivityForResult(intent, 0);
            }

            @Override
            public void onDelete(Context c, int i) {
                //TODO: Add confirmation message first

                DeleteWorkout myTask = new DeleteWorkout(c);
                myTask.execute(workouts.get(i).getId());

                workouts.remove(i);
                notifyItemRemoved(i);
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull AlterWorkoutViewHolder holder, int position) {
        holder.tvName.setText(workouts.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    public class DeleteWorkout extends AsyncTask<Integer, Void, Void> {

        private Context context;

        public DeleteWorkout(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            SQLiteDatabase database = new WorkoutDBHelper(context).getWritableDatabase();

            // delete the existing workout information
            database.delete("exercises", "workout_id = " + integers[0], null);
            database.delete("workouts", "workout_id = " + integers[0], null);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }
}
