package com.debamalya.shopapp;

public class SliderItem {

    private String image;
    private String action;

    public SliderItem() {
    }

    public SliderItem(String image, String action) {
        this.image = image;
        this.action = action;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
