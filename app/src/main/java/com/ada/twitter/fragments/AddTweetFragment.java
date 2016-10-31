package com.ada.twitter.fragments;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

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

    private static final int MAX_BODY_LENGTH = 140;

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
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText et = binding.etBody;
        setCoutenr(et.length());
        et.setSelection(et.length());

        binding.etBody.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setCoutenr(s.length());
            }
        });

        binding.btnSave.setOnClickListener((v) -> {
            int length = binding.etBody.length();
            if (length <= MAX_BODY_LENGTH && length > 0) {
                mTweet.setBody(binding.etBody.getText().toString());
                mListener.onSend(Parcels.wrap(mTweet));
                dismiss();
            } else {
                Toast.makeText(getContext(), "The Tweet is to long", Toast.LENGTH_LONG).show();
            }
        });

        binding.btnCancel.setOnClickListener((v) -> {
            mTweet.setBody(binding.etBody.getText().toString());
            mListener.onDataChanged(Parcels.wrap(mTweet));
            dismiss();
        });
    }

    private void setCoutenr(int bodyLength) {
        int counter = MAX_BODY_LENGTH - bodyLength;
        binding.tvCounter.setText(String.valueOf(counter));
        binding.tvCounter.setTextColor(counter >= 0 ? Color.BLACK : Color.RED);
        binding.btnSave.setClickable(counter >= 0 && counter < MAX_BODY_LENGTH);
    }

}
