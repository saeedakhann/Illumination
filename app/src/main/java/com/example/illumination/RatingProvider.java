package com.example.illumination;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class RatingProvider extends ContentProvider
{
    static final String PROVIDER_NAME = "com.example.illumination.RatingProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/rating";
    static final Uri CONTENT_URI = Uri.parse(URL);
    static final UriMatcher uriMatcher;
    static final int uriCode = 1;
    static final String EmailID = "EmailID";
    static final String BookID = "BookID";
    static final String Rating = "Rating";
    private BookStoreDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    static
    {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "rating", uriCode);
        uriMatcher.addURI(PROVIDER_NAME, "rating/*", uriCode);
    }

    public boolean onCreate()
    {
        dbHelper = new BookStoreDatabaseHelper(getContext());
        db = dbHelper.getWritableDatabase();

        if (db != null)
            return true;

        return false;
    }

    public String getType(Uri uri)
    {
        switch (uriMatcher.match(uri)) {
            case uriCode:
                return "vnd.android.cursor.dir/rating";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    public Uri insert(Uri uri, ContentValues values)
    {
        long rowID = db.insert(dbHelper.TABLE_NAME_4, "", values);

        if (rowID > 0)
        {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);
    }

    public int update(Uri uri, ContentValues values, String selection, String[] args)
    {
        int count = 0;
        switch (uriMatcher.match(uri))
        {
            case uriCode:
                count = db.update(dbHelper.TABLE_NAME_4, values, selection, args);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    public int delete(Uri uri, String selection, String[] args)
    {
        int count = 0;
        switch (uriMatcher.match(uri))
        {
            case uriCode:
                count = db.delete(dbHelper.TABLE_NAME_4, selection, args);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    public Cursor query(Uri uri, String []projection, String selection, String[] selectionArgs, String sortOrder)
    {
        if (uriMatcher.match(uri) == 1)
            return db.rawQuery("Select * From Rating", null);

        return null;
    }
}