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

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<TaskInfo> todoList;
    private MainActivity mainActivity;
    private DBClass db;

    public MyAdapter(DBClass db, MainActivity mainActivity) {
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

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                builder.setTitle("Delete Task");
                builder.setMessage("Are you sure you want to delete this Task?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteItem(position);
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notifyItemChanged(position);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        holder.update.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                editItem(position);
            }
        });

        holder.progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                builder.setPositiveButton("Set to Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editStatus(position, "Done");
                        int index = R.drawable.ic_done_one;
                        holder.progress.setImageResource(index);
                    }
                });
                builder.setNegativeButton("Set to In progress", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editStatus(position, "In progress");
                        int index = R.drawable.inprogress;
                        holder.progress.setImageResource(index);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        if(todoList.get(position).getStatus().equals("Done")) {
            int index = R.drawable.ic_done_one;
            holder.progress.setImageResource(index);
        }
        else {
            int index = R.drawable.inprogress;
            holder.progress.setImageResource(index);
        }
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

    public void deleteItem(int position) {
        //had to handle this case manually, for some reason, sometimes the position is 1 and the size is 1 as well, which triggers out of boundary error
        if(todoList.size() == position)
            position = position - 1;
        TaskInfo item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        if (todoList.isEmpty())
            mainActivity.tv.setText("No tasks so far");
        notifyItemRemoved(position);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void editItem(int position) {
        if(todoList.size() == position)
            position = position - 1;
        TaskInfo item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        bundle.putString("date", LocalDateTime.now().toString());
        UpdateTask fragment = new UpdateTask();
        fragment.setArguments(bundle);
        fragment.show(mainActivity.getSupportFragmentManager(), UpdateTask.TAG);
        notifyDataSetChanged();
    }

    public void editStatus(int position, String answer) {
        TaskInfo item = todoList.get(position);
        db.updateStatus(item.getId(), answer);
        todoList.get(position).setStatus(answer);
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
