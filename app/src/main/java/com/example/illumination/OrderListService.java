package com.example.illumination;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
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

public class OrderListService extends Service
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
        FirebaseApp.initializeApp(com.example.illumination.OrderListService.this);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("orders");

        Handler = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                try
                {
                    getContentResolver().delete(OrderProvider.CONTENT_URI , "" , null);
                    Order value;

                    for (DataSnapshot data : dataSnapshot.getChildren())
                    {
                        value = data.getValue(Order.class);
                        ContentValues values = new ContentValues();
                        values.put(OrderProvider.Books, value.Books);
                        values.put(OrderProvider.Email, value.Email);
                        values.put(OrderProvider.TotalPrice, value.TotalPrice);
                        values.put(OrderProvider.Date, value.Date);
                        getContentResolver().insert(OrderProvider.CONTENT_URI , values);
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
                bundle.putString("ERROR", "Order List Database Failure");
                FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(com.example.illumination.OrderListService.this);
                firebaseAnalytics.logEvent("Order_List_Database_Update_Failed", bundle);
            }
        };

        myRef.addValueEventListener(Handler);
    }
}
