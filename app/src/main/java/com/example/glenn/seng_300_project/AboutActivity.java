package com.example.glenn.seng_300_project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //Enable back button on action bar to go back to previous activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Clickable link to view the opencsv license
        TextView openCSVLicense = (TextView) findViewById(R.id.openCSV);
        openCSVLicense.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * Back button in action bar moves application back to previous page
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
