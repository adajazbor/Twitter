package com.ada.twitter.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.ada.twitter.fragments.HomeTweetListFragment;
import com.ada.twitter.fragments.MentionsTweetListFragment;
import com.ada.twitter.fragments.TweetListFragment;

/**
 * Created by ada on 11/5/16.
 */

public class TimelineFragmentAdapter extends FragmentStatePagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "HOME", "MENTIONS"};
    private TweetListFragment.DataProvider mFragmentDataProvider;

    public TimelineFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setFragmentDataProvider(TweetListFragment.DataProvider fragmentDataProvider) {
        this.mFragmentDataProvider = fragmentDataProvider;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new HomeTweetListFragment();
        } else {
            return new MentionsTweetListFragment();
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TweetListFragment fragment = (TweetListFragment) super.instantiateItem(container, position);
        fragment.setDataProvider(mFragmentDataProvider);
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
