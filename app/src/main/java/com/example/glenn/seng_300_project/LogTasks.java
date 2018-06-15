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
    private String CSVfileName;
    private File csvFile;
    private static LogTasks instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_tasks);
        instance = this;

        Intent intent = getIntent();
        if(intent.getExtras() == null) {
            hour = 0;
            minute = 0;
            interval = 0;
        }
        else
        {
            Bundle extras = getIntent().getExtras();
            hour = extras.getInt("HOUR_KEY");
            minute = extras.getInt("MINUTE_KEY");
            interval = extras.getInt("FREQUENCY_KEY");
        }
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        setContentView(R.layout.activity_log_tasks);
        final LogTask logTask = new LogTask(); // new instance of logTask class

        lvTaskItems = (ListView)findViewById(R.id.list_view);

        //for test purposes
        lvTaskItems = (ListView)findViewById(R.id.list_view);

        mTaskItems = new ArrayList<>(); // set new array of task times in unspecified type

        c.add(Calendar.MINUTE, interval);
        while(c.before(Calendar.getInstance()))
        {

            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
            if(hour <= 12) {
                if (minute == 0)
                    mTaskItems.add(new TaskInterval(hour + ":0" + minute + "AM", Integer.toString(interval), ""));
                else
                    mTaskItems.add(new TaskInterval(hour + ":" + minute + "AM", Integer.toString(interval), ""));
            }
            else {
                if (minute == 0)
                    mTaskItems.add(new TaskInterval(hour - 12 + ":0" + minute + "PM", Integer.toString(interval), ""));
                else
                    mTaskItems.add(new TaskInterval(hour - 12 + ":" + minute + "PM", Integer.toString(interval), ""));
            }
            c.add(Calendar.MINUTE, interval);
        }

        adapter = new TaskItemsAdapter(getApplicationContext(), mTaskItems);
        lvTaskItems.setAdapter(adapter);

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
        CSVfileName = filename;
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

        startAlarm(c);
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
        Uri uri = Uri.fromFile(attachment);

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

    private void startAlarm(Calendar c)
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, alertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, intent, Intent.FILL_IN_DATA);

        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        TextView timeView = (TextView) findViewById(R.id.timeOfNext);
        if(hour > 12) {
            if (minute == 0)
                timeView.setText(hour - 12 + ":0" + minute + "PM");
            else
                timeView.setText(hour - 12 + ":" + minute + "PM");
        }
        else {
            if (minute == 0)
                timeView.setText(hour - 12 + ":0" + minute + "AM");
            else
                timeView.setText(hour - 12 + ":" + minute + "+AM");
        }

        SharedPreferences preferences = this.getSharedPreferences("MyPref", 0);
        final SharedPreferences.Editor editor = preferences.edit();

        editor.putInt("HOUR_TIME_KEY", hour).apply();
        editor.putInt("MINUTE_TIME_KEY", minute).apply();
        editor.putInt("INTERVAL_TIME_KEY", interval).apply();
        editor.putString("FILENAME_KEY", CSVfileName).apply();

        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis() + (interval*60*1000), pendingIntent);
    }

    private void cancelAlarm()
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, alertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.cancel(pendingIntent);
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
    public static LogTasks getInstance() {
        return instance;
    }

    public void updateTheTextView(final String t)
    {
        LogTasks.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textView = (TextView) findViewById(R.id.timeOfNext);
                textView.setText(t);
            }
        });
    }
}

