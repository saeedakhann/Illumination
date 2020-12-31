package com.example.illumination;

import java.util.StringTokenizer;

public class Order
{
    public String Books;
    public String Email;
    public String TotalPrice;
    public String Date;

    public Order()
    {
        this.Books = null;
        this.Email = null;
        this.TotalPrice = null;
        this.Date = null;
    }

    public Order(String books, String email, String totalPrice, String date)
    {
        this.Books = books;
        this.Email = email;
        this.TotalPrice = totalPrice;
        this.Date = date;
    }
}