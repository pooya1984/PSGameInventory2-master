package com.example.android.psgameinventory.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.psgameinventory.data.GameContract.GameEntry;

public class GameDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = GameDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "shelter.db";
    private static final int DATABASE_VERSION = 1;

    public GameDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //update new column
        String SQL_CREATE_GAMES_TABLE =  "CREATE TABLE " + GameEntry.TABLE_NAME + " ("
                + GameEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + GameEntry.COLUMN_GAME_NAME + " TEXT NOT NULL, "
                + GameEntry.COLUMN_GAME_GENRE + " INTEGER NOT NULL, "
                + GameEntry.COLUMN_GAME_CONSOLE + " INTEGER NOT NULL, "
                + GameEntry.COLUMN_GAME_IMAGE + " TEXT, "
                +GameEntry.COLUMN_GAME_PRICE + " INTEGER NOT NULL DEFAULT 0, "
                + GameEntry.COLUMN_GAME_STOCK + " INTEGER NOT NULL DEFAULT 0);";
        db.execSQL(SQL_CREATE_GAMES_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
        }
    }
}
