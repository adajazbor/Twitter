package com.ada.twitter.network;

import android.content.Context;

import com.ada.twitter.models.TweetListType;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

public class TwitterClient extends OAuthBaseClient {
	private static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
	private static final String REST_URL = "https://api.twitter.com/1.1";
	private static final String REST_CALLBACK_URL = "oauth://cpadatwitter";
	private static final String REST2_CONSUMER_KEY = "VkGqGdKUhqm4OwRJrTpS0pm6f";
	private static final String REST2_CONSUMER_SECRET = "7Cszh56nuHEZ5hOY0T5n4vOUj8WiNINBLCDztAZgR93GkeFdWM";
	private static final String REST1_CONSUMER_KEY = "xvFxsSAdwI6l0V4GDVKCJR8dm";
	private static final String REST1_CONSUMER_SECRET = "dpqjuMXItnDoQehjjiQ8ZE9EsxkrAz2BYY2nrjK73KHgDnHavZ";
	private static final String GET_HOME_TIMELINE = "/statuses/home_timeline.json";
	private static final String GET_MENTIONS_TIMELINE = "/statuses/mentions_timeline.json";
	private static final String GET_USER_TIMELINE = "/statuses/user_timeline.json";
	private static final String GET_TWITTER_LOGGED_USER = "/account/verify_credentials.json";
	private static final String POST_TWITTER_ADD_TWEET = "/statuses/update.json";
	private static final String POST_TWITTER_LIKE_TWEET = "/favorites/create.json";
	private static final String POST_TWITTER_UNLIKE_TWEET = "/favorites/destroy.json";
	private static final String POST_TWITTER_RETWEET = "/statuses/retweet/%d.json";
	private static final String POST_TWITTER_UNRETWEET = "/statuses/unretweet/%d.json";
    private static final String USERS_SHOW = "/users/show.json?user_id=%d";

    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST1_CONSUMER_KEY, REST1_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    public void getTimeLine(TweetListType tweetListType, TextHttpResponseHandler responseHandler,
                            TwitterSearchParam params) {
        String timelineUrlSuffix = null;
        switch (tweetListType) {
            case HOME_TIMELINE:
                timelineUrlSuffix = GET_HOME_TIMELINE;
                break;
            case MENTIONS_TIMELINE:
                timelineUrlSuffix = GET_MENTIONS_TIMELINE;
                break;
            case USER_TIMELINE:
                timelineUrlSuffix = GET_USER_TIMELINE;
                break;
            default:
                throw new UnsupportedOperationException("unsupported tweet list type: " + tweetListType);
        }
        String apiUrl = getApiUrl(timelineUrlSuffix);
        client.get(apiUrl, prepareParams(params, tweetListType == TweetListType.USER_TIMELINE), responseHandler);
    }

	public void getLoggedUserInfo(TextHttpResponseHandler responseHandler) {
		client.get(getApiUrl(GET_TWITTER_LOGGED_USER), responseHandler);
	}

	public void getUserInfo(TextHttpResponseHandler responseHandler, Long userId) {
		RequestParams rParams = new RequestParams();
		rParams.put("user_id", userId);
		client.post(getApiUrl(USERS_SHOW), rParams, responseHandler);
	}

    public void sendTweet(TextHttpResponseHandler responseHandler, String body) {
        RequestParams rParams = new RequestParams();
        rParams.put("status", body);
        client.post(getApiUrl(POST_TWITTER_ADD_TWEET), rParams, responseHandler);
    }

    public void likeTweet(TextHttpResponseHandler responseHandler, Long id, boolean currentlyLike) {
		if (currentlyLike) {
			likeTweet(responseHandler, id);
		} else {
			unlikeTweet(responseHandler, id);
		}
	}

	private void likeTweet(TextHttpResponseHandler responseHandler, Long id) {
		RequestParams rParams = new RequestParams();
		rParams.put("id", id);
		client.post(getApiUrl(POST_TWITTER_LIKE_TWEET), rParams, responseHandler);
	}

	private void unlikeTweet(TextHttpResponseHandler responseHandler, Long id) {
		RequestParams rParams = new RequestParams();
		rParams.put("id", id);
		client.post(getApiUrl(POST_TWITTER_UNLIKE_TWEET), rParams, responseHandler);
	}

	public void retweet(TextHttpResponseHandler responseHandler, Long id, boolean currentlyRetweeted) {
		if (currentlyRetweeted) {
			unretweet(responseHandler, id);
		} else {
			retweet(responseHandler, id);
		}
	}

	private void retweet(TextHttpResponseHandler responseHandler, Long id) {
		client.post(getApiUrl(String.format(POST_TWITTER_RETWEET, id)), responseHandler);
	}

	private void unretweet(TextHttpResponseHandler responseHandler, Long id) {
		client.post(getApiUrl(String.format(POST_TWITTER_UNRETWEET, id)), responseHandler);
	}

	private RequestParams prepareParams(TwitterSearchParam params, boolean addUserId) {
		RequestParams rParams = new RequestParams();
		rParams.put("count", params.getCount());
		if (params.getPage() > 0) {
			rParams.put("max_id", params.getMaxId());
		} else {
			rParams.put("since_id", params.getSinceId());
		}
        if (addUserId) {
            rParams.put("user_id", params.getUserId());
        }
		return rParams;
	}

}
