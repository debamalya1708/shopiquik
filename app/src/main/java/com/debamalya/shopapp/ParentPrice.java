package com.debamalya.shopapp;

import java.util.List;

public class ParentPrice{

    List<Price> priceList;

    public ParentPrice(){}

    public ParentPrice(List<Price> priceList) {
        this.priceList = priceList;
    }

    public List<Price> getPriceList() {
        return priceList;
    }

    public void setPriceList(List<Price> priceList) {
        this.priceList = priceList;
    }
}

class Price {
    String marketPlaceName;
    String marketPlacePrice;

    public Price() {
    }

    public Price(String marketPlaceName, String marketPlacePrice) {
        this.marketPlaceName = marketPlaceName;
        this.marketPlacePrice = marketPlacePrice;
    }

    public String getMarketPlaceName() {
        return marketPlaceName;
    }

    public void setMarketPlaceName(String marketPlaceName) {
        this.marketPlaceName = marketPlaceName;
    }

    public String getMarketPlacePrice() {
        return marketPlacePrice;
    }

    public void setMarketPlacePrice(String marketPlacePrice) {
        this.marketPlacePrice = marketPlacePrice;
    }
}
