package com.debamalya.shopapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AllBrand extends AppCompatActivity {
    private RecyclerView brandRecyclerView;
    private BrandDB brandDB;
    private List<Brand> brandList = new ArrayList<>();
    private ArrayList<String> brandNameList = new ArrayList<>();
    private ArrayList<Integer> brandIdList = new ArrayList<>();
    private ArrayList<String> brandImageList = new ArrayList<>();
    GridLayoutManager gridLayoutManager;
    private TextView favoriteCount;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FavoriteDB favoriteDB;
    private ImageView infoIcon, favIcon,homeIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_brand);


        brandDB = BrandDB.getInstance(this);
        brandRecyclerView = findViewById(R.id.all_brand_recyclerView);
        gridLayoutManager = new GridLayoutManager(AllBrand.this, 3);
        showBrand();
        favoriteDB = FavoriteDB.getInstance(this);
        setFavoriteCount();

        favIcon = findViewById(R.id.favIconV2);


        favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllBrand.this, FavoriteProducts.class);
                AllBrand.this.startActivity(intent);
            }
        });

        infoIcon = findViewById(R.id.infoIconV2);

        infoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllBrand.this, MoreActivity.class);
                AllBrand.this.startActivity(intent);
            }
        });

        homeIcon= findViewById(R.id.homeIconV2);

        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllBrand.this, MainActivity.class);
                AllBrand.this.startActivity(intent);
            }
        });

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do your refresh logic here
                // For example, fetch new data from a network request
                // Once the refresh is complete, call setRefreshing(false) to stop the animation
                showBrand();
                setFavoriteCount();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setFavoriteCount(){
        int favoriteItemCount = favoriteDB.favoriteDAO().getAllFavorites().size();
        favoriteCount = findViewById(R.id.favoriteCount);
        if(favoriteItemCount<9){
            favoriteCount.setText(String.valueOf(favoriteItemCount));
        }else{
            favoriteCount.setText("9+");
        }
    }


    private void showBrand() {

        brandImageList.clear();
        brandNameList.clear();

        List<Brand> brandList = brandDB.brandDAO().getAllBrand();
        Collections.shuffle(brandList);
        for(Brand b:brandList){
            brandNameList.add(b.getName());
            brandImageList.add(b.getImage());
        }
        brandRecyclerView.setLayoutManager(gridLayoutManager);
        HomeBrandHorizontalRecyclerView adapter = new HomeBrandHorizontalRecyclerView(
                AllBrand.this,brandList);
        brandRecyclerView.setAdapter(adapter);
    }
}