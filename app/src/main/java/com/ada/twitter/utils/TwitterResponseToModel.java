package com.ada.twitter.utils;

import android.text.TextUtils;
import android.util.Log;

import com.ada.twitter.models.Tweet;
import com.ada.twitter.models.User;
import com.ada.twitter.network.model.twitter.Twitter;
import com.annimon.stream.Stream;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ada on 10/29/16.
 */
public class TwitterResponseToModel {

    private static final String DATE_FORMAT = "EEE MMM dd hh:mm:ss Z yyyy";

    public static List<Tweet> twitterListToModelTweet(List<Twitter> resTweets) {
        List<Tweet> modelTweets = new ArrayList<>();
        Stream.of(resTweets).forEach(rt -> modelTweets.add(twitterToModelTweet(rt)));
        return modelTweets;
    }

    public static Tweet twitterToModelTweet(Twitter resTweet) {
        Tweet modelTweet = new Tweet();
        modelTweet.setIdStr(resTweet.getIdStr());
        modelTweet.setId(resTweet.getId());
        modelTweet.setBody(resTweet.getText());
        modelTweet.setCreatedAt(parseDate(resTweet.getCreatedAt()));
        modelTweet.setUser(twitterToUserModel(resTweet.getUser()));
        modelTweet.setFavorited(resTweet.getFavorited());
        modelTweet.setRetweeted(resTweet.getRetweeted());
        modelTweet.setRetweetCount(resTweet.getRetweetCount());
        modelTweet.setInReplyToStatusId(resTweet.getInReplyToStatusId());
        return modelTweet;
    }

    public static User twitterToUserModel(com.ada.twitter.network.model.twitter.User resUser) {
        User modelUser = new User();
        modelUser.setId(resUser.getId());
        modelUser.setName(resUser.getName());
        modelUser.setProfileImageUrl(resUser.getProfileImageUrl());
        modelUser.setScreenName(resUser.getScreenName());
        modelUser.setDescription(resUser.getDescription());
        modelUser.setFollowersCount(resUser.getFollowersCount());
        modelUser.setFriendsCount(resUser.getFriendsCount());
        return modelUser;
    }

    private static Date parseDate(String date) {
        if (!TextUtils.isEmpty(date)) {
            SimpleDateFormat dt = new SimpleDateFormat(DATE_FORMAT);
            try {
                return dt.parse(date);
            } catch (ParseException e) {
                Log.e("PARSE_DATE", e.getMessage());
            }
        }
        return null;
    }
}
