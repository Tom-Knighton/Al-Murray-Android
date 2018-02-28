package com.almurray.android.almurrayportal.chatUsers;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.almurray.android.almurrayportal.R;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by tom on 18/01/2018.
 */

public class userAdapter extends FirebaseAdapter<userAdapter.ViewHolder, userChat> {




    public static class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView userImage ;
        TextView name;
        CheckBox checkBox;


        public ViewHolder(View view) {
            super(view);
            userImage = (CircleImageView) view.findViewById(R.id.userDisplayProfile);
            name = (TextView) view.findViewById(R.id.userDisplayName);
            checkBox = (CheckBox) view.findViewById(R.id.userDisplayCheck);
        }
    }

    Context mContext;


    public userAdapter(Query query, @Nullable ArrayList<userChat> items,
                       @Nullable ArrayList<String> keys, Context context) {
        super(query, items, keys);
        mContext = context;

    }

    @Override public userAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userdisplay_item, parent, false);

        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(userAdapter.ViewHolder holder, int position) {
        final userChat item = getItem(position);
        holder.name.setText(item.getName());
        Picasso.with(mContext).load(String.valueOf(item.getUrl())).into(holder.userImage);

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                chatUserList.onSelected(isChecked, item.getSendbird());
            }
        });


    }

    @Override protected void itemAdded(userChat item, String key, int position) {
        Log.d("MyAdapter", "Added a new item to the adapter.");
    }

    @Override protected void itemChanged(userChat oldItem, userChat newItem, String key, int position) {
        Log.d("MyAdapter", "Changed an item.");
    }

    @Override protected void itemRemoved(userChat item, String key, int position) {
        Log.d("MyAdapter", "Removed an item from the adapter.");
    }

    @Override protected void itemMoved(userChat item, String key, int oldPosition, int newPosition) {
        Log.d("MyAdapter", "Moved an item.");
    }
}
