package com.example.glenn.seng_300_project;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.widget.TextView;
import android.widget.Toast;


public class alertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "ALARM SET",
                Toast.LENGTH_LONG).show();
        // Vibrate the mobile phone
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);

        int i = intent.getIntExtra("I_KEY", 1);
        i = 5;

        SharedPreferences preferences = context.getSharedPreferences("MyPref", 0);
        int currentCount = preferences.getInt("KEY_COUNT", 0);

        final Intent activityDataFetcherIntent = new Intent(context, getClass());
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 4334, activityDataFetcherIntent, 0);

        final AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (i * 1000), pendingIntent);

        currentCount++;

        try {
            LogTasks.getInstance().updateTheTextView(Integer.toString(currentCount));
        } catch (Exception e) {

        }

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("KEY_COUNT", currentCount);
        editor.commit();

    }
}
