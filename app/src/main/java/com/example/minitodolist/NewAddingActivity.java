package com.example.minitodolist;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.util.List;

public class NewAddingActivity extends AppCompatActivity {
    private Button add;
    private Button cancel;
    private DBClass db;
    private EditText et;
    private MyAdapter adapter;
    private List<TaskInfo> taskInfoList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_adding);

        add = findViewById(R.id.newTaskButton);
        cancel = findViewById(R.id.cancel_button);
        et = findViewById(R.id.newTaskText);
        db = new DBClass(this);
        db.openDatabase();

        add.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String text = et.getText().toString();
                if(text.isEmpty())
                    Toast.makeText(NewAddingActivity.this, "The new task field needs to be filled", Toast.LENGTH_SHORT).show();
                else {
                    TaskInfo task = new TaskInfo();
                    task.setTask(text);
                    task.setStatus(task.getStatus());
                    task.setDate(LocalDateTime.now().toString());
                    System.out.println(task.getTask() + task.getDate() + "-------------------------\n");
                    db.insertTask(task);
                    //taskInfoList = db.getAllTasks();
                    //System.out.println("task is here " + taskInfoList.toString());
                    //adapter.setTasks(taskInfoList);

                    Intent intent = new Intent(NewAddingActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewAddingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}