package com.ada.twitter.fragments;

import com.ada.twitter.models.TweetListType;

/**
 * Created by ada on 11/5/16.
 */

public class HomeTweetListFragment extends TweetListFragment {

    public HomeTweetListFragment() {
    }

    @Override
    protected TweetListType getTweetListType() {
        return TweetListType.HOME_TIMELINE;
    }
}
