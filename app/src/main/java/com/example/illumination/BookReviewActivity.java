package com.example.illumination;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Objects;

public class BookReviewActivity extends AppCompatActivity
{
    ReviewListAdapter reviewListAdapter;
    ListView bookReviewList;
    ArrayList<Review> bookReviews;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAnalytics firebaseAnalytics;
    String userReview;
    EditText reviewText;
    int reviewID;
    boolean flag = false;
    String review_ID, user_comment;
    AdapterView.AdapterContextMenuInfo info;
    AdView adView;
    SwipeRefreshLayout pullToRefresh;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(false);
        setContentView(R.layout.activity_book_reviews);

        adView = findViewById(R.id.ad_view_review);
        MobileAds.initialize(this,"ca-app-pub-5095893688489893~1033123996");
        adView.setAdListener(new AdListener()
        {
            @Override
            public void onAdLoaded()
            {
                adView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode)
            {
                Toast.makeText(BookReviewActivity.this, "Ad Failed to Load!", Toast.LENGTH_SHORT).show();
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("reviews");
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        bookReviews = new ArrayList<>();
        bookReviewList = findViewById(R.id.reviewList);

        Intent broadcastIntent = new Intent(this, ReviewReceiver.class);
        broadcastIntent.setAction("com.example.illumination.CUSTOM_INTENT2");
        sendBroadcast(broadcastIntent);

        final DatabaseReference reviewReference = FirebaseDatabase.getInstance().getReference("ReviewID");
        reviewReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                try
                {
                    String value = "";
                    value = dataSnapshot.getValue(String.class);
                    reviewID = Integer.parseInt(value);
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
                bundle.putString("ERROR", "Review Activity Database Failure");
                FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(BookReviewActivity.this);
                firebaseAnalytics.logEvent("Review_Activity_Database_Update_Failed", bundle);
            }
        });

        SQLiteOpenHelper dbHelper = new BookStoreDatabaseHelper(BookReviewActivity.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * From Review Where BookID = '" + getIntent().getStringExtra("BookID") + "'",
                null);
        Cursor checkUsers = db.rawQuery("Select * From User", null);

        if (cursor.getCount() > 0)
        {
            if (cursor.moveToFirst())
            {
                do
                {
                    if (checkUsers.getCount() > 0)
                    {
                        if (checkUsers.moveToFirst())
                        {
                            do
                            {
                                if (cursor.getString(cursor.getColumnIndex("EmailID")).equals
                                        (checkUsers.getString(checkUsers.getColumnIndex("Email"))))
                                {
                                    Review review = new Review();
                                    review.ReviewID = cursor.getString(cursor.getColumnIndex("ReviewID"));
                                    review.EmailID = cursor.getString(cursor.getColumnIndex("EmailID"));
                                    review.BookID = cursor.getString(cursor.getColumnIndex("BookID"));
                                    review.Comment = cursor.getString(cursor.getColumnIndex("Comment"));
                                    bookReviews.add(review);
                                    break;
                                }
                            }
                            while (checkUsers.moveToNext());
                        }
                    }
                }
                while (cursor.moveToNext());
            }
        }

        pullToRefresh = findViewById(R.id.swipeToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                SQLiteOpenHelper dbHelper = new BookStoreDatabaseHelper(BookReviewActivity.this);
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("Select * From Review Where BookID = '" + getIntent().getStringExtra("BookID") + "'",
                        null);
                Cursor checkUsers = db.rawQuery("Select * From User", null);
                bookReviews.clear();

                if (cursor.getCount() > 0)
                {
                    if (cursor.moveToFirst())
                    {
                        do
                        {
                            if (checkUsers.getCount() > 0)
                            {
                                if (checkUsers.moveToFirst())
                                {
                                    do
                                    {
                                        if (cursor.getString(cursor.getColumnIndex("EmailID")).equals
                                                (checkUsers.getString(checkUsers.getColumnIndex("Email"))))
                                        {
                                            Review review = new Review();
                                            review.ReviewID = cursor.getString(cursor.getColumnIndex("ReviewID"));
                                            review.EmailID = cursor.getString(cursor.getColumnIndex("EmailID"));
                                            review.BookID = cursor.getString(cursor.getColumnIndex("BookID"));
                                            review.Comment = cursor.getString(cursor.getColumnIndex("Comment"));
                                            bookReviews.add(review);
                                            break;
                                        }
                                    }
                                    while (checkUsers.moveToNext());
                                }
                            }
                        }
                        while (cursor.moveToNext());
                    }
                }

                reviewListAdapter.notifyDataSetChanged();
                registerForContextMenu(bookReviewList);
                pullToRefresh.setRefreshing(false);
            }
        });
        reviewListAdapter = new ReviewListAdapter(BookReviewActivity.this, R.layout.activity_review_list_view, bookReviews);
        bookReviewList.setAdapter(reviewListAdapter);
        registerForContextMenu(bookReviewList);

        reviewText = findViewById(R.id.addReview);
        FloatingActionButton submitReview = findViewById(R.id.submitReview);
        submitReview.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!flag)
                {
                    userReview = reviewText.getText().toString();

                    if (!userReview.isEmpty())
                    {
                        Review review = new Review();
                        SQLiteOpenHelper dbHelper = new BookStoreDatabaseHelper(BookReviewActivity.this);
                        SQLiteDatabase db = dbHelper.getReadableDatabase();
                        Cursor cursor = db.rawQuery("Select * From CurrentUser", null);

                        if (cursor.getCount() > 0)
                        {
                            cursor.moveToFirst();
                            review.ReviewID = String.valueOf(reviewID);
                            review.EmailID = cursor.getString(cursor.getColumnIndex("Email"));
                            review.BookID = getIntent().getStringExtra("BookID");
                            review.Comment = userReview;
                            databaseReference.child(Integer.toString(reviewID)).setValue(review);
                            reviewReference.setValue(String.valueOf(reviewID + 1));
                            cursor.close();
                        }

                        bookReviews.add(review);
                        reviewListAdapter.notifyDataSetChanged();
                        reviewText.setText("");
                    }
                    else
                        Toast.makeText(BookReviewActivity.this, "Empty comment NOT allowed.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    flag = false;
                    String new_comment = reviewText.getText().toString();
                    databaseReference.child(review_ID).child("Comment").setValue(new_comment);
                    reviewListAdapter.notifyDataSetChanged();
                    Objects.requireNonNull(reviewListAdapter.getItem(info.position)).Comment = new_comment;
                    reviewText.setText("");
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        String user_email = Objects.requireNonNull(reviewListAdapter.getItem(info.position)).EmailID;

        SQLiteOpenHelper dbHelper = new BookStoreDatabaseHelper(BookReviewActivity.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * From CurrentUser", null);

        if (cursor.getCount() > 0)
        {
            if (cursor.moveToFirst())
            {
                if (!user_email.equals(cursor.getString(cursor.getColumnIndex("Email"))))
                {
                    menu.getItem(0).setEnabled(false);
                    menu.getItem(1).setEnabled(false);
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onContextItemSelected(MenuItem menuItem)
    {
        info = (AdapterView.AdapterContextMenuInfo)menuItem.getMenuInfo();
        review_ID = Objects.requireNonNull(reviewListAdapter.getItem(info.position)).ReviewID;
        user_comment = Objects.requireNonNull(reviewListAdapter.getItem(info.position)).Comment;

        switch (menuItem.getItemId())
        {
            case R.id.updateOption:
                reviewText.setText(user_comment);
                flag = true;
                return true;
            case R.id.deleteOption:
                DatabaseReference reviewReference = FirebaseDatabase.getInstance().getReference("reviews");
                reviewReference.child(review_ID).removeValue();
                reviewListAdapter.remove(reviewListAdapter.getItem(info.position));
                reviewListAdapter.notifyDataSetChanged();
                return true;
        }

        return false;
    }
}