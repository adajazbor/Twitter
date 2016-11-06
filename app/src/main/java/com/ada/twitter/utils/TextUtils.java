package com.ada.twitter.utils;

import android.graphics.Color;
import android.widget.TextView;

import com.ada.twitter.activities.UserInfoActivity;
import com.ada.twitter.models.Tweet;

import java.util.regex.Pattern;

/**
 * Created by ada on 11/6/16.
 */
public class TextUtils {
    private final static Pattern EMAIL_ADDRESS = Pattern.compile("\\@(\\w+)");
    private final static int LINK_COLOR = Color.parseColor("#ff0099cc");

    public static void makeEmailsAddressesSelectedAndClickable(TextView textView, Tweet tweet) {
        new PatternEditableBuilder().
                addPattern(EMAIL_ADDRESS, LINK_COLOR,
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                UserInfoActivity.startActivity(textView.getContext(), tweet.getUser());
                            }
                        })
                .into(textView);
    }
}
