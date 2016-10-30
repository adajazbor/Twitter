package com.ada.twitter.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.ada.twitter.R;
import com.ada.twitter.databinding.FragmentAddTweetBinding;
import com.ada.twitter.models.Tweet;
import com.ada.twitter.models.User;
import com.ada.twitter.utils.Constants;

import org.parceler.Parcels;

public class AddTweetFragment extends DialogFragment {

    private FragmentAddTweetBinding binding;
    private OnFragmenAddTweetListener mListener;
    private Tweet mTweet;

    public AddTweetFragment() {
        // Required empty public constructor
    }

    public interface OnFragmenAddTweetListener {
        void onDataChanged(Parcelable parcelable);
        void onSend(Parcelable parcelable);
    }

    //maybe we will need edit functionality in the future so it is better to send tweet
    public static AddTweetFragment newInstance(OnFragmenAddTweetListener listener, User user, Tweet tweet) {
        AddTweetFragment frag = new AddTweetFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.PARAM_TWEET, Parcels.wrap(tweet));
        args.putParcelable(Constants.PARAM_USER, Parcels.wrap(user));
        frag.setArguments(args);
        frag.setListener(listener);
        return frag;
    }

    public void setListener(OnFragmenAddTweetListener listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_tweet, container, false);
        Parcelable param = getArguments().getParcelable(Constants.PARAM_TWEET);
        mTweet = (param == null ? new Tweet() : Parcels.unwrap(param));
        User user = Parcels.unwrap(getArguments().getParcelable(Constants.PARAM_USER));
        mTweet.setUser(user);
        binding.setTweet(mTweet);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnDone = binding.btnSave;
        btnDone.setOnClickListener((v) -> {
            mTweet.setBody(binding.etBody.getText().toString());
            mListener.onSend(Parcels.wrap(mTweet));
            dismiss();
        });

        ImageButton btnCancel = binding.btnCancel;
        btnCancel.setOnClickListener((v) -> {
            mTweet.setBody(binding.etBody.getText().toString());
            mListener.onDataChanged(Parcels.wrap(mTweet));
            dismiss();
        });

    }
/*
    private View.OnClickListener getOnDoneListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTweet.setBody(binding.etBody.getText().toString());
                mListener.onDateChanged(Parcels.wrap(mTweet));
                dismiss();
            }
        };
    }
*/
    /*
    private View.OnClickListener getOnCancelListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTweet.setBody(binding.etBody.getText().toString());
                dismiss();
            }
        };
    }
    */

}
