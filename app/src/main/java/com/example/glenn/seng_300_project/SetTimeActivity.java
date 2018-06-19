package com.example.glenn.seng_300_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;

public class SetTimeActivity extends Activity {

    int interval = 15, hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_time);
        Calendar c = Calendar.getInstance();
        final int hourNow = c.get(Calendar.HOUR_OF_DAY);
        final int minuteNow = c.get(Calendar.MINUTE);

        NumberPicker intervalPicker = (NumberPicker) findViewById(R.id.intervalPicker);
        final String[] displayedValues1 = {"15 MINS", "30 MINS", "45 MINS", "1 HOUR", "2 HOURS", "3 HOURS", "4 HOURS", "8 HOURS"};
        intervalPicker.setMinValue(0);
        intervalPicker.setMaxValue(7);
        intervalPicker.setDisplayedValues(displayedValues1);

        NumberPicker minutePicker = (NumberPicker) findViewById(R.id.minutePicker);
        final String[] displayedValues3 = {"00", "15", "30", "45"};
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(3);
        minutePicker.setDisplayedValues(displayedValues3);
        if(minuteNow >= 0 && minuteNow < 15) {
            minutePicker.setValue(1);
            minute = Integer.parseInt(displayedValues3[1]);
        }
        else if(minuteNow >= 15 && minuteNow < 30) {
            minutePicker.setValue(2);
            minute = Integer.parseInt(displayedValues3[2]);
        }
        else if(minuteNow >= 30 && minuteNow < 45) {
            minutePicker.setValue(3);
            minute = Integer.parseInt(displayedValues3[3]);
        }
        else if(minuteNow >= 45 && minuteNow < 60) {
            minutePicker.setValue(0);
            minute = Integer.parseInt(displayedValues3[0]);
        }

        NumberPicker hourPicker = (NumberPicker) findViewById(R.id.hourPicker);
        final String[] displayedValues2 = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(11);
        hourPicker.setDisplayedValues(displayedValues2);
        if(minute == 0) {
            hourPicker.setValue(hourNow);
            hour = hourNow + 1;
        }
        else {
            hourPicker.setValue(hourNow - 1);
            hour = hourNow;
        }


        final NumberPicker middayPicker = (NumberPicker) findViewById(R.id.middayPicker);
        final String[] displayedValues4 = {"AM", "PM"};
        middayPicker.setMinValue(0);
        middayPicker.setMaxValue(1);
        middayPicker.setDisplayedValues(displayedValues4);
        if(hourNow >= 12)
            middayPicker.setValue(1);


        intervalPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal)
            {
                interval = 0;
                if(displayedValues1[newVal] == "15 MINS")
                    interval = 15;
                else if(displayedValues1[newVal] == "30 MINS")
                    interval = 30;
                else if(displayedValues1[newVal] == "45 MINS")
                    interval = 45;
                else if(displayedValues1[newVal] == "1 HOUR")
                    interval = 60;
                else if(displayedValues1[newVal] == "2 HOURS")
                    interval = 2*60;
                else if(displayedValues1[newVal] == "3 HOURS")
                    interval = 3*60;
                else if(displayedValues1[newVal] == "4 HOURS")
                    interval = 4*60;
                else if(displayedValues1[newVal] == "8 HOURS")
                    interval = 8*60;
            }
        });

        hourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal)
            {
                hour = Integer.parseInt(displayedValues2[newVal]);
                if(middayPicker.getValue() == 1 && (hour >= 0 && hour < 12))
                    hour += 12;
                else if(middayPicker.getValue() == 0 && (hour >= 12 && hour < 24))
                    hour -= 12;
            }
        });

        minutePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal)
            {
                minute = Integer.parseInt(displayedValues3[newVal]);
            }
        });

        middayPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal)
            {
                if(displayedValues4[newVal] == "PM" && (hour >= 0 && hour < 12))
                    hour += 12;
                else if(displayedValues4[newVal] == "AM" && (hour >= 12 && hour < 24))
                    hour -= 12;
            }
        });


        Button nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetTimeActivity.this, LogTasks.class);
                intent.putExtra("HOUR_KEY", hour);
                intent.putExtra("MINUTE_KEY", minute);
                intent.putExtra("FREQUENCY_KEY", interval);
                // create the CSV to start file here
                CSVManager CSV = new CSVManager(""); //
                startActivity(intent);
            }
        });

    }
}