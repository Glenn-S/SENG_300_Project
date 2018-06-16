package com.example.glenn.seng_300_project;

import android.widget.ListView;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LogTaskTest {
    /*
        Tests

        Need to adjust some of these tests to deal with function name changes etc
     */

    // good to start
    @Test
    public void addTaskTest () {
        LogTask.list = new ArrayList();
        List tasks = new ArrayList();
        LogTask task = new LogTask();
        assertEquals(0, tasks.size());
        tasks = task.addTask("Coded");
        assertEquals("Coded", tasks.get(0).toString());
        assertEquals(1, tasks.size());
    }

    // good to start
    @Test
    public void taskListTest () {
        LogTask.list = new ArrayList();
        LogTask task = new LogTask();
        List tasks = new ArrayList();

        tasks = task.addTask("Coding");
        tasks = task.addTask("Meeting");
        tasks = task.addTask("Read email");
        tasks = task.addTask("Refactored");
        tasks = task.addTask("");
        assertEquals("Refactored", tasks.get(3).toString());
        assertEquals("Meeting", tasks.get(1).toString());
    }

    // good to start
    @Test
    public void displayIntervalTest () {
        // will need to write some time
        LogTask.list = new ArrayList();
        LogTask task = new LogTask();
        ArrayList<TaskList> logItems = new ArrayList<>();
        logItems.add(new TaskList("9:00", "Coded"));
        logItems.add(new TaskList("10:00", "Read Email"));
        String start = "9:00";
        String freq = "60"; // time is in minutes
        String intervalValue = task.getNextInterval(logItems, start, freq);
        assertEquals("11:00", intervalValue.toString());
        return;
    }

    // good to start
    @Test
    public void addTaskToLogTest () {
        LogTask.list = new ArrayList();
        ArrayList<TaskList> logItems = new ArrayList<>();
        LogTask task = new LogTask();
        assertEquals(0, logItems.size());
        task.addTaskToLog(logItems, "9:00", "Coded");
        task.addTaskToLog(logItems, "10:00", "Read Email");
        task.addTaskToLog(logItems, "11:00", "Meetings");
        assertEquals(3, logItems.size());
        assertEquals("9:00", logItems.get(0).getTime().toString());
        assertEquals("11:00", logItems.get(2).getTime().toString());
    }

    // good to start
    @Test
    public void setFileTest () {
        ArrayList<TaskInterval> returnedItems = new ArrayList<>();
        ArrayList<TaskInterval> mTaskItems = new ArrayList<>(); // set new array of task times in unspecified type
        LogTask lt = new LogTask();

        mTaskItems.add(new TaskInterval("9am", "60", "Coded"));
        mTaskItems.add(new TaskInterval("10am", "60", "Read Email"));
        mTaskItems.add(new TaskInterval("11am", "60", "Meetings"));
        mTaskItems.add(new TaskInterval("12am", "60", "Read Emails"));
        mTaskItems.add(new TaskInterval("1pm", "60", "Refactored"));
        mTaskItems.add(new TaskInterval("2pm", "60", ""));
        mTaskItems.add(new TaskInterval("3pm", "60", "At Lunch"));
        mTaskItems.add(new TaskInterval("4pm", "60", "Edited Document"));
        mTaskItems.add(new TaskInterval("5pm", "60", "Talked with Coworker"));
        lt.setFile(mTaskItems);
        returnedItems = lt.getFile();
        for (int i = 0; i < mTaskItems.size(); i++){
            assertEquals(mTaskItems.get(i).startTime, returnedItems.get(i).startTime);
            assertEquals(mTaskItems.get(i).durationInMin, returnedItems.get(i).durationInMin);
            assertEquals(mTaskItems.get(i).taskName, returnedItems.get(i).taskName);
        }
    }

    // good to start
    @Test
    public void addBlankLogItemTest () {
        LogTask.list = new ArrayList();
        ArrayList<TaskList> logItems = new ArrayList<>();
        LogTask task = new LogTask();
        assertEquals(0, logItems.size());
        task.addBlankLogItem(logItems, "10:00");
        assertEquals(1, logItems.size());
        assertEquals("", logItems.get(0).getTask().toString());
    }
}