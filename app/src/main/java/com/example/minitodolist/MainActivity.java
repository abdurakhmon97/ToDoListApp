package com.example.minitodolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
//Пнукты:
//Архитектура приложения не совсем "чистая", так как есть два адаптер класса, которые передают данные по трем разным активити
//Не применен вндроид жетпак
//Я точно не знаю, можно ли считать это приложение "Model, View, ViewModel", скорее всего нет
//Приложение не имеет никаких депенденси инжекшнов
//Было использовано recycleview and cardview
//База данных является sqlite, но без использования Rooms

public class MainActivity extends AppCompatActivity implements DialogCloseListener{
    private RecyclerView taskRecycleView;
    private MyAdapter adapter;
    private List<TaskInfo> taskInfoList;
    private FloatingActionButton floatingActionButton;
    private DBClass db;
    public TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tasksText);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavBar);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.doneTasks:
                        startActivity(new Intent(getApplicationContext(), Done.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        return true;

                    case R.id.inProgress:
                        startActivity(new Intent(getApplicationContext(), InProgress.class));
                        overridePendingTransition(0, 0);
                        return true;}
                return false;
            }
        });

        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewAddingActivity.class);
                startActivity(intent);
            }
        });
        //taskInfoList = new ArrayList<>();

        db = new DBClass(this);
        db.openDatabase();

        taskRecycleView = findViewById(R.id.tasksRecyclerView);
        taskRecycleView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(db, this);
        taskRecycleView.setAdapter(adapter);
        taskInfoList = db.getAllTasks();
        Collections.reverse(taskInfoList);
        /*System.out.println("--------------------------------");
        System.out.println(taskInfoList + "it can be empty -------");*/

        if(!taskInfoList.isEmpty())
            tv.setText("Tasks");
        else
            tv.setText("No tasks so far");
        adapter.setTasks(taskInfoList);
    }

    @Override
    public void handleDialogClose(DialogInterface dialog){
        taskInfoList = db.getAllTasks();
        if(!taskInfoList.isEmpty())
            tv.setText("Tasks");
        else
            tv.setText("No tasks so far");
        Collections.reverse(taskInfoList);
        adapter.setTasks(taskInfoList);
        adapter.notifyDataSetChanged();
    }
}