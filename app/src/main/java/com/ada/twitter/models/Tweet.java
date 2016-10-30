package com.ada.twitter.models;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.ocpsoft.prettytime.PrettyTime;
import org.parceler.Parcel;

import java.util.Date;

/**
 * Created by ada on 10/28/16.
 */
@Parcel(analyze={Tweet.class})
@Table(database = TwitterDatabase.class)
public class Tweet extends BaseModel {

    @Column
    @PrimaryKey
    Long id;

    @Column
    String body;

    @ForeignKey(saveForeignKeyModel = false)
    User user;

    @Column(name = "created_at")
    Date createdAt;

    @Column(name = "id_str")
    String idStr;


    public Tweet() {
        super();
    }

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedFromNow() {
        return new PrettyTime().format(this.createdAt);
    }
}
