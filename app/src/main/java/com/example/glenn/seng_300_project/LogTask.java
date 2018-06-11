package com.example.glenn.seng_300_project;

import java.util.ArrayList;
import java.util.List;

public class LogTask {
    static List list = new ArrayList();


    // calculates the time and adds it into the text box
    public String displayInterval (ArrayList<TaskList> logItems, String startTime, String frequency) {
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

    // changes the background colour of a task when completed
    public void changeBackColour (String colourValue) {
        //will need to stub out this method
        return;
    }

    public String getBackColour () { // stub method
        String string = new String();
        return string;
    }
    // takes a struct of time and task
    public void setFile (ArrayList<TaskList> dayLog) {
        return;
    }

    public ArrayList<TaskList> getFile () {
        ArrayList<TaskList> list = new ArrayList<TaskList>();
        return list;
    }

    // adds the next blank time interval
    public ArrayList<TaskList> addBlankLogItem (ArrayList<TaskList> listItems, String time) {
        listItems.add(new TaskList(time, ""));
        return listItems;
    }
}
