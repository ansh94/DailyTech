package com.anshdeep.dailytech.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ANSHDEEP on 19-05-2017.
 */

public class ArticleDBHelper extends SQLiteOpenHelper {

    // The name of the database
    private static final String DATABASE_NAME = "articles.db";

    // If you change the database schema, you must increment the database version
    private static final int VERSION = 1;


    // Constructor
    ArticleDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /**
     * Called when the tasks database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create tasks table (careful to follow SQL formatting rules)
        final String CREATE_TABLE = "CREATE TABLE " + ArticleContract.ArticleEntry.TABLE_NAME + " (" +
                ArticleContract.ArticleEntry._ID + " INTEGER PRIMARY KEY, " +
                ArticleContract.ArticleEntry.COLUMN_ARTICLE_AUTHOR + " TEXT, " +
                ArticleContract.ArticleEntry.COLUMN_ARTICLE_TITLE + " TEXT NOT NULL, " +
                ArticleContract.ArticleEntry.COLUMN_ARTICLE_DESCRIPTION + " TEXT, " +
                ArticleContract.ArticleEntry.COLUMN_ARTICLE_URL + " TEXT, " +
                ArticleContract.ArticleEntry.COLUMN_ARTICLE_IMAGE_URL + " TEXT, " +
                ArticleContract.ArticleEntry.COLUMN_ARTICLE_SOURCE + " TEXT, " +
                ArticleContract.ArticleEntry.COLUMN_ARTICLE_TIME + " TEXT);";


        db.execSQL(CREATE_TABLE);
    }


    /**
     * This method discards the old table of data and calls onCreate to recreate a new one.
     * This only occurs when the version number for this database (DATABASE_VERSION) is incremented.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ArticleContract.ArticleEntry.TABLE_NAME);
        onCreate(db);
    }
}
