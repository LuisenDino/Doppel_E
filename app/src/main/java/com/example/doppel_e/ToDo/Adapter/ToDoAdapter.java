package com.example.doppel_e.ToDo.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.doppel_e.ToDo.Model.ToDoModel;
import com.example.doppel_e.R;
import com.example.doppel_e.ToDo.Utils.ToDoDataBaseHandler;
import com.example.doppel_e.ToDoListFragment;

import org.json.JSONException;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {


    private ToDoDataBaseHandler dbHandler;
    private Activity activity;

    private List<ToDoModel> todoList;

    public ToDoAdapter(Activity activity){
        this.activity = activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        dbHandler = new ToDoDataBaseHandler(this.getContext());
        return new ViewHolder(itemView);
    }

    public Context getContext() {
        return activity;
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public void onBindViewHolder(ViewHolder holder, int position){
        ToDoModel item = todoList.get(position);
        holder.task.setText(item.getTask());
    }


    public void setTasks(List<ToDoModel> todoList){
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    public boolean deleteTask(int pos){
        try {
            dbHandler.deleteTask(pos);
            todoList.remove(pos);
            return true;
        } catch (JSONException e) {
            return false;
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView task;
        ViewHolder(View view){
            super(view);
            task = view.findViewById(R.id.taskTextView);
        }
    }


}
