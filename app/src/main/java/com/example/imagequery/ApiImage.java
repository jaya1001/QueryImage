package com.example.imagequery;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ApiImage {

    @SerializedName("page")
    private int page;

    @SerializedName("pages")
    private long pages;

    @SerializedName("perpage")
    private int perpage;

    @SerializedName("total")
    private String total;

    @SerializedName("photo")
    private ArrayList<fetchPhoto> photos;

    public ApiImage(int page, long pages, int perpage, String total, ArrayList<fetchPhoto> photos) {

        this.photos = photos;
        this.page = page;
        this.pages = pages;
        this.perpage = perpage;
        this.total = total;

    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public long getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPerpage() {
        return perpage;
    }

    public void setPerpage(int perpage) {
        this.perpage = perpage;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public ArrayList<fetchPhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<fetchPhoto> photos) {
        this.photos = photos;
    }

}