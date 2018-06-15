package com.example.glenn.seng_300_project;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.SharedPreferences;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;


public class alertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "ALARM SET",
                Toast.LENGTH_LONG).show();
        // Vibrate the mobile phone
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);

        SharedPreferences preferences = context.getSharedPreferences("MyPref", 0);
        int hour = preferences.getInt("HOUR_TIME_KEY", 0);
        int minute = preferences.getInt("MINUTE_TIME_KEY", 0);
        int interval = preferences.getInt("INTERVAL_TIME_KEY", 15);
        String fileName = preferences.getString("FILENAME_KEY", "no_name.csv");

        final Intent activityDataFetcherIntent = new Intent(context, getClass());
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 4334, activityDataFetcherIntent, 0);

        final AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP, ((1000*60)*(hour + minute)) + (interval*60*1000), pendingIntent);

        minute += interval;

        if(minute >= 60)
        {
            hour += 1;
            minute -= 60;
        }

        try {
            LogTasks.getInstance().updateTheTextView(hour + ":" + minute);
        } catch (Exception e) {

        }

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("HOUR_TIME_KEY", hour);
        editor.putInt("MINUTE_TIME_KEY", minute);

        editor.commit();

    }
}
