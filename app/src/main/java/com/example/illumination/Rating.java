package com.example.illumination;

public class Rating
{
    public String EmailID;
    public String BookID;
    public String Rating;

    public Rating()
    {
        this.EmailID = null;
        this.BookID = null;
        this.Rating = null;
    }

    public Rating(String emailID, String bookID, String rating)
    {
        this.EmailID = emailID;
        this.BookID = bookID;
        this.Rating = rating;
    }
}