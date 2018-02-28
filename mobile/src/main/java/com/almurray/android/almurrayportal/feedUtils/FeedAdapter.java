package com.almurray.android.almurrayportal.feedUtils;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.Image;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.almurray.android.almurrayportal.MainNav;
import com.almurray.android.almurrayportal.R;
import com.almurray.android.almurrayportal.dinosaur;
import com.almurray.android.almurrayportal.editProfileView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by tom on 18/01/2018.
 */

public class FeedAdapter extends FirebaseAdapter<FeedAdapter.ViewHolder, post> {




    public static class ViewHolder extends RecyclerView.ViewHolder {
        Context context;
        Integer num;
        ImageView image;
        CircleImageView posterImage ;
        TextView poster, likes, description;
        Button comments, options;
        CardView cardView;
        Boolean hasUp, hasDown, canUp, canDown, canDoStuff;
        ImageView heart;


        public ViewHolder(View view) {
            super(view);

            context = view.getContext();
            cardView = (CardView) view.findViewById(R.id.feedItem);
            image = (ImageView) view.findViewById(R.id.feedImage);
            posterImage = (CircleImageView) view.findViewById(R.id.feedPosterImage);
            poster = (TextView) view.findViewById(R.id.feedPoster);
            likes = (TextView) view.findViewById(R.id.feedLikes);
            description = (TextView) view.findViewById(R.id.feedDescription);
            comments = (Button) view.findViewById(R.id.feedComments);
            options = (Button) view.findViewById(R.id.feedOptions);
            heart = (ImageView) view.findViewById(R.id.feedHeart);
        }
    }

    Context mContext;


    public FeedAdapter(Query query, @Nullable ArrayList<post> items,
                       @Nullable ArrayList<String> keys, Context context) {
        super(query, items, keys);
        mContext = context;

    }

    @Override public FeedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, parent, false);

        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(final FeedAdapter.ViewHolder holder, final int position) {
        final post item = getItem(position);
        holder.poster.setText(item.getPosterName());
        holder.likes.setText("Likes: "+String.valueOf(item.getLikes()));
        holder.comments.setText("View "+String.valueOf(item.getComments())+" Comments");
        holder.description.setText(String.valueOf(item.getDesc()));
        Picasso.with(mContext).load(String.valueOf(item.getPostURL())).into(holder.image);
        Picasso.with(mContext).load(String.valueOf(item.getPosterURL())).into(holder.posterImage);
        holder.hasUp = false;
        holder.hasDown = false;
        holder.canUp = true;
        holder.canDown = true;
        holder.canDoStuff = false;
        holder.num = item.getPostNum();

        final SharedPreferences preferences = holder.context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("feed").child(String.valueOf(holder.num));
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //holder.heart.setImageDrawable(ContextCompat.getDrawable(holder.context, R.drawable.ic_heart));
                holder.heart.setImageResource(R.drawable.ic_heart);

                for(DataSnapshot snapshot : dataSnapshot.child("likeList").getChildren()) {
                    if(snapshot.getValue(String.class).equals(preferences.getString("currentFullName", ""))) {
                        holder.hasUp = true;
                        holder.canUp = false;
                        holder.canDown = true;
                        holder.hasDown = false;
                        Log.d("TAG", "hmmmm");
                        //holder.heart.setImageDrawable(ContextCompat.getDrawable(holder.context, R.drawable.ic_favorite_heart_button));
                        holder.heart.setImageResource(R.drawable.ic_favorite_heart_button);

                    }
                }
                holder.canDoStuff = true;


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        holder.heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.canUp && holder.canDoStuff) {
                    Log.d("TAG", "WE DIDDA UP");
                    holder.canUp = false;
                    holder.canDoStuff = false;
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            dataSnapshot.child("likeList").child(preferences.getString("currentFullName", "")).getRef().setValue(preferences.getString("currentFullName", ""));
                            Integer old = dataSnapshot.child("lastLike").getValue(Integer.class);
                            Integer newN = old + 1;
                            dataSnapshot.child("lastLike").getRef().setValue(newN);
                            dataSnapshot.child("Likes").getRef().setValue(newN);
                            holder.canDown = true;
                            holder.hasDown = false;
                            holder.hasUp = true;
                            holder.canUp = false;

                            holder.heart.setImageResource(R.drawable.ic_favorite_heart_button);
                            holder.canDoStuff = true;
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                if(holder.canDown && holder.canDoStuff) {
                    Log.d("TAG", "WE DIDDA DOWN");
                    holder.canDown = false;
                    holder.canDoStuff = false;
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            dataSnapshot.child("likeList").child(preferences.getString("currentFullName", "")).getRef().removeValue();
                            Integer old = dataSnapshot.child("lastLike").getValue(Integer.class);
                            Integer newN = old - 1;
                            dataSnapshot.child("lastLike").getRef().setValue(newN);
                            dataSnapshot.child("Likes").getRef().setValue(newN);
                            holder.heart.setImageResource(R.drawable.ic_heart);
                            holder.canUp = true;
                            holder.hasDown = true;
                            holder.hasUp = false;
                            holder.canDown = false;
                            holder.canDoStuff = true;
                            holder.heart.setImageResource(R.drawable.ic_heart);
                            holder.heart.setImageResource(R.drawable.ic_heart);
                            holder.heart.setImageResource(R.drawable.ic_heart);
                            holder.heart.setImageResource(R.drawable.ic_heart);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });


        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.context, seePhoto.class);
                i.putExtra("photoToSee", item.getPostURL());
                holder.context.startActivity(i);
            }
        });

        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.context, feedCommentsView.class);
                i.putExtra("postNum", holder.num);
                holder.context.startActivity(i);

            }
        });

        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("feed").child(String.valueOf(holder.num));
                final SharedPreferences prefs = holder.context.getSharedPreferences("prefs", Context.MODE_PRIVATE);

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        String[] options = new String[] { "Delete Post"};
                        String[] otherOptions = new String[] {"Report Post", "View Profile of poster"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.context);

                        if(dataSnapshot.child("posterName").getValue(String.class).equals(prefs.getString("currentFullName", ""))) {
                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if(which == 0) {
                                        dataSnapshot.getRef().removeValue();
                                    }

                                }
                            });


                            builder.create().show();
                        } else {
                            builder.setItems(otherOptions, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if(which == 0) {
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("reported");
                                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                Integer old = dataSnapshot.child("lastReport").getValue(Integer.class);
                                                Integer newN = old + 1;

                                                dataSnapshot.child(String.valueOf(newN)).child("type").getRef().setValue("post");
                                                dataSnapshot.child(String.valueOf(newN)).child("post").getRef().setValue(holder.num);

                                                dataSnapshot.child("lastReport").getRef().setValue(newN);

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    } else if (which == 1) {
                                        Intent i = new Intent(holder.context, editProfileView.class);
                                        i.putExtra("currentSendbird", dataSnapshot.child("sendbird").getValue(String.class));
                                        holder.context.startActivity(i);
                                    }

                                }
                            });


                            builder.create().show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });







    }

    @Override protected void itemAdded(post item, String key, int position) {
        Log.d("MyAdapter", "Added a new item to the adapter.");
    }

    @Override protected void itemChanged(post oldItem, post newItem, String key, int position) {
        Log.d("MyAdapter", "Changed an item.");
    }

    @Override protected void itemRemoved(post item, String key, int position) {
        Log.d("MyAdapter", "Removed an item from the adapter.");
    }

    @Override protected void itemMoved(post item, String key, int oldPosition, int newPosition) {
        Log.d("MyAdapter", "Moved an item.");
    }
}
