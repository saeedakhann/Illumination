package com.example.illumination;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BookStoreDatabaseHelper extends SQLiteOpenHelper
{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "BookStore.db";
    public static final String TABLE_NAME = "CurrentUser";
    public static final String TABLE_NAME_1 = "User";
    public static final String TABLE_NAME_2 = "Cart";
    public static final String TABLE_NAME_3 = "Review";
    public static final String TABLE_NAME_4 = "Rating";
    public static final String TABLE_NAME_5 = "Orders";

    public BookStoreDatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db)
    {
        String sql = "CREATE TABLE " + TABLE_NAME +
                     "(Name TEXT NOT NULL," +
                     "Email TEXT PRIMARY KEY NOT NULL," +
                     "Password TEXT NOT NULL," +
                     "PhoneNumber TEXT," +
                     "Address TEXT);";

        String sql1 = "CREATE TABLE " + TABLE_NAME_1 +
                      "(Name TEXT NOT NULL," +
                      "Email TEXT PRIMARY KEY NOT NULL," +
                      "Password TEXT NOT NULL," +
                      "PhoneNumber TEXT," +
                      "Address TEXT);";

        String sql2 = "CREATE TABLE " + TABLE_NAME_2 +
                      "(Email TEXT NOT NULL," +
                      "BookAuthor TEXT NOT NULL," +
                      "BookName TEXT," +
                      "Price INTEGER," +
                      "Quantity INTEGER," +
                      "PRIMARY KEY (Email, BookName)"+ ");";

        String sql3 = "CREATE TABLE " + TABLE_NAME_3 +
                      "(ReviewID TEXT," +
                      "EmailID TEXT NOT NULL," +
                      "BookID INTEGER NOT NULL," +
                      "Comment TEXT);";

        String sql4 = "CREATE TABLE " + TABLE_NAME_4 +
                      "(EmailID TEXT, " +
                      "BookID TEXT," +
                      "Rating TEXT);";

        String sql5 = "CREATE TABLE " + TABLE_NAME_5 +
                      "(Books TEXT, " +
                      "Email TEXT," +
                      "TotalPrice TEXT," +
                      "Date TEXT)";

        db.execSQL(sql);
        db.execSQL(sql1);
        db.execSQL(sql2);
        db.execSQL(sql3);
        db.execSQL(sql4);
        db.execSQL(sql5);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_3);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_4);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_5);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onUpgrade(db, oldVersion, newVersion);
    }
}