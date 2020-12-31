package com.example.illumination;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class PlaceOrderActivity extends AppCompatActivity
{
    String Email;
    int TotalPrice;
    String Phone;
    String Address;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference orderReference;
    AdView adView1;
    InterstitialAd adView2;
    int OrderID;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(false);

        Intent broadcastIntent = new Intent(this, OrderListReceiver.class);
        broadcastIntent.setAction("com.example.illumination.CUSTOM_INTENT3");
        sendBroadcast(broadcastIntent);

        final DatabaseReference orderIDReference = FirebaseDatabase.getInstance().getReference("OrderID");
        orderIDReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                try
                {
                    String value = "";
                    value = dataSnapshot.getValue(String.class);
                    OrderID = Integer.parseInt(value);
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
                bundle.putString("ERROR" , "Order ID Database Failure");
                FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(PlaceOrderActivity.this);
                firebaseAnalytics.logEvent("OrderID_Database_Update_Failed" , bundle);
            }
        });

        MobileAds.initialize(this,"ca-app-pub-5095893688489893~1033123996");
        adView1 = findViewById(R.id.ad_view2);
        adView1.setAdListener(new AdListener()
        {
            @Override
            public void onAdLoaded()
            {
                adView1.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode)
            {
                Toast.makeText(PlaceOrderActivity.this, "Ad Failed to Load", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened()
            {
                Toast.makeText(PlaceOrderActivity.this, "Ad Opened", Toast.LENGTH_SHORT).show();
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        adView1.loadAd(adRequest);

        SQLiteOpenHelper dbHelper = new BookStoreDatabaseHelper(PlaceOrderActivity.this);
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * From CurrentUser", null);

        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            Email = cursor.getString(cursor.getColumnIndex("Email"));
            Phone = cursor.getString(cursor.getColumnIndex("PhoneNumber"));
            Address = cursor.getString(cursor.getColumnIndex("Address"));
        }

        cursor = db.rawQuery("Select * From Cart", null);
        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount(); i++)
        {
            if (cursor.getString(cursor.getColumnIndex("Email")).equals(Email))
            {
                int price = cursor.getInt(cursor.getColumnIndex("Price"));
                int quantity = cursor.getInt(cursor.getColumnIndex("Quantity"));
                TotalPrice = TotalPrice + (price * quantity);
            }

            cursor.moveToNext();
        }

        cursor.close();
        TextView email = findViewById(R.id.email);
        TextView phone = findViewById(R.id.phone);
        TextView address = findViewById(R.id.address);
        TextView total_price = findViewById(R.id.total_price);

        email.setText("Email: ".concat(Email));
        phone.setText("Phone: ".concat(Phone));
        address.setText("Address: ".concat(Address));
        total_price.setText("Total Price: ".concat(Integer.toString(TotalPrice)));

        Button ConfirmOrder = findViewById(R.id.Place_Order);
        ConfirmOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Order order = new Order();
                order.Email = Email;
                order.TotalPrice = Integer.toString(TotalPrice);

                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                String date = simpleDateFormat.format(Calendar.getInstance().getTime());
                order.Date = date;

                String bookList = "";
                String temp;
                Cursor cursor = db.rawQuery("Select * From Cart", null);
                cursor.moveToFirst();

                for (int i = 0; i < cursor.getCount(); i++)
                {
                    if (cursor.getString(cursor.getColumnIndex("Email")).equals(Email))
                    {
                        String bookName = cursor.getString(cursor.getColumnIndex("BookName"));
                        int quantity = cursor.getInt(cursor.getColumnIndex("Quantity"));
                        int price = cursor.getInt(cursor.getColumnIndex("Price"));

                        temp = bookName + "\nPrice: " + Integer.toString(price) + "\nQuantity: " + Integer.toString(quantity) + "\n\n";
                        bookList = bookList + temp;
                    }

                    cursor.moveToNext();
                }

                cursor.close();
                order.Books = bookList;

                firebaseDatabase = FirebaseDatabase.getInstance();
                orderReference = firebaseDatabase.getReference("orders");
                orderReference.child(Integer.toString(OrderID)).setValue(order);
                orderIDReference.setValue(Integer.toString(OrderID + 1));

                Toast.makeText(PlaceOrderActivity.this, "Your order has been placed.", Toast.LENGTH_LONG).show();
                db.delete("Cart","Email = ?", new String[]{Email});
                Intent launchIntent = new Intent(PlaceOrderActivity.this, HomeActivity.class);
                startActivity(launchIntent);
                finish();
            }
        });

        adView2 = new InterstitialAd(this);
        adView2.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        adView2.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());

        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }
}