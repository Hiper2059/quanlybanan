package com.example.quanlybanan.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlybanan.R;

import com.example.quanlybanan.database.DBHelper;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DBHelper db;
    FloatingActionButton btnAdd;
    SharedPreferences prefs;
    int userId, isOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);
        isOwner = prefs.getInt("isOwner", 0);

        if (userId == -1) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }


        db = new DBHelper(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        if (isOwner == 0) {
            menu.findItem(R.id.menu_account).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_logout) {
            prefs.edit().clear().apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        } else if (itemId == R.id.menu_change_password) {
            showChangePasswordDialog();
            return true;
        } else if (itemId == R.id.menu_account && isOwner == 1) {
            startActivity(new Intent(this, AccountManagerActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showChangePasswordDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_change_password, null);
        EditText edtNew = view.findViewById(R.id.edtNewPass);

        new AlertDialog.Builder(this)
                .setTitle("Đổi mật khẩu")
                .setView(view)
                .setPositiveButton("Cập nhật", (dialog, which) -> {
                    String newPass = edtNew.getText().toString().trim();
                    if (!newPass.isEmpty()) {
                        db.changePassword(userId, newPass);
                        Toast.makeText(this, "Đã đổi mật khẩu!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Mật khẩu mới không được để trống!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }
}