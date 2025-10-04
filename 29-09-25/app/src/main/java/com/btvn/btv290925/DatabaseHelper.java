package com.btvn.btv290925;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    
    // Database info
    private static final String DATABASE_NAME = "StudentManager.db";
    private static final int DATABASE_VERSION = 1;
    
    // Table name
    private static final String TABLE_STUDENTS = "students";
    
    // Column names
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_CLASS = "class_name";
    
    // Create table SQL
    private static final String CREATE_TABLE_STUDENTS = 
        "CREATE TABLE " + TABLE_STUDENTS + "(" +
        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
        COLUMN_NAME + " TEXT NOT NULL," +
        COLUMN_AGE + " INTEGER NOT NULL," +
        COLUMN_CLASS + " TEXT NOT NULL" +
        ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STUDENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        onCreate(db);
    }

    // Thêm sinh viên mới
    public long addStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(COLUMN_NAME, student.getName());
        values.put(COLUMN_AGE, student.getAge());
        values.put(COLUMN_CLASS, student.getClassName());
        
        long id = db.insert(TABLE_STUDENTS, null, values);
        db.close();
        return id;
    }

    // Lấy tất cả sinh viên
    public List<Student> getAllStudents() {
        List<Student> studentList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_STUDENTS + " ORDER BY " + COLUMN_NAME;
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                Student student = new Student();
                student.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                student.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                student.setAge(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE)));
                student.setClassName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLASS)));
                
                studentList.add(student);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return studentList;
    }

    // Cập nhật sinh viên
    public int updateStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(COLUMN_NAME, student.getName());
        values.put(COLUMN_AGE, student.getAge());
        values.put(COLUMN_CLASS, student.getClassName());
        
        int result = db.update(TABLE_STUDENTS, values, 
                COLUMN_ID + " = ?", 
                new String[]{String.valueOf(student.getId())});
        
        db.close();
        return result;
    }

    // Xóa sinh viên
    public void deleteStudent(int studentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STUDENTS, COLUMN_ID + " = ?", 
                new String[]{String.valueOf(studentId)});
        db.close();
    }

    // Lấy sinh viên theo ID
    public Student getStudent(int studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STUDENTS, 
                new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_AGE, COLUMN_CLASS},
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(studentId)},
                null, null, null);
        
        Student student = null;
        if (cursor != null && cursor.moveToFirst()) {
            student = new Student();
            student.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            student.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
            student.setAge(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE)));
            student.setClassName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLASS)));
        }
        
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return student;
    }

    // Đếm số lượng sinh viên
    public int getStudentCount() {
        String countQuery = "SELECT * FROM " + TABLE_STUDENTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
}
