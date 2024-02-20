package com.debamalya.shopapp;

public class Link {
    String amazon;
    String other;

    public Link() {
    }

    public String getAmazon() {
        return amazon;
    }

    public void setAmazon(String amazon) {
        this.amazon = amazon;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public Link(String amazon, String other) {
        this.amazon = amazon;
        this.other = other;
    }
}
