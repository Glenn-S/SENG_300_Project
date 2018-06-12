package com.example.glenn.seng_300_project;

public class TaskList {

    private String time;
    private String task;

    // constructor
    public TaskList(String time, String task){
        this.time=time; // String of format HH:MM
        this.task=task;
    }

    // setter and getter methods

    /**
     * gets the time stored in the task list index
     * @return String with the time
     */
    public String getTime(){
        return time;
    }

    /**
     * sets the time for the index in the task list
     * @param time
     */
    public void setTime(String time){
        this.time = time;
    }

    /**
     * gets the task stored in the task list index
     * @return String with the task
     */
    public String getTask(){
        return task;
    }

    /**
     * set the task for the index in the task list
     * @param task
     */
    public void setTask(String task){
        this.task = task;
    }
}