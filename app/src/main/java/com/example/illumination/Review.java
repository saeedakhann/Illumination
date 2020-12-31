package com.example.illumination;

public class Review
{
    public String ReviewID;
    public String EmailID;
    public String BookID;
    public String Comment;

    public Review()
    {
        this.ReviewID = null;
        this.EmailID = null;
        this.BookID = null;
        this.Comment = null;
    }

    public Review(String reviewID, String emailID, String bookID, String comment)
    {
        this.ReviewID = reviewID;
        this.EmailID = emailID;
        this.BookID = bookID;
        this.Comment = comment;
    }
}