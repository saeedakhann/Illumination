package com.example.illumination;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.RequiresApi;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RatingService extends Service
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
        FirebaseApp.initializeApp(RatingService.this);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("rating");

        Handler = new ValueEventListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                try
                {
                    Rating value;
                    getContentResolver().delete(RatingProvider.CONTENT_URI , "" , null);

                    for (DataSnapshot data : dataSnapshot.getChildren())
                    {
                        value = data.getValue(Rating.class);
                        ContentValues values = new ContentValues();
                        values.put(RatingProvider.EmailID, value.EmailID);
                        values.put(RatingProvider.BookID, value.BookID);
                        values.put(RatingProvider.Rating, value.Rating);
                        getContentResolver().insert(RatingProvider.CONTENT_URI , values);
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
                // Failed to read value.
                Bundle bundle = new Bundle();
                bundle.putString("ERROR", "Rating Database Failure");
                FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(RatingService.this);
                firebaseAnalytics.logEvent("Rating_Database_Update_Failed", bundle);
            }
        };

        myRef.addValueEventListener(Handler);
    }
}