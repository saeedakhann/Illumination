package com.example.illumination;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class BookListAdapter extends ArrayAdapter<Book> implements Filterable
{
    Context context;
    ViewHolder viewHolder;
    public ArrayList<Book> books;
    public ArrayList<Book> filteredBooks;
    private ArrayList<Book> booksCopy;
    private Filter filter;

    public BookListAdapter(Context context, int resourceID, ArrayList<Book> books)
    {
        super(context, resourceID, books);
        this.context = context;
        this.books = books;
        this.filteredBooks = books;
        this.booksCopy = new ArrayList<>();
        this.booksCopy.addAll(books);
    }

    private class ViewHolder
    {
        ImageView bookImage;
        TextView bookTitle;
        TextView bookAuthor;
        TextView bookPrice;
        Button viewBookDescription;
    }

    @SuppressLint("SetTextI18n")
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        viewHolder = null;

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_home_listview, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.bookImage = convertView.findViewById(R.id.bookImage);
            viewHolder.bookTitle = convertView.findViewById(R.id.bookTitle);
            viewHolder.bookAuthor = convertView.findViewById(R.id.bookAuthor);
            viewHolder.bookPrice = convertView.findViewById(R.id.bookPrice);
            viewHolder.viewBookDescription = convertView.findViewById(R.id.viewBookDescription);
            viewHolder.viewBookDescription.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    int getPosition = (Integer) view.getTag();
                    Intent viewBookDetails = new Intent(context, BookDetailActivity.class);
                    viewBookDetails.putExtra("BookImageID", getItem(getPosition).GetBookID());
                    viewBookDetails.putExtra("BookName", getItem(getPosition).GetTitle());
                    viewBookDetails.putExtra("BookAuthor", getItem(getPosition).GetAuthor());
                    viewBookDetails.putExtra("BookPrice", getItem(getPosition).GetPrice());
                    viewBookDetails.putExtra("BookDescription", getItem(getPosition).GetBookDescription());
                    context.startActivity(viewBookDetails);
                }
            });

            convertView.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.viewBookDescription.setTag(position);
        Book book = books.get(position);

        if (book.GetBookID() == 1)
            viewHolder.bookImage.setImageResource(R.drawable.book1);
        else if (book.GetBookID() == 2)
            viewHolder.bookImage.setImageResource(R.drawable.book2);
        else if (book.GetBookID() == 3)
            viewHolder.bookImage.setImageResource(R.drawable.book3);
        else if (book.GetBookID() == 4)
            viewHolder.bookImage.setImageResource(R.drawable.book4);
        else if (book.GetBookID() == 5)
            viewHolder.bookImage.setImageResource(R.drawable.book5);
        else if (book.GetBookID() == 6)
            viewHolder.bookImage.setImageResource(R.drawable.book6);
        else if (book.GetBookID() == 7)
            viewHolder.bookImage.setImageResource(R.drawable.book7);
        else if (book.GetBookID() == 8)
            viewHolder.bookImage.setImageResource(R.drawable.book8);
        else if (book.GetBookID() == 9)
            viewHolder.bookImage.setImageResource(R.drawable.book9);
        else if (book.GetBookID() == 10)
            viewHolder.bookImage.setImageResource(R.drawable.book10);
        else if (book.GetBookID() == 11)
            viewHolder.bookImage.setImageResource(R.drawable.book11);
        else if (book.GetBookID() == 12)
            viewHolder.bookImage.setImageResource(R.drawable.book12);

        viewHolder.bookTitle.setText(book.GetTitle());
        viewHolder.bookAuthor.setText("By " + book.GetAuthor());
        viewHolder.bookPrice.setText("Price: " + book.GetPrice());

        return convertView;
    }

    @Override
    public Filter getFilter()
    {
        if(filter == null)
            filter = new BooksFilter();

        return filter;
    }

    private class BooksFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0)
            {
                ArrayList<Book> filteredList = new ArrayList<>();

                for (int i = 0; i < booksCopy.size(); i++)
                {
                    if (booksCopy.get(i).GetTitle().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                        booksCopy.get(i).GetAuthor().toLowerCase().contains(constraint.toString().toLowerCase()))
                    {
                        filteredList.add(booksCopy.get(i));
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
            }
            else
            {
                results.count = booksCopy.size();
                results.values = booksCopy;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            filteredBooks.clear();
            filteredBooks.addAll((ArrayList<Book>) results.values);
            notifyDataSetChanged();
        }
    }
}
