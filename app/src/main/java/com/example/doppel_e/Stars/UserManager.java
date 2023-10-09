package com.example.doppel_e.Stars;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserManager {
    private Context context;
    private UserDBhandler dbHandler;

    public UserManager(Context context){
        this.context = context;
        dbHandler = new UserDBhandler(context);
    }

    public void saveUserCode(String userCode){
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("userCode", userCode);
        db.insert("user", null, values);
        db.close();
    }

    public String getUserCode(){
        String userCode = "";

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cursor = db.query("user", new String[]{"userCode"}, null, null, null, null, null);
        if (cursor.moveToFirst()){
            userCode = cursor.getString(0);
        }

        cursor.close();
        db.close();

        return userCode;
    }
}
