package com.example.quanlybanan.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        db.execSQL("CREATE TABLE tables (id INTEGER PRIMARY KEY AUTOINCREMENT, seats INTEGER, status TEXT)");
        db.execSQL("CREATE TABLE menu_items (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price INTEGER, image_path TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS tables");
        db.execSQL("DROP TABLE IF EXISTS menu_items"); // THÊM LỆNH XÓA
        onCreate(db);
    }

    // ==========================================================
    // CÁC PHƯƠNG THỨC QUẢN LÝ MÓN ĂN
    // ==========================================================

    public long insertMenuItem(String name, int price, String imagePath) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        values.put("image_path", imagePath);
        long id = db.insert("menu_items", null, values);
        db.close();
        return id;
    }

    public int updateMenuItem(int id, String name, int price, String imagePath) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        values.put("image_path", imagePath);
        int rows = db.update("menu_items", values, "id=?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }

    public int deleteMenuItem(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete("menu_items", "id=?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }

    public Cursor getAllMenuItems() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM menu_items ORDER BY name ASC", null);
    }

    // ==========================================================
    // CÁC PHƯƠNG THỨC KHÁC GIỮ NGUYÊN
    // ==========================================================

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

    public long insertTable(int seats, String status) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("seats", seats);
        values.put("status", status);
        return db.insert("tables", null, values);
    }

    public Cursor getAllTables() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM tables", null);
    }

    public int updateTable(int id, int seats, String status) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("seats", seats);
        values.put("status", status);
        return db.update("tables", values, "id=?", new String[]{String.valueOf(id)});
    }

    public int deleteTable(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete("tables", "id=?", new String[]{String.valueOf(id)});
    }


}