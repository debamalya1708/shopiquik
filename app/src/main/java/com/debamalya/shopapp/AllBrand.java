package com.debamalya.shopapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_brand);


        brandDB = BrandDB.getInstance(this);
        brandRecyclerView = findViewById(R.id.all_brand_recyclerView);
        gridLayoutManager = new GridLayoutManager(AllBrand.this, 3);
        showBrand();
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