package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class TweetDetailsActivity extends AppCompatActivity {

    Tweet tweet;
    ImageView ivProfileImg;
    TextView tvUserNameDetails;
    TextView tvBodyDetails;
    TextView tvHandleDetails;
    TextView tvTime;
    TextView tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        ivProfileImg = (ImageView) findViewById(R.id.ivProfileImg);
        tvUserNameDetails = (TextView) findViewById(R.id.tvUserNameDetails);
        tvBodyDetails = (TextView) findViewById(R.id.tvBodyDetails);
        tvHandleDetails = (TextView) findViewById(R.id.tvHandleDetails);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvDate = (TextView) findViewById(R.id.tvDate);


        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        tvUserNameDetails.setText(tweet.user.name);
        tvHandleDetails.setText("@" + tweet.user.screenName);
        tvBodyDetails.setText(tweet.body);
        tvTime.setText(tweet.DetailsTime);
        tvDate.setText(tweet.DetailsDate);
        Glide.with(getApplicationContext()).load(tweet.user.profileImageurl).into(ivProfileImg);

    }
}
