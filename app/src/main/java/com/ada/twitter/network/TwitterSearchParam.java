package com.ada.twitter.network;

/**
 * Created by ada on 10/28/16.
 */
public class TwitterSearchParam {

    private int count = 25;
    private long sinceId = 1;
    private int page = 0;
    private long maxId = -1;
    private long userId;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getSinceId() {
        return (page > 0 ? -1 : sinceId);
    }

    public void setSinceId(long sinceId) {
        this.sinceId = sinceId;
    }

    public long getMaxId() {
        return (page > 0 ? maxId : -1);
    }

    public void setMaxId(long maxId) {
        this.maxId = maxId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
