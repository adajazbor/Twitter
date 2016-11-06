package com.ada.twitter.models;

/**
 * Created by ada on 11/5/16.
 */
public enum TweetListType {
    HOME_TIMELINE("H"),
    USER_TIMELINE("U"),
    MENTIONS_TIMELINE("M");

    private String mSurogateIdPrefix;

    TweetListType(String surogateIdPrefix) {
        mSurogateIdPrefix = surogateIdPrefix;
    }

    public String getSurogateIdPrefix() {
        return mSurogateIdPrefix;
    }
}
