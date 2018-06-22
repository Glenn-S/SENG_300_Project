package com.example.glenn.seng_300_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Set;

public class SetTime extends LogTasks {
    public int hour;
    public int minute;
    public int interval;
    private Intent intent;


    public SetTime (Intent intent){
        this.intent = intent;
    }

    public void setDisplay(){
        if(intent.getExtras() == null) {
            this.hour = 0;
            this.minute = 0;
        }
        else
        {
            Bundle extras = intent.getExtras();
            this.hour = extras.getInt("HOUR_KEY");
            this.minute = extras.getInt("MINUTE_KEY");
            this.interval = extras.getInt("INTERVAL_KEY");
        }

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, this.hour);
        c.set(Calendar.MINUTE, this.minute);
        c.set(Calendar.SECOND, 0);

        //This is temporary
        this.hour = c.get(Calendar.HOUR_OF_DAY);
        this.minute = c.get(Calendar.MINUTE);
        this.hour = 12;
        this.minute = 00;


    }


}
