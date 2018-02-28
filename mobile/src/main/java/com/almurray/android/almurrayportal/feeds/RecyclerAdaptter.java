package com.almurray.android.almurrayportal.feeds;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.almurray.android.almurrayportal.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by tom on 14/01/2018.
 */

public class RecyclerAdaptter extends RecyclerView.Adapter<RecyclerAdaptter.MyHolder> {


    List<post> list;
    Context context;

    public RecyclerAdaptter(List<post> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.feed_item,parent,false);
        MyHolder myHoder = new MyHolder(view);


        return myHoder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        post mylist = list.get(position);
        holder.name.setText(mylist.getPosterName());
        holder.likes.setText("Likes: "+String.valueOf(mylist.getLikes()));
        holder.description.setText(mylist.getDesc());
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {

        int arr = 0;

        try{
            if(list.size()==0){

                arr = 0;
                Log.d("TAG", "NO FEEDS :(");

            }
            else{

                arr=list.size();
                Log.d("TAG", String.valueOf(list.size()) + "AMOUNT OF STUDF");
            }



        }catch (Exception e){
            Log.d("TAG", e.getLocalizedMessage());


        }

        return arr;

    }




    class MyHolder extends RecyclerView.ViewHolder{
        TextView name,likes,description;
        CircleImageView userImage;
        ImageView image;
        String postURL, posterURL;

        public MyHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.feedPoster);
            likes = (TextView) itemView.findViewById(R.id.feedLikes);
            description = (TextView) itemView.findViewById(R.id.feedDescription);

            userImage = (CircleImageView) itemView.findViewById(R.id.feedPosterImage);
            image = (ImageView) itemView.findViewById(R.id.feedImage);


        }
    }
}
