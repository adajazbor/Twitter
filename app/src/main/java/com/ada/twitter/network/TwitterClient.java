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

	public static final String REST1_CONSUMER_KEY = "gnNtJsuRxuAaSscEZrasX5uAI";
	public static final String REST1_CONSUMER_SECRET = "JEcOur7wC4tovkgQ7d1k8MWg5jGvBLWo42IUa7BLxa8LfEcd0z";

	public static final String REST2_CONSUMER_KEY = "dSuIDnjoR9bh2sLZ4hFLbAPMM";
	public static final String REST2_CONSUMER_SECRET = "PVwOGO1pGqZa9MaURUdbknILkwwlkSx4OJprvFjnvEK186N55h";

	private static final String GET_TWITTER_TIMELINE = "/statuses/home_timeline.json";
	private static final String GET_TWITTER_LOGGED_USER = "/account/verify_credentials.json";

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
