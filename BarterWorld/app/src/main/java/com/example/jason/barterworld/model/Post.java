package com.example.jason.barterworld.model;

/**
 * Created by jason on 4/3/2017.
 */

public class Post {

    private String uid;
    private String title;//1
    private String description;//2
    private String barter_img;//3

    private String type;//4
    private String value;//5
   // private String userId;
    private String username;//6
    private String time;//7
    private String like_count;//8

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    private String latitude;//9
    private String longitude;//10

    public Post(){}


    public String getLike_count() {
        return like_count;
    }

    public void setLike_count(String like_count) {
        this.like_count = like_count;
    }

    public Post(String uid, String title, String time, String username, String value, String type, String like_count,String latitude, String longitude, String imgUrl, String desc) {
        this.uid=uid;
        this.title = title;
        this.description = desc;
        this.time = time;
        this.username=username;
       // this.userId = userId;
        this.value = value;
        this.type = type;
        this.like_count = like_count;

        this.latitude=latitude;
        this.longitude=longitude;
        this.barter_img = imgUrl;


    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public  String getUsername(){return username;}

    public  void setUsername(String username_){this.username=username_;}



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public String getBarter_img() {
        return barter_img;
    }

    public void setBarter_img(String imgUrl) {


        this.barter_img = imgUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


/*
    public String getUserId() {

        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }  */

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }




}
