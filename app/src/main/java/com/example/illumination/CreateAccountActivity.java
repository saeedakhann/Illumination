package com.example.illumination;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class CreateAccountActivity extends AppCompatActivity
{
    EditText username, email, password, phoneNumber, address;
    Boolean check_length, check_symbol, check_upper_case, check_lower_case;
    BookStoreDatabaseHelper dbHelper;
    SQLiteDatabase db;
    String newActivityName, oldName, oldEmailID, oldPassword, oldPhoneNumber, oldAddress;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

        Objects.requireNonNull(CreateAccountActivity.this.getSupportActionBar()).setDisplayShowHomeEnabled(false);
        int orientation = CreateAccountActivity.this.getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT)
            setContentView(R.layout.activity_signup);
        else
            setContentView(R.layout.landscape_activity_signup);

        dbHelper = new BookStoreDatabaseHelper(CreateAccountActivity.this);
        db = dbHelper.getReadableDatabase();
        Intent intent = getIntent();
        newActivityName = intent.getStringExtra("New Activity Name");
        username = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phoneNumber = findViewById(R.id.phoneNumber);
        address = findViewById(R.id.address);
        check_length = check_symbol = check_upper_case = check_lower_case = false;

        if (newActivityName != null && newActivityName.equals("Edit Account"))
        {
            CreateAccountActivity.this.setTitle("Edit Account");
            Cursor cursor = db.rawQuery("Select * From CurrentUser", null);

            if ((cursor.moveToFirst()))
            {
                oldName = cursor.getString(cursor.getColumnIndex("Name"));
                oldEmailID = cursor.getString(cursor.getColumnIndex("Email"));
                oldPassword = cursor.getString(cursor.getColumnIndex("Password"));
                oldPhoneNumber = cursor.getString(cursor.getColumnIndex("PhoneNumber"));
                oldAddress = cursor.getString(cursor.getColumnIndex("Address"));
                ((EditText) findViewById(R.id.name)).setText(oldName, TextView.BufferType.EDITABLE);
                ((EditText) findViewById(R.id.email)).setText(oldEmailID, TextView.BufferType.EDITABLE);
                findViewById(R.id.email).setEnabled(false);
                ((EditText) findViewById(R.id.password)).setText(oldPassword, TextView.BufferType.EDITABLE);
                ((EditText) findViewById(R.id.phoneNumber)).setText(oldPhoneNumber, TextView.BufferType.EDITABLE);
                ((EditText) findViewById(R.id.address)).setText(oldAddress, TextView.BufferType.EDITABLE);
            }

            cursor.close();
        }

        Button createAccount = findViewById(R.id.createAccount);

        if ( newActivityName != null && newActivityName.equals("Edit Account"))
            createAccount.setText("Update Account");

        createAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (!username.getText().toString().equals("") && !email.getText().toString().equals("") &&
                        !password.getText().toString().equals("") && !phoneNumber.getText().toString().equals("") &&
                        !address.getText().toString().equals(""))
                {
                    Cursor cursor = db.rawQuery("Select * From User where Email = '" + email.getText() + "'", null);

                    if (newActivityName != null && newActivityName.equals("Edit Account"))
                    {
                        cursor.close();
                        String _name = ((EditText) findViewById(R.id.name)).getText().toString();
                        String _password = ((EditText) findViewById(R.id.password)).getText().toString();
                        String _phoneNumber = ((EditText) findViewById(R.id.phoneNumber)).getText().toString();
                        String _address = ((EditText) findViewById(R.id.address)).getText().toString();

                        boolean checkPassword, checkPhoneNumber;
                        ContentValues values = new ContentValues();

                        if (!_name.equals(oldName))
                        {
                            values.put(UserProvider.Name, _name);
                            getContentResolver().update(CurrentUserProvider.CONTENT_URI, values, "", null);
                            try
                            {
                                myRef.child(oldEmailID.replace(".", "")).child("Username").setValue(_name);
                            }
                            catch (Exception ex)
                            {
                                Toast.makeText(CreateAccountActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                        if (!_password.equals(oldPassword))
                        {
                            if (check_length && check_symbol && check_lower_case && check_upper_case)
                            {
                                checkPassword = true;
                                values.put(UserProvider.Password, _password);
                                getContentResolver().update(CurrentUserProvider.CONTENT_URI, values, "", null);
                                try
                                {
                                    myRef.child(oldEmailID.replace(".", "")).child("Password").setValue(_password);
                                }
                                catch (Exception ex)
                                {
                                    Toast.makeText(CreateAccountActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                            else if (!check_length)
                            {
                                checkPassword = false;
                                password.requestFocus();
                                Toast.makeText(CreateAccountActivity.this, "Password Length MUST be greater than 8.", Toast.LENGTH_LONG).show();
                            }
                            else if (!check_symbol)
                            {
                                checkPassword = false;
                                password.requestFocus();
                                Toast.makeText(CreateAccountActivity.this, "Password MUST contain a symbol.", Toast.LENGTH_LONG).show();
                            }
                            else if (!check_lower_case)
                            {
                                checkPassword = false;
                                password.requestFocus();
                                Toast.makeText(CreateAccountActivity.this, "Password MUST contain at least one lower case letter.", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                checkPassword = false;
                                password.requestFocus();
                                Toast.makeText(CreateAccountActivity.this, "Password MUST contain at least one upper case letter.", Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                            checkPassword = true;

                        if (!_phoneNumber.equals(oldPhoneNumber))
                        {
                            if (isPhoneValid(_phoneNumber))
                            {
                                checkPhoneNumber = true;
                                values.put(UserProvider.PhoneNumber, _phoneNumber);
                                getContentResolver().update(CurrentUserProvider.CONTENT_URI, values, "", null);
                                try
                                {
                                    myRef.child(oldEmailID.replace(".", "")).child("PhoneNumber").setValue(_phoneNumber);
                                }
                                catch (Exception ex)
                                {
                                    Toast.makeText(CreateAccountActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                            else
                            {
                                checkPhoneNumber = false;
                                phoneNumber.requestFocus();
                                Toast.makeText(CreateAccountActivity.this, "Enter a valid phone number.", Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                            checkPhoneNumber = true;

                        if (!_address.equals(oldAddress))
                        {
                            values.put(UserProvider.Address, _address);
                            getContentResolver().update(CurrentUserProvider.CONTENT_URI, values, "", null);
                            try
                            {
                                myRef.child(oldEmailID.replace(".", "")).child("Address").setValue(_address);
                            }
                            catch (Exception ex)
                            {
                                Toast.makeText(CreateAccountActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                        if (checkPassword && checkPhoneNumber)
                            finish();
                    }
                    else if (cursor.getCount() > 0)
                    {
                        cursor.close();
                        Toast.makeText(CreateAccountActivity.this, "Email ID Already Used!", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        boolean emailValidity = isEmailValid(email.getText().toString());
                        boolean phoneValidity = isPhoneValid(phoneNumber.getText().toString());

                        if (check_length && check_symbol && check_lower_case && check_upper_case && emailValidity && phoneValidity)
                        {
                            cursor.close();
                            ContentValues values = new ContentValues();
                            values.put(CurrentUserProvider.Name, username.getText().toString());
                            values.put(CurrentUserProvider.Email, email.getText().toString());
                            values.put(CurrentUserProvider.Address,address.getText().toString());
                            values.put(CurrentUserProvider.PhoneNumber, phoneNumber.getText().toString());
                            values.put(CurrentUserProvider.Password, password.getText().toString());
                            getContentResolver().insert(CurrentUserProvider.CONTENT_URI, values);

                            AddUser(username.getText().toString(), email.getText().toString(), password.getText().toString(),
                                    address.getText().toString(), phoneNumber.getText().toString());

                            Intent homeIntent = new Intent(CreateAccountActivity.this, HomeActivity.class);
                            startActivity(homeIntent);
                        }
                        else if (!check_length)
                        {
                            password.requestFocus();
                            Toast.makeText(CreateAccountActivity.this, "Password Length MUST be greater than 8.", Toast.LENGTH_LONG).show();
                        }
                        else if (!check_symbol)
                        {
                            password.requestFocus();
                            Toast.makeText(CreateAccountActivity.this, "Password MUST contain a symbol.", Toast.LENGTH_LONG).show();
                        }
                        else if (!check_lower_case)
                        {
                            password.requestFocus();
                            Toast.makeText(CreateAccountActivity.this, "Password MUST contain at least one lower case letter.", Toast.LENGTH_LONG).show();
                        }
                        else if (!check_upper_case)
                        {
                            password.requestFocus();
                            Toast.makeText(CreateAccountActivity.this, "Password MUST contain at least one upper case letter.", Toast.LENGTH_LONG).show();
                        }
                        else if (!emailValidity)
                        {
                            email.requestFocus();
                            Toast.makeText(CreateAccountActivity.this, "Enter a valid email address.", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            phoneNumber.requestFocus();
                            Toast.makeText(CreateAccountActivity.this, "Enter a valid phone number.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                else
                {
                    username.requestFocus();
                    Toast.makeText(CreateAccountActivity.this, "Empty Fields NOT Allowed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        password.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(password.getText().toString().length() > 0)
                {
                    check_length = hasLength(password.getText().toString(), 8);
                    check_symbol = hasSymbol(password.getText().toString());
                    check_lower_case = hasLowerCase(password.getText().toString());
                    check_upper_case = hasUpperCase(password.getText().toString());
                }
                else
                {
                    check_length = false;
                    check_symbol = false;
                    check_lower_case = false;
                    check_upper_case = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s){}
        });

        if (getIntent().getBooleanExtra("EXIT", false))
        {
            finish();
        }
    }

    public void ShowAndHidePassword(View view)
    {
        ImageView imageView = findViewById(R.id.show_password_button);

        if(password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance()))
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

    public void AddUser(String name, String email, String password, String address, String phone)
    {
        User user = new User(name , email , password , phone , address);
        String UserEmail = email.replace(".", "");

        try
        {
            myRef.child(UserEmail).setValue(user);
        }
        catch (Exception ex)
        {
            Toast.makeText(CreateAccountActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

       /* FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        User user = new User(name, email, password, phone, address);
        myRef.push().setValue(user);
        /*email = email.replace(".", "");
        myRef.push().setValue(email);
        myRef.child(email).push().setValue(user);*/
    }

    private static boolean hasLength(CharSequence data, int minLen)
    {
        return String.valueOf(data).length() >= minLen;
    }

    private static boolean hasSymbol(CharSequence data)
    {
        String password = String.valueOf(data);
        return !password.matches("[A-Za-z0-9 ]*");
    }

    private static boolean hasUpperCase(CharSequence data)
    {
        String password = String.valueOf(data);
        return !password.equals(password.toLowerCase());
    }

    private static boolean hasLowerCase(CharSequence data)
    {
        String password = String.valueOf(data);
        return !password.equals(password.toUpperCase());
    }

    public static boolean isEmailValid(CharSequence email)
    {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPhoneValid(CharSequence phoneNumber)
    {
        return android.util.Patterns.PHONE.matcher(phoneNumber).matches();
    }
}