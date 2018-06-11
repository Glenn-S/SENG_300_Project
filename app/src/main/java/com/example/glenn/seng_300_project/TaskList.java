package com.example.glenn.seng_300_project;

public class TaskList {

    private String time;
    private String task;

    // constructor
    public TaskList(String time, String task){
        this.time=time;
        this.task=task;
    }

    // setter and getter methods
    public String getTime(){
        return time;
    }

    public void setTime(String time){
        this.time = time;
    }

    public String getTask(){
        return task;
    }

    public void setTask(String task){
        this.task = task;
    }
}