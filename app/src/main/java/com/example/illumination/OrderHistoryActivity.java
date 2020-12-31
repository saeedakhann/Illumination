package com.example.illumination;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Objects;

public class OrderHistoryActivity extends AppCompatActivity
{
    ArrayAdapter<String> OrderHistoryAdapter;
    ListView OrderHistoryList;
    ArrayList<String> OrderHistory;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAnalytics firebaseAnalytics;
    SwipeRefreshLayout pullToRefresh;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(false);
        setContentView(R.layout.activity_order_history);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("orders");
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        OrderHistory = new ArrayList<>();
        OrderHistoryList = findViewById(R.id.orderHistoryList);

        Intent broadcastIntent = new Intent(this, OrderListReceiver.class);
        broadcastIntent.setAction("com.example.illumination.CUSTOM_INTENT3");
        sendBroadcast(broadcastIntent);

        SQLiteOpenHelper dbHelper = new BookStoreDatabaseHelper(OrderHistoryActivity.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * From CurrentUser" , null);
        String Email = null;

        if (cursor.getCount() > 0)
        {
            if (cursor.moveToFirst())
                Email = cursor.getString(cursor.getColumnIndex("Email"));
        }

        cursor = db.rawQuery("Select * From Orders Where Email = '" + Email + "'" , null);

        if (cursor.getCount() > 0)
        {
            TextView textView = findViewById(R.id.orderHistoryEmpty);
            textView.setText("");

            if (cursor.moveToFirst())
            {
                do
                {
                    String Order = new String();
                    Order = "Date: " + cursor.getString(cursor.getColumnIndex("Date")) + "\n" +
                            cursor.getString(cursor.getColumnIndex("Books")) + "\n" + "Total Price: " +
                            cursor.getString(cursor.getColumnIndex("TotalPrice"));
                    OrderHistory.add(Order);
                }
                while (cursor.moveToNext());
            }
        }

        OrderHistoryAdapter = new ArrayAdapter<>(OrderHistoryActivity.this, android.R.layout.simple_list_item_1, OrderHistory);
        OrderHistoryList.setAdapter(OrderHistoryAdapter);

        pullToRefresh = findViewById(R.id.swipeToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                SQLiteOpenHelper dbHelper = new BookStoreDatabaseHelper(OrderHistoryActivity.this);
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("Select * From CurrentUser" , null);
                String Email = null;
                OrderHistory.clear();

                if (cursor.getCount() > 0)
                {
                    if (cursor.moveToFirst())
                        Email = cursor.getString(cursor.getColumnIndex("Email"));
                }

                cursor = db.rawQuery("Select * From Orders Where Email = '" + Email + "'" , null);

                if (cursor.getCount() > 0)
                {
                    TextView textView = findViewById(R.id.orderHistoryEmpty);
                    textView.setText("");

                    if (cursor.moveToFirst())
                    {
                        do
                        {
                            String Order = new String();
                            Order = "Date: " + cursor.getString(cursor.getColumnIndex("Date")) + "\n" +
                                    cursor.getString(cursor.getColumnIndex("Books")) + "\n" + "Total Price: " +
                                    cursor.getString(cursor.getColumnIndex("TotalPrice")) + "\n";
                            OrderHistory.add(Order);
                        }
                        while (cursor.moveToNext());
                    }
                }

                OrderHistoryAdapter = new ArrayAdapter<>(OrderHistoryActivity.this, android.R.layout.simple_list_item_1, OrderHistory);
                OrderHistoryList.setAdapter(OrderHistoryAdapter);
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    @SuppressLint("RestrictedApi")
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if(menu instanceof MenuBuilder)
            ((MenuBuilder) menu).setOptionalIconsVisible(true);

        getMenuInflater().inflate(R.menu.options_menu, menu);
        menu.getItem(3).setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.homeOption:
                Intent homeIntent = new Intent(OrderHistoryActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                return true;
            case R.id.cartOption:
                Intent cartIntent = new Intent(OrderHistoryActivity.this, CartActivity.class);
                startActivity(cartIntent);
                return true;
            case R.id.settingOption:
                Intent accountSettingIntent = new Intent(OrderHistoryActivity.this, AccountSettingActivity.class);
                startActivity(accountSettingIntent);
                return true;
            case R.id.logOutOption:
                Intent logOutIntent = new Intent(OrderHistoryActivity.this, MainActivity.class);
                logOutIntent.putExtra("LogOut", "LOGOUT");
                getContentResolver().delete(CurrentUserProvider.CONTENT_URI, "", null);
                startActivity(logOutIntent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}