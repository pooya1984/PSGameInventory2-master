package com.example.android.psgameinventory.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.android.psgameinventory.data.GameContract.GameEntry;

public class GameProvider extends ContentProvider {

    public static final String LOG_TAG = GameProvider.class.getSimpleName();
    private static final int GAMES = 100;
    private static final int GAME_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(GameContract.CONTENT_AUTHORITY, GameContract.PATH_GAMES, GAMES);
        sUriMatcher.addURI(GameContract.CONTENT_AUTHORITY, GameContract.PATH_GAMES + "/#", GAME_ID);
    }

    private GameDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new GameDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case GAMES:

                cursor = database.query(GameContract.GameEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case GAME_ID:

                selection = GameContract.GameEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(GameEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case GAMES:
                return insertGame(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a pet into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertGame(Uri uri, ContentValues values) {
        // Check that the name is not null
        String name = values.getAsString(GameEntry.COLUMN_GAME_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Game requires a name");
        }

        // Check that the console is valid
        Integer console = values.getAsInteger(GameEntry.COLUMN_GAME_CONSOLE);
        if (console == null || !GameEntry.isValidConsole(console)) {
            throw new IllegalArgumentException("Game requires valid console");
        }

        // Check that the genre is valid
        Integer genre = values.getAsInteger(GameEntry.COLUMN_GAME_GENRE);
        if (genre == null || !GameEntry.isValidGenre(genre)) {
            throw new IllegalArgumentException("Game requires valid genre");
        }

        // If the quantity is provided, check that it's greater than or equal to 0
        Integer quantity = values.getAsInteger(GameEntry.COLUMN_GAME_STOCK);
        if (quantity != null && quantity < 0) {
            throw new IllegalArgumentException("Game requires valid quantity");
        }

        // If the price is provided, check that it's greater than or equal to 0
        Integer price = values.getAsInteger(GameEntry.COLUMN_GAME_PRICE);
        if (price != null && price < 0) {
            throw new IllegalArgumentException("Game requires valid price");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(GameEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case GAMES:
                return updateGame(uri, contentValues, selection, selectionArgs);
            case GAME_ID:
                selection = GameEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateGame(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateGame(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(GameEntry.COLUMN_GAME_NAME)) {
            String name = values.getAsString(GameEntry.COLUMN_GAME_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Game requires a name");
            }
        }

        if (values.containsKey(GameEntry.COLUMN_GAME_CONSOLE)) {
            Integer console = values.getAsInteger(GameEntry.COLUMN_GAME_CONSOLE);
            if (console == null || !GameEntry.isValidConsole(console)) {
                throw new IllegalArgumentException("Game requires valid console");
            }
        }

        if (values.containsKey(GameEntry.COLUMN_GAME_PRICE)) {
            Integer price = values.getAsInteger(GameEntry.COLUMN_GAME_PRICE);
            if (price != null && price < 0) {
                throw new IllegalArgumentException("Game requires valid price");
            }
        }

        if (values.containsKey(GameEntry.COLUMN_GAME_GENRE)) {
            Integer genre = values.getAsInteger(GameEntry.COLUMN_GAME_GENRE);
            if (genre == null || !GameEntry.isValidGenre(genre)) {
                throw new IllegalArgumentException("Game requires valid genre");
            }
        }

        if (values.containsKey(GameEntry.COLUMN_GAME_STOCK)) {
            // Check that the quantity is greater than or equal to 0
            Integer quantity = values.getAsInteger(GameEntry.COLUMN_GAME_STOCK);
            if (quantity != null && quantity < 0) {
                throw new IllegalArgumentException("Game requires valid quantity");
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsUpdated = database.update(GameEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case GAMES:
                rowsDeleted = database.delete(GameEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case GAME_ID:
                selection = GameEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(GameEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case GAMES:
                return GameEntry.CONTENT_LIST_TYPE;
            case GAME_ID:
                return GameEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
