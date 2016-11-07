package com.ada.twitter.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ada.twitter.R;
import com.ada.twitter.RestApplication;
import com.ada.twitter.adapters.TimelineFragmentAdapter;
import com.ada.twitter.databinding.ActivityMainBinding;
import com.ada.twitter.fragments.TweetListFragment;
import com.ada.twitter.models.User;
import com.ada.twitter.network.TwitterClient;
import com.ada.twitter.utils.TwitterResponseToModel;
import com.ada.twitter.utils.Utils;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements TweetListFragment.TweetListFragmentHost {

    private static final String CURRENT_FRAGMENT_IDX_KEY = "current_fragment_idx_key";
    private static final String CURRENT_USER_ID_SHARED_PREF_KEY = "current_user_id_shared_pref_key";

    private User mCurrentUser;
    private TwitterClient mClient;
    private Toolbar toolbar;
    private ActivityMainBinding binding;

    private TimelineFragmentAdapter mFragmentAdapter;
    private ViewPager mViewPager;
    private int mCurrentFragmentIdx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mClient = RestApplication.getRestClient();
        populateCurrentUser();
        setupToolbar();

        initializeDummyCurrentUserSharedPreferences();

        if (mCurrentUser != null) {
            initializeTabs();
        }

        if (savedInstanceState != null) {
            mCurrentFragmentIdx = savedInstanceState.getInt(CURRENT_FRAGMENT_IDX_KEY);
        }
    }

    public void initializeTabs() {
        if (mViewPager != null) {
            return;
        }
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = binding.viewpager;
        mFragmentAdapter = new TimelineFragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mFragmentAdapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentFragmentIdx = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mViewPager.setCurrentItem(mCurrentFragmentIdx);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(CURRENT_FRAGMENT_IDX_KEY, mCurrentFragmentIdx);
    }

    public void populateCurrentUser() {
        readCurrentUser();
    }

    private void setupToolbar() {
        toolbar = binding.includedToolbar.toolbar;
        setSupportActionBar(toolbar);
    }
    //====menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_timeline2, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d("MENU OPTIONS", "itemId = " + id + ", advanced = " + android.R.id.home);
        switch (id) {
            case R.id.miNewTweet:
                getCurrentFragment().showAddDialog();
                return false;
            case android.R.id.home:
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private TweetListFragment getCurrentFragment() {
        return (TweetListFragment) mFragmentAdapter.instantiateItem(mViewPager, mCurrentFragmentIdx);
    }

    private void readCurrentUser() {
        final String TAG = "LOAD_CURRENT_USER";
        mClient.getLoggedUserInfo(new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG, "Cannot download data: " + responseString);
                if (!Utils.isOnline()) {
                    Toast.makeText(MainActivity.this, R.string.error_network_connection_lost, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d(TAG, "ready to parse: " + responseString);
                com.ada.twitter.network.model.twitter.User user = Utils.parseJSON(responseString, com.ada.twitter.network.model.twitter.User.class);
                mCurrentUser = TwitterResponseToModel.twitterToUserModel(user);
                saveCurrentUserIdToSharedPreferences();
                initializeTabs();
                binding.setCurrentUser(mCurrentUser);
                View.OnClickListener userDetailsOpenListener =
                        v -> UserInfoActivity.startActivity(MainActivity.this, mCurrentUser);
                binding.includedToolbar.imLogo.setOnClickListener(userDetailsOpenListener);
                binding.includedToolbar.imText.setOnClickListener(userDetailsOpenListener);
                binding.executePendingBindings();
                Log.d(TAG, "done");
            }
        });
    }

    @Override
    public User getUser() {
        initializeDummyCurrentUserSharedPreferences();
        return mCurrentUser;
    }

    @Override
    public TwitterClient getClient() {
        return mClient;
    }

    private void saveCurrentUserIdToSharedPreferences() {
        SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
        editor.putLong(CURRENT_USER_ID_SHARED_PREF_KEY, mCurrentUser.getId());
        editor.commit();
    }

    private void initializeDummyCurrentUserSharedPreferences() {
        if (mCurrentUser != null) {
            return;
        }
        long userId = getPreferences(Context.MODE_PRIVATE).getLong(CURRENT_USER_ID_SHARED_PREF_KEY, -1);
        if (userId != -1) {
            mCurrentUser = new User();
            mCurrentUser.setId(userId);
        }
    }
}
