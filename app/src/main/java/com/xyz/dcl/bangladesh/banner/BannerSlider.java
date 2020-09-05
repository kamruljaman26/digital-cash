package com.xyz.dcl.bangladesh.banner;

import android.graphics.drawable.Drawable;

public class BannerSlider {

    private String bannerID;
    private String contentID;
    private String givenData;
    private String fileName;

    private Drawable im;

    public BannerSlider(){

    }

    public String getBannerID() {
        return bannerID;
    }

    public void setBannerID(String bannerID) {
        this.bannerID = bannerID;
    }

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    public String getGivenData() {
        return givenData;
    }

    public void setGivenData(String givenData) {
        this.givenData = givenData;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Drawable getIm() {
        return im;
    }

    public void setIm(Drawable im) {
        this.im = im;
    }
}

