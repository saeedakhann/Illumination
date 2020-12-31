package com.example.illumination;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.Objects;

public class MainActivity extends AppCompatActivity
{
    Intent signUpIntent;
    Intent homeIntent;
    EditText username, password;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getIntent().getBooleanExtra("EXIT", false))
        {
            finish();
        }

        if (!Objects.equals(getIntent().getStringExtra("LogOut"), "LOGOUT"))
        {
            if (!getIntent().getBooleanExtra("EXIT" , false))
            {
                if (isUserLoggedIn())
                {
                    homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(homeIntent);
                }
            }
        }

        int orientation = MainActivity.this.getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT)
            setContentView(R.layout.activity_login);
        else
            setContentView(R.layout.landscape_activity_login);

        Intent broadcastIntent = new Intent(this, UserReceiver.class);
        broadcastIntent.setAction("com.example.illumination.CUSTOM_INTENT");
        sendBroadcast(broadcastIntent);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        final Button logIn = findViewById(R.id.login);
        logIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (!username.getText().toString().equals("") && !password.getText().toString().equals(""))
                {
                    if (UserVerification())
                    {
                        homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(homeIntent);
                    }
                    else
                        Toast.makeText(MainActivity.this, "INCORRECT EMAIL OR PASSWORD!", Toast.LENGTH_SHORT).show();
                }
                else if (!username.getText().toString().equals(""))
                {
                    password.requestFocus();
                    Toast.makeText(MainActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    username.requestFocus();
                    Toast.makeText(MainActivity.this, "Enter Username", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Button signUp = findViewById(R.id.signUp);
        signUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                signUpIntent =  new Intent(MainActivity.this, CreateAccountActivity.class);
                startActivity(signUpIntent);
            }
        });
    }

    boolean isUserLoggedIn()
    {
        SQLiteOpenHelper dbHelper = new BookStoreDatabaseHelper(MainActivity.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * From CurrentUser", null);
        return cursor.getCount() > 0;
    }

    private boolean UserVerification()
    {
        SQLiteOpenHelper dbHelper = new BookStoreDatabaseHelper(MainActivity.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * From User where Email = '" + username.getText().toString() + "' and Password = '" +
                                    password.getText().toString() + "'" , null);

        if (cursor.getCount() > 0)
        {
            if (cursor.moveToFirst())
            {
                do
                {
                    ContentValues values = new ContentValues();
                    values.put(CurrentUserProvider.Name, cursor.getString(cursor.getColumnIndex("Name")));
                    values.put(CurrentUserProvider.Email, cursor.getString(cursor.getColumnIndex("Email")));
                    values.put(CurrentUserProvider.Address, cursor.getString(cursor.getColumnIndex("Address")));
                    values.put(CurrentUserProvider.PhoneNumber, cursor.getString(cursor.getColumnIndex("PhoneNumber")));
                    values.put(CurrentUserProvider.Password, cursor.getString(cursor.getColumnIndex("Password")));
                    getContentResolver().insert(CurrentUserProvider.CONTENT_URI, values);
                }
                while (cursor.moveToNext());
            }

            cursor.close();
            return true;
        }

        return false;
    }

    public void ShowAndHidePassword(View view)
    {
        ImageView imageView = findViewById(R.id.show_password_button);

        if (password != null && imageView != null)
        {
            if (password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance()))
            {
                imageView.setImageResource(R.drawable.hide_password);
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else
            {
                imageView.setImageResource(R.drawable.show_password);
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent exitApp = new Intent(getApplicationContext(), MainActivity.class);
        exitApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        exitApp.putExtra("EXIT", true);
        startActivity(exitApp);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        SQLiteOpenHelper dbHelper = new BookStoreDatabaseHelper(MainActivity.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * From CurrentUser", null);

        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            db.delete("Cart", "Email = ?", new String[]{cursor.getString(cursor.getColumnIndex("Email"))});
        }
    }
}