package com.example.quanlybanan.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "dining.db";
    // TĂNG VERSION LÊN 4 ĐỂ KÍCH HOẠT onUpgrade và tạo bảng mới
    private static final int DB_VERSION = 4;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT, is_owner INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS users");

        onCreate(db);
    }

    public long insertUser(String username, String password, int isOwner) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("is_owner", isOwner);
        return db.insert("users", null, values);
    }

    public Cursor login(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM users WHERE username=? AND password=?", new String[]{username, password});
    }

    public int changePassword(int userId, String newPassword) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", newPassword);
        return db.update("users", values, "id=?", new String[]{String.valueOf(userId)});
    }

    public int deleteUser(int userId) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete("users", "id=? AND is_owner=0", new String[]{String.valueOf(userId)});
    }

    public Cursor getAllManagers() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM users WHERE is_owner=0", null);
    }


}