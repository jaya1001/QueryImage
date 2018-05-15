package com.example.imagequery;

import com.google.gson.annotations.SerializedName;

public class JSONResponse {

    @SerializedName("photos")
    private ApiImage image;

    @SerializedName("stat")
    private String stat;

    public void setImage(ApiImage image) {
        this.image = image;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public ApiImage getImage() {
        return image;
    }

}