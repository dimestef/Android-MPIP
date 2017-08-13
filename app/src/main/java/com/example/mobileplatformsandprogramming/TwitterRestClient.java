package com.example.mobileplatformsandprogramming;

import com.loopj.android.http.*;

/**
 * Created by Dimitar on 7/31/2017.
 */

public class TwitterRestClient {
    private static AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
    private static final String BASE_URL = "http://www.theimdbapi.org/api/find/movie?title=";
//    private static final String BASE_URL = "http://jsonplaceholder.typicode.com/";

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

}
