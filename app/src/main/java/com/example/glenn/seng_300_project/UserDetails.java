package com.example.glenn.seng_300_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserDetails extends NavigationBaseActivity {
    //Get user information to create the filename
    public SharedPreferences pref;
    public String firstName;
    public String lastName;
    public String email;
    public Context context;
    public String nameDate;
    public File csvFile;
    public String filename;
    public File filePath;


    public UserDetails (Context context){
        this.context = context;
        this.pref = context.getSharedPreferences("MyPref", 0); // 0 - for private mode
    }

    public void storeDetails (){
        this.firstName = pref.getString("FIRST_KEY", "no first name");
        this.lastName = pref.getString("LAST_KEY", "no last name");
        this.email = pref.getString("EMAIL_KEY", "no last name");

    }

    public void createFile(List<TaskInterval> mTaskItems) {
        //Get date in YYYY-MM-DD format to append as part of the filename
        Date date = new Date();
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        nameDate = this.lastName + "_" + this.firstName + "_" + formattedDate;

        filename = nameDate + ".csv";
        filePath = Environment.getExternalStorageDirectory();
        csvFile = new File(filePath, filename);

        this.filePath.mkdir();
        try{
            //Create a new csv file and email it
            this.csvFile.createNewFile();
            CSVManager csvManager = new CSVManager(this.csvFile.getPath());
            csvManager.writeTaskList(mTaskItems, this.context);
        }catch (IOException e){
            Log.e("LogTasks", "File could not be created");
        }
    }
}
