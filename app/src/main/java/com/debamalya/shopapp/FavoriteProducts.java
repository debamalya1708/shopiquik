package com.debamalya.shopapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FavoriteProducts extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private FavoriteDB favoriteDB;
    private RoomDB productDb;
    private RecyclerView favoriteProductRecyclerView;
    private ImageView infoIcon,homeIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_products);
        productDb=RoomDB.getInstance(this);
        favoriteDB = FavoriteDB.getInstance(this);
        favoriteProductRecyclerView = findViewById(R.id.favorite_product_recyclerView);
        favoriteProductRecyclerView.setHasFixedSize(true);
        favoriteProductRecyclerView.setLayoutManager(new GridLayoutManager(FavoriteProducts.this,2));
        getAllFavourite();

        infoIcon = findViewById(R.id.infoIconV2);

        infoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoriteProducts.this, MoreActivity.class);
                FavoriteProducts.this.startActivity(intent);
            }
        });

        homeIcon= findViewById(R.id.homeIconV2);

        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoriteProducts.this, MainActivity.class);
                FavoriteProducts.this.startActivity(intent);
            }
        });

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do your refresh logic here
                // For example, fetch new data from a network request
                // Once the refresh is complete, call setRefreshing(false) to stop the animation
                getAllFavourite();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

//    private void showFav() {
//        List<Integer> favList = getAllFavourite();
//        showProducts(favList);
//    }

    private void showProducts(List<Integer> favList) {
        List<Product> searchProductList = new ArrayList<>();

        Log.d("Product","Show Product Called");
        for (int i:favList){
            Optional<Product> product = productDb.mainDao().getProduct(i);
            searchProductList.add(product.get());
        }
        Collections.shuffle(searchProductList);
        System.out.println("Fav product...");
        System.out.println(searchProductList.size());
        FeatureProductAdapter adapter = new FeatureProductAdapter
                (FavoriteProducts.this, searchProductList);
        favoriteProductRecyclerView.setAdapter(adapter);
    }

    public void getAllFavourite(){
        List<Integer> favoriteList = favoriteDB.favoriteDAO().getAllFavorites();
        if (favoriteList.size()>0)
            showProducts(favoriteList);
    }
}