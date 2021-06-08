package com.example.minitodolist;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;

public class TaskInfo {
    private int id;
    private String task, status;
    private String date;

    public TaskInfo() {
        this.status = "In progress";
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
