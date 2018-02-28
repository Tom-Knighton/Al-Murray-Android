package com.almurray.android.almurrayportal.chatUsers;

/**
 * Created by tom on
 *
 * 09/01/2018.
 */

public class userChat {

    private String name;
    private String uid;
    private String url;
    private String sendbird;



    public String getName() {
        return name;
    }
    public String getUid() {
        return uid;
    }
    public String getUrl() {
        return url;
    }
    public String getSendbird() { return sendbird; }
    //Set

    public void setName(String name) {
        this.name = name;
    }
    public void setUid(String url) {
        this.uid = url;
    }
    public void setUrl(String UID) {
        this.url = UID;
    }
    public void setSendbird(String sendbird) { this.sendbird = sendbird; }

}
