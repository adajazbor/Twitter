package com.ada.twitter.fragments;

import com.ada.twitter.models.TweetListType;

/**
 * Created by ada on 11/5/16.
 */

public class MentionsTweetListFragment extends TweetListFragment {

    public MentionsTweetListFragment() {
    }

    @Override
    protected TweetListType getTweetListType() {
        return TweetListType.MENTIONS_TIMELINE;
    }
}
