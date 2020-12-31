package com.example.illumination;

public class User
{
    public String Username;
    public String Email;
    public String Password;
    public String PhoneNumber;
    public String Address;

    public User()
    {
        this.Username = null;
        this.Email = null;
        this.Password = null;
        this.PhoneNumber = null;
        this.Address = null;
    }

    public User(String username, String email, String password, String phone, String address)
    {
        this.Username = username;
        this.Email = email;
        this.Password = password;
        this.PhoneNumber = phone;
        this.Address = address;
    }
}