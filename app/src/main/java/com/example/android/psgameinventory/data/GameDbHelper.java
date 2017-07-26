package com.example.android.psgameinventory.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.psgameinventory.data.GameContract.GameEntry;

import static android.provider.MediaStore.Images.Thumbnails.IMAGE_ID;
import static com.example.android.psgameinventory.data.GameContract.GameEntry.TABLE_NAME;

public class GameDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = GameDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "shelter.db";
    private static final int DATABASE_VERSION = 1;
    private final Context context;
    private SQLiteDatabase db;

    public GameDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //update new column
        String SQL_CREATE_GAMES_TABLE =  "CREATE TABLE " + TABLE_NAME + " ("
                + GameEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + GameEntry.COLUMN_GAME_NAME + " TEXT NOT NULL, "
                + GameEntry.COLUMN_GAME_GENRE + " INTEGER NOT NULL, "
                + GameEntry.COLUMN_GAME_CONSOLE + " INTEGER NOT NULL, "
                + GameEntry.COLUMN_GAME_IMAGE + " Blob, "
                +GameEntry.COLUMN_GAME_PRICE + " INTEGER NOT NULL DEFAULT 0, "
                + GameEntry.COLUMN_GAME_STOCK + " INTEGER NOT NULL DEFAULT 0);";
        this.db = db;
        this.db.execSQL(SQL_CREATE_GAMES_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
        }
    }

    // Insert the image to the Sqlite DB
    public void updateImage(byte[] imageBytes,int id) {
        ContentValues cv = new ContentValues();
        cv.put(GameEntry.COLUMN_GAME_IMAGE, imageBytes);
        db.update(TABLE_NAME, cv, "_id="+id, null);
    }

    // Get the image from SQLite DB
    // We will just get the last image we just saved for convenience...
    public byte[] retreiveImageFromDB() {
        Cursor cur = db.query(true, TABLE_NAME, new String[]{GameEntry.COLUMN_GAME_IMAGE,},
                null, null, null, null,
                IMAGE_ID + " DESC", "1");
        if (cur.moveToFirst()) {
            byte[] blob = cur.getBlob(cur.getColumnIndex(GameEntry.COLUMN_GAME_IMAGE));
            cur.close();
            return blob;
        }
        cur.close();
        return null;
    }

    public GameDbHelper open() throws SQLException {
        GameDbHelper mDbHelper = new GameDbHelper(context);

        db = mDbHelper.getWritableDatabase();
        return this;
    }
}