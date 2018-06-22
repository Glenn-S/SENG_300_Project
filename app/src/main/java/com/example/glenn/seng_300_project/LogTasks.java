package com.example.glenn.seng_300_project;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class LogTasks extends NavigationBaseActivity {
    final static int emailCode = 0;
    private PopulateList populateList;
    private UserDetails userDetails;
    private SetTime setTime;
   // private int hour, minute, interval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_tasks);

        setTime = new SetTime(getIntent());
        setTime.setDisplay();
        TextView timeOfNext = (TextView) findViewById(R.id.timeOfNext);
        //this is temporary
        timeOfNext.setText(setTime.hour + ":" + setTime.minute);
        setContentView(R.layout.activity_log_tasks);

        // initialize the adapter and populate the list
        populateList = new PopulateList(getApplicationContext(), (ListView)findViewById(R.id.list_view));
        populateList.setupList();
        // save the user details in database
        userDetails = new UserDetails(getApplicationContext());
        userDetails.storeDetails();

        // call the pop up to select a task
        populateList.lvTaskItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LogTasks.this, Pop.class);
                populateList.passIntents(intent, position, userDetails);
                startActivity(intent);
            }
        });
        //Get user information to create the filename
        userDetails.createFile(populateList.mTaskItems);

        FloatingActionButton fab = findViewById(R.id.sendMail);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Permissions permissions = new Permissions(userDetails, populateList, LogTasks.this);
                permissions.createPermission();
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();

        //After the user selects a task in the pop activity, the task will be saved into the file
        //We need to retrieve the contents of the file to populate the list for user to see
        Log.e("FilePath onResume", userDetails.csvFile.getPath());
        CSVManager csvManager = new CSVManager(userDetails.csvFile.getPath());

        //Create a backup of the list in case we cannot populate it after clearing it
        ArrayList mTaskItemsBackup = new ArrayList(populateList.mTaskItems);

        try{
            populateList.mTaskItems.clear();
            populateList.mTaskItems.addAll(csvManager.readTaskList());
        }
        catch(IOException e){
            populateList.mTaskItems.addAll(mTaskItemsBackup);
        }
        finally {
            populateList.adapter.notifyDataSetChanged();
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
        c.add(Calendar.MINUTE, setTime.interval);
        while(c.before(Calendar.getInstance()))
        {
            // add createNewTaskLog() method
            c.add(Calendar.MINUTE, setTime.interval);
        }

        setTime.hour = c.get(Calendar.HOUR_OF_DAY);
        setTime.minute = c.get(Calendar.MINUTE);
        TextView timeView = (TextView) findViewById(R.id.timeOfNext);
        timeView.setText(setTime.hour + ":" + setTime.minute);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 1000*60*setTime.interval, pendingIntent);

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
            timeView.setText(setTime.hour + ":" + setTime.minute);
            setTime.hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            setTime.minute = Calendar.getInstance().get(Calendar.MINUTE);

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

