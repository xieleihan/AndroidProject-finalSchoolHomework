package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class StudentDataSource {

    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public StudentDataSource(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // 添加学生信息
    public void addStudent(String name, String className, String course, String phone) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_NAME, name);
        values.put(DBHelper.COLUMN_CLASS, className);
        values.put(DBHelper.COLUMN_COURSE, course);
        values.put(DBHelper.COLUMN_PHONE, phone);
        database.insert(DBHelper.TABLE_NAME, null, values);
    }

    // 查询所有学生信息
    public Cursor getAllStudents() {
        return database.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
    }

    // 查询指定姓名的学生信息
    public Cursor getStudentByName(String name) {
        return database.query(DBHelper.TABLE_NAME, null, DBHelper.COLUMN_NAME + " = ?",
                new String[]{name}, null, null, null);
    }

    // 更新学生信息
    public void updateStudent(long id, String name, String className, String course, String phone) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_NAME, name);
        values.put(DBHelper.COLUMN_CLASS, className);
        values.put(DBHelper.COLUMN_COURSE, course);
        values.put(DBHelper.COLUMN_PHONE, phone);
        database.update(DBHelper.TABLE_NAME, values, DBHelper.COLUMN_ID + " = " + id, null);
    }

    // 删除学生信息
    public void deleteStudent(long id) {
        database.delete(DBHelper.TABLE_NAME, DBHelper.COLUMN_ID + " = " + id, null);
    }
}

