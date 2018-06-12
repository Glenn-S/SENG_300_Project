package com.example.glenn.seng_300_project;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
    // set up the add task button
    private FloatingActionButton addBtn;
    private TaskAdapter mAdapter;
    private TaskManager mTaskManager;


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

        // populate the list view
        final TaskManager mTasks = new TaskManager(Pop.this);
        final List<String> customTaskList;
        //customTaskList = new ArrayList<String>();
        customTaskList = mTasks.getTaskList();
        CSVManager CSV = new CSVManager(""); // pick a name for the CSV file that is common
        //task list for test
        //customTaskList.addAll(Arrays.asList("Coding", "Meeting", "Coffee Break", "Checked Emails", "Worked on the big project for Encana that is due at the end of the quarter", "Ran Errands"));

        // set button and list properties
        addBtn = (FloatingActionButton) findViewById(R.id.fabAddTask);
        listView = (ListView)findViewById(R.id.customTasksList);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, customTaskList);
        listView.setAdapter(adapter);

        // get value of extra passed from previous activity, this contains the value being passed
        final String tasksTime = getIntent().getStringExtra("time");
        TextView tv = (TextView) findViewById(R.id.textView3);
        tv.setText(tasksTime);

        // for test purposes
        customTaskList.addAll(Arrays.asList("Coding", "Meeting", "Coffee Break", "Checked Emails", "Worked on the big project for Encana that is due at the end of the quarter", "Ran Errands"));

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

        // previous onClick for add button
        // to add a new task to the tasks list
        /*addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call alert dialogue


                listView.invalidateViews();
                Toast.makeText(Pop.this, "Added Task", Toast.LENGTH_SHORT).show();
                //should this window close after the user has entered their new task?
                finish();
            }
        });*/

        mTaskManager = new TaskManager(this);
        mAdapter = new TaskAdapter(this, mTaskManager);


        // there is an error in here after the user presses ok
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

                //listView.invalidateViews();
                //Toast.makeText(Pop.this, "Added Task", Toast.LENGTH_SHORT).show();
                //should this window close after the user has entered their new task?
                //finish();
            }
        });
    }
}
