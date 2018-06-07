package com.example.momentary.sportapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper  extends SQLiteOpenHelper{
    private final static String table_name = "tableFile";
    private final static String createTable = "CREATE TABLE tableFile(name text unique not null, content text)";

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public DBOpenHelper(Context context) {
        super(context, "demo.db",null , 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
