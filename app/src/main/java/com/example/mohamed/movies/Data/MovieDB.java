package com.example.mohamed.movies.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mohamed on 30/11/2016.
 */

public class MovieDB extends SQLiteOpenHelper {

    public MovieDB(Context context) {
        super(context, "Movie Data Base", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_FAVOURITE_TABLE = "CREATE TABLE " + MovieDBSchema.Favourite.TABLE_NAME + " (" +
                MovieDBSchema.Favourite.ID + " INTEGER PRIMARY KEY," +
                MovieDBSchema.Favourite.POSTER_PATH + " VARCHAR(150), " +
                MovieDBSchema.Favourite.OVERVIEW + " TEXT NOT NULL, " +
                MovieDBSchema.Favourite.RELEASE_DATE + " VARCHAR(50), " +
                MovieDBSchema.Favourite.TITLE + " VARCHAR(50), " +
                MovieDBSchema.Favourite.VOTE_AVERAGE + " VARCHAR(10)" +
                " );";

        db.execSQL(SQL_CREATE_FAVOURITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST" + MovieDBSchema.Favourite.TABLE_NAME);
        onCreate(db);
    }

    public Long insertFavourite(int id, String poster_path, String overview,
                                String release_date, String title, String vote_average) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("poster_path", poster_path);
        values.put("title", title);
        values.put("vote_average", vote_average);
        values.put("release_date", release_date);
        values.put("overview", overview);
        return sqLiteDatabase.insert(MovieDBSchema.Favourite.TABLE_NAME, null, values);
    }

    public Cursor getMovieData() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor c = sqLiteDatabase.query(MovieDBSchema.Favourite.TABLE_NAME, null, null, null, null, null, null);
        return c;
    }

}
