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

    private List<TaskInterval> newTaskItems;
    private LogTasks logTasks = new LogTasks();

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "TASK READY",
                Toast.LENGTH_LONG).show();
        // Vibrate the mobile phone
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);

        SharedPreferences preferences = context.getSharedPreferences("MyPref", 0);
        int hour = preferences.getInt("HOUR_TIME_KEY", 0);
        int minute = preferences.getInt("MINUTE_TIME_KEY", 0);
        int interval = preferences.getInt("INTERVAL_TIME_KEY", 15);
        newTaskItems = logTasks.mTaskItems;

        final Intent activityDataFetcherIntent = new Intent(context, getClass());
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, activityDataFetcherIntent, 0);

        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (1000*60*interval), pendingIntent);

        if(hour < 12) {
            if (minute == 0)
                newTaskItems.add(new TaskInterval(hour + ":0" + minute + "am", Integer.toString(interval), ""));
            else
                newTaskItems.add(new TaskInterval(hour + ":" + minute + "am", Integer.toString(interval), ""));
        }
        else if(hour == 12) {
            if (minute == 0)
                newTaskItems.add(new TaskInterval("12:0" + minute + "pm", Integer.toString(interval), ""));
            else
                newTaskItems.add(new TaskInterval("12:" + minute + "pm", Integer.toString(interval), ""));
        }
        else if(hour > 12 && hour < 24) {
            if (minute == 0)
                newTaskItems.add(new TaskInterval(hour -12 + ":0" + minute + "pm", Integer.toString(interval), ""));
            else
                newTaskItems.add(new TaskInterval(hour - 12 + ":" + minute + "pm", Integer.toString(interval), ""));
        }
        else if(hour == 0) {
            if (minute == 0)
                newTaskItems.add(new TaskInterval("12:0" + minute + "am", Integer.toString(interval), ""));
            else
                newTaskItems.add(new TaskInterval("12:" + minute + "am", Integer.toString(interval), ""));
        }

        minute += interval;
        while(minute >= 60)
        {
            hour += 1;
            minute -= 60;
        }

        try {
            if(hour < 12) {
                if (minute == 0)
                    LogTasks.getInstance().updateTheTextView(hour + ":0" + minute + "am");
                else
                    LogTasks.getInstance().updateTheTextView(hour + ":" + minute + "am");
            }
            else if(hour == 12)
                if (minute == 0)
                    LogTasks.getInstance().updateTheTextView("12:0" + minute + "pm");
                else
                    LogTasks.getInstance().updateTheTextView("12:" + minute + "pm");
            else if(hour > 12 && hour < 24) {
                if (minute == 0)
                    LogTasks.getInstance().updateTheTextView(hour - 12 + ":0" + minute + "pm");
                else
                    LogTasks.getInstance().updateTheTextView(hour - 12 + ":" + minute + "pm");
            }
            else if(hour == 0) {
                if (minute == 0)
                    LogTasks.getInstance().updateTheTextView("12:0" + minute + "am");
                else
                    LogTasks.getInstance().updateTheTextView("12:" + minute + "am");
            }
        } catch (Exception e) {

        }

        logTasks.mTaskItems = newTaskItems;
        logTasks.adapter.notifyDataSetChanged();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("HOUR_TIME_KEY", hour);
        editor.putInt("MINUTE_TIME_KEY", minute);

        editor.commit();

    }
}
