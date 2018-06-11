package com.example.glenn.seng_300_project;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
<<<<<<< HEAD
import android.support.design.widget.FloatingActionButton;
=======
>>>>>>> joseph
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
<<<<<<< HEAD
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;
=======
import android.widget.ListView;
>>>>>>> joseph

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
<<<<<<< HEAD
import java.util.List;
=======
>>>>>>> joseph

public class Pop extends Activity {

    ListView listView;
    ArrayAdapter<String> adapter;
<<<<<<< HEAD
    // set up the add task button
    FloatingActionButton addBtn;

=======
>>>>>>> joseph

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< HEAD
        //set the window properties
=======
>>>>>>> joseph
        setContentView(R.layout.popwindow);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*0.7), (int)(height*0.5));

<<<<<<< HEAD

        // populate the list view
        TaskManager mTasks = new TaskManager(Pop.this);
        //for test purposes
        //final List<String> customTaskList = new ArrayList<String>();
        final List<String> customTaskList;
        //customTaskList = new ArrayList<String>();
        customTaskList = mTasks.getTaskList();

        //task list for test
        //customTaskList.addAll(Arrays.asList("Coding", "Meeting", "Coffee Break", "Checked Emails", "Worked on the big project for Encana that is due at the end of the quarter", "Ran Errands"));

        // set button and list properties
        addBtn = (FloatingActionButton) findViewById(R.id.fabAddTask);
=======
        // populate the list view
        ArrayList<String> customTaskList = new ArrayList<String>();
        customTaskList.addAll(Arrays.asList("Coding",
                                            "Meeting",
                                            "Coffee Break",
                                            "Checked Emails",
                                            "Worked on the big project for Encana that is due at the end of the quarter",
                                            "Ran Errands"));

>>>>>>> joseph
        listView = (ListView)findViewById(R.id.customTasksList);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, customTaskList);
        listView.setAdapter(adapter);

<<<<<<< HEAD
        // get value of extra passed from previous activity, this contains the value being passed
        final String tasksTime = getIntent().getStringExtra("time");
        TextView tv = (TextView) findViewById(R.id.textView3);
        tv.setText(tasksTime);

        customTaskList.addAll(Arrays.asList("Coding", "Meeting", "Coffee Break", "Checked Emails", "Worked on the big project for Encana that is due at the end of the quarter", "Ran Errands"));

        // for adding a new item to the log of tasks
=======
>>>>>>> joseph
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
<<<<<<< HEAD
                //setFile(customTaskList.get(position)); // this order may not work
                finish();
            }

        });

        // to add a new task to the tasks list
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // got to the add task menu to create a new one
                customTaskList.add("added task");
                listView.invalidateViews();
                Toast.makeText(Pop.this, "Added Task", Toast.LENGTH_SHORT).show();
                //finish();
            }
=======
                finish();
            }
>>>>>>> joseph
        });
    }
}
