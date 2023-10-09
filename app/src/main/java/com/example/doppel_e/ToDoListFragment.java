package com.example.doppel_e;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.doppel_e.ToDo.Adapter.ToDoAdapter;
import com.example.doppel_e.ToDo.Model.ToDoModel;
import com.example.doppel_e.ToDo.RecyclerItemTouchHelper;
import com.example.doppel_e.ToDo.Utils.ToDoDataBaseHandler;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ToDoListFragment extends Fragment {



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters

    private ToDoDataBaseHandler dbHandler;
    private View view;
    private RecyclerView taskRecyclerView;
    private ToDoAdapter taskAdapter;

    private List<ToDoModel> taskList;
    public ToDoListFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHandler = new ToDoDataBaseHandler(this.getContext());
        taskList = dbHandler.getTasks();
        setHasOptionsMenu(true);
    }


    public boolean deleteTask(int pos){
        try {
            dbHandler.deleteTask(pos);
            taskList.remove(pos);
            taskAdapter.notifyItemRemoved(pos);
            return true;
        } catch (JSONException e) {
            return false;
        }

    }

    public boolean addTask() {
        EditText editText = this.view.findViewById(R.id.taskET);

        if (!editText.getText().toString().equals("")) {
            ToDoModel task = new ToDoModel();
            task.setTask(editText.getText().toString());
            task.setId(0);

            try {
                dbHandler.addTask(task);
                taskList.add(0, task);
                taskAdapter.notifyDataSetChanged();
                editText.setText("");
                return true;
            } catch (JSONException e) {
                return false;
            }
        }else {return false;}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_to_do_list, container, false);

        Button addButton = this.view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTask();
                taskAdapter.notifyDataSetChanged();
            }
        });


        taskRecyclerView = view.findViewById(R.id.taskRecyclerview);
        ProgressBar progressBar = view.findViewById(R.id.loading);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        taskAdapter = new ToDoAdapter(this.getActivity());

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(taskAdapter));
        itemTouchHelper.attachToRecyclerView(taskRecyclerView);

        dbHandler.setAdapter(taskAdapter);
        dbHandler.setProgressBar(progressBar);
        taskAdapter.setTasks(taskList);
        taskRecyclerView.setAdapter(taskAdapter);
        taskAdapter.notifyDataSetChanged();
        return view;
    }
}