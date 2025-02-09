package com.debamalya.shopapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

public class SubCategoryProducts extends AppCompatActivity {

    private SubCategoryDB subCategoryDb;
    private RoomDB productDb;
    private ImageView infoIcon, favIcon,homeIcon;
    String searchItem;
    String event;
    private RequestQueue mRequestQueue;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NestedScrollView nestedScrollView;
    private RecyclerView productByCategoryRecyclerView, subCategoryByCategoryRecyclerView;

    private ViewFlipper viewFlipper1,viewFlipperCover,viewFlipperCover1,viewFlipperCover2;
    private ImageButton mBtnPrevious,btn_previousCover,btn_previousCover1,btn_previousCover2,searchProduct;
    private ImageButton mBtnNext,btn_nextCover,btn_nextCover1,btn_nextCover2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category_products);
        
        subCategoryDb=SubCategoryDB.getInstance(this);
        productDb = RoomDB.getInstance(this);

        favIcon = findViewById(R.id.favIconV2);
        favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubCategoryProducts.this, FavoriteProducts.class);
                SubCategoryProducts.this.startActivity(intent);
            }
        });

        infoIcon = findViewById(R.id.infoIconV2);
        infoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubCategoryProducts.this, MoreActivity.class);
                SubCategoryProducts.this.startActivity(intent);
            }
        });

        mRequestQueue = Volley.newRequestQueue(SubCategoryProducts.this);

        homeIcon= findViewById(R.id.homeIconV2);
        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubCategoryProducts.this, MainActivity.class);
                SubCategoryProducts.this.startActivity(intent);
            }
        });

        viewFlipper1 = findViewById(R.id.viewFlipper1);
        viewFlipperCover = findViewById(R.id.viewFlipperCover);

        mBtnPrevious = findViewById(R.id.btn_previous);
        mBtnNext = findViewById(R.id.btn_next);

        btn_previousCover = findViewById(R.id.btn_previousCover);
        btn_nextCover = findViewById(R.id.btn_nextCover);

        Intent intent = getIntent();

        if(intent.getStringExtra("Event") != null){
            event = intent.getStringExtra("Event");
        } else{
            Toast.makeText(SubCategoryProducts.this, "Choose Brand or Category", Toast.LENGTH_SHORT).show();
        }

        if(intent.getStringExtra("SearchItem") != null){
            searchItem = intent.getStringExtra("SearchItem");
        } else {
            Toast.makeText(SubCategoryProducts.this, "Select One Brand or Category", Toast.LENGTH_SHORT).show();
        }
        productByCategoryRecyclerView = findViewById(R.id.category_product_recyclerView);
        productByCategoryRecyclerView.setHasFixedSize(true);
        productByCategoryRecyclerView.setLayoutManager(
                new GridLayoutManager(SubCategoryProducts.this,2));

        subCategoryByCategoryRecyclerView = findViewById(R.id.sub_category_recyclerView);
        subCategoryByCategoryRecyclerView.setHasFixedSize(true);
        subCategoryByCategoryRecyclerView.setLayoutManager(
                new GridLayoutManager(SubCategoryProducts.this,3));

        showSubCategory(searchItem);
        showProduct(searchItem);

        setFeatureFlipper();
        setViewFlipperCover();


        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do your refresh logic here
                // For example, fetch new data from a network request
                // Once the refresh is complete, call setRefreshing(false) to stop the animation
                showSubCategory(searchItem);
                showProduct(searchItem);
                setFeatureFlipper();
                setViewFlipperCover();

                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setFeatureFlipper(){
        String URL = "https://res.cloudinary.com/dq5jpef6l/raw/upload/Shopping%20App/image_albp25.json";
        setFeaturedImageSlider(URL,viewFlipper1,mBtnPrevious,mBtnNext);
    }

    private void setViewFlipperCover(){
        String URL = "https://res.cloudinary.com/dq5jpef6l/raw/upload/Shopping%20App/image_albp25.json";
        setFeaturedImageSlider(URL,viewFlipperCover,btn_previousCover,btn_nextCover);
    }

    private void setFeaturedImageSlider(String URL,ViewFlipper viewFlipper,ImageButton preBtn, ImageButton nextBtn) {

        List<SliderItem> imageSliderList = new ArrayList<>();

        mRequestQueue.getCache().clear();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("items");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject responseObj = jsonArray.getJSONObject(i);
                        String image = responseObj.getString("image");
                        String action = responseObj.getString("action");
                        imageSliderList.add(new SliderItem(image,action));
                    }
                    Log.d("imageSliderList", String.valueOf(imageSliderList.size()));

                    ImageSliderAdapter adapter = new ImageSliderAdapter(SubCategoryProducts.this, viewFlipper, imageSliderList);
                    adapter.loadImages();

                    // Initialize GestureDetector
//                    mGestureDetector = new GestureDetector(MainActivity.this, new SwipeGestureListener());
                    preBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewFlipper.showPrevious();
                            stopAutoFlipping();
                        }
                    });

                    nextBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewFlipper.showNext();
                            stopAutoFlipping();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request);

    }
    // Method to stop the automatic flipping
    private void stopAutoFlipping() {
        viewFlipper1.stopFlipping();
        viewFlipperCover.stopFlipping();
//        viewFlipperCover1.stopFlipping();
//        viewFlipperCover2.stopFlipping();
    }

    private void showProduct(String searchItem) {
        List<Product> featureProductList = productDb.mainDao().
                getAllProductByCategory(searchItem);

        List<Product> listWithoutDuplicates = featureProductList.subList
                (0,Math.min(10,featureProductList.size()));

        Collections.shuffle(listWithoutDuplicates);

        Log.d("featureProductList", Integer.toString(listWithoutDuplicates.size()));

        productByCategoryRecyclerView.setAdapter(new FeatureProductAdapter
                (SubCategoryProducts.this, listWithoutDuplicates));


    }

    private void showSubCategory(String searchItem) {
        List<SubCategory> subCategoryList = subCategoryDb.subCategoryDAO().
                getAllSubCategoryName(searchItem);

        List<SubCategory> listWithoutDuplicates = new ArrayList<>(new LinkedHashSet<>(subCategoryList));

        Collections.shuffle(listWithoutDuplicates);


        Log.d("subCategoryList", Integer.toString(listWithoutDuplicates.size()));


        AllSubCategoryCategoryHorizontalRecyclerView adapter = new AllSubCategoryCategoryHorizontalRecyclerView(
                SubCategoryProducts.this,listWithoutDuplicates);
        subCategoryByCategoryRecyclerView.setAdapter(adapter);

    }
}