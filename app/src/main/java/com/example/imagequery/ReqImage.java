package com.example.imagequery;

public class ReqImage {

    private String imageUrl;
    private String queryTag;

    public ReqImage(String imageUrl, String queryTag) {
        this.imageUrl = imageUrl;
        this.queryTag = queryTag;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getQueryTag() {
        return queryTag;
    }

    public void setQueryTag(String queryTag) {
        this.queryTag = queryTag;
    }
}