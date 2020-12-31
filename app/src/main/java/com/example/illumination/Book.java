package com.example.illumination;

public class Book
{
    private int BookID;
    private String Title;
    private String Author;
    private String BookDescription;
    private String Genre;
    private int Price;
    private int Quantity;
    private boolean inStock;

    Book()
    {
        this.BookID = 0;
        this.Title = null;
        this.Author = null;
        this.BookDescription = null;
        this.Genre = null;
        this.Price = 0;
        this.Quantity = 0;
        inStock = false;
    }

    void AddNewBook(int bookID, String title, String author, String genre, int price)
    {
        this.BookID = bookID;
        this.Title = title;
        this.Author = author;
        this.Genre = genre;
        this.Price = price;
        this.Quantity = 10;     //Initial Quantity
        this.inStock = true;
    }

    void SetDescription(String bookDescription)
    {
        this.BookDescription = bookDescription;
    }

    void SetBookID(int bookID)
    {
        BookID = bookID;
    }

    void SetTitle(String title)
    {
        this.Title = title;
    }

    void SetAuthor(String author)
    {
        this.Author = author;
    }

    void SetPrice(int price)
    {
        this.Price = price;
    }

    void SetQuantity(int quantity)
    {
        this.Quantity = quantity;
    }

    void SetInStock(boolean inStock)
    {
        this.inStock = inStock;
    }

    int GetBookID()
    {
        return BookID;
    }

    String GetTitle()
    {
        return this.Title;
    }

    String GetAuthor()
    {
        return this.Author;
    }

    String GetBookDescription()
    {
        return this.BookDescription;
    }

    int GetPrice()
    {
        return this.Price;
    }

    int GetQuantity()
    {
        return this.Quantity;
    }

    boolean GetInStock()
    {
        return this.inStock;
    }
}