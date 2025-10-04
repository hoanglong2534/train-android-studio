package com.btvn.btv290925;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BookDatabaseHelper extends SQLiteOpenHelper {
    
    // Database info
    private static final String DATABASE_NAME = "BookProvider.db";
    private static final int DATABASE_VERSION = 1;
    
    // Table name
    public static final String TABLE_BOOKS = "books";
    
    // Column names
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_YEAR = "year";
    
    // Create table SQL
    private static final String CREATE_TABLE_BOOKS = 
        "CREATE TABLE " + TABLE_BOOKS + "(" +
        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
        COLUMN_TITLE + " TEXT NOT NULL," +
        COLUMN_AUTHOR + " TEXT NOT NULL," +
        COLUMN_YEAR + " INTEGER NOT NULL" +
        ")";

    public BookDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BOOKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        onCreate(db);
    }
}
