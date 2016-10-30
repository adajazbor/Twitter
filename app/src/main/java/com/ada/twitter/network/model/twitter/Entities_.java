package com.ada.twitter.network.model.twitter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Entities_ {

    @SerializedName("url")
    @Expose
    public Url url;
    @SerializedName("description")
    @Expose
    public Description description;

    public Url getUrl() {
        return url;
    }

    public void setUrl(Url url) {
        this.url = url;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }
}
