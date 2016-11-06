package com.ada.twitter.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ada.twitter.R;
import com.ada.twitter.databinding.ActivityUserInfoBinding;
import com.ada.twitter.models.User;

import org.parceler.Parcels;

public class UserInfoActivity extends AppCompatActivity {
    private static final String USER_PARCELABLE_EXTRA = "user";

    private User mUser;
    private ActivityUserInfoBinding mBinding;

    public UserInfoActivity() {
    }

    public static Intent getIntent(Context context, User user) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        intent.putExtra(USER_PARCELABLE_EXTRA, Parcels.wrap(user));
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = Parcels.unwrap(getIntent().getParcelableExtra(USER_PARCELABLE_EXTRA));
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_info);
        mBinding.setUser(mUser);
        mBinding.followersCount.setText(mUser.getFollowersCount() + " " + getString(R.string.followers));
        mBinding.friendsCount.setText(mUser.getFriendsCount() + " " + getString(R.string.friends));
    }

    public static void startActivity(Context context, User user) {
        context.startActivity(getIntent(context, user));
    }
}
