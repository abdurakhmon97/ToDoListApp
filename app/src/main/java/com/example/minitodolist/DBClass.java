package com.example.minitodolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;
import java.util.List;

public class DBClass extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "miniToDoListDB";
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String DATE = "date";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
            + STATUS + " TEXT, " + DATE + " TEXT)";

    private SQLiteDatabase db;

    public DBClass(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        // Create tables again
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertTask(TaskInfo task){
        ContentValues cv = new ContentValues();
        System.out.println("it is insert task DB class ------------------------- \n");
        System.out.println(task.getTask());
        cv.put(TASK, task.getTask());
        cv.put(STATUS, task.getStatus());
        cv.put(DATE, task.getDate());
        db.insert(TODO_TABLE, null, cv);
    }

    public List<TaskInfo> getAllTasks(){
        System.out.println("happening ------------------------------");
        List<TaskInfo> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do {
                        TaskInfo task = new TaskInfo();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setStatus(cur.getString(cur.getColumnIndex(STATUS)));
                        //making the date and time looking better
                        String answer = cur.getString(cur.getColumnIndex(DATE));
                        String firstPart = answer.substring(0, 10);
                        String secondPart = answer.substring(11, 19);
                        String finalAnswer = "Date: " + firstPart;
                        task.setDate(finalAnswer);
                        taskList.add(task);
                    }
                    while (cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return taskList;
    }

    public void updateTask(int id, String task, String date) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        cv.put(DATE, date);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id){
        db.delete(TODO_TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateStatus(int id, String answer) {
        ContentValues cv = new ContentValues();
        cv.put(STATUS, answer);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }
}