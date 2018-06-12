package com.example.glenn.seng_300_project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class TaskDBManager extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TaskManager.db";
    //Create the table
    private  static final String SQL_CREATE_TASKS =
            "CREATE TABLE " + TaskEntry.TABLE_NAME + " (" +
            TaskEntry._ID + " INTEGER PRIMARY KEY," +
            TaskEntry.COLUMN_NAME_TASK + " TEXT)";
    //Drop the table
    private  static final String SQL_DROP_TASKS =
            "DROP TABLE IF EXISTS " + TaskEntry.TABLE_NAME;
    private SQLiteDatabase db;

    public TaskDBManager(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Create new table if doesn't exist
        sqLiteDatabase.execSQL(SQL_CREATE_TASKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Drop the table and start over
        sqLiteDatabase.execSQL(SQL_DROP_TASKS);
        onCreate(sqLiteDatabase);
    }

    public static class TaskEntry implements BaseColumns{
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_NAME_TASK = "task";
    }

}
