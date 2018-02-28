package com.almurray.android.almurrayportal.feedUtils;

/**
 * Created by tom on 20/01/2018.
 */

public class comment {


    private String comment;
    private String commenterurl;
    private String date;



    public String getComment() {
        return comment;
    }

    public String getCommenterurl() {
        return commenterurl;
    }

    public String getDate() {
        return date;
    }


    //Set


    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCommenterurl(String url) {
        this.commenterurl = url;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
