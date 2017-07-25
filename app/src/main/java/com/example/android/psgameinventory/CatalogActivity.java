package com.example.android.psgameinventory;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.android.psgameinventory.data.GameContract.GameEntry;

public class CatalogActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    /** Identifier for the pet data loader */
    private static final int GAME_LOADER = 0;

    /** Adapter for the ListView */
    GameCursorAdapter mCursorAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        ImageButton fab = (ImageButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        ListView gameListView = (ListView) findViewById(R.id.list);

        View emptyView = findViewById(R.id.empty_view);
        gameListView.setEmptyView(emptyView);

        mCursorAdapter = new GameCursorAdapter(this, null);
        gameListView.setAdapter(mCursorAdapter);

        gameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                Uri currentGameUri = ContentUris.withAppendedId(GameEntry.CONTENT_URI, id);
                intent.setData(currentGameUri);
                startActivity(intent);
            }
        });

        // Kick off the loader
        getLoaderManager().initLoader(GAME_LOADER, null, this);
    }

    private void insertGame() {
        ContentValues values = new ContentValues();
        values.put(GameEntry.COLUMN_GAME_NAME, "The Last Of Us");
        values.put(GameEntry.COLUMN_GAME_STOCK, 7);
        values.put(GameEntry.COLUMN_GAME_PRICE,20);
        values.put(GameEntry.COLUMN_GAME_GENRE,GameEntry.GENRE_ADVENTURE);
        values.put(GameEntry.COLUMN_GAME_CONSOLE, GameEntry.CONSOLE_PS4);
        values.put(GameEntry.COLUMN_GAME_IMAGE, "no image");



        getContentResolver().insert(GameEntry.CONTENT_URI, values);

    }
    private void deleteAllGames() {
        int rowsDeleted = getContentResolver().delete(GameEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from game database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertGame();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllGames();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                GameEntry._ID,
                GameEntry.COLUMN_GAME_CONSOLE,
                GameEntry.COLUMN_GAME_GENRE,
                GameEntry.COLUMN_GAME_NAME,
                GameEntry.COLUMN_GAME_PRICE,
                GameEntry.COLUMN_GAME_STOCK,
                GameEntry.COLUMN_GAME_IMAGE };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                GameEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update {@link PetCursorAdapter} with this new cursor containing updated pet data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }}