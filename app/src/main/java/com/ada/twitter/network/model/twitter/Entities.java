package com.ada.twitter.network.model.twitter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Entities {

    @SerializedName("urls")
    @Expose
    public List<Object> urls = new ArrayList<Object>();
    @SerializedName("hashtags")
    @Expose
    public List<Object> hashtags = new ArrayList<Object>();
    @SerializedName("user_mentions")
    @Expose
    public List<Object> userMentions = new ArrayList<Object>();

    public List<Object> getUrls() {
        return urls;
    }

    public void setUrls(List<Object> urls) {
        this.urls = urls;
    }

    public List<Object> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<Object> hashtags) {
        this.hashtags = hashtags;
    }

    public List<Object> getUserMentions() {
        return userMentions;
    }

    public void setUserMentions(List<Object> userMentions) {
        this.userMentions = userMentions;
    }
}
