package com.example.glenn.seng_300_project;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pop extends Activity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private FloatingActionButton addBtn;
    private TaskAdapter mAdapter;
    private TaskManager mTaskManager;
    private List<String> customTaskList;
    private String timeValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the window properties
        setContentView(R.layout.popwindow);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*0.7), (int)(height*0.5));
        // create instance of CSV manager to write to and from the file
        //final CSVManager CSV= new CSVManager();

        // receive the intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            timeValue = extras.getString("time");
            //The key argument here must match that used in the other activity
        }

        // populate the list view
        TaskManager mTasks = new TaskManager(Pop.this);
        customTaskList = mTasks.getTaskList();
        CSVManager CSV = new CSVManager(""); // pick a name for the CSV file that is common

        // list of tasks to populate list with
        listView = (ListView)findViewById(R.id.customTasksList);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, customTaskList);
        listView.setAdapter(adapter);

        // get value of extra passed from previous activity, this contains the value being passed
        final String tasksTime = getIntent().getStringExtra("time");
        TextView tv = (TextView) findViewById(R.id.tvTime);
        tv.setText(tasksTime);

        mTaskManager = new TaskManager(this);
        mTaskManager.openDB();
        mAdapter = new TaskAdapter(this, mTaskManager);

        //Button to add new task
        addBtn = (FloatingActionButton) findViewById(R.id.fabAddTask);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Alert Dialog for entering a new task
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Pop.this);
                dialogBuilder.setTitle("Enter a short task name:");

                //Edit text accept the name of the new task
                final EditText input = new EditText(Pop.this);
                input.setHint("New Task");

                input.setInputType(InputType.TYPE_CLASS_TEXT);
                dialogBuilder.setView(input);

                //User Clicks "OK", add new task to list
                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // when ok, update the list and database
                        // need to call add task

                        mAdapter.add(input.getText().toString());
                        mAdapter.notifyItemInserted(mAdapter.getItemCount() - 1);
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(Pop.this, "Added Task", Toast.LENGTH_SHORT).show();

                        // Close after adding
                        finish();
                    }
                });

                //User clicks "Cancel", change nothing
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                //Make keyboard appear automatically
                AlertDialog addTaskDialog = dialogBuilder.create();
                addTaskDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                addTaskDialog.show();

            }
        });
        // for adding a new item to the log of tasks
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // need to write the log task item
                // call setFile to write this item into the list
                // value passed from intent (time) will be used here with the string at position to write to the file
                finish();
                // views will need to be updated to have the latest item
            }

        });

    }
    /**
     * This class provides a workaround for a threading error in RecycleView.
     * https://stackoverflow.com/questions/31759171/recyclerview-and-java-lang-indexoutofboundsexception-inconsistency-detected-in
     */
    public class WrappedLinearLayoutManager extends LinearLayoutManager{

        public WrappedLinearLayoutManager(Context context){
            super(context);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state){
            try{
                super.onLayoutChildren(recycler,state);
            }
            catch(IndexOutOfBoundsException e){
                Log.e("LinearLayoutManager: ", "Catching threading error in RecycleView");
            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mTaskManager.closeDB();
    }
}

/*

        // for test purposes
        //customTaskList.addAll(Arrays.asList("Coding", "Meeting", "Coffee Break", "Checked Emails", "Worked on the big project for Encana that is due at the end of the quarter", "Ran Errands"));

 */