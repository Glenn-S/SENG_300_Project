package com.example.glenn.seng_300_project;

import android.util.Log;

import com.opencsv.CSVIterator;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Struct;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class CSVManager {
    /*
    public class TaskInterval{
        public String taskName;
        public String startTime;
        public long durationInMin;

        public TaskInterval(String start, long duration, String task){
            taskName = task;
            startTime = start;
            durationInMin = duration;
        }
    }*/

    String filename;

    public CSVManager(String filename){
        this.filename = filename;
    }

    /**
     * Write a list of TaskIntervals into a CSV file
     * @param taskIntervalList list of task intervals
     * @throws IOException
     */
    public void writeTaskList(List<TaskInterval> taskIntervalList) throws IOException{
        CSVWriter writer = new CSVWriter(new FileWriter(filename), '\t', '"','"',"\n");
        String[] line;
        for(TaskInterval t : taskIntervalList){
            if(t.taskName.isEmpty()){
                line = new String[] {t.startTime, t.durationInMin,""};
            }

            //startTime, duration, Task
            line = new String[] {t.startTime, t.durationInMin, t.taskName};
            writer.writeNext(line);
        }

        writer.close();
    }

    public List<TaskInterval> readTaskList() throws IOException{
        CSVReader reader = new CSVReader(new FileReader(filename));
        ArrayList<TaskInterval> intervalList = new ArrayList<TaskInterval>();

        String [] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            if(nextLine[0] == ""){
                intervalList.add(null);
            }
            else{
                intervalList.add(new TaskInterval(nextLine[0], nextLine[1], nextLine[2]));
            }

        }

        return intervalList;
    }

}
