package com.ada.twitter.network.model.twitter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Twitter {

    @SerializedName("coordinates")
    @Expose
    public Object coordinates;
    @SerializedName("truncated")
    @Expose
    public Boolean truncated;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("favorited")
    @Expose
    public Boolean favorited;
    @SerializedName("id_str")
    @Expose
    public String idStr;
    @SerializedName("in_reply_to_user_id_str")
    @Expose
    public Object inReplyToUserIdStr;
    @SerializedName("entities")
    @Expose
    public Entities entities;
    @SerializedName("text")
    @Expose
    public String text;
    @SerializedName("contributors")
    @Expose
    public Object contributors;
    @SerializedName("id")
    @Expose
    public Long id;
    @SerializedName("retweet_count")
    @Expose
    public Integer retweetCount;
    @SerializedName("in_reply_to_status_id_str")
    @Expose
    public Object inReplyToStatusIdStr;
    @SerializedName("geo")
    @Expose
    public Object geo;
    @SerializedName("retweeted")
    @Expose
    public Boolean retweeted;
    @SerializedName("in_reply_to_user_id")
    @Expose
    public Object inReplyToUserId;
    @SerializedName("place")
    @Expose
    public Object place;
    @SerializedName("source")
    @Expose
    public String source;
    @SerializedName("user")
    @Expose
    public User user;
    @SerializedName("in_reply_to_screen_name")
    @Expose
    public Object inReplyToScreenName;
    @SerializedName("in_reply_to_status_id")
    @Expose
    public Object inReplyToStatusId;
    @SerializedName("possibly_sensitive")
    @Expose
    public Boolean possiblySensitive;

    public Object getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Object coordinates) {
        this.coordinates = coordinates;
    }

    public Boolean getTruncated() {
        return truncated;
    }

    public void setTruncated(Boolean truncated) {
        this.truncated = truncated;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getFavorited() {
        return favorited;
    }

    public void setFavorited(Boolean favorited) {
        this.favorited = favorited;
    }

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public Object getInReplyToUserIdStr() {
        return inReplyToUserIdStr;
    }

    public void setInReplyToUserIdStr(Object inReplyToUserIdStr) {
        this.inReplyToUserIdStr = inReplyToUserIdStr;
    }

    public Entities getEntities() {
        return entities;
    }

    public void setEntities(Entities entities) {
        this.entities = entities;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Object getContributors() {
        return contributors;
    }

    public void setContributors(Object contributors) {
        this.contributors = contributors;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(Integer retweetCount) {
        this.retweetCount = retweetCount;
    }

    public Object getInReplyToStatusIdStr() {
        return inReplyToStatusIdStr;
    }

    public void setInReplyToStatusIdStr(Object inReplyToStatusIdStr) {
        this.inReplyToStatusIdStr = inReplyToStatusIdStr;
    }

    public Object getGeo() {
        return geo;
    }

    public void setGeo(Object geo) {
        this.geo = geo;
    }

    public Boolean getRetweeted() {
        return retweeted;
    }

    public void setRetweeted(Boolean retweeted) {
        this.retweeted = retweeted;
    }

    public Object getInReplyToUserId() {
        return inReplyToUserId;
    }

    public void setInReplyToUserId(Object inReplyToUserId) {
        this.inReplyToUserId = inReplyToUserId;
    }

    public Object getPlace() {
        return place;
    }

    public void setPlace(Object place) {
        this.place = place;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Object getInReplyToScreenName() {
        return inReplyToScreenName;
    }

    public void setInReplyToScreenName(Object inReplyToScreenName) {
        this.inReplyToScreenName = inReplyToScreenName;
    }

    public Object getInReplyToStatusId() {
        return inReplyToStatusId;
    }

    public void setInReplyToStatusId(Object inReplyToStatusId) {
        this.inReplyToStatusId = inReplyToStatusId;
    }

    public Boolean getPossiblySensitive() {
        return possiblySensitive;
    }

    public void setPossiblySensitive(Boolean possiblySensitive) {
        this.possiblySensitive = possiblySensitive;
    }
}
