package com.ada.twitter.models;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by ada on 10/29/16.
 */
@Database(name = TwitterDatabase.NAME, version = TwitterDatabase.VERSION)
public class TwitterDatabase {
    public static final String NAME = "twitterDB";
    public static final int VERSION = 1;
}
