package com.example.illumination;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class ReviewListAdapter extends ArrayAdapter<Review>
{
    Context context;
    ReviewListAdapter.ViewHolder viewHolder;
    public ArrayList<Review> reviews;

    public ReviewListAdapter(Context context, int resourceID, ArrayList<Review> reviews)
    {
        super(context, resourceID, reviews);
        this.context = context;
        this.reviews = reviews;
    }

    private class ViewHolder
    {
        TextView reviewID;
        TextView userEmail;
        TextView userComment;
    }

    public View getView(final int position, View convertView, ViewGroup parent)
    {
        viewHolder = null;

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_review_list_view, parent, false);

            viewHolder = new ReviewListAdapter.ViewHolder();
            viewHolder.reviewID = convertView.findViewById(R.id.review_ID);
            viewHolder.userEmail = convertView.findViewById(R.id.email_ID);
            viewHolder.userComment = convertView.findViewById(R.id.comment);

            convertView.setTag(viewHolder);
        }
        else
            viewHolder = (ReviewListAdapter.ViewHolder) convertView.getTag();

        viewHolder.reviewID.setTag(position);
        Review review = reviews.get(position);

        viewHolder.reviewID.setText(review.ReviewID);
        viewHolder.userEmail.setText(review.EmailID);
        viewHolder.userComment.setText(review.Comment);

        return convertView;
    }
}