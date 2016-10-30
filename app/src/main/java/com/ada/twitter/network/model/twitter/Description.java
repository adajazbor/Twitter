package com.ada.twitter.network.model.twitter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Description {

    @SerializedName("urls")
    @Expose
    public List<Object> urls = new ArrayList<Object>();

}
