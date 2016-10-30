package com.ada.twitter.utils;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.ada.twitter.R;
import com.bumptech.glide.Glide;

/**
 * Created by ada on 10/22/16.
 */
public class AdapterUtils {

    private static final int MIN_COL_WIDTH = 320;

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String url) {

        Glide.with(view.getContext())
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.cast_mini_controller_img_placeholder)
                .error(R.drawable.cast_mini_controller_img_placeholder)
                .into(view);
    }

    public static int getColNm(Context context) {
        int fullWidth = Utils.getDisplayWidth(context);
        int cols = fullWidth / MIN_COL_WIDTH;
        //return (Configuration.ORIENTATION_LANDSCAPE == context.getResources().getConfiguration().orientation ? 4 : 2);
        return cols;
    }
}
