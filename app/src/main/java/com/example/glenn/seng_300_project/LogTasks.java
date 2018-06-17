package com.example.glenn.seng_300_project;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LogTasks extends NavigationBaseActivity {

    final static int emailCode = 0;

    private ListView lvTaskItems;
    private TaskItemsAdapter adapter;
    private List<TaskInterval> mTaskItems;
    private int hour, minute, interval;
    private File csvFile;
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

        //This is temporary
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        hour = 12;
        minute = 00;
        TextView timeOfNext = (TextView) findViewById(R.id.timeOfNext);
        timeOfNext.setText(hour + ":" + minute);

        setContentView(R.layout.activity_log_tasks);
        final LogTask logTask = new LogTask(); // new instance of logTask class

        lvTaskItems = (ListView)findViewById(R.id.list_view);

        //for test purposes
        lvTaskItems = (ListView)findViewById(R.id.list_view);

        mTaskItems = new ArrayList<>(); // set new array of task times in unspecified type

        // Test values
        // CSV manager need the duration as well
        mTaskItems.add(new TaskInterval("9am", "60", "Coding"));
        mTaskItems.add(new TaskInterval("10am", "60", "Read Email"));
        mTaskItems.add(new TaskInterval("11am", "60", "Meetings"));
        mTaskItems.add(new TaskInterval("12am", "60", "At Lunch"));
        mTaskItems.add(new TaskInterval("1pm", "60", "Refactored"));
        mTaskItems.add(new TaskInterval("2pm", "60", "Integrated Parts"));
        mTaskItems.add(new TaskInterval("3pm", "60", "Coding"));
        mTaskItems.add(new TaskInterval("4pm", "60", "Edited Document"));
        mTaskItems.add(new TaskInterval("5pm", "60", ""));

        //Init adapter
        adapter = new TaskItemsAdapter(getApplicationContext(), mTaskItems);
        lvTaskItems.setAdapter(adapter);

        // create method for setting using the view.setBack. based on if task string is empty or not
        //startAlarm(c);


        // call the pop up to select a task
        lvTaskItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LogTasks.this, Pop.class);
                intent.putExtra("interval", mTaskItems.get(position).durationInMin);
                intent.putExtra("time", mTaskItems.get(position).startTime);
                intent.putExtra("filename", csvFile.getPath());
                intent.putExtra("intervalPosition", position);
                startActivity(intent);
                //view.setBackgroundColor(Color.parseColor("#3174C6"));
            }
        });

        //Get user information to create the filename
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        final String firstName = pref.getString("FIRST_KEY", "no first name");
        final String lastName = pref.getString("LAST_KEY", "no last name");
        final String email = pref.getString("EMAIL_KEY", "no last name");

        //Get date in YYYY-MM-DD format to append as part of the filename
        Date date = new Date();
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

        final String nameDate = lastName + "_" + firstName + "_" + formattedDate;

        final String filename = nameDate + ".csv";
        final File filePath = Environment.getExternalStorageDirectory();
        csvFile = new File(filePath, filename);

        //Create directory if it doesn't already exist
        filePath.mkdir();
        try{
            //Create a new csv file and email it
            csvFile.createNewFile();
            CSVManager csvManager = new CSVManager(csvFile.getPath());
            csvManager.writeTaskList(mTaskItems, LogTasks.this);
        }catch (IOException e){
            Log.e("LogTasks", "File could not be created");
        }

        FloatingActionButton fab = findViewById(R.id.sendMail);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CSVManager csvManager = new CSVManager(csvFile.getPath());

                verifyStoragePermissions(LogTasks.this); //get permission to use file storage
                try{
                    //Delete any previous files with the same name
                    csvFile.delete();

                    //Open a csv file and write the list of tasks to the csv file
                    csvFile.createNewFile();
                    csvManager.writeTaskList(mTaskItems, LogTasks.this);

                    sendEmailWithOtherApp(LogTasks.this, email, nameDate, "", csvFile.getPath());

                }
                catch(IOException e){
                    Toast.makeText(LogTasks.this, "Could not create file.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();

        //After the user selects a task in the pop activity, the task will be saved into the file
        //We need to retrieve the contents of the file to populate the list for user to see
        Log.e("FilePath onResume", csvFile.getPath());
        CSVManager csvManager = new CSVManager(csvFile.getPath());

        //Create a backup of the list in case we cannot populate it after clearing it
        ArrayList mTaskItemsBackup = new ArrayList(mTaskItems);

        try{
            mTaskItems.clear();
            mTaskItems.addAll(csvManager.readTaskList());
        }
        catch(IOException e){
            mTaskItems.addAll(mTaskItemsBackup);
        }
        finally {
            adapter.notifyDataSetChanged();
        }


    }

    /**
     * Send an email using a client of the user's choice
     * @param context - Activity's context
     * @param recipientEmail
     * @param subject - Subject line
     * @param body - Body of the email
     * @param filePath -Filepath to an attachment
     */
    public static void sendEmailWithOtherApp(Activity context, String recipientEmail, String subject, String body, String filePath){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipientEmail}); // Set to: field
        intent.putExtra(Intent.EXTRA_SUBJECT, subject); //Set subject line
        intent.putExtra(Intent.EXTRA_TEXT, body); //Write body of the email

        File attachment = new File(filePath);

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".my.package.name.provider", attachment);

        intent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivityForResult(Intent.createChooser(intent, "Send mail"),emailCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == emailCode && resultCode == RESULT_OK){
            Intent intent = new Intent(LogTasks.this, StartActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /*
    For timer interrupt (things to be done inside)
    read from CSV file to get current list
    List.add(new TaskInterval(timeOfInterval, "");
    write the list back to the file
    Update Time of Next Interval
    update the view
     */



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

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage and prompts the user if they do not
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        if (permission2 != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}

