package com.example.illumination;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserService extends Service
{
    FirebaseDatabase database;
    DatabaseReference myRef;
    ValueEventListener Handler;

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        FirebaseApp.initializeApp(UserService.this);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

        Handler = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                try
                {
                    boolean CurrentUserExists = false;
                    User CurrentUser = null;
                    User value;
                    SQLiteOpenHelper dbHelper = new BookStoreDatabaseHelper(UserService.this);
                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    Cursor cursor = db.rawQuery("Select * From CurrentUser" , null);

                    if (cursor.getCount() > 0)
                    {
                        if (cursor.moveToFirst())
                        {
                            do
                            {
                                CurrentUser = new User();
                                CurrentUser.Username = cursor.getString(cursor.getColumnIndex("Name"));
                                CurrentUser.Email = cursor.getString(cursor.getColumnIndex("Email"));
                                CurrentUser.Address = cursor.getString(cursor.getColumnIndex("Address"));
                                CurrentUser.PhoneNumber = cursor.getString(cursor.getColumnIndex("PhoneNumber"));
                                CurrentUser.Password = cursor.getString(cursor.getColumnIndex("Password"));
                            }
                            while (cursor.moveToNext());
                        }
                    }

                    getContentResolver().delete(UserProvider.CONTENT_URI , "" , null);

                    for (DataSnapshot data : dataSnapshot.getChildren())
                    {
                        value = data.getValue(User.class);

                        if (CurrentUser != null && value.Email.equalsIgnoreCase(CurrentUser.Email) && value.Password.equals(CurrentUser.Password))
                        {
                            CurrentUserExists = true;
                            ContentValues values = new ContentValues();
                            values.put(CurrentUserProvider.Name, value.Username);
                            values.put(CurrentUserProvider.PhoneNumber, value.PhoneNumber);
                            values.put(CurrentUserProvider.Address, value.Address);
                            getContentResolver().update(CurrentUserProvider.CONTENT_URI, values, "", null);
                        }

                        ContentValues values = new ContentValues();
                        values.put(UserProvider.Name , value.Username);
                        values.put(UserProvider.Email , value.Email);
                        values.put(UserProvider.Password , value.Password);
                        values.put(UserProvider.PhoneNumber , value.PhoneNumber);
                        values.put(UserProvider.Address , value.Address);
                        getContentResolver().insert(UserProvider.CONTENT_URI , values);
                    }

                    if (!CurrentUserExists && CurrentUser != null)
                    {
                        Toast.makeText(UserService.this, "ACCOUNT DELETED OR PASSWORD CHANGED!", Toast.LENGTH_LONG).show();
                        Intent logOutIntent = new Intent(UserService.this, MainActivity.class);
                        logOutIntent.putExtra("LogOut", "LOGOUT");
                        getContentResolver().delete(CurrentUserProvider.CONTENT_URI, "", null);
                        logOutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(logOutIntent);
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError error)
            {
                Bundle bundle = new Bundle();
                bundle.putString("ERROR", "User Database Failure");
                FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(UserService.this);
                firebaseAnalytics.logEvent("User_Database_Update_Failed", bundle);
            }
        };

        myRef.addValueEventListener(Handler);
    }
}