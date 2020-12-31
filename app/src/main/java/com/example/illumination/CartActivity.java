package com.example.illumination;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import java.util.ArrayList;
import java.util.Objects;

public class CartActivity extends AppCompatActivity
{
    ListView cartItems;
    CartListAdapter cartListAdapter;
    int TotalPrice;
    String userEmail;
    InterstitialAd adView;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(false);
        setContentView(R.layout.activity_cart);
        adView = new InterstitialAd(this);
        adView.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        adView.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());

        cartItems = findViewById(R.id.CartListView);
        final ArrayList<Cart> CartList = new ArrayList<>();
        final TextView cartEmpty = findViewById(R.id.cart_empty);

        final Button CheckoutButton = findViewById(R.id.Place_Order);
        CheckoutButton.setClickable(true);
        CheckoutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                adView.show();
                Intent launchIntent = new Intent(CartActivity.this, PlaceOrderActivity.class);

                if (CartList.size() != 0)
                    startActivity(launchIntent);
                else
                {
                    CheckoutButton.setClickable(false);
                    cartEmpty.setText("Cart Empty");
                }
            }
        });

        SQLiteOpenHelper dbHelper = new BookStoreDatabaseHelper(CartActivity.this);
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select Email From User", null);

        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            userEmail = cursor.getString(cursor.getColumnIndex("Email"));
        }

        cursor = db.rawQuery("Select * From Cart", null);
        cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++)
        {
            String email = cursor.getString(cursor.getColumnIndex("Email"));

            if (email.equals(userEmail))
            {
                Cart cart = new Cart();
                String bookName = cursor.getString(cursor.getColumnIndex("BookName"));
                String bookAuthor = cursor.getString(cursor.getColumnIndex("BookAuthor"));
                int quantity = cursor.getInt(4);
                int price = cursor.getInt(3);

                cart.SetEmail(userEmail);
                cart.SetBookName(bookName);
                cart.SetBookAuthor(bookAuthor);
                cart.SetQuantity(quantity);
                cart.SetPrice(price);
                TotalPrice = TotalPrice + (price * quantity);
                CartList.add(cart);
            }

            cursor.moveToNext();
        }

        cursor.close();

        if (CartList.size() != 0)
        {
            cartListAdapter = new CartListAdapter(CartActivity.this, R.layout.activity_cart_listview, CartList);
            cartItems.setAdapter(cartListAdapter);
        }
        else
        {
            cartEmpty.setText("Cart Empty");
            CheckoutButton.setClickable(false);
        }
    }

    @SuppressLint("RestrictedApi")
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if(menu instanceof MenuBuilder)
            ((MenuBuilder) menu).setOptionalIconsVisible(true);

        getMenuInflater().inflate(R.menu.options_menu, menu);
        menu.getItem(1).setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.homeOption:
                Intent homeIntent = new Intent(CartActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                return true;
            case R.id.settingOption:
                Intent accountSettingIntent = new Intent(CartActivity.this, AccountSettingActivity.class);
                startActivity(accountSettingIntent);
                return true;
            case R.id.viewOrderHistoryOption:
                Intent orderHistoryIntent = new Intent(CartActivity.this, OrderHistoryActivity.class);
                startActivity(orderHistoryIntent);
                return true;
            case R.id.logOutOption:
                Intent logOutIntent = new Intent(CartActivity.this, MainActivity.class);
                logOutIntent.putExtra("LogOut", "LOGOUT");
                getContentResolver().delete(CurrentUserProvider.CONTENT_URI, "", null);
                startActivity(logOutIntent);
                finish();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}