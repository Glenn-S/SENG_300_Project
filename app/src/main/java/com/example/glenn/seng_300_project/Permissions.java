package com.example.glenn.seng_300_project;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import java.io.IOException;

public class Permissions {
    private UserDetails userDetails;
    private PopulateList populateList;
    private Context context;
    private CSVManager csvManager;

    public Permissions (UserDetails userDetails, PopulateList populateList, Context context){
        this.userDetails = userDetails;
        this.populateList = populateList;
        this.context = context;
    }

    public void createPermission () {
        csvManager = new CSVManager(userDetails.csvFile.getPath());
        try{
            //Delete any previous files with the same name
            userDetails.csvFile.delete();

            //Open a csv file and write the list of tasks to the csv file
            userDetails.csvFile.createNewFile();
            csvManager.writeTaskList(populateList.mTaskItems, context);

            LogTasks.sendEmailWithOtherApp((Activity) context, userDetails.email, userDetails.nameDate, "", userDetails.csvFile.getPath());

        }
        catch(IOException e){
            Toast.makeText(context, "Could not create file.", Toast.LENGTH_SHORT).show();
        }
    }

}
