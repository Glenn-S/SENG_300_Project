package com.example.glenn.seng_300_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class updates the permanant storage of tasks.
 */

public class TaskManager {

    private Set<String> taskSet;
    private TaskDBManager dbHelper;
    private SQLiteDatabase db;

    public TaskManager(Context context){
        dbHelper = new TaskDBManager(context);

        //Initialize the task list
        taskSet = getTaskSetFromDB();
    }

    /**
     * Open the information store
     */
    public void openDB(){
        db = dbHelper.getWritableDatabase();
    }

    /**
     * Close the information store
     */
    public void closeDB(){
        dbHelper.close();
    }

    /**
     * Populate the set of tasks with data from the database
     * @return TreeSet containing all the tasks in the databse
     */
    private Set<String> getTaskSetFromDB(){
        openDB();

        //Query for the list of tasks
        String[] projections = {TaskDBManager.TaskEntry.COLUMN_NAME_TASK};
        Cursor cursor = db.query(
                TaskDBManager.TaskEntry.TABLE_NAME,
                projections,
                null,
                null,
                null,
                null,
                null);

        HashSet<String> set = new HashSet<>();

        while(cursor.moveToNext()){
            String taskName = cursor.getString(cursor.getColumnIndexOrThrow(TaskDBManager.TaskEntry.COLUMN_NAME_TASK));
            set.add(taskName);
        }

        cursor.close();
        closeDB();

        return set;

    }

    /**
     * Adds a new task to the permanent file store. Does nothing if task already exists.
     * @param taskName the name of the new task
     * @throws RuntimeException if there is already another task with the same name
     */
    public void addTask(String taskName) throws RuntimeException{
        if(taskSet.contains(taskName)){
            return;
        }
        else if (taskName.isEmpty()){
            throw new RuntimeException("Task is empty.");
        }

        //Add to set of tasks
        taskSet.add(taskName);

        //Add to persistent memory
        ContentValues values = new ContentValues();
        values.put(TaskDBManager.TaskEntry.COLUMN_NAME_TASK, taskName);

        db.insert(TaskDBManager.TaskEntry.TABLE_NAME, null, values);
    }

    /**
     * Removes a specified task from the list and database.
     * @param taskName task to be removed
     * @throws RuntimeException
     */
    public void deleteTask(String taskName) throws RuntimeException{
        if(!taskSet.contains(taskName)){
            throw new RuntimeException("Task: " + "\'" + taskName + "\' does not exist.");
        }

        //Remove from set
        taskSet.remove(taskName);

        //Remove from the database

        //WHERE $COLUMN_NAME_TASK = $taskName
        String selection = TaskDBManager.TaskEntry.COLUMN_NAME_TASK + " = ?";
        String[] selectionArgs = {taskName};

        db.delete(TaskDBManager.TaskEntry.TABLE_NAME, selection, selectionArgs);
    }

    /**
     * Edit a specified task and update the database. If new task is already in the database,
     * do nothing.
     * @param oldTaskName original name of the task
     * @param newTaskName new name for the task
     */
    public void editTask(String oldTaskName, String newTaskName){
        if(!taskSet.contains(oldTaskName)){
            throw new RuntimeException("Task: " + "\'" + oldTaskName + "\' does not exist.");
        }
        else if (newTaskName.isEmpty()){
            deleteTask(oldTaskName);
            return;
        }
        else if (taskSet.contains(newTaskName)){
            return;
        }

        //Update value in set
        taskSet.remove(oldTaskName);
        taskSet.add(newTaskName);

        //Update value in the database
        ContentValues values = new ContentValues();
        values.put(TaskDBManager.TaskEntry.COLUMN_NAME_TASK, newTaskName);

        String selection = TaskDBManager.TaskEntry.COLUMN_NAME_TASK + " = ?";
        String[] selectionArgs = {oldTaskName};

        db.update(TaskDBManager.TaskEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

    }

    public List<String> getTaskList(){
        return new ArrayList<String>(taskSet);
    }

}
