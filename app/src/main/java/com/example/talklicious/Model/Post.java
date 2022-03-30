package com.example.talklicious.Model;

public class Post {

    String statusText,statusImg,postId,creatorId,createOn;
    int like;
    int postLike;


    public Post(String statusText, String statusImg, String postId, String creatorId, String createOn) {
        this.statusText = statusText;
        this.statusImg = statusImg;
        this.postId = postId;
        this.creatorId = creatorId;
        this.createOn = createOn;
    }

    public Post() {
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getStatusImg() {
        return statusImg;
    }

    public void setStatusImg(String statusImg) {
        this.statusImg = statusImg;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreateOn() {
        return createOn;
    }

    public void setCreateOn(String createOn) {
        this.createOn = createOn;
    }

    public int getPostLike() {
        return postLike;
    }

    public void setPostLike(int postLike) {
        this.postLike = postLike;
    }
}
