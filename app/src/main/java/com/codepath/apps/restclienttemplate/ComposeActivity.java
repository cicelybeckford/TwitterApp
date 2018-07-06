package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {
    private TwitterClient client;
    private String tweetMessage;
    Button btnTweet;
    EditText twInput;
    TextView tvTHandle;
    TextView tvTName;
    ImageView ivProfile;
    TextView tvCharCount;
    TextView tvReplyTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        btnTweet = (Button) findViewById(R.id.btnTweet);
        twInput = (EditText) findViewById(R.id.twInput);
        tvTHandle = (TextView) findViewById(R.id.tvTHandle);
        tvTName = (TextView) findViewById(R.id.tvTName);
        ivProfile = (ImageView) findViewById(R.id.ivProfile);
        tvCharCount = (TextView) findViewById(R.id.tvCharCount);
        tvReplyTo = (TextView) findViewById(R.id.tvReplyTo);
        String twitterhandle = "";

        client = TwitterApp.getRestClient(this);
        userInfo();
        tvCharCount.setText("140");

        twitterhandle = getIntent().getStringExtra("TwitterHandle");
        if (twitterhandle != null) {
            tvReplyTo.setText("Replying to " + twitterhandle);
            tvReplyTo.setVisibility(View.VISIBLE);
            String message = "@" + twitterhandle;
            twInput.setText(message);
            tvCharCount.setText("" + (140 - message.length()));
        }

        inputCount(twInput, tvCharCount);
    }

    public void inputCount (EditText editText, final TextView textView) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                int remaining = 140 - input.length();
                textView.setText("" + remaining);
            }
        });
    }

    public void cancelTweet (View view) { finish();}

    public void userInfo () {
        client.getCurrentUser(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    User user = User.fromJSON(response);
                    tvTHandle.setText("@" + user.screenName);
                    tvTName.setText(user.name);
                    Glide.with(getApplicationContext()).load(user.profileImageurl).into(ivProfile);
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

    public void postTweet(View view) {
        tweetMessage = twInput.getText().toString();
        client.sendTweet(tweetMessage, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Tweet tweet = Tweet.fromJSON(response);
                    Intent i = new Intent();
                    i.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                    setResult(RESULT_OK, i);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
        tvReplyTo.setVisibility(View.INVISIBLE);
    }
}
