package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Parcel
public class Tweet {

    //list out the attributes
    public String body;
    public long uid; //database ID for the tweet
    public User user;
    public String createdAt;
    public String DetailsTime;
    public String DetailsDate;
    public Long favCount;
    public Long rtCount;
    public boolean favorited;

    //deserialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        //extract the values from the JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = getRelativeTimeAgo(jsonObject.getString("created_at"));
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.DetailsDate = getDateForDetails(jsonObject.getString("created_at"));
        tweet.DetailsTime = getTimeForDetails(jsonObject.getString("created_at"));
        tweet.favCount = jsonObject.getLong("favorite_count");
        tweet.rtCount = jsonObject.getLong("retweet_count");
        tweet.favorited = jsonObject.getBoolean("favorited");

        return tweet;
    }

    public static String getTimeForDetails(String timeCreated) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";

        String newTime = "";
        try {
            Date sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH).parse(timeCreated);
            newTime = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(sf);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return newTime;
    }

    public static String getDateForDetails(String timeCreated) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";

        String newDate = "";
        try {
            Date sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH).parse(timeCreated);
            newDate = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(sf);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return newDate;
    }

    public static String getRelativeTimeAgo(String timeCreated) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(timeCreated).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE).toString();
            if (relativeDate.toLowerCase().contains("sec")) {
                relativeDate = relativeDate.replaceAll("[^\\d.]", "");
                relativeDate = relativeDate + "s";
            }
            else if (relativeDate.toLowerCase().contains("min")) {
                relativeDate = relativeDate.replaceAll("[^\\d.]", "");
                relativeDate = relativeDate + "m";
            }
            else {
                relativeDate = relativeDate.replaceAll("[^\\d.]", "");
                relativeDate = relativeDate + "d";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
