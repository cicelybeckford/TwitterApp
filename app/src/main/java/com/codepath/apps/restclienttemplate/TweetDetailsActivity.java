package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class TweetDetailsActivity extends AppCompatActivity {
    private TwitterClient client;
    Tweet tweet;
    ImageView ivProfileImg;
    TextView tvUserNameDetails;
    TextView tvBodyDetails;
    TextView tvHandleDetails;
    TextView tvTime;
    TextView tvDate;
    TextView tvfavCount;
    TextView tvrtCount;
    ImageButton btnReplyDetails;
    ImageButton btnFavoriteDetails;

    private final int REQUEST_CODE = 20;

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
        tvfavCount = (TextView) findViewById(R.id.tvfavCount);
        tvrtCount = (TextView) findViewById(R.id.tvrtCount);
        btnFavoriteDetails = (ImageButton) findViewById(R.id.btnFavoriteDetails);
        client = TwitterApp.getRestClient(this);


        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        tvUserNameDetails.setText(tweet.user.name);
        tvHandleDetails.setText("@" + tweet.user.screenName);
        tvBodyDetails.setText(tweet.body);
        tvTime.setText(tweet.DetailsTime);
        tvDate.setText(tweet.DetailsDate);
        tvfavCount.setText(Long.toString(tweet.favCount));
        tvrtCount.setText(Long.toString(tweet.rtCount));
        Glide.with(getApplicationContext()).load(tweet.user.profileImageurl).into(ivProfileImg);
        if (tweet.favorited == true) {
            btnFavoriteDetails.setBackgroundColor(getResources().getColor(R.color.medium_red));
        }

        btnFavoriteDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tweet.favorited == false) {
                    v.setBackgroundColor(getResources().getColor(R.color.medium_red));
                    favorite();
                    tweet.favorited = true;
                }
                else {
                    v.setBackgroundColor(Color.TRANSPARENT);
                    favorite();
                    tweet.favorited = false;
                }
            }
        });
    }

    public void replyTo (View view) {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, ComposeActivity.class);
        intent.putExtra("TwitterHandle", tweet.user.screenName);
        context.startActivity(intent);
    }

    public void favorite () {
        long tweetid = tweet.uid;
        client.favoriteTweet(tweetid, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Tweet favtweet = Tweet.fromJSON(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public void unfavorite () {
        long tweetid = tweet.uid;
        client.unfavoriteTweet(tweetid, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Tweet unfavtweet = Tweet.fromJSON(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

}
