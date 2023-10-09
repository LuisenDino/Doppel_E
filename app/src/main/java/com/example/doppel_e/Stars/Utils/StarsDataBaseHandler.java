package com.example.doppel_e.Stars.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.doppel_e.Stars.Model.StarModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StarsDataBaseHandler extends SQLiteOpenHelper
{
    public Context context;
    //SQL DB
    public static final String DATABASE_NAME = "doppele.db";
    public static final String TABLE_NAME = "Stars";
    public static final int DATABASE_VERSION = 1;

    //Spreadsheet DB
    private String spreadSheetId = "1XD9qxtadLJVY-IGYxrYdWkAfYHmcAHrxvLzUBVEgchA";
    private String sheet = "Stars";
    private String urlGet = "https://script.google.com/macros/s/AKfycbwf07lgYfM1X4vwgU0n8cgF7vubmEmnBJSMIcZXO173_d7d0ZWH6WNgCAkCPtC3J7-C/exec?spreadsheetId="+spreadSheetId+"&sheet="+sheet;
    private String urlAdd = "https://script.google.com/macros/s/AKfycbwFHGNWqIcFUg8x9HtO1Z1a9Xv8V5ocMlIaN5hcWpxJnXIbAM2R5x8Lrzh3Y2hqScefRQ/exec";
    private String urlDelete = "https://script.google.com/macros/s/AKfycbxKPf2S26Jgkfly8xQYB7r2oGd5PQbCOnTGxTfSKqJJuUy8hF59BhiIYbAbpyj5ZwIq/exec";
    private String urlUpdateCell = "https://script.google.com/macros/s/AKfycbyd1BKSAnob7y_2CW3b5WRQwIaTZo_J919XwFkVjLpldM2jGyeyIeLWjDp9EfhJisdnuw/execs";



    public StarsDataBaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE "+ TABLE_NAME + "("+
                "code TEXT PRIMARY KEY,"+
                "name TEXT,"+
                "stars INTEGER,"+
                "pass TEXT"+
                ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    public void addStar(StarModel star){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("code", star.getCode());
        values.put("name", star.getStars());
        values.put("stars", star.getStars());
        values.put("pass", star.getPass());

        db.insert(TABLE_NAME, null , values);
        db.close();
    }

    public void deleteStar(String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "code" + "=?", new String[]{code});
        db.close();
    }

    public void updateStarStars(String code, int newStars){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("stars", newStars);
        db.update(TABLE_NAME, values, "code" + "=?", new String[]{code});
        db.close();
    }

    public void updateStarStars(String code, boolean increment){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET stars = stars" ;
        if (increment){
            query += "+ 1";
        } else {
            query += "- 1";
        }

        query += " WHERE code = '" + code + "'";
        db.execSQL(query);
        db.close();
        pushStars(code);
    }

    public StarModel findStar(String code) {
        SQLiteDatabase db = this.getReadableDatabase();
        StarModel star = null;

        Cursor cursor = db.query(TABLE_NAME, null, "code" + "=?", new String[]{code}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            star = new StarModel();
            star.setCode(cursor.getString(cursor.getColumnIndex("code")));
            star.setName(cursor.getString(cursor.getColumnIndex("name")));
            star.setStars(cursor.getInt(cursor.getColumnIndex("stars")));
            star.setPass(cursor.getString(cursor.getColumnIndex("pass")));

            cursor.close();
        }

        db.close();

        return star;
    }

    public List<StarModel> getAll(){
        List<StarModel> starList = new ArrayList<>();
        String query = "SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do{
                StarModel star = new StarModel();
                star.setCode(cursor.getString(cursor.getColumnIndex("code")));
                star.setName(cursor.getString(cursor.getColumnIndex("name")));
                star.setStars(cursor.getInt(cursor.getColumnIndex("stars")));
                star.setPass(cursor.getString(cursor.getColumnIndex("pass")));

                starList.add(star);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return starList;
    }

    public void pullStars(){
        RequestQueue queue = Volley.newRequestQueue(this.context);
        StringRequest request = new StringRequest(Request.Method.GET, this.urlGet,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        JsonArray jsonArray = gson.fromJson(response.toString(), JsonArray.class);
                        for (JsonElement obj: jsonArray){
                            JsonObject jsonObject = obj.getAsJsonObject() ;
                            String code = jsonObject.get("code").getAsString();
                            int stars = jsonObject.get("stars").getAsInt();
                            if(findStar(code).getStars() != stars){
                                /*StarModel player = new StarModel();
                                player.setCode(jsonObject.get("code").getAsString());
                                player.setName(jsonObject.get("name").getAsString());
                                player.setStars(jsonObject.get("stars").getAsInt());
                                player.setPass(jsonObject.get("pass").getAsString());
                                addStar(player);*/
                                updateStarStars(code, stars);
                            }
                        };

                        /*if(progressBar != null){
                            progressBar.setVisibility(View.GONE);
                        }*/
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        );
        queue.add(request);
    }

    public void pushStars(String code){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("spreadsheet_id", spreadSheetId);
            jsonObject.put("sheet", sheet);
            jsonObject.put("col", "stars");
            jsonObject.put("row", code);
            jsonObject.put("value", findStar(code).getStars());
            RequestQueue queue = Volley.newRequestQueue(this.context);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlUpdateCell, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(request);
        }catch (Exception e){

        }
    }
}
