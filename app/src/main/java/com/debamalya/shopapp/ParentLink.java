package com.debamalya.shopapp;

import java.util.List;

public class ParentLink{

    List<Link> LinkList;

    public ParentLink(){}

    public ParentLink(List<Link> LinkList) {
        this.LinkList = LinkList;
    }

    public List<Link> getLinkList() {
        return LinkList;
    }

    public void setLinkList(List<Link> LinkList) {
        this.LinkList = LinkList;
    }
}

class Link {
    String marketPlaceName;
    String marketPlaceLink;

    public Link() {
    }

    public Link(String marketPlaceName, String marketPlaceLink) {
        this.marketPlaceName = marketPlaceName;
        this.marketPlaceLink = marketPlaceLink;
    }

    public String getMarketPlaceName() {
        return marketPlaceName;
    }

    public void setMarketPlaceName(String marketPlaceName) {
        this.marketPlaceName = marketPlaceName;
    }

    public String getMarketPlaceLink() {
        return marketPlaceLink;
    }

    public void setMarketPlaceLink(String marketPlaceLink) {
        this.marketPlaceLink = marketPlaceLink;
    }
}
