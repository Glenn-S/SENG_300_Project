package com.example.glenn.seng_300_project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class LogTasks extends AppCompatActivity {
    private ListView lvTaskItems;
    private TaskItemsAdapter adapter;
    private List<TaskList> mTaskItems;
    public static final int POP_WINDOW_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_tasks);

        lvTaskItems = (ListView)findViewById(R.id.list_view);


        mTaskItems = new ArrayList<>(); // set new array of task times in unspecified type
        mTaskItems.add(new TaskList("9am", "Coded"));
        mTaskItems.add(new TaskList("10am", "Read Email"));
        mTaskItems.add(new TaskList("11am", "Meetings"));
        mTaskItems.add(new TaskList("12am", "Read Emails"));
        mTaskItems.add(new TaskList("1pm", "Refactored"));
        mTaskItems.add(new TaskList("2pm", ""));
        mTaskItems.add(new TaskList("3pm", "At Lunch"));
        mTaskItems.add(new TaskList("4pm", "Edited Document"));
        mTaskItems.add(new TaskList("5pm", "Talked with Coworker"));

        //Init adapter
        adapter = new TaskItemsAdapter(getApplicationContext(), mTaskItems);
        lvTaskItems.setAdapter(adapter);

        // create method for setting using the view.setBack. based on if task string is empty or not

        lvTaskItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(LogTasks.this, Pop.class));
                view.setBackgroundColor(Color.parseColor("#3174C6"));
            }
        });
    }

}
