package com.example.illumination;

public class Cart
{
    private String Email;
    private String BookName;
    private String BookAuthor;
    private Integer Quantity;
    private Integer Price;

    Cart()
    {
        this.Email = null;
        this.BookName = null;
        this.BookAuthor = null;
        this.Quantity = 1;
        this.Price = 0;
    };

    void SetEmail(String email)
    {
        this.Email = email;
    }

    void SetBookName(String bookName)
    {
        this.BookName = bookName;
    }

    void SetBookAuthor(String bookAuthor)
    {
        BookAuthor = bookAuthor;
    }

    void SetQuantity(Integer quantity)
    {
        this.Quantity = quantity;
    }

    void SetPrice(Integer price)
    {
        this.Price = price;
    }

    String GetEmail()
    {
        return this.Email;
    }

    String GetBookName()
    {
        return this.BookName;
    }

    String GetBookAuthor()
    {
        return this.BookAuthor;
    }

    Integer GetQuantity()
    {
        return this.Quantity;
    }

    Integer GetPrice()
    {
        return this.Price;
    }
}