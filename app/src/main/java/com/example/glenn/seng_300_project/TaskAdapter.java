package com.example.glenn.seng_300_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    //private List<String> taskList;
    private SortedList<String> taskList;
    private Context context;
    private TaskManager taskManager;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView;
        public ImageButton mDeleteButton;

        public ViewHolder(View v){
            super(v);
            mTextView = (TextView) v.findViewById(R.id.task_name);
            mDeleteButton = (ImageButton) v.findViewById(R.id.delete_button);
        }

    }

    public TaskAdapter(Context context, TaskManager tm){
        this.context = context;
        this.taskManager = tm;

        taskList = new SortedList<>(String.class, new SortedListAdapterCallback<String>(this) {
            @Override
            public int compare(String s, String t21) {
                return s.compareToIgnoreCase(t21);
            }

            @Override
            public boolean areContentsTheSame(String s, String t21) {
                return s.equalsIgnoreCase(t21);
            }

            @Override
            public boolean areItemsTheSame(String s, String t21) {
                return s.equalsIgnoreCase(t21);
            }
        });

        taskList.addAll(taskManager.getTaskList());

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = (View) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        final TextView taskTextView = viewHolder.mTextView;

        //Replace viewHolder content with data at position i in dataset
        taskTextView.setText(taskList.get(position));

        //Short click allows user to edit task name
        taskTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Modify task name:");

                //Text box to modify task name
                final EditText editText = new EditText(context);
                editText.setText(taskTextView.getText().toString());

                //Attach edit text box to pop-up
                builder.setView(editText);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String modifiedTask = editText.getText().toString();
                        if(!modifiedTask.isEmpty()){
                            updateItemAt(position, modifiedTask);

                            //Removes item from the list
                            notifyItemChanged(position);
                            //Remaps the position of the remaining items
                            notifyDataSetChanged();
                        }
                        else{
                            //Empty string should result in deletion of the task
                            removeItemAt(position);

                            //Removes item from the list
                            notifyItemRemoved(position);
                            //Remaps the position of the remaining items
                            notifyDataSetChanged();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                //Make keyboard appear automatically
                AlertDialog editTaskDialog = builder.create();
                editTaskDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                editTaskDialog.show();


            }
        });

        //Click on delete icon removes the item from the list
        final ImageButton deleteButton = viewHolder.mDeleteButton;
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItemAt(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();

            }
        });


    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    // region PageList Helpers
    public String get(int position) {
        return taskList.get(position);
    }

    public int add(String item) {
        taskManager.addTask(item);
        return taskList.add(item);
    }

    public int indexOf(String item) {
        return taskList.indexOf(item);
    }

    public void updateItemAt(int index, String item) {
        taskManager.editTask(taskList.get(index),item);
        taskList.updateItemAt(index, item);
    }

    public void addAll(List<String> items) {
        taskList.beginBatchedUpdates();
        for (String item : items) {
            taskList.add(item);
        }
        taskList.endBatchedUpdates();
        notifyDataSetChanged();
    }

    public void addAll(String[] items) {
        addAll(Arrays.asList(items));
    }

    public boolean remove(String item) {
        return taskList.remove(item);
    }

    public String removeItemAt(int index) {
        taskManager.deleteTask(taskList.get(index));
        return taskList.removeItemAt(index);
    }

    public void clear() {
        taskList.beginBatchedUpdates();
        //remove items at end, to avoid unnecessary array shifting
        while (taskList.size() > 0) {
            taskList.removeItemAt(taskList.size() - 1);
        }
        taskList.endBatchedUpdates();
    }



}
