package com.ada.twitter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ada.twitter.R;
import com.ada.twitter.databinding.ItemTweetBinding;
import com.ada.twitter.models.Tweet;

import java.util.List;

/**
 * Created by ada on 10/28/16.
 */

public class TweetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Tweet> mItems;
    private ItemArrayAdapterDelegate mDelegate;
    private Context mContext;

    public TweetAdapter(Context context, List<Tweet> items, ItemArrayAdapterDelegate delegate) {
        mItems = items;
        mContext = context;
        mDelegate = delegate;
    }

    public interface ItemArrayAdapterDelegate {
        void onClick(int position);
        void onLike(int position, ItemTweetBinding binding);
        void onRetweet(int position, ItemTweetBinding binding);
        void onReply(int position, ItemTweetBinding binding);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = new ViewHolderTweet(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tweet, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        configureViewHolderTweet((ViewHolderTweet) holder, position);
    }

    private void configureViewHolderTweet(ViewHolderTweet viewHolder, int position) {
        Tweet tweet = mItems.get(position);
        viewHolder.binding.setTweet(tweet);
        viewHolder.binding.btnLike.setImageResource(!Boolean.TRUE.equals(tweet.getFavorited()) ? R.drawable.ic_favorite_border_white_18dp : R.drawable.ic_favorite_white_18dp);
        viewHolder.binding.btnLink.setImageResource(!Boolean.TRUE.equals(tweet.getRetweeted()) ? R.drawable.ic_link_white_18dp : R.drawable.ic_link_black_18dp);
        viewHolder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        if (mItems == null) {
            return 0;
        }
        return mItems.size();
    }

    public class ViewHolderTweet extends RecyclerView.ViewHolder {

        ItemTweetBinding binding;

        public ViewHolderTweet(View itemView) {
            super(itemView);
            binding = ItemTweetBinding.bind(itemView);

            binding.btnReply.setOnClickListener((v) -> mDelegate.onReply(getAdapterPosition(), binding));
            itemView.setOnClickListener((v) -> mDelegate.onClick(getAdapterPosition()));
            binding.btnLike.setOnClickListener((v) -> {
                mDelegate.onLike(getAdapterPosition(), binding);
                boolean currentStatus = binding.getTweet().getFavorited();
                binding.getTweet().setFavorited(!currentStatus);
                binding.btnLike.setImageResource(currentStatus ? R.drawable.ic_favorite_border_white_18dp : R.drawable.ic_favorite_white_18dp);
            });
            binding.btnLink.setOnClickListener((v) -> {
                mDelegate.onRetweet(getAdapterPosition(), binding);
                boolean currentStatus = binding.getTweet().getRetweeted();
                binding.getTweet().setRetweeted(!currentStatus);
                binding.btnLink.setImageResource(currentStatus ? R.drawable.ic_link_white_18dp : R.drawable.ic_link_black_18dp);
            });
            binding.btnReply.setOnClickListener((v) -> mDelegate.onReply(getAdapterPosition(), binding));
        }
    }
}
