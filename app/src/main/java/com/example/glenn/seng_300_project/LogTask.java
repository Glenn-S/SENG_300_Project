package com.example.glenn.seng_300_project;

import java.util.ArrayList;
import java.util.List;

public class LogTask {
    static List list = new ArrayList();
    static ArrayList<TaskInterval> taskList = new ArrayList<>();

    // calculates the time of the current interval for the list view
    public String getNextInterval (ArrayList<TaskList> logItems, String startTime, String frequency) {
        int totalItems = logItems.size();
        String times[] = startTime.split(":");
        int startHour = Integer.parseInt(times[0]); // convert to minutes
        int startMin = Integer.parseInt(times[1]);
        int start = (startHour*60) + startMin;
        int freq = Integer.parseInt(frequency);
        int totalTime = start + (totalItems * freq);
        int totalHour = totalTime/60; // divide by min per hour
        int totalMin = totalTime%60; // modulo to get remainder in minutes
        String finalTime;
        if (totalMin == 0){
            finalTime = totalHour + ":" + totalMin + "0";
        }
        else {
            finalTime =  totalHour + ":" + totalMin;
        }
        return finalTime;
    }

    // calculates the time of the next interval for the text box
    public String displayNextInterval (ArrayList<TaskList> logItems, String startTime, String frequency) {
        int totalItems = logItems.size();
        String times[] = startTime.split(":");
        int startHour = Integer.parseInt(times[0]); // convert to minutes
        int startMin = Integer.parseInt(times[1]);
        int start = (startHour*60) + startMin;
        int freq = Integer.parseInt(frequency);
        int totalTime = start + (totalItems * freq) + freq; // add frequency to get the next interval
        int totalHour = totalTime/60; // divide by min per hour
        int totalMin = totalTime%60; // modulo to get remainder in minutes
        String finalTime;
        if (totalMin == 0){
            finalTime = totalHour + ":" + totalMin + "0";
        }
        else {
            finalTime =  totalHour + ":" + totalMin;
        }
        return finalTime;
    }

    // adds a new task to the task list
    public List addTask (String task) {
        list.add(task); // append to the end of the list
        return list;
    }

    // returns the list of tasks contained in the static variable list
    public List getTask () {
        return list;
    }

    // adds a task into the task log
    public ArrayList<TaskList> addTaskToLog (ArrayList<TaskList> listItems, String time, String task) {
        listItems.add(new TaskList(time, task));
        return listItems;
    }

    // takes a struct of time and task
    public void setFile (ArrayList<TaskInterval> dayLog) { // stubbed out for now but will have access to a database
        taskList = dayLog;
        return;
    }

    public ArrayList<TaskInterval> getFile () { // stubbed out for now but will have access to a database
        return taskList;
    }

    // adds the next blank time interval
    public ArrayList<TaskList> addBlankLogItem (ArrayList<TaskList> listItems, String time) {
        listItems.add(new TaskList(time, ""));
        return listItems;
    }
}
