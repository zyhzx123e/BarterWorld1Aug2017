package com.example.jason.barterworld.model;

/**
 * Created by User on 8/18/2017.
 */

public class Comment {
    private String comment_uid;//1
    private String comment_user_name;//2
    private String comment_user_img;//3
    private String comment_content;//4


    public  Comment(){}
    public Comment(String comment_uid, String comment_user_name, String comment_user_img, String comment_content) {
        this.comment_uid = comment_uid;
        this.comment_user_name = comment_user_name;
        this.comment_user_img = comment_user_img;
        this.comment_content = comment_content;
    }


    public String getComment_uid() {
        return comment_uid;
    }

    public void setComment_uid(String comment_uid) {
        this.comment_uid = comment_uid;
    }

    public String getComment_user_name() {
        return comment_user_name;
    }

    public void setComment_user_name(String comment_user_name) {
        this.comment_user_name = comment_user_name;
    }

    public String getComment_user_img() {
        return comment_user_img;
    }

    public void setComment_user_img(String comment_user_img) {
        this.comment_user_img = comment_user_img;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }
}
