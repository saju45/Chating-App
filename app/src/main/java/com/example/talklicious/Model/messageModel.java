package com.example.talklicious.Model;

public class messageModel {

    String uId,message,messageId;
    String time;

    public messageModel() {
    }

    public messageModel(String uId, String message) {
        this.uId = uId;
        this.message = message;
    }

    public messageModel(String uId, String message, String messageId, String time) {
        this.uId = uId;
        this.message = message;
        this.messageId = messageId;
        this.time = time;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
