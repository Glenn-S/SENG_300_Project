package com.example.glenn.seng_300_project;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LogTasks extends Activity {
    private ListView lvTaskItems;
    private TaskItemsAdapter adapter;
    private List<TaskList> mTaskItems;
    public static final int POP_WINDOW_REQUEST_CODE = 0;

    private int hour, minute, interval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_tasks);

        Intent intent = getIntent();

        if(intent.getExtras() == null) {
            hour = 0;
            minute = 0;
        }
        else
        {
            Bundle extras = getIntent().getExtras();
            hour = extras.getInt("HOUR_KEY");
            minute = extras.getInt("MINUTE_KEY");
            interval = extras.getInt("INTERVAL_KEY");
        }
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        setContentView(R.layout.activity_log_tasks);
        LogTask logTask = new LogTask(); // new instance of logTask class

        lvTaskItems = (ListView)findViewById(R.id.list_view);

        //for test purposes
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
        startAlarm(c);

        lvTaskItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LogTasks.this, Pop.class);
                // pass the time string to the pop class
                intent.putExtra("time", mTaskItems.get(position).getTime());
                startActivity(intent);
                //view.setBackgroundColor(Color.parseColor("#3174C6"));
                startActivity(new Intent(LogTasks.this, Pop.class));
                view.setBackgroundColor(Color.parseColor("#3174C6"));
            }
        });
    }

    private void startAlarm(Calendar c)
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        // This block checks for if the user chooses an earlier time and add the blocks accordingly.
        c.add(Calendar.MINUTE, interval);
        while(c.before(Calendar.getInstance()))
        {
            // add createNewTaskLog() method
            c.add(Calendar.MINUTE, interval);
        }

        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        TextView timeView = (TextView) findViewById(R.id.timeOfNext);
        timeView.setText(hour + ":" + minute);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 1000*60*interval, pendingIntent);

    }

    private void cancelAlarm()
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.cancel(pendingIntent);
    }

    public class AlertReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Implement adding new task block, updating time, pop up task methods.
            TextView timeView = (TextView) findViewById(R.id.timeOfNext);
            timeView.setText(hour + ":" + minute);
            hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            minute = Calendar.getInstance().get(Calendar.MINUTE);

        }
    }
}

