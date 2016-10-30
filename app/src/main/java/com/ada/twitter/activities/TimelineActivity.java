package com.ada.twitter.activities;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.ada.twitter.R;
import com.ada.twitter.RestApplication;
import com.ada.twitter.adapters.TweetAdapter;
import com.ada.twitter.databinding.ActivityTimelineBinding;
import com.ada.twitter.fragments.AddTweetFragment;
import com.ada.twitter.models.Tweet;
import com.ada.twitter.models.User;
import com.ada.twitter.network.TwitterClient;
import com.ada.twitter.network.TwitterSearchParam;
import com.ada.twitter.network.model.twitter.Twitter;
import com.ada.twitter.utils.EndlessRecycleViewScrollListener;
import com.ada.twitter.utils.TwitterResponseToModel;
import com.ada.twitter.utils.Utils;
import com.annimon.stream.Stream;
import com.loopj.android.http.TextHttpResponseHandler;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private List<Tweet> mTwitts = new ArrayList<>();
    private User mCurrentUser;
    private TweetAdapter mAdapter;
    private EndlessRecycleViewScrollListener scrollListener;
    private SwipeRefreshLayout swipeContainer;
    private TwitterSearchParam mSearchParams = new TwitterSearchParam();
    private TwitterClient mClient;

    private Tweet workInProgressTweet;

    private RecyclerView rvItems;
    private Toolbar toolbar;
    private ActivityTimelineBinding binding;

    private static final String TAG = TimelineActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_timeline);

        mClient = RestApplication.getRestClient();
        populateCurrentUser();
        populateArrayItems();
        setupToolbar();

        rvItems = binding.rvItems;
        LinearLayoutManager lLayoutManager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(lLayoutManager);
        rvItems.setAdapter(mAdapter);
        scrollListener = new EndlessRecycleViewScrollListener(lLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d("on Load More", "page = " + page);
                readItems(page);
            }
        };
        // Adds the scroll listener to RecyclerView
        rvItems.addOnScrollListener(scrollListener);

        swipeContainer = binding.swipeContainer;
        swipeContainer.setOnRefreshListener(() -> {
            populateCurrentUser();
            readItems();
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    public void populateArrayItems() {
        mAdapter = new TweetAdapter(
                this,
                mTwitts,
                new TweetAdapter.ItemArrayAdapterDelegate() {
                    @Override
                    public void onClick(int position) {
                        /*TODO Show details
                        Intent i = new Intent(TwittAdapter.this, ArticleActivity.class);
                        i.putExtra(Constants.PARAM_ITEM, Parcels.wrap(articles.get(position)));
                        startActivity(i);
                        */
                    }
                });
        readItems();
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
        //miClear.setVisible(false);\

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d("MENU OPTIONS", "itemId = " + id + ", advanced = " + android.R.id.home);
        switch (id) {
            case R.id.miNewTweet:
                showAddDialog();
                return false;
            case android.R.id.home:
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /*
    private void showAddDialog() {
        AddTweetFragment dialog = AddTweetFragment.newInstance((params) -> {
            Tweet tweet = Parcels.unwrap(params);
            mTwitts.add(0, tweet);
            mAdapter.notifyDataSetChanged();
            sendTweet(tweet);
            ;}, mCurrentUser, null);
        dialog.show(getSupportFragmentManager(), "fragment_add_tweet");
    }
     */
    private void showAddDialog() {
        AddTweetFragment dialog = AddTweetFragment.newInstance(new AddTweetFragment.OnFragmenAddTweetListener() {
            @Override
            public void onDataChanged(Parcelable parcelable) {
                workInProgressTweet = Parcels.unwrap(parcelable);
            }

            @Override
            public void onSend(Parcelable parcelable) {
                Tweet tweet = Parcels.unwrap(parcelable);
                mTwitts.add(0, tweet);
                mAdapter.notifyDataSetChanged();
                sendTweet(tweet);
                workInProgressTweet = null;
            }
        }, mCurrentUser, workInProgressTweet);
        dialog.show(getSupportFragmentManager(), "fragment_add_tweet");
    }

    //==== data operations
    private boolean readItems(int page) {
        //TODO show progress bar
        mSearchParams.setPage(page);
        mClient.getHomeTimeLine(getTweetListResponseHandler(page > 0), mSearchParams);
        return true;
    }

    private boolean readItems() {
        return readItems(0);
    }

    private TextHttpResponseHandler getTweetListResponseHandler(final boolean nextPage) {
        return new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable throwable) {
                if (!Utils.isOnline()) {
                    Toast.makeText(TimelineActivity.this, R.string.error_network_connection_lost, Toast.LENGTH_LONG).show();
                }
                Log.e(TAG, "Cannot download data: " + res);
                new ReadTweetsFromDBTask().execute();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String res) {
                Log.d(TAG, "downloaded");
                List<Twitter> response = Utils.parseJSONArray(res, Twitter[].class);
                if (!nextPage) {
                    mTwitts.clear();
                }
                Log.d(TAG, "res to model done:" + (response == null ? "EMPTY" : response.size()));
                if (response != null && response.size() > 0) {
                    mSearchParams.setMaxId(response.get(response.size() -1).getId());
                    mTwitts.addAll(TwitterResponseToModel.twitterListToModelTweet(response));
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(TimelineActivity.this, R.string.error_no_results_info, Toast.LENGTH_LONG);
                }
                //TODO hide progress bar
                new SaveTweetsInDBTask().execute(mTwitts);
                swipeContainer.setRefreshing(false);
            }
        };
    }

    private void sendTweet(Tweet tweet) {
        mClient.sendTweet(new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("SEND_TWEETER", "Cannot save data: " + responseString);
                Toast.makeText(TimelineActivity.this, R.string.error_not_saved, Toast.LENGTH_LONG);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("SEND_TWEETER", "ready to parse: " + responseString);
                Twitter twitter = Utils.parseJSON(responseString, Twitter.class);
                Tweet tweet = TwitterResponseToModel.twitterToModelTweet(twitter);
                new SaveTweetsInDBTask().execute(Arrays.asList(tweet));
                Log.d("SEND_TWEETER", "done");
                Toast.makeText(TimelineActivity.this, R.string.info_saved, Toast.LENGTH_LONG);
            }
        }, tweet.getBody());
    }

    private void readCurrentUser() {
        mClient.getLoggedUserInfo(new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("CURRENT_USER", "Cannot download data: " + responseString);
                if (!Utils.isOnline()) {
                    Toast.makeText(TimelineActivity.this, R.string.error_network_connection_lost, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("CURRENT_USER", "ready to parse: " + responseString);
                com.ada.twitter.network.model.twitter.User user = Utils.parseJSON(responseString, com.ada.twitter.network.model.twitter.User.class);
                mCurrentUser = TwitterResponseToModel.twitterToUserModel(user);
                binding.setCurrentUser(mCurrentUser);
                binding.executePendingBindings();
                Log.d("CURRENT_USER", "done");
            }
        });
    }

    //========== Tasks ============
    private class SaveTweetsInDBTask extends AsyncTask<List<Tweet>, Void, Void> {

        @Override
        protected void onPreExecute() {
            //progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Void doInBackground(List<Tweet>... tweets) {
            Stream.of(tweets[0]).forEach(t -> t.save());
            return null;
        }

    }

    private class ReadTweetsFromDBTask extends AsyncTask<Void, Void, List<Tweet>> {

        @Override
        protected void onPreExecute() {
            //progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected List<Tweet> doInBackground(Void... params) {
            return Tweet.getAll();
        }

        @Override
        protected void onPostExecute(List<Tweet> tweets) {
            mTwitts.clear();
            mTwitts.addAll(tweets);
            mAdapter.notifyDataSetChanged();
        }
    }
}
