package com.ada.twitter.fragments;

import com.ada.twitter.models.TweetListType;

/**
 * Created by ada on 11/6/16.
 */
public class UserTweetListFragment extends TweetListFragment {
    @Override
    protected TweetListType getTweetListType() {
        return TweetListType.USER_TIMELINE;
    }
}
