package com.almurray.android.almurrayportal.feedUtils;

/**
 * Created by tom on
 *
 * 09/01/2018.
 */

public class post {

    private String posterName;
    private String posterURL;
    private String posterUID;
    private String postURL;
    private String Desc;
    private Integer Likes;
    private Integer Comments;
    private Integer postNum;



    public String getPosterName() {
        return posterName;
    }
    public String getPosterURL() {
        return posterURL;
    }
    public String getPosterUID() {
        return posterUID;
    }
    public String getPostURL() {
        return postURL;
    }
    public String getDesc() {
        return Desc;
    }
    public Integer getLikes() {
        return Likes;
    }
    public Integer getComments() {
        return Comments;
    }
    public Integer getPostNum() { return  postNum; }



    //Set

    public void setPosterName(String name) {
        this.posterName = name;
    }
    public void setPosterURL(String url) {
        this.posterURL = url;
    }
    public void setPosterUID(String UID) {
        this.posterUID = UID;
    }
    public void setPostURL(String URL) {
        this.postURL = URL;
    }
    public void setDesc(String Desc) {
        this.Desc = Desc;
    }
    public void setLikes(Integer likes) {
        this.Likes = likes;
    }
    public void setComments(Integer comments) {this.Comments = comments; }
    public void setPostNum(Integer num) {this.postNum = num;}

}
