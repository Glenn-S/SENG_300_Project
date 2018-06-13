package com.example.glenn.seng_300_project;

public class TaskInterval {

    public String taskName;
    public String startTime;
    public long durationInMin;

    public TaskInterval(String start, long duration, String task){
        taskName = task;
        startTime = start;
        durationInMin = duration;
    }
}
