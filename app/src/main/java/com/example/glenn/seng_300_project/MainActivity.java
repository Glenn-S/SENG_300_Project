package com.example.glenn.seng_300_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends NavigationBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);

        // To log Tasks screen (temporary)
        Button btnLog = (Button) findViewById(R.id.btnLogTasks);
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), LogTasks.class);
                startActivity(startIntent);
            }
        });

        //Seems to be errors linking up with Josephs pages
        // Anthony's pages work just fine
        // to Set Time activity (temporary)
        Button btnSetTime = (Button) findViewById(R.id.btnSetTime);
        btnSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), SetTimeActivity.class);
                startActivity(startIntent);
            }
        });

        // side bar activity works
        // to Set Time activity (temporary)
        Button btnStart = (Button) findViewById(R.id.btnStart);
        btnSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(startIntent);
            }
        });

    }

}
