package com.debamalya.shopapp;

import android.app.Activity;

import java.util.List;

public class MainAdapter {

    private List<Product> productList;
    private Activity context;
    private RoomDB database;

    public MainAdapter(Activity context,List<Product> dataList) {
        this.productList = dataList;
        this.context = context;
//        notifyDataSetChanged();
    }
}
