package com.example.jason.barterworld.model;

/**
 * Created by User on 8/16/2017.
 */

public class Message {

    private String fromid;//1
    private String fromname;//2
    private String from_img;//3

    private String toid;//4
    private String toname;//5
    private String to_img;//6

    private String barter_id;//7
    private String barter_name;//8
    private String barter_img;//9

    private String msg_content;//10

    public Message(){}

    public Message(String fromid, String fromname, String from_img, String toid, String toname, String to_img, String barter_id, String barter_name, String barter_img, String msg_content) {
        this.fromid = fromid;
        this.fromname = fromname;
        this.from_img = from_img;
        this.toid = toid;
        this.toname = toname;
        this.to_img = to_img;
        this.barter_id = barter_id;
        this.barter_name = barter_name;
        this.barter_img = barter_img;
        this.msg_content = msg_content;
    }

    public String getFromid() {
        return fromid;
    }

    public void setFromid(String fromid) {
        this.fromid = fromid;
    }

    public String getFromname() {
        return fromname;
    }

    public void setFromname(String fromname) {
        this.fromname = fromname;
    }

    public String getFrom_img() {
        return from_img;
    }

    public void setFrom_img(String from_img) {
        this.from_img = from_img;
    }

    public String getToid() {
        return toid;
    }

    public void setToid(String toid) {
        this.toid = toid;
    }

    public String getToname() {
        return toname;
    }

    public void setToname(String toname) {
        this.toname = toname;
    }

    public String getTo_img() {
        return to_img;
    }

    public void setTo_img(String to_img) {
        this.to_img = to_img;
    }

    public String getBarter_id() {
        return barter_id;
    }

    public void setBarter_id(String barter_id) {
        this.barter_id = barter_id;
    }

    public String getBarter_name() {
        return barter_name;
    }

    public void setBarter_name(String barter_name) {
        this.barter_name = barter_name;
    }

    public String getBarter_img() {
        return barter_img;
    }

    public void setBarter_img(String barter_img) {
        this.barter_img = barter_img;
    }

    public String getMsg_content() {
        return msg_content;
    }

    public void setMsg_content(String msg_content) {
        this.msg_content = msg_content;
    }




}
