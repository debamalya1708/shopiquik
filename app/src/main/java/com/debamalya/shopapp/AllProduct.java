package com.debamalya.shopapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AllProduct extends AppCompatActivity {

    private List<Product> featureProductList = new ArrayList<>();
    private RoomDB productDb;
    private RecyclerView featureProductRecyclerView1;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_product);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        productDb=RoomDB.getInstance(this);
        showProduct();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do your refresh logic here
                // For example, fetch new data from a network request
                // Once the refresh is complete, call setRefreshing(false) to stop the animation
                showProduct();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void showProduct() {

        featureProductList = productDb.mainDao().getAllProduct();
        Collections.shuffle(featureProductList);
        featureProductRecyclerView1 = findViewById(R.id.all_product_recyclerView);

        featureProductRecyclerView1.setHasFixedSize(true);

        featureProductRecyclerView1.setLayoutManager(new GridLayoutManager(AllProduct.this,2));

        FeatureProductAdapter adapter = new FeatureProductAdapter(AllProduct.this, featureProductList);
        featureProductRecyclerView1.setAdapter(adapter);
    }

}