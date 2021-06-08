package com.example.minitodolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InProgress extends AppCompatActivity {
    private PassiveAdapter adapter;
    private List<TaskInfo> inProgressList;
    private List<TaskInfo> taskInfoList;
    private DBClass db;
    private RecyclerView taskRecycleView;
    private TextView tv;
    private MainActivity mainActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_progress);

        db = new DBClass(this);
        db.openDatabase();
        inProgressList = new ArrayList<>();

        tv = findViewById(R.id.tasksText);
        taskRecycleView = findViewById(R.id.tasksRecyclerView);
        taskRecycleView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PassiveAdapter(db, mainActivity);
        taskRecycleView.setAdapter(adapter);
        taskInfoList = db.getAllTasks();
        Collections.reverse(taskInfoList);
        for(TaskInfo ti : taskInfoList) {
            if(ti.getStatus().equals("In progress"))
                inProgressList.add(ti);
        }

        System.out.println("--------------------------------");
        System.out.println(taskInfoList + "it can be empty -------");

        if(!inProgressList.isEmpty())
            tv.setText("Tasks");
        else
            tv.setText("No tasks so far");
        adapter.setTasks(inProgressList);



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavBar);
        bottomNavigationView.setSelectedItemId(R.id.inProgress);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.doneTasks:
                        startActivity(new Intent(getApplicationContext(), Done.class));
                        overridePendingTransition(0, 0);

                        return true;
                    case R.id.allTasks:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.inProgress:

                        return true;
                }
                return false;
            }
        });

    }
}
