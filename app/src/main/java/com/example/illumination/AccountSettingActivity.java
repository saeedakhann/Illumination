package com.example.illumination;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Objects;

public class AccountSettingActivity extends AppCompatActivity
{
    Button editAccount, deleteAccount;
    AlertDialog.Builder alertDialogBuilder;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(false);
        int orientation = AccountSettingActivity.this.getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT)
            setContentView(R.layout.activity_account_settings);
        else
            setContentView(R.layout.landscape_activity_account_settings);

        editAccount = findViewById(R.id.editAccount);
        deleteAccount = findViewById(R.id.deleteAccount);

        editAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent editAccountIntent = new Intent(AccountSettingActivity.this, CreateAccountActivity.class);
                editAccountIntent.putExtra("New Activity Name", "Edit Account");
                startActivity(editAccountIntent);
            }
        });

        deleteAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                alertDialogBuilder = new AlertDialog.Builder(AccountSettingActivity.this);
                alertDialogBuilder.setTitle("Confirm Account Deletion");

                alertDialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        BookStoreDatabaseHelper dbHelper = new BookStoreDatabaseHelper(AccountSettingActivity.this);
                        SQLiteDatabase db = dbHelper.getReadableDatabase();
                        Cursor cursor = db.rawQuery("Select * From CurrentUser", null);

                        if (cursor.getCount() > 0)
                        {
                            cursor.moveToFirst();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("users");
                            String UserEmail = (cursor.getString(cursor.getColumnIndex("Email")).replace(".", ""));
                            myRef.child(UserEmail).removeValue();
                        }
                    }
                });

                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();
                    }
                });

                alertDialogBuilder.show();
            }
        });
    }

    @SuppressLint("RestrictedApi")
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if(menu instanceof MenuBuilder)
            ((MenuBuilder) menu).setOptionalIconsVisible(true);

        getMenuInflater().inflate(R.menu.options_menu, menu);
        menu.getItem(2).setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.homeOption:
                Intent homeIntent = new Intent(AccountSettingActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                return true;
            case R.id.cartOption:
                Intent cartIntent = new Intent(AccountSettingActivity.this, CartActivity.class);
                startActivity(cartIntent);
                return true;
            case R.id.viewOrderHistoryOption:
                Intent orderHistoryIntent = new Intent(AccountSettingActivity.this, OrderHistoryActivity.class);
                startActivity(orderHistoryIntent);
                return true;
            case R.id.logOutOption:
                Intent logOutIntent = new Intent(AccountSettingActivity.this, MainActivity.class);
                logOutIntent.putExtra("LogOut", "LOGOUT");
                getContentResolver().delete(CurrentUserProvider.CONTENT_URI, "", null);
                startActivity(logOutIntent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }
}