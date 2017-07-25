package com.example.android.psgameinventory.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * API Contract for the PSGameInventory app.
 */
public final class GameContract {

    private GameContract() {}



    public static final String CONTENT_AUTHORITY = "com.example.android.games";
    public static final String PATH_GAMES = "games";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);



    public static final class GameEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_GAMES);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GAMES;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GAMES;

        /** Name of database table for games */
        public final static String TABLE_NAME = "games";



        /**
         * Unique ID number for the pet (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_GAME_NAME ="name";
        public final static String COLUMN_GAME_STOCK = "quantity";
        public final static String COLUMN_GAME_PRICE = "price";
        public final static String COLUMN_GAME_IMAGE = "IMAGE";

        /**
         * Gender of the console.
         * Type: INTEGER
         */
        public final static String COLUMN_GAME_GENRE = "genre";
        public final static String COLUMN_GAME_CONSOLE = "console";

        /**
         * Possible values for the GENRE of the GAME.
         */
        public static final int GENRE_UNKNOWN = 0;
        public static final int GENRE_SCI_FI = 1;
        public static final int GENRE_ACTION = 2;
        public static final int GENRE_SPORT = 3;
        public static final int GENRE_ADVENTURE = 4;
        public static final int GENRE_SHOOTING = 5;


        public static boolean isValidGenre(int genre) {
            if (genre == GENRE_UNKNOWN ||
                    genre == GENRE_SCI_FI ||
                    genre == GENRE_ACTION ||
                    genre == GENRE_SPORT ||
                    genre == GENRE_ADVENTURE ||
                    genre == GENRE_SHOOTING) {
                return true;
            }
            return false;
        }

        /**
         * Possible values for the CONSOLE of the GAME.
         */
        public static final int CONSOLE_UNKNOWN = 0;
        public static final int CONSOLE_PS = 1;
        public static final int CONSOLE_PS1 = 2;
        public static final int CONSOLE_PS2 = 3;
        public static final int CONSOLE_PS3 = 4;
        public static final int CONSOLE_PS4 = 5;


        public static boolean isValidConsole(int console) {
            if (console == CONSOLE_UNKNOWN ||
                    console == CONSOLE_PS ||
                    console == CONSOLE_PS1 ||
                    console == CONSOLE_PS2 ||
                    console == CONSOLE_PS3 ||
                    console == CONSOLE_PS4) {
                return true;
            }
            return false;
        }}
}