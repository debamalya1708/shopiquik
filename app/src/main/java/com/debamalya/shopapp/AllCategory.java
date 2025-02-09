package com.debamalya.shopapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AllCategory extends AppCompatActivity {

    private RecyclerView categoryRecyclerView;
    private CategoryDB categoryDb;
    private List<Category> categoryList = new ArrayList<>();
    private ArrayList<String> categoryNameList = new ArrayList<>();
    private ArrayList<Integer> categoryIdList = new ArrayList<>();
    private ArrayList<String> categoryImageList = new ArrayList<>();
    GridLayoutManager gridLayoutManager;
    private TextView favoriteCount;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FavoriteDB favoriteDB;
    private ImageView infoIcon, favIcon,homeIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_category);

        // Clear the cache and print result
        boolean isCacheCleared = CacheCleaner.clearCache(this);
        if (isCacheCleared) {
            Log.d("Cache: ", "Cache cleared successfully.");
        } else {
            Log.d("Cache: ", "Failed to clear cache.");
        }

        categoryDb = CategoryDB.getInstance(this);
        categoryRecyclerView = findViewById(R.id.all_category_recyclerView);
        gridLayoutManager = new GridLayoutManager(AllCategory.this, 3);
        showCategory();
        favoriteDB = FavoriteDB.getInstance(this);
        setFavoriteCount();

        favIcon = findViewById(R.id.favIconV2);


        favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllCategory.this, FavoriteProducts.class);
                AllCategory.this.startActivity(intent);
            }
        });

        infoIcon = findViewById(R.id.infoIconV2);

        infoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllCategory.this, MoreActivity.class);
                AllCategory.this.startActivity(intent);
            }
        });

        homeIcon= findViewById(R.id.homeIconV2);

        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllCategory.this, MainActivity.class);
                AllCategory.this.startActivity(intent);
            }
        });

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do your refresh logic here
                // For example, fetch new data from a network request
                // Once the refresh is complete, call setRefreshing(false) to stop the animation
                showCategory();
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

    private void showCategory() {
        categoryNameList.clear();
        categoryIdList.clear();

        List<Category> categoryList = categoryDb.categoryDAO().getAllCategory();
        Collections.shuffle(categoryList);
        for(Category c:categoryList){
            categoryNameList.add(c.getName());
            categoryIdList.add(c.getId());
            categoryImageList.add(c.getImage());
        }
        categoryRecyclerView.setLayoutManager(gridLayoutManager);
        AllCategoryHorizontalRecyclerView adapter = new AllCategoryHorizontalRecyclerView(
                AllCategory.this,categoryList);
        categoryRecyclerView.setAdapter(adapter);
    }
}