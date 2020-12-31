package com.example.illumination;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Objects;

public class BookDetailActivity extends AppCompatActivity
{
    ImageView bookImage;
    TextView bookName, bookAuthor, bookPrice, bookDescription;
    Button addToCart, viewBookReviews;
    android.widget.RatingBar ratingBar;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    BookStoreDatabaseHelper bookStoreDatabaseHelper;
    SQLiteDatabase db;
    TextView rateText;
    String userEmail;
    Cart cart;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(false);
        setContentView(R.layout.activity_book_description);
        Intent intent = getIntent();
        bookImage = findViewById(R.id.book_image);
        bookName = findViewById(R.id.book_title);
        bookAuthor = findViewById(R.id.book_author);
        bookPrice = findViewById(R.id.book_price);
        bookDescription = findViewById(R.id.book_description);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("rating");
        bookStoreDatabaseHelper = new BookStoreDatabaseHelper(BookDetailActivity.this);
        db = bookStoreDatabaseHelper.getReadableDatabase();
        cart = new Cart();

        final int bookID = intent.getIntExtra("BookImageID", 0);
        String bookTitle = intent.getStringExtra("BookName");
        String bookWriter = intent.getStringExtra("BookAuthor");
        final int bookCost = intent.getIntExtra("BookPrice", 0);
        String bookDetail = intent.getStringExtra("BookDescription");

        if (bookID == 1)
            bookImage.setImageResource(R.drawable.book1);
        else if (bookID == 2)
            bookImage.setImageResource(R.drawable.book2);
        else if (bookID == 3)
            bookImage.setImageResource(R.drawable.book3);
        else if (bookID == 4)
            bookImage.setImageResource(R.drawable.book4);
        else if (bookID == 5)
            bookImage.setImageResource(R.drawable.book5);
        else if (bookID == 6)
            bookImage.setImageResource(R.drawable.book6);
        else if (bookID == 7)
            bookImage.setImageResource(R.drawable.book7);
        else if (bookID == 8)
            bookImage.setImageResource(R.drawable.book8);
        else if (bookID == 9)
            bookImage.setImageResource(R.drawable.book9);
        else if (bookID == 10)
            bookImage.setImageResource(R.drawable.book10);
        else if (bookID == 11)
            bookImage.setImageResource(R.drawable.book11);
        else if (bookID == 12)
            bookImage.setImageResource(R.drawable.book12);

        bookName.setText(bookTitle);
        bookAuthor.setText("By " + bookWriter);
        bookPrice.setText("Price: " + bookCost);
        bookDescription.setText(bookDetail);
        ratingBar = findViewById(R.id.ratingBar);

        Intent broadcastIntent = new Intent(this, RatingReceiver.class);
        broadcastIntent.setAction("com.example.illumination.CUSTOM_INTENT1");
        sendBroadcast(broadcastIntent);

        Cursor cursor1 = db.rawQuery("Select * From CurrentUser", null);
        cursor1.moveToFirst();
        Cursor cursor2 = db.rawQuery("Select * From Rating Where EmailID = '" + cursor1.getString(cursor1.getColumnIndex("Email")) +
                                     "' AND BookID = '" + Integer.toString(bookID) + "'", null);
        cursor1.close();
        rateText = findViewById(R.id.rate_book);

        if (cursor2.getCount() > 0)
        {
            rateText.setText("Your Rating");
            cursor2.moveToFirst();
            ratingBar.setRating(Float.parseFloat(cursor2.getString(cursor2.getColumnIndex("Rating"))));
            cursor2.close();
        }

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener()
        {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
            {
                //Set new values of rating bar.
                Cursor cursor1 = db.rawQuery("Select * From CurrentUser", null);
                cursor1.moveToFirst();
                Cursor cursor2 = db.rawQuery("Select * From Rating Where EmailID = '" + cursor1.getString(cursor1.getColumnIndex("Email")) +
                                             "' AND BookID = '" + Integer.toString(bookID) + "'", null);

                if (cursor2.getCount() > 0)
                {
                    cursor2.moveToFirst();
                    String email = cursor2.getString(cursor2.getColumnIndex("EmailID")).replace(".", "");

                    if (ratingBar.getProgress() == 0)
                    {
                        rateText.setText("Rate It");
                        databaseReference.child(email + String.valueOf(bookID)).removeValue();
                    }
                    else
                    {
                        rateText.setText("Your Rating");
                        databaseReference.child(email + String.valueOf(bookID)).child("Rating").setValue(String.valueOf(ratingBar.getRating()));
                    }

                    cursor2.close();
                }
                else
                {
                    Rating userRating = new Rating(cursor1.getString(cursor1.getColumnIndex("Email")),
                                                   Integer.toString(bookID),
                                                   String.valueOf(ratingBar.getRating()));
                    rateText.setText("Your Rating");
                    String email = cursor1.getString(cursor1.getColumnIndex("Email")).replace(".", "");
                    databaseReference.child(email + bookID).setValue(userRating);
                    cursor1.close();
                }
            }
        });

        Cursor cursor = db.rawQuery("Select Email From CurrentUser", null);

        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            userEmail = cursor.getString(cursor.getColumnIndex("Email"));
            cursor.close();
        }

        addToCart = findViewById(R.id.buyBook);
        addToCart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                bookName = findViewById(R.id.book_title);
                bookAuthor = findViewById(R.id.book_author);

                Cursor cursor = db.rawQuery("Select * From Cart",null);
                boolean flag = false;
                int size = cursor.getCount();
                cursor.moveToFirst();

                for (int i = 0; i < size; i++)
                {
                    String email = cursor.getString(cursor.getColumnIndex("Email"));
                    String book_Name = cursor.getString(cursor.getColumnIndex("BookName"));
                    String book_Author = cursor.getString(cursor.getColumnIndex("BookAuthor"));

                    if (email.equals(userEmail) && book_Name.equals(bookName.getText().toString())
                        && book_Author.equals(bookAuthor.getText().toString()))
                    {
                        flag = true;
                        break;
                    }

                    cursor.moveToNext();
                }

                if (!flag)
                {
                    ContentValues values = new ContentValues();
                    values.put(CartProvider.BookName, bookName.getText().toString());
                    values.put(CartProvider.BookAuthor, bookAuthor.getText().toString());
                    values.put(CartProvider.Email, userEmail);
                    values.put(CartProvider.Price, bookCost);
                    values.put(CartProvider.Quantity, 1);
                    db.insert("Cart", "", values);
                    Toast.makeText(BookDetailActivity.this, "Book Added to Cart", Toast.LENGTH_LONG).show();
                }
                else
                {
                    ContentValues values = new ContentValues();
                    Cursor _cursor = db.rawQuery("Select * From Cart",null);
                    _cursor.moveToFirst();
                    String db_email, db_bookName, db_bookAuthor;
                    int int_quantity = 0;
                    String string_quantity = "";

                    for (int i = 0; i < size; i++)
                    {
                        db_email = _cursor.getString(_cursor.getColumnIndex("Email"));
                        db_bookName = _cursor.getString(_cursor.getColumnIndex("BookName"));
                        db_bookAuthor = _cursor.getString(_cursor.getColumnIndex("BookAuthor"));

                        if (db_email.equals(userEmail) && db_bookName.equals(bookName.getText().toString())
                            && db_bookAuthor.equals(bookAuthor.getText().toString()))
                        {
                            int_quantity = _cursor.getInt(_cursor.getColumnIndex("Quantity")) + 1;
                            string_quantity = Integer.toString(int_quantity);
                        }

                        _cursor.moveToNext();
                    }

                    _cursor.close();

                    if(int_quantity > 10)
                    {
                        int_quantity = 10;
                        string_quantity = Integer.toString(int_quantity);
                        Toast.makeText(BookDetailActivity.this, "Cannot increase quantity beyond 10", Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(BookDetailActivity.this, "Quantity: " + int_quantity, Toast.LENGTH_LONG).show();

                    values.put(CartProvider.Quantity, Integer.parseInt(string_quantity));
                    db.update("Cart", values, "Email = ? AND BookName = ? AND BookAuthor = ?",
                              new String[]{userEmail, bookName.getText().toString(), bookAuthor.getText().toString()});
                }

                cursor.close();
            }
        });

        viewBookReviews = findViewById(R.id.viewReviews);
        viewBookReviews.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent bookReviewIntent = new Intent(BookDetailActivity.this, BookReviewActivity.class);
                bookReviewIntent.putExtra("BookID", String.valueOf(bookID));
                startActivity(bookReviewIntent);
            }
        });
    }

    @SuppressLint("RestrictedApi")
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if(menu instanceof MenuBuilder)
            ((MenuBuilder) menu).setOptionalIconsVisible(true);

        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.homeOption:
                Intent homeIntent = new Intent(BookDetailActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                return true;
            case R.id.cartOption:
                Intent cartIntent = new Intent(BookDetailActivity.this, CartActivity.class);
                startActivity(cartIntent);
                return true;
            case R.id.settingOption:
                Intent accountSettingIntent = new Intent(BookDetailActivity.this, AccountSettingActivity.class);
                startActivity(accountSettingIntent);
                return true;
            case R.id.viewOrderHistoryOption:
                Intent orderHistoryIntent = new Intent(BookDetailActivity.this, OrderHistoryActivity.class);
                startActivity(orderHistoryIntent);
                return true;
            case R.id.logOutOption:
                Intent logOutIntent = new Intent(BookDetailActivity.this, MainActivity.class);
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