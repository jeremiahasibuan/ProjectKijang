package com.example.repeatit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SongsDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "SongsDatabase";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_SONGS = "songs";
    public static final String COLUMN_SONG_ID = "id";
    public static final String COLUMN_SONG_TITLE = "title";
    public static final String COLUMN_SONG_ARTIST = "artist";
    public static final String COLUMN_SONG_ALBUM = "album";
    public static final String COLUMN_SONG_ARTWORK = "artwork";  // ID gambar album

    public SongsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createSongsTable = "CREATE TABLE " + TABLE_SONGS + " ("
                + COLUMN_SONG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_SONG_TITLE + " TEXT, "
                + COLUMN_SONG_ARTIST + " TEXT, "
                + COLUMN_SONG_ALBUM + " TEXT, "
                + COLUMN_SONG_ARTWORK + " INTEGER);";
        db.execSQL(createSongsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONGS);
        onCreate(db);
    }
}

