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

public class SetTimeActivity extends Activity {

    int interval = 15, hour = 1, minute = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_time);

        NumberPicker intervalPicker = (NumberPicker) findViewById(R.id.intervalPicker);
        final String[] displayedValues1 = {"15 MINS", "30 MINS", "45 MINS", "1 HOUR", "2 HOURS", "3 HOURS", "4 HOURS", "8 HOURS"};
        intervalPicker.setMinValue(0);
        intervalPicker.setMaxValue(7);
        intervalPicker.setDisplayedValues(displayedValues1);

        NumberPicker hourPicker = (NumberPicker) findViewById(R.id.hourPicker);
        final String[] displayedValues2 = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(11);
        hourPicker.setDisplayedValues(displayedValues2);

        NumberPicker minutePicker = (NumberPicker) findViewById(R.id.minutePicker);
        final String[] displayedValues3 = {"00", "15", "30", "45"};
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(3);
        minutePicker.setDisplayedValues(displayedValues3);

        NumberPicker middayPicker = (NumberPicker) findViewById(R.id.middayPicker);
        final String[] displayedValues4 = {"AM", "PM"};
        middayPicker.setMinValue(0);
        middayPicker.setMaxValue(1);
        middayPicker.setDisplayedValues(displayedValues4);

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
                if(displayedValues4[newVal] == "PM" && hour < 24)
                    hour += 12;
                else if(displayedValues4[newVal] == "AM" && hour > 12)
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
                startActivity(intent);
            }
        });

    }
}