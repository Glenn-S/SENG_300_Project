package com.example.glenn.seng_300_project;

public class TaskInterval {

    public String taskName;
    public String startTime;
    public String durationInMin;

    public TaskInterval(String start, String duration, String task){
        taskName = task;
        startTime = start;
        durationInMin = duration;
    }
}
