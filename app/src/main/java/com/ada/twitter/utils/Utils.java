package com.ada.twitter.utils;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.ada.twitter.network.model.twitter.Twitter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ada on 9/29/16.
 */
public class Utils {

    public static final String GSON_DATE_FORMAT = "yyyy-MM-dd";

    public static <T> T parseJSON(String response,  Class<T> classOfT) {
        if (TextUtils.isEmpty(response)) {
            return null;
        }
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat(GSON_DATE_FORMAT);
        Gson gson = gsonBuilder.create();
        return gson.fromJson(response, classOfT);
    }

    public static <T> List<T> parseJSONArray(String response, Class<T[]> classOfT) {
        if (TextUtils.isEmpty(response)) {
            return null;
        }
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat(GSON_DATE_FORMAT);
        Gson gson = gsonBuilder.create();
        gson.fromJson(response, Twitter[].class);
        T[] entities = gson.fromJson(response, classOfT);
        return Arrays.asList(entities);


    }

    public static boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // DeviceDimensionsHelper.getDisplayWidth(context) => (display width in pixels)
    public static int getDisplayWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    // DeviceDimensionsHelper.getDisplayHeight(context) => (display height in pixels)
    public static int getDisplayHeight(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    // DeviceDimensionsHelper.convertDpToPixel(25f, context) => (25dp converted to pixels)
    public static float convertDpToPixel(float dp, Context context){
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    // DeviceDimensionsHelper.convertPixelsToDp(25f, context) => (25px converted to dp)
    public static float convertPixelsToDp(float px, Context context){
        Resources r = context.getResources();
        DisplayMetrics metrics = r.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }
}
