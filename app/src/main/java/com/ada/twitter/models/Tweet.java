package com.ada.twitter.models;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.ocpsoft.prettytime.PrettyTime;
import org.parceler.Parcel;

import java.util.Date;
import java.util.List;

/**
 * Created by ada on 10/28/16.
 */
@Parcel(analyze={Tweet.class})
@Table(database = TwitterDatabase.class)
public class Tweet extends BaseModel {

    @Column
    @PrimaryKey
    String surogateId; // this.tweetListType.surogateIdPrefix + this.id

    @Column
    Long id;

    @Column
    String body;

    @ForeignKey(saveForeignKeyModel = false)
    User user;

    @Column(name = "created_at")
    Date createdAt;

    @Column(name = "id_str")
    String idStr;

    @Column(name = "retweet_count")
    Integer retweetCount;

    @Column(name = "retweeted")
    Boolean retweeted;

    @Column(name = "favorited")
    Boolean favorited;

    @Column(name = "in_reply_to_status_id")
    Long inReplyToStatusId;

    @Column(name = "tweet_list_type_ordinal")
    Integer tweetListTypeOdrinal;

    TweetListType tweetListType;

    Tweet inReplyToStatus;

    public Tweet() {
        super();
    }

    public Tweet(Tweet parentTweet) {
        this();
        setInReplyToStatus(parentTweet);
        setBody("@" + parentTweet.getUser().getScreenName());
    }

    public Long getInReplyToStatusId() {
        return inReplyToStatusId;
    }

    public void setInReplyToStatusId(Long inReplyToStatusId) {
        this.inReplyToStatusId = inReplyToStatusId;
    }

    public Tweet getInReplyToStatus() {
        return inReplyToStatus;
    }

    public void setInReplyToStatus(Tweet inReplyToStatus) {
        this.inReplyToStatus = inReplyToStatus;
        this.inReplyToStatusId = inReplyToStatus.getId();
    }

    public void initSurogateId() {
        if (id == null) {
            throw new RuntimeException("id needs to be set before surogate id");
        }
        if (tweetListType == null) {
            throw new RuntimeException("tweet list type needs to be set before surogate id");
        }
        surogateId = tweetListType.getSurogateIdPrefix() + id;
    }

    public Integer getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(Integer retweetCount) {
        this.retweetCount = retweetCount;
    }

    public Boolean getRetweeted() {
        return retweeted;
    }

    public void setRetweeted(Boolean retweeted) {
        this.retweeted = retweeted;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public TweetListType getTweetListType() {
        return TweetListType.values()[tweetListTypeOdrinal];
    }

    public void setTweetListType(TweetListType tweetListType) {
        this.tweetListTypeOdrinal = tweetListType.ordinal();
        this.tweetListType = tweetListType;
    }

    public String getCreatedFromNow() {
        return new PrettyTime().format(this.createdAt);
    }

    public static List<Tweet> getAll() {
        return SQLite.select().from(Tweet.class).orderBy(Tweet_Table.id, false).queryList();
    }
}
