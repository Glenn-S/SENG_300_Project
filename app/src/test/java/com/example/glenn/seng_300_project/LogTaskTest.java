package com.example.glenn.seng_300_project;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LogTaskTest {
    /*
        Tests
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

    // changes the background colour of a task when completed
    @Test
    public void changeBackColourTest () {
        LogTask.list = new ArrayList();
        LogTask task = new LogTask();
        String colourVal = "#FF3174C6";
        task.changeBackColour(colourVal);
        String value = task.getBackColour();
        assertEquals(colourVal, value);
    }

    // good to start
    @Test
    public void setFileTest () {
        LogTask.list = new ArrayList();
        LogTask task = new LogTask();
        ArrayList<TaskList> logItems = new ArrayList<>();
        ArrayList<TaskList> returnedItems;
        Calendar time = Calendar.getInstance();
        logItems.add(new TaskList("9:00", "Coded"));
        logItems.add(new TaskList("10:00", "Read Email"));
        logItems.add(new TaskList("11:00", "Meetings"));
        logItems.add(new TaskList("12:00", "Read Emails"));
        logItems.add(new TaskList("1:00", "Refactored"));
        task.setFile(logItems);
        returnedItems = task.getFile();
        for (int i = 0; i < logItems.size(); i++){
            assertEquals(logItems.get(0).getTime().toString(), returnedItems.get(0).getTime().toString());
            assertEquals(logItems.get(0).getTask().toString(), returnedItems.get(0).getTask().toString());
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