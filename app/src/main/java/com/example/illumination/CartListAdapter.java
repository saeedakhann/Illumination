package com.example.illumination;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import java.util.ArrayList;

public class CartListAdapter extends ArrayAdapter<Cart>
{
    private Context context;
    private ArrayList<Cart> CartList;
    private int resource;
    private ArrayList<Cart> Cart;

    public CartListAdapter(Context context, int resource, ArrayList<com.example.illumination.Cart> cartList)
    {
        super(context, resource, cartList);
        this.context = context;
        this.CartList = cartList;
        this.resource = resource;
        this.Cart = null;
    }

    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        String email = getItem(position).GetEmail();
        final String bookName = getItem(position).GetBookName();
        final String bookAuthor = getItem(position).GetBookAuthor();
        int quantity = getItem(position).GetQuantity();
        int cost = getItem(position).GetPrice();
        final Cart cart = new Cart();

        cart.SetEmail(email);
        cart.SetBookName(bookName);
        cart.SetBookAuthor(bookAuthor);
        cart.SetPrice(cost);
        cart.SetQuantity(quantity);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.activity_cart_listview, parent, false);

        TextView name = convertView.findViewById(R.id.bookTitle);
        TextView price = convertView.findViewById(R.id.price);

        final ImageButton imageButton = convertView.findViewById(R.id.crossButton);
        final ElegantNumberButton button = convertView.findViewById(R.id.IncrementButton);

        imageButton.setTag(position);
        button.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener()
        {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue)
            {
                int getPosition = (Integer) imageButton.getTag();
                SQLiteOpenHelper dbHelper = new BookStoreDatabaseHelper(context);
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("Select * From Cart", null);
                cursor.moveToFirst();

                for(int i = 0; i < cursor.getCount(); i++)
                {
                    String db_email = cursor.getString(cursor.getColumnIndex("Email"));
                    String db_bookName = cursor.getString(cursor.getColumnIndex("BookName"));
                    String db_bookAuthor = cursor.getString(cursor.getColumnIndex("BookAuthor"));
                    ContentValues values = new ContentValues();
                    String quantity = button.getNumber();

                    if (db_email.equals(CartList.get(getPosition).GetEmail()) && db_bookName.equals(CartList.get(getPosition).GetBookName())
                            && db_bookAuthor.equals(CartList.get(getPosition).GetBookAuthor()))
                    {
                        if (quantity.equals("1") || quantity.equals("2")|| quantity.equals("3") || quantity.equals("4")
                            || quantity.equals("5") || quantity.equals("6") || quantity.equals("7") || quantity.equals("8")
                            || quantity.equals("9") || quantity.equals("10"))
                        {
                            values.put(CartProvider.Quantity, Integer.parseInt(quantity));
                            db.update("Cart", values, "Email = ? AND BookName = ? AND BookAuthor = ?",
                                    new String[]{CartList.get(getPosition).GetEmail(), CartList.get(getPosition).GetBookName(),
                                            CartList.get(getPosition).GetBookAuthor()});
                            CartList.get(getPosition).SetQuantity(Integer.parseInt(quantity));

                            button.setNumber(quantity);
                            notifyDataSetChanged();
                        }
                        else if(quantity.equals("0"))
                        {
                            button.setNumber("1");
                            Toast.makeText(context, "Cannot drop Quantity below 1! Delete Item to Remove from cart", Toast.LENGTH_LONG).show();
                        }
                        else if(quantity.equals("11"))
                        {
                            button.setNumber("10");
                            Toast.makeText(context, "Cannot exceed Quantity! Limit reached", Toast.LENGTH_LONG).show();
                        }
                        break;
                    }

                    cursor.moveToNext();
                }

                cursor.close();
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int getPosition = (Integer) imageButton.getTag();
                SQLiteOpenHelper dbHelper = new BookStoreDatabaseHelper(context);
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("Select * From Cart", null);
                cursor.moveToFirst();

                for(int i = 0; i < cursor.getCount(); i++)
                {
                    String db_email = cursor.getString(cursor.getColumnIndex("Email"));
                    String db_bookName = cursor.getString(cursor.getColumnIndex("BookName"));
                    String db_bookAuthor = cursor.getString(cursor.getColumnIndex("BookAuthor"));

                    if(db_email.equals(CartList.get(getPosition).GetEmail()) && db_bookName.equals(CartList.get(getPosition).GetBookName())
                            && db_bookAuthor.equals(CartList.get(getPosition).GetBookAuthor()))
                    {
                        db.delete("Cart","Email = ? AND BookName = ? AND BookAuthor = ?", new String[]{db_email,
                                  db_bookName, db_bookAuthor});
                        CartList.remove(getPosition);
                        notifyDataSetChanged();
                        break;
                    }

                    cursor.moveToNext();
                }

                cursor.close();
            }
        });

        name.setText(cart.GetBookName().concat(" ").concat(cart.GetBookAuthor()));
        price.setText("    Price: ".concat(cart.GetPrice().toString()).concat(" Rs.").concat("                  "));
        button.setNumber(cart.GetQuantity().toString());
        return convertView;
    }
}