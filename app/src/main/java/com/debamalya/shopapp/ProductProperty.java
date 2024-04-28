package com.debamalya.shopapp;


public class ProductProperty {

    private String price;
    private String marketPlaceName;
    private String link;

    public ProductProperty() {
    }

    public ProductProperty(String price, String marketPlaceName, String link) {
        this.price = price;
        this.marketPlaceName = marketPlaceName;
        this.link = link;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMarketPlaceName() {
        return marketPlaceName;
    }

    public void setMarketPlaceName(String marketPlaceName) {
        this.marketPlaceName = marketPlaceName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
