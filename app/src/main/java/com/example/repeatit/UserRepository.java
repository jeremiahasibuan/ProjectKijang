package com.example.repeatit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserRepository {

    private UsersDBHelper dbHelper;

    public UserRepository(Context context) {
        dbHelper = new UsersDBHelper(context);
    }

    // Menambahkan pengguna baru ke database
    public long addUser(String username, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);

        // Menyimpan data dan mengembalikan ID baris yang dimasukkan (id auto-increment)
        long rowId = db.insert("users", null, values);
        db.close();
        return rowId;
    }

    // Mengambil semua data pengguna
    public void getAllUsers() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("users", null, null, null, null, null, null);

        // Mengecek apakah cursor memiliki data
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Mengambil data pengguna dari kolom
                int id = cursor.getInt(cursor.getColumnIndex("id"));  // Kolom id auto-increment
                String username = cursor.getString(cursor.getColumnIndex("username"));
                String password = cursor.getString(cursor.getColumnIndex("password"));

                // Menampilkan data pengguna di log atau TextView
                Log.d("User Data", "ID: " + id + ", Username: " + username + ", Password: " + password);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
    }

    // Menambahkan fungsi untuk memverifikasi login
    public boolean verifyLogin(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Query untuk mencocokkan username dan password
        String[] columns = {"username", "password"};
        String selection = "username = ? AND password = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query("users", columns, selection, selectionArgs, null, null, null);

        boolean isValid = false;
        if (cursor != null && cursor.getCount() > 0) {
            isValid = true;  // Username dan password cocok
        }
        cursor.close();
        db.close();
        return isValid;
    }


}
