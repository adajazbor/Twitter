package com.ada.twitter.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ada.twitter.R;
import com.ada.twitter.databinding.FragmentDetailsTweetBinding;
import com.ada.twitter.models.Tweet;
import com.ada.twitter.utils.Constants;

import org.parceler.Parcels;

public class DetailsTweetFragment extends DialogFragment {

    private FragmentDetailsTweetBinding binding;
    private OnFragmenDetailsTweetListener mListener;
    private Tweet mTweet;

    public DetailsTweetFragment() {
        // Required empty public constructor
    }

    public interface OnFragmenDetailsTweetListener {
    }

    //maybe we will need edit functionality in the future so it is better to send tweet
    public static DetailsTweetFragment newInstance(OnFragmenDetailsTweetListener listener, Tweet tweet) {
        DetailsTweetFragment frag = new DetailsTweetFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.PARAM_TWEET, Parcels.wrap(tweet));
        frag.setArguments(args);
        frag.setListener(listener);
        return frag;
    }

    public void setListener(OnFragmenDetailsTweetListener listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details_tweet, container, false);
        Parcelable param = getArguments().getParcelable(Constants.PARAM_TWEET);
        mTweet = Parcels.unwrap(param);
        binding.setTweet(mTweet);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnCancel.setOnClickListener((v) -> {
            dismiss();
        });
    }

}
