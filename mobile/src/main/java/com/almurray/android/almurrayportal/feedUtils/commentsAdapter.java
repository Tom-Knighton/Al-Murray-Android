package com.almurray.android.almurrayportal.feedUtils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.almurray.android.almurrayportal.R;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by tom on 20/01/2018.
 */

public class commentsAdapter extends FirebaseAdapter<commentsAdapter.ViewHolder, comment> {




    public static class ViewHolder extends RecyclerView.ViewHolder {


        CircleImageView commenterImage ;
        TextView comment, date;

        public ViewHolder(View view) {
            super(view);
            comment = (TextView) view.findViewById(R.id.commentsComment);
            date = (TextView) view.findViewById(R.id.commentsDate);
            commenterImage = (CircleImageView) view.findViewById(R.id.commentsUser);
        }
    }

    Context mContext;


    public commentsAdapter(Query query, @Nullable ArrayList<comment> items,
                       @Nullable ArrayList<String> keys, Context context) {
        super(query, items, keys);
        mContext = context;

    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_comment, parent, false);

        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(commentsAdapter.ViewHolder holder, int position) {
        comment item = getItem(position);
        holder.comment.setText(item.getComment());
        holder.date.setText(item.getDate());
        Picasso.with(mContext).load(String.valueOf(item.getCommenterurl())).into(holder.commenterImage);




    }

    @Override protected void itemAdded(comment item, String key, int position) {
        Log.d("MyAdapter", "Added a new item to the adapter.");
    }

    @Override protected void itemChanged(comment oldItem, comment newItem, String key, int position) {
        Log.d("MyAdapter", "Changed an item.");
    }

    @Override protected void itemRemoved(comment item, String key, int position) {
        Log.d("MyAdapter", "Removed an item from the adapter.");
    }

    @Override protected void itemMoved(comment item, String key, int oldPosition, int newPosition) {
        Log.d("MyAdapter", "Moved an item.");
    }
}
