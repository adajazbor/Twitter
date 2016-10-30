package com.ada.twitter.network;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.com.ada.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
	public static final String REST_URL = "https://api.twitter.com/1.1";
	public static final String REST_CALLBACK_URL = "oauth://cpadatwitter";

	public static final String REST1_CONSUMER_KEY = "VkGqGdKUhqm4OwRJrTpS0pm6f";
	public static final String REST1_CONSUMER_SECRET = "7Cszh56nuHEZ5hOY0T5n4vOUj8WiNINBLCDztAZgR93GkeFdWM";

	public static final String REST2_CONSUMER_KEY = "xvFxsSAdwI6l0V4GDVKCJR8dm";
	public static final String REST2_CONSUMER_SECRET = "dpqjuMXItnDoQehjjiQ8ZE9EsxkrAz2BYY2nrjK73KHgDnHavZ";

	private static final String GET_TWITTER_TIMELINE = "/statuses/home_timeline.json";
	private static final String GET_TWITTER_LOGGED_USER = "/account/verify_credentials.json";
	private static final String POST_TWITTER_ADD_TWEET = "/statuses/update.json";

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST2_CONSUMER_KEY, REST2_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	public void getHomeTimeLine(TextHttpResponseHandler responseHandler, TwitterSearchParam params) {
		String apiUrl = getApiUrl(GET_TWITTER_TIMELINE);
		client.get(apiUrl, prepareParams(params), responseHandler);
	}

	public void getLoggedUserInfo(TextHttpResponseHandler responseHandler) {
		client.get(getApiUrl(GET_TWITTER_LOGGED_USER), responseHandler);
	}

	public void sendTweet(TextHttpResponseHandler responseHandler, String body) {
		RequestParams rParams = new RequestParams();
		rParams.put("status", body);
		client.post(getApiUrl(POST_TWITTER_ADD_TWEET), rParams, responseHandler);
	}

	private RequestParams prepareParams(TwitterSearchParam params) {
		RequestParams rParams = new RequestParams();
		rParams.put("count", params.getCount());
		if (params.getPage() > 0) {
			rParams.put("max_id", params.getMaxId());
		} else {
			rParams.put("since_id", params.getSinceId());
		}
		return rParams;
	}

}
