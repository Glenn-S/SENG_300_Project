package com.example.glenn.seng_300_project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class LogTasks extends AppCompatActivity {
    private ListView lvTaskItems;
    private TaskItemsAdapter adapter;
    private ArrayList<TaskList> mTaskItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_tasks);
        LogTask logTask = new LogTask(); // new instance of logTask class

        lvTaskItems = (ListView)findViewById(R.id.list_view);
        // values passed from the setFrequency page to establish the time and frequency
/*        String startTime = getIntent().getStringExtra("TIME_KEY");
        String frequency = getIntent().getStringExtra("FREQUENCY_KEY");
        TextView timeofNext = (TextView) findViewById(R.id.timeOfNext);
        timeofNext.setText(logTask.displayNextInterval(mTaskItems, startTime, frequency));
*/
        // on creating the screen, the mTaskItems will be unpopulated
        // call getFile even though because it should be empty


        //for test purposes
        mTaskItems = new ArrayList<>(); // set new array of task times in unspecified type
       /* CSVManager CSV = new CSVManager("");
        try{
            mTaskItems = CSV.readTaskList();
        };*/

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
                Intent intent = new Intent(LogTasks.this, Pop.class);
                // pass the time string to the pop class for saving the selected value
                intent.putExtra("time", mTaskItems.get(position).getTime());
                startActivity(intent);

                // get background colour to not change back after the screen is moved up or down
                //view.setBackgroundColor(Color.parseColor("#3174C6"));

            }
        });
    }
    //NOTES FOR IMPLEMENTING THE TIMER WHEN IT IS AVAILABLE
    // when the onAlarmListener is added, add the update of the log task list into it
    // also the time to next interval text will be linked to it as well
    // also get and set file will be called every interval when adding the new empty tas
    // use dipl
}
