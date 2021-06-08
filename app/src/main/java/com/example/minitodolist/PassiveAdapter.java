package com.example.minitodolist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

public class PassiveAdapter extends RecyclerView.Adapter<PassiveAdapter.ViewHolder> {
    private List<TaskInfo> todoList;
    private MainActivity mainActivity;
    private DBClass db;

    public PassiveAdapter(DBClass db, MainActivity mainActivity) {
        this.db = db;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        db.openDatabase();
        holder.task.setText(todoList.get(position).getTask());
        System.out.println(todoList.get(position).getTask());
        holder.date.setText(todoList.get(position).getDate());
        System.out.println(todoList.get(position).getDate() + "----------------qwer");
        holder.progress.setVisibility(View.GONE);
        holder.delete.setVisibility(View.GONE);
        holder.update.setVisibility(View.GONE);
        holder.status.setText(todoList.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public void setTasks(List<TaskInfo> infoList) {
        this.todoList = infoList;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView task;
        TextView date;
        TextView status;
        ImageView progress, update, delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            task = itemView.findViewById(R.id.todoCheckBox);
            date = itemView.findViewById(R.id.date);
            status = itemView.findViewById(R.id.status);
            progress = itemView.findViewById(R.id.progressIcon);
            update = itemView.findViewById(R.id.updateIcon);
            delete = itemView.findViewById(R.id.deleteIcon);
        }
    }
}
