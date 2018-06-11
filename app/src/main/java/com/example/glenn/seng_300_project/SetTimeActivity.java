package com.example.glenn.seng_300_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

public class SetTimeActivity extends AppCompatActivity {

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
        String[] displayedValues2 = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(11);
        hourPicker.setDisplayedValues(displayedValues2);

        NumberPicker minutePicker = (NumberPicker) findViewById(R.id.minutePicker);
        String[] displayedValues3 = {"00", "15", "30", "45"};
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(3);
        minutePicker.setDisplayedValues(displayedValues3);

        NumberPicker middayPicker = (NumberPicker) findViewById(R.id.middayPicker);
        String[] displayedValues4 = {"AM", "PM"};
        middayPicker.setMinValue(0);
        middayPicker.setMaxValue(1);
        middayPicker.setDisplayedValues(displayedValues4);

        intervalPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal)
            {
                if(displayedValues1[newVal] == "15 MINS"){

                }

            }
        });

        Button nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SetTimeActivity.this, LogTasks.class));
            }
        });
    }
}