package com.debamalya.shopapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_category);

        categoryDb = CategoryDB.getInstance(this);
        categoryRecyclerView = findViewById(R.id.all_category_recyclerView);
        gridLayoutManager = new GridLayoutManager(AllCategory.this, 3);
        showCategory();

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