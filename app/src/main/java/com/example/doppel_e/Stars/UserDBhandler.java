package com.example.doppel_e.Stars;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDBhandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "doppele.db";
    public static final String TABLE_NAME = "Stars";
    private static final int DATABASE_VERSION = 1;
    private Context context;

    public UserDBhandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public boolean checkDatabaseExists() {
        SQLiteDatabase db = null;
        try {
            String databasePath = this.context.getDatabasePath(UserDBhandler.DATABASE_NAME).getPath();
            db = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            // La base de datos no existe
        }

        if (db != null) {
            db.close();
        }

        return db != null;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE user (id TEXT PRIMARY KEY AUTOINCREMENT, userCode TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }
}
