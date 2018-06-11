package com.example.glenn.seng_300_project;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Pop extends Activity {

    ListView listView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popwindow);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*0.7), (int)(height*0.5));

        // populate the list view
        ArrayList<String> customTaskList = new ArrayList<String>();
        customTaskList.addAll(Arrays.asList("Coding",
                                            "Meeting",
                                            "Coffee Break",
                                            "Checked Emails",
                                            "Worked on the big project for Encana that is due at the end of the quarter",
                                            "Ran Errands"));

        listView = (ListView)findViewById(R.id.customTasksList);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, customTaskList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                finish();
            }
        });
    }
}
