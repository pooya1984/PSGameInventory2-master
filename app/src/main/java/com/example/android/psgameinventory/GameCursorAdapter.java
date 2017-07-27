package com.example.android.psgameinventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.psgameinventory.data.GameContract.GameEntry;

import static android.content.ContentValues.TAG;
import static com.example.android.psgameinventory.R.id.console;
import static com.example.android.psgameinventory.R.id.game_image;
import static com.example.android.psgameinventory.R.id.game_price;

public class GameCursorAdapter extends CursorAdapter {

    public GameCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView summaryTextView = (TextView) view.findViewById(R.id.summary);
        TextView genreTextView = (TextView) view.findViewById(R.id.genre);
        TextView consoleTextView = (TextView) view.findViewById(console);
        TextView priceTextView = (TextView) view.findViewById(game_price);
        ImageView mImageView = (ImageView) view.findViewById(game_image);

        ImageView btn = (ImageView) view.findViewById(R.id.action_sale_by_one);
        ImageView btn2 = (ImageView) view.findViewById(R.id.order_button);


        // Find the columns of pet attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(GameEntry.COLUMN_GAME_NAME);
        final int gameColumnIndex = cursor.getColumnIndex(GameEntry.COLUMN_GAME_STOCK);
        int priceColumnIndex = cursor.getColumnIndex(GameEntry.COLUMN_GAME_PRICE);
        int genreColumnIndex = cursor.getColumnIndex(GameEntry.COLUMN_GAME_GENRE);
        int consoleColumnIndex = cursor.getColumnIndex(GameEntry.COLUMN_GAME_CONSOLE);

        final int id = cursor.getInt(cursor.getColumnIndex(GameEntry._ID));
        final Uri currentProductUri = ContentUris.withAppendedId(GameEntry.CONTENT_URI, id);
        context.getContentResolver().notifyChange(currentProductUri, null);

        int imageColumnIndex = cursor.getColumnIndex(GameEntry.COLUMN_GAME_IMAGE);
        byte[] imageBytes = cursor.getBlob(imageColumnIndex);
        if(imageBytes != null){
            mImageView.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
        }else{
            mImageView.setImageResource(R.drawable.ps_logo);}

        // Read the pet attributes from the Cursor for the current pet
        final String gameName = cursor.getString(nameColumnIndex);
        final int gameStock = cursor.getInt(gameColumnIndex);
        final String gamePrice = "$" + cursor.getString(priceColumnIndex);
        String genre = cursor.getString(genreColumnIndex);
        String console = cursor.getString(consoleColumnIndex);

        String GenreString = "";
        String ConsoleString = "";


        switch (Integer.parseInt(genre)) {
            case 1:
                GenreString = context.getString(R.string.SCI_FI);
                break;
            case 2:
                GenreString = context.getString(R.string.ACTION);
                break;
            case 3:
                GenreString = context.getString(R.string.SPORT);
                break;
            case 4:
                GenreString = context.getString(R.string.ADVENTURE);
                break;
            case 5:
                GenreString = context.getString(R.string.SHOOTING);
                break;
        }

        switch (Integer.parseInt(console)) {
            case 1:
                GenreString = context.getString(R.string.PS);
                break;
            case 2:
                GenreString = context.getString(R.string.PS1);
                break;
            case 3:
                GenreString = context.getString(R.string.PS2);
                break;
            case 4:
                GenreString = context.getString(R.string.PS3);
                break;
            case 5:
                GenreString = context.getString(R.string.PS4);
                break;
        }

        // Update the TextViews with the attributes for the current pet
        nameTextView.setText(gameName);

        String itemQuantity = "Game IN STOCK: " + String.valueOf(gameStock);
        summaryTextView.setText(itemQuantity);

        priceTextView.setText(gamePrice);
        genreTextView.setText(GenreString);
        consoleTextView.setText(ConsoleString);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                if (gameStock > 0) {
                    int qq = gameStock;
                    Log.d(TAG, "new quabtity= " + qq);
                    values.put(GameEntry.COLUMN_GAME_STOCK, --qq);
                    Log.d(TAG, "gameStock= " + cursor.getInt(gameColumnIndex));


                    Uri uri = ContentUris.withAppendedId(GameEntry.CONTENT_URI, id);
                    context.getContentResolver().update(uri, values, null, null);
                    Log.d(TAG, "gameStock= " + cursor.getInt(gameColumnIndex));

                    context.getContentResolver().notifyChange(currentProductUri, null);
                    return;
                } else {
                    Toast.makeText(context, "Item out of stock", Toast.LENGTH_SHORT).show();
                }
                context.getContentResolver().notifyChange(GameEntry.CONTENT_URI, null);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_SUBJECT, "PS Game Inventory ordered for " + gameName);
                intent.putExtra(Intent.EXTRA_SUBJECT, gamePrice);
            }
        });
    }
}