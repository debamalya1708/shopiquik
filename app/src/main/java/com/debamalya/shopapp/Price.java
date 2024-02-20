package com.debamalya.shopapp;

public class Price {
    String amazon;
    String other;

    public Price() {
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

    public Price(String amazon, String other) {
        this.amazon = amazon;
        this.other = other;
    }
}
