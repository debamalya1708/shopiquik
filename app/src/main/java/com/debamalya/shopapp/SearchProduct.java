package com.debamalya.shopapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SearchProduct extends AppCompatActivity {

    String searchItem;
    String event,category;
    private TextView appBarName;
    private List<Product> featureProductList = new ArrayList<>();
    private RoomDB productDb;
    private FavoriteDB favoriteDB;
    private RecyclerView searchProductRecyclerView;
    private TextView favoriteCount;
    private ImageView infoIcon, favIcon,homeIcon;

    private List<String> genders = Arrays.asList("All", "Male", "Female");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);

        productDb=RoomDB.getInstance(this);
        appBarName = findViewById(R.id.appBarTitle);

        favoriteDB = FavoriteDB.getInstance(this);
//        setFavoriteCount();

        favIcon = findViewById(R.id.favIconV2);
        favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchProduct.this, FavoriteProducts.class);
                SearchProduct.this.startActivity(intent);
            }
        });

        infoIcon = findViewById(R.id.infoIconV2);
        infoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchProduct.this, MoreActivity.class);
                SearchProduct.this.startActivity(intent);
            }
        });

        homeIcon= findViewById(R.id.homeIconV2);
        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchProduct.this, MainActivity.class);
                SearchProduct.this.startActivity(intent);
            }
        });

        Intent intent = getIntent();
        if(intent.getStringExtra("Event") != null){
            event = intent.getStringExtra("Event");
        } else{
            Toast.makeText(SearchProduct.this, "Choose Brand or Category", Toast.LENGTH_SHORT).show();
        }
        if(intent.getStringExtra("Category")!=null){
            category = intent.getStringExtra("Category");
        }
        if(intent.getStringExtra("SearchItem") != null){
            searchItem = intent.getStringExtra("SearchItem");
//            appBarName.setText(searchItem);
        } else {
            Toast.makeText(SearchProduct.this, "Select One Brand or Category", Toast.LENGTH_SHORT).show();
        }
//        searchProductRecyclerView = findViewById(R.id.search_product_recyclerView);
//        searchProductRecyclerView.setHasFixedSize(true);
//        searchProductRecyclerView.setLayoutManager(new GridLayoutManager(SearchProduct.this,2));
        searchProduct(searchItem);

        TabLayout searchTabLayout = findViewById(R.id.searchTabLayout);
        ViewPager2 searchViewPager = findViewById(R.id.searchViewPager);

        SearchTabPagerAdapter searchTabPagerAdapter = new SearchTabPagerAdapter(this,featureProductList);
        searchViewPager.setAdapter(searchTabPagerAdapter);

        new TabLayoutMediator(searchTabLayout, searchViewPager, (tab, position) ->
                tab.setText(genders.get(position))
        ).attach();

        searchTabLayout.getTabAt(0).select(); // Select first tab by default

    }


//    private void setFavoriteCount(){
//        int favoriteItemCount = favoriteDB.favoriteDAO().getAllFavorites().size();
//        favoriteCount = findViewById(R.id.favoriteCount);
//        if(favoriteItemCount<9){
//            favoriteCount.setText(String.valueOf(favoriteItemCount));
//        }else{
//            favoriteCount.setText("9+");
//        }
//    }


    private void searchProduct(String searchItem) {
        String newSearchTerm = "%"+searchItem+"%";
        featureProductList.clear();

        if(event.equals("Category")){
            featureProductList = productDb.mainDao().getAllProductByCategory(newSearchTerm);
            Collections.shuffle(featureProductList);
        }
        else if(event.equals("Brand")){
            featureProductList = productDb.mainDao().getAllProductByBrand(newSearchTerm);
            Collections.shuffle(featureProductList);
        }else if(event.equals("Global")){
            newSearchTerm = searchItem;
            String[] searchTerms = newSearchTerm.split(" ");
            for(String term : searchTerms){
                Log.d("searchTerm", term);
                term = "%"+term+"%";
                featureProductList.addAll(productDb.mainDao().searchProduct(term));
            }
            Collections.shuffle(featureProductList);
        }else if(event.equals("subCategory")){
            featureProductList = productDb.mainDao().getAllProductBySubCategory(newSearchTerm,category);
            Collections.shuffle(featureProductList);
        }
//        searchProductRecyclerView.setAdapter(new FeatureProductAdapter
//                (SearchProduct.this, featureProductList));
    }
}