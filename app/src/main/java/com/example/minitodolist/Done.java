package com.example.minitodolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Done extends AppCompatActivity {
    private PassiveAdapter adapter;
    private List<TaskInfo> doneList;
    private List<TaskInfo> taskInfoList;
    private DBClass db;
    private RecyclerView taskRecycleView;
    private TextView tv;
    private MainActivity mainActivity;
    ImageView progress, delete, update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);
        doneList = new ArrayList<>();

        db = new DBClass(this);
        db.openDatabase();

        tv = findViewById(R.id.tasksText);
        taskRecycleView = findViewById(R.id.tasksRecyclerView);
        taskRecycleView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PassiveAdapter(db, mainActivity);
        taskRecycleView.setAdapter(adapter);
        taskInfoList = db.getAllTasks();
        Collections.reverse(taskInfoList);
        System.out.println("--------------------------------");
        System.out.println(taskInfoList + "it can be empty -------");

        for(TaskInfo ti : taskInfoList) {
            if(ti.getStatus().equals("Done"))
                doneList.add(ti);
        }

        if(!doneList.isEmpty())
            tv.setText("Tasks");
        else
            tv.setText("No tasks so far");
        adapter.setTasks(doneList);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavBar);
        bottomNavigationView.setSelectedItemId(R.id.doneTasks);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.doneTasks:

                        return true;
                    case R.id.allTasks:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.inProgress:
                        startActivity(new Intent(getApplicationContext(), InProgress.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

    }
}