package com.ada.twitter.network.model.twitter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Url_ {

    @SerializedName("expanded_url")
    @Expose
    public String expandedUrl;
    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("indices")
    @Expose
    public List<Integer> indices = new ArrayList<Integer>();
    @SerializedName("display_url")
    @Expose
    public String displayUrl;

    public String getExpandedUrl() {
        return expandedUrl;
    }

    public void setExpandedUrl(String expandedUrl) {
        this.expandedUrl = expandedUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Integer> getIndices() {
        return indices;
    }

    public void setIndices(List<Integer> indices) {
        this.indices = indices;
    }

    public String getDisplayUrl() {
        return displayUrl;
    }

    public void setDisplayUrl(String displayUrl) {
        this.displayUrl = displayUrl;
    }
}