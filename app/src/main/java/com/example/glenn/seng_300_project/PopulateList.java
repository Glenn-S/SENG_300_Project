package com.example.glenn.seng_300_project;

import android.content.Context;
import android.content.Intent;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class PopulateList extends LogTasks {
    public ListView lvTaskItems;
    public List<TaskInterval> mTaskItems;
    public TaskItemsAdapter adapter;
    private Context context;

    public PopulateList (Context context, ListView lv){
        this.context = context;
        this.lvTaskItems = lv;
    }

    public void setupList(){
        //lvTaskItems = (ListView)findViewById(R.id.list_view);
        this.mTaskItems = new ArrayList<>(); // set new array of task times in unspecified type
        this.adapter = new TaskItemsAdapter(context, this.mTaskItems); // something to do with the context
        this.lvTaskItems.setAdapter(this.adapter);

        // Test values
        // CSV manager need the duration as well
        this.mTaskItems.add(new TaskInterval("9am", "60", "Coding"));
        this.mTaskItems.add(new TaskInterval("10am", "60", "Read Email"));
        this.mTaskItems.add(new TaskInterval("11am", "60", "Meetings"));
        this.mTaskItems.add(new TaskInterval("12am", "60", "At Lunch"));
        this.mTaskItems.add(new TaskInterval("1pm", "60", "Refactored"));
        this.mTaskItems.add(new TaskInterval("2pm", "60", "Integrated Parts"));
        this.mTaskItems.add(new TaskInterval("3pm", "60", "Coding"));
        this.mTaskItems.add(new TaskInterval("4pm", "60", "Edited Document"));
        this.mTaskItems.add(new TaskInterval("5pm", "60", ""));
    }

    public void passIntents(Intent intent, int position, UserDetails userDetails) {
        intent.putExtra("interval", this.mTaskItems.get(position).durationInMin);
        intent.putExtra("time", this.mTaskItems.get(position).startTime);
        intent.putExtra("filename", userDetails.csvFile.getPath());
        intent.putExtra("intervalPosition", position);

    }
}
