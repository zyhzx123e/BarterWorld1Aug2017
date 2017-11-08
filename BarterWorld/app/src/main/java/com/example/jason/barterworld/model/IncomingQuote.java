package com.example.jason.barterworld.model;

/**
 * Created by User on 8/15/2017.
 */

public class IncomingQuote {

    private String sender_uid;
    private String sender_name;
    private String sender_img;




    private String uid;
    private String barter_id;//1
    private String title;//2
    private String description;//3
    private String barter_img;//4

    private String type;//5
    private String value;//6

    private String username;//7
    private String time;//8
    private String like_count;//9
    private String incoming_msg;//10
    private String latitude;//11
    private String longitude;//12

    public String getSender_uid() {
        return sender_uid;
    }

    public void setSender_uid(String sender_uid) {
        this.sender_uid = sender_uid;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getSender_img() {
        return sender_img;
    }

    public void setSender_img(String sender_img) {
        this.sender_img = sender_img;
    }

    public String getBarter_id() {
        return barter_id;
    }

    public void setBarter_id(String barter_id) {
        this.barter_id = barter_id;
    }

    public String getIncoming_msg() {
        return incoming_msg;
    }

    public void setIncoming_msg(String incoming_msg) {
        this.incoming_msg = incoming_msg;
    }



    public String getOutgoing_msg() {
        return incoming_msg;
    }

    public void setOutgoing_msg(String outgoing_msg) {
        this.incoming_msg = outgoing_msg;
    }



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



    public IncomingQuote(){}


    public String getLike_count() {
        return like_count;
    }

    public void setLike_count(String like_count) {
        this.like_count = like_count;
    }

    public IncomingQuote(String sender_uid, String sender_name, String sender_img, String uid, String title, String time, String username, String value, String type, String like_count, String latitude, String longitude, String imgUrl, String desc, String incoming_msg) {
        this.sender_uid=sender_uid;
        this.sender_name=sender_name;
        this.sender_img=sender_img;


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

        this.incoming_msg=incoming_msg;


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
