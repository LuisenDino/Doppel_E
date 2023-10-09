package com.example.doppel_e.ToDo.Utils;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.doppel_e.ToDo.Adapter.ToDoAdapter;
import com.example.doppel_e.ToDo.Model.ToDoModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ToDoDataBaseHandler {
    private String spreadSheetId = "1XD9qxtadLJVY-IGYxrYdWkAfYHmcAHrxvLzUBVEgchA";
    private String sheet = "ToDo";
    private String urlGet = "https://script.google.com/macros/s/AKfycbwf07lgYfM1X4vwgU0n8cgF7vubmEmnBJSMIcZXO173_d7d0ZWH6WNgCAkCPtC3J7-C/exec?spreadsheetId="+spreadSheetId+"&sheet="+sheet;
    private String urlAdd = "https://script.google.com/macros/s/AKfycbwFHGNWqIcFUg8x9HtO1Z1a9Xv8V5ocMlIaN5hcWpxJnXIbAM2R5x8Lrzh3Y2hqScefRQ/exec";
    private String urlDelete = "https://script.google.com/macros/s/AKfycbxKPf2S26Jgkfly8xQYB7r2oGd5PQbCOnTGxTfSKqJJuUy8hF59BhiIYbAbpyj5ZwIq/exec";
    private Context context;
    private ToDoAdapter adapter;
    private ProgressBar progressBar;

    public ToDoDataBaseHandler(Context context){
        this.context = context;
    }
    public ToDoDataBaseHandler(Context context, ProgressBar progressBar){
        this.context = context;
        this.progressBar = progressBar;
    }

    public boolean deleteTask(int index) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("spreadsheet_id", spreadSheetId);
        jsonObject.put("sheet", sheet);
        jsonObject.put("row_position",index+2);
        RequestQueue queue = Volley.newRequestQueue(this.context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlDelete, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(context, "Tarea Eliminada", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
        return false;
    }

    public void setProgressBar(ProgressBar progressBar){this.progressBar = progressBar;}

    public void setAdapter(ToDoAdapter adapter){
        this.adapter = adapter;
    }
    public List<ToDoModel> getTasks(){
        RequestQueue queue = Volley.newRequestQueue(this.context);
        List<ToDoModel> tasksList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, this.urlGet,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        JsonArray jsonArray = gson.fromJson(response.toString(), JsonArray.class);
                        for (JsonElement obj: jsonArray){
                            JsonObject jsonObject = obj.getAsJsonObject() ;
                            ToDoModel task = new ToDoModel();
                            task.setId(jsonObject.get("id").getAsInt());
                            task.setTask(jsonObject.get("task").getAsString());
                            tasksList.add(task);
                        };
                        Collections.reverse(tasksList);
                        adapter.notifyDataSetChanged();

                        if(progressBar != null){
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        );
        queue.add(request);

        return tasksList;
    }

    public boolean addTask(ToDoModel task) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("spreadsheet_id", spreadSheetId);
        jsonObject.put("sheet", sheet);
        JSONArray jsonArray = new JSONArray();
        JSONArray row = new JSONArray();

        row.put(task.getId());
        row.put(task.getTask());
        jsonArray.put(row);
        jsonObject.put("rows", jsonArray);
        RequestQueue queue = Volley.newRequestQueue(this.context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlAdd, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(context, "Tarea añadida", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
        return false;
    }

}
