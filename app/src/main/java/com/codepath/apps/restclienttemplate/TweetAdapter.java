package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Tweet> mTweets;
    Context context;

    //pass in the tweets array in the constructor
    public TweetAdapter(List<Tweet> tweets) {
        mTweets = tweets;
    }

    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(ArrayList<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }

    //for each row, inflate the layout and cache references into the ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    //bind the values based on the position of the element

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //get the data according to position
        Tweet tweet = mTweets.get(position);

        //populate the views according to this data
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvHandle.setText("@" + tweet.user.screenName);
        holder.tvTimestamp.setText(tweet.createdAt);


        Glide.with(context).load(tweet.user.profileImageurl).into(holder.ivProfileImage);
    }

    @Override
    public int getItemCount() { return mTweets.size(); }

    //create ViewHolder class

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public  TextView tvBody;
        public TextView tvHandle;
        public TextView tvTimestamp;
        public ImageButton btnReply;
        public ImageButton btnReplyDetails;

        public ViewHolder (View itemView) {
            super (itemView);

            //perform findViewById lookups
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvHandle = (TextView) itemView.findViewById(R.id.tvHandle);
            tvTimestamp = (TextView) itemView.findViewById(R.id.tvTimestamp);
            btnReply = (ImageButton) itemView.findViewById(R.id.btnReply) ;

            btnReply.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                if (v.getId() == itemView.getId()) {
                    Tweet tweet = mTweets.get(position);
                    Intent intent = new Intent(context, TweetDetailsActivity.class);
                    intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                    context.startActivity(intent);
                }
                else {
                    Tweet tweet = mTweets.get(position);
                    Intent intent = new Intent(context, ComposeActivity.class);
                    intent.putExtra("TwitterHandle", tweet.user.screenName);
                    context.startActivity(intent);
                }
            }
        }
    }
}
