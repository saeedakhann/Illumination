package com.example.illumination;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReviewService extends Service
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
        FirebaseApp.initializeApp(ReviewService.this);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("reviews");

        Handler = new ValueEventListener()
        {
            @SuppressLint("ResourceType")
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                try
                {
                    Review value;
                    getContentResolver().delete(ReviewProvider.CONTENT_URI , "" , null);

                    for (DataSnapshot data : dataSnapshot.getChildren())
                    {
                        value = data.getValue(Review.class);
                        ContentValues values = new ContentValues();
                        values.put(ReviewProvider.ReviewID, value.ReviewID);
                        values.put(ReviewProvider.EmailID, value.EmailID);
                        values.put(ReviewProvider.BookID, value.BookID);
                        values.put(ReviewProvider.Comment, value.Comment);
                        getContentResolver().insert(ReviewProvider.CONTENT_URI , values);
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                // Failed to read value.
                Bundle bundle = new Bundle();
                bundle.putString("ERROR", "Review Database Failure");
                FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(ReviewService.this);
                firebaseAnalytics.logEvent("Review_Database_Update_Failed", bundle);
            }
        };

        myRef.addValueEventListener(Handler);
        Toast.makeText(ReviewService.this , "DATABASE SERVICE CREATED!" , Toast.LENGTH_LONG).show();
    }
}