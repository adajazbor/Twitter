package com.ada.twitter.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ada.twitter.R;
import com.ada.twitter.activities.UserInfoActivity;
import com.ada.twitter.adapters.TweetAdapter;
import com.ada.twitter.databinding.ItemTweetBinding;
import com.ada.twitter.models.Tweet;
import com.ada.twitter.models.TweetListType;
import com.ada.twitter.models.Tweet_Table;
import com.ada.twitter.models.User;
import com.ada.twitter.network.TwitterClient;
import com.ada.twitter.network.TwitterSearchParam;
import com.ada.twitter.network.model.twitter.Twitter;
import com.ada.twitter.utils.EndlessRecycleViewScrollListener;
import com.ada.twitter.utils.TwitterResponseToModel;
import com.ada.twitter.utils.Utils;
import com.annimon.stream.Stream;
import com.loopj.android.http.TextHttpResponseHandler;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public abstract class TweetListFragment extends Fragment {

    public interface TweetListFragmentHost {
        User getUser();
        TwitterClient getClient();
    }

    private List<Tweet> mTwitts = new ArrayList<>();
    private RecyclerView rvItems;
    private TweetAdapter mAdapter;
    private EndlessRecycleViewScrollListener scrollListener;
    private SwipeRefreshLayout swipeContainer;
    private Tweet workInProgressTweet;
    private TwitterSearchParam mSearchParams = new TwitterSearchParam();

    public TweetListFragment() {
        // default, required constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tweet_list, container, false);

        setupRecyclerViewAdapter();

        rvItems = (RecyclerView) rootView.findViewById(R.id.rvItems);
        LinearLayoutManager lLayoutManager = new LinearLayoutManager(getContext());
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

        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(() -> {
            readItems();
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);
                readItems();
            }
        });
        return rootView;
    }

    private TweetListFragmentHost getFragmentHost() {
        return (TweetListFragmentHost) getActivity();
    }

    private void setupRecyclerViewAdapter() {
        mAdapter = new TweetAdapter(
                getActivity(),
                mTwitts,
                new TweetAdapter.ItemArrayAdapterDelegate() {
                    @Override
                    public void onClick(int position) {
                        showDetailDialog(mTwitts.get(position));
                    }

                    @Override
                    public void onLike(final int position, final ItemTweetBinding binding) {
                        final String TAG = "SEND_LIKE";
                        final Tweet tw = mTwitts.get(position);
                        boolean currentStatus =  !Boolean.TRUE.equals(tw.getFavorited());
                        Log.d(TAG, "try to " + currentStatus +" :" + tw.getId());
                        getFragmentHost().getClient().likeTweet(new TextHttpResponseHandler() {
                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                Log.e(TAG, "Unable to like/unlike tweet: " + throwable.getStackTrace());
                                binding.btnLike.setImageResource(currentStatus ? R.drawable.ic_favorite_border_white_18dp: R.drawable.ic_favorite_white_18dp);
                                Toast.makeText(getActivity(), R.string.error_send_like, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                Log.d(TAG, "ready to parse: " + responseString);
                                Tweet tweet = saveTwitterTweetResponseInDB(responseString, position);
                                Log.d(TAG, "done");
                            }
                        }, tw.getId(), currentStatus);
                    }

                    @Override
                    public void onReply(int position, final ItemTweetBinding binding) {
                        //in_reply_to_status_id
                        final String TAG = "PREPARE_TO_REPLY";
                        final Tweet tw = mTwitts.get(position);
                        showReplyDialog(tw);
                    }

                    @Override
                    public void onRetweet(final int position, final ItemTweetBinding binding) {
                        final String TAG = "SEND_RETWEET";
                        final Tweet tw = mTwitts.get(position);
                        boolean currentStatus =  !Boolean.TRUE.equals(tw.getRetweeted());
                        Log.d(TAG, "try to " + currentStatus +" :" + tw.getId());
                        getFragmentHost().getClient().retweet(new TextHttpResponseHandler() {
                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                Log.e(TAG, "Unable to un/retweet: " + throwable.getMessage());
                                Toast.makeText(getActivity(), R.string.error_send_retweet, Toast.LENGTH_LONG).show();
                                binding.btnLink.setImageResource(currentStatus ? R.drawable.ic_link_white_18dp : R.drawable.ic_link_black_18dp);
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                Log.d(TAG, "ready to parse: " + responseString);
                                Tweet tweet = saveTwitterTweetResponseInDB(responseString, position);
                                Log.d(TAG, "done");
                            }
                        }, tw.getId(), currentStatus);
                    }

                    @Override
                    public void onProfilePictureClick(int position) {
                        UserInfoActivity.startActivity(getActivity(), mTwitts.get(position).getUser());
                    }
                });
    }

    private void showReplyDialog(Tweet parentTweet) {
        workInProgressTweet = new Tweet(parentTweet);
        showAddDialog();
    }

    public void showAddDialog() {
        AddTweetFragment dialog = AddTweetFragment.newInstance(new AddTweetFragment.OnFragmenAddTweetListener() {
            @Override
            public void onDataChanged(Parcelable parcelable) {
                Tweet tw = Parcels.unwrap(parcelable);
                //do not save reply to
                if (tw.getInReplyToStatusId() == null) {
                    workInProgressTweet = tw;
                } else {
                    workInProgressTweet = new Tweet();
                }
            }

            @Override
            public void onSend(Parcelable parcelable) {
                Tweet tweet = Parcels.unwrap(parcelable);
                mTwitts.add(0, tweet);
                mAdapter.notifyDataSetChanged();
                sendTweet(tweet);
                workInProgressTweet = null;
            }
        }, getFragmentHost().getUser(), workInProgressTweet);
        dialog.show(getFragmentManager(), "fragment_add_tweet");
    }

    private void showDetailDialog(Tweet tweet) {
        DetailsTweetFragment dialog = DetailsTweetFragment.newInstance(new DetailsTweetFragment.OnFragmenDetailsTweetListener() {
        }, tweet);
        dialog.show(getFragmentManager(), "fragment_details_tweet");
    }

    //==== data operations =========

    private boolean readItems(int page) {
        //TODO show progress bar
        mSearchParams.setPage(page);
        mSearchParams.setUserId(getFragmentHost().getUser().getId());
        getFragmentHost().getClient().getTimeLine(getTweetListType(),
                getTweetListResponseHandler(page > 0), mSearchParams);
        return true;
    }

    private boolean readItems() {
        return readItems(0);
    }

    private TextHttpResponseHandler getTweetListResponseHandler(final boolean nextPage) {
        final String TAG = "LOAD_TWEETS";
        return new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable throwable) {
                if (!Utils.isOnline()) {
                    Toast.makeText(getActivity(), R.string.error_network_connection_lost, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                Log.e(TAG, "Cannot download data: " + throwable.getMessage());
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
                    List<Tweet> newTweets = TwitterResponseToModel.twitterListToModelTweet(response);
                    mTwitts.addAll(newTweets);
                    mAdapter.notifyDataSetChanged();
                    new SaveTweetsInDBTask().execute(newTweets);
                } else {
                    Toast.makeText(getActivity(), R.string.error_no_results_info, Toast.LENGTH_LONG).show();
                }
                swipeContainer.setRefreshing(false);
                //TODO hide progress bar
            }
        };
    }

    private void sendTweet(Tweet tweet) {
        final String TAG = "SEND_TWEET";
        getFragmentHost().getClient().sendTweet(new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG, "Cannot save data: " + responseString);
                Toast.makeText(getActivity(), R.string.error_not_saved, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d(TAG, "ready to parse: " + responseString);
                saveTwitterTweetResponseInDB(responseString);
                Log.d(TAG, "done");
                Toast.makeText(getActivity(), R.string.info_saved, Toast.LENGTH_LONG).show();
            }
        }, tweet.getBody());
    }

    private Tweet saveTwitterTweetResponseInDB(String responseString, int position) {
        Tweet tweet = saveTwitterTweetResponseInDB(responseString);
        mTwitts.set(position, tweet);
        mAdapter.notifyDataSetChanged();
        return tweet;
    }

    private Tweet saveTwitterTweetResponseInDB(String responseString) {
        Twitter twitter = Utils.parseJSON(responseString, Twitter.class);
        Tweet tweet = TwitterResponseToModel.twitterToModelTweet(twitter);
        new SaveTweetsInDBTask().execute(Arrays.asList(tweet));
        return tweet;
    }

    protected abstract TweetListType getTweetListType();

    //========== Tasks ============
    private class SaveTweetsInDBTask extends AsyncTask<List<Tweet>, Void, Void> {

        @Override
        protected void onPreExecute() {
            //progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Void doInBackground(List<Tweet>... tweets) {
            Stream.of(tweets[0]).forEach(t -> {
                t.setTweetListType(getTweetListType());
                t.initSurogateId();
                t.save();});
            return null;
        }
    }

    private class ReadTweetsFromDBTask extends AsyncTask<Void, Void, List<Tweet>> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected List<Tweet> doInBackground(Void... params) {
            return SQLite
                    .select()
                    .from(Tweet.class)
                    .where(Tweet_Table.tweet_list_type_ordinal.is(getTweetListType().ordinal()))
                    .and(Tweet_Table.user_id.is(getFragmentHost().getUser().getId()))
                    .queryList();
        }

        @Override
        protected void onPostExecute(List<Tweet> tweets) {
            mTwitts.clear();
            mTwitts.addAll(tweets);
            mAdapter.notifyDataSetChanged();
        }
    }
}