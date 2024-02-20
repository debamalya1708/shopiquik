package com.debamalya.shopapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView categoryRecyclerView;
    private RecyclerView brandRecyclerView;
    private RecyclerView featureProductRecyclerView;
    private List<Product> productList = new ArrayList<>();
    private List<Product> featureProductList = new ArrayList<>();
    private List<Brand> brandList = new ArrayList<>();
    private RoomDB productDb;
    private CategoryDB categoryDb;
    private FavoriteDB favoriteDB;
    private BrandDB brandDB;
    private ImageButton allCategoryButton;
    private MainAdapter adapter;
    private RequestQueue mRequestQueue;
    LinearLayoutManager linearLayoutManager;
    private List<Category> categoryList = new ArrayList<>();
    private ArrayList<String> categoryNameList = new ArrayList<>();
    private ArrayList<Integer> categoryIdList = new ArrayList<>();
    GridLayoutManager gridLayoutManager;
    GridLayoutManager featureProductGridLayoutManager;
    private ArrayList<String> brandNameList = new ArrayList<>();
    private ArrayList<String> brandImageList = new ArrayList<>();
    private TextView viewAllBrands;
    private ImageView infoIcon, favIcon;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton floatingActionButton;
    private Spinner categorySpinner,brandSpinner, genderSpinner,priceSpinner;
    private ArrayAdapter<CharSequence> genderArrayAdapter,priceArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productDb=RoomDB.getInstance(this);

        categoryDb = CategoryDB.getInstance(this);

        favoriteDB = FavoriteDB.getInstance(this);

        brandDB = BrandDB.getInstance(this);

        mRequestQueue = Volley.newRequestQueue(MainActivity.this);

        linearLayoutManager = new LinearLayoutManager(MainActivity.this,
                LinearLayoutManager.HORIZONTAL,false);


        categoryRecyclerView = findViewById(R.id.category_recyclerView);

        favIcon = findViewById(R.id.fav_Icon);

        viewAllBrands = findViewById(R.id.viewAllBrands);

        allCategoryButton = findViewById(R.id.allCategoryButton);

        floatingActionButton = findViewById(R.id.filter_fab_Icon);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog(); // Show the custom dialog when the button is clicked
            }
        });

        allCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AllCategory.class);
                MainActivity.this.startActivity(intent);
            }
        });

        viewAllBrands.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AllBrand.class);
                MainActivity.this.startActivity(intent);
            }
        });

        favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FavoriteProducts.class);
                MainActivity.this.startActivity(intent);
            }
        });

        gridLayoutManager = new GridLayoutManager(MainActivity.this, 3);

        brandRecyclerView = findViewById(R.id.brand_recyclerView);

        featureProductRecyclerView = findViewById(R.id.feature_product_recyclerView);

        featureProductRecyclerView.setHasFixedSize(true);

        featureProductRecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,2));

        parseProductJson();
        showProduct();

        parseCategoryJson();
        showCategory();

        parseBrandJson();
        showBrand();

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do your refresh logic here
                // For example, fetch new data from a network request
                // Once the refresh is complete, call setRefreshing(false) to stop the animation
                parseProductJson();
                parseCategoryJson();
                parseBrandJson();
                showProduct();
                showCategory();
                showBrand();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    public void showCustomDialog() {

        // Inflate the custom layout
        View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog_layout, null);

        categorySpinner = dialogView.findViewById(R.id.category_spinner);
        brandSpinner = dialogView.findViewById(R.id.brand_spinner);
        genderSpinner = dialogView.findViewById(R.id.gender_spinner);
        priceSpinner = dialogView.findViewById(R.id.price_spinner);

        List<String> categoryNameList = categoryDb.categoryDAO().getAllCategoryName();

        List<String> brandNameList = brandDB.brandDAO().getAllBrandName();

        ArrayAdapter<String> brandArrayAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_layout, brandNameList);

        brandArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> categoryArrayAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_layout, categoryNameList);

        categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        genderArrayAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.gender_array,
                R.layout.spinner_layout
        );
        genderArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        priceArrayAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.price_array,
                R.layout.spinner_layout
        );
        priceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner.
        categorySpinner.setAdapter(categoryArrayAdapter);
        brandSpinner.setAdapter(brandArrayAdapter);
        genderSpinner.setAdapter(genderArrayAdapter);
        priceSpinner.setAdapter(priceArrayAdapter);

        // Create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        // Set up dialog button click listener
        Button closeButton = dialogView.findViewById(R.id.dialog_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // Dismiss the dialog when the button is clicked
            }
        });

        // Show the dialog
        dialog.show();
    }

    private void showProduct() {
        Log.d("Product","Show Product Called");
        featureProductList.clear();

        featureProductList = productDb.mainDao().getAllFeatureProduct();
        Collections.shuffle(featureProductList);
        FeatureProductAdapter adapter = new FeatureProductAdapter
                (MainActivity.this, featureProductList);
        featureProductRecyclerView.setAdapter(adapter);
    }

    private void parseBrandJson(){
        mRequestQueue.getCache().clear();
        String URL = "https://res.cloudinary.com/dq5jpef6l/raw/upload/Shopping%20App/brands_rqtfnt.json";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("brands");
                    JSONObject syncTime = response.getJSONObject("syncTime");
                    String time = syncTime.getString("time");

                    // Parse the sync time into LocalDateTime
                    LocalDateTime latestTime = LocalDateTime.parse(time);

                    // Get the previous insert time from the database
                    List<Brand> brandList = brandDB.brandDAO().getAllBrand();
                    LocalDateTime previousTime = LocalDateTime.MIN; // Initialize with minimum value

                    // If there are products in the database, get the timestamp of the first product
                    if (!brandList.isEmpty()) {
                        previousTime = LocalDateTime.parse(brandList.get(0).getCreatedAt());
                    }

                    // Compare timestamps and decide whether to insert new data
                    Log.d("latestTime", latestTime.toString());
                    Log.d("previousTime", previousTime.toString());
                    if (latestTime.isAfter(previousTime)) {
                        // Clear existing data and insert new products
                        brandDB.brandDAO().deleteAllData();
                        insertBrand(jsonArray, time);
                    } else {
                        // Log message indicating no new data to insert
                        Log.d("parseBrandJson", "No new data to insert.");
                    }

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

    private void insertBrand(JSONArray jsonArray,String createdAt) throws JSONException {
        for(int i=0;i<jsonArray.length();i++){

            JSONObject responseObj = jsonArray.getJSONObject(i);
            int id = Integer.parseInt(responseObj.getString("id"));
            String name = responseObj.getString("name");
            String image = responseObj.getString("image");

            brandList.add(new Brand(id,name,image,createdAt));
        }

        for(Brand b:brandList){
            brandDB.brandDAO().insert(b);
        }

//                    mProductAdapter = new ProuctAdapter(ShowAllProduct.this,mProductList);
//                    mRecyclerView.setAdapter(mProductAdapter);
//                    mProductAdapter.setOnItemClickListener(ShowAllProduct.this);
//                    findViewById(R.id.gridLoading).setVisibility(View.GONE);
    }

    private void parseCategoryJson(){
        mRequestQueue.getCache().clear();
        String URL = "https://res.cloudinary.com/dq5jpef6l/raw/upload/Shopping%20App/category_wu9sa7.json";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("category");
                    JSONObject syncTime = response.getJSONObject("syncTime");
                    String time = syncTime.getString("time");

                    // Parse the sync time into LocalDateTime
                    LocalDateTime latestTime = LocalDateTime.parse(time);

                    // Get the previous insert time from the database
                    List<Category> categoryList = categoryDb.categoryDAO().getAllCategory();
                    LocalDateTime previousTime = LocalDateTime.MIN; // Initialize with minimum value

                    // If there are products in the database, get the timestamp of the first product
                    if (!categoryList.isEmpty()) {
                        previousTime = LocalDateTime.parse(categoryList.get(0).getCreatedAt());
                    }

                    // Compare timestamps and decide whether to insert new data
                    Log.d("latestTime", latestTime.toString());
                    Log.d("previousTime", previousTime.toString());
                    if (latestTime.isAfter(previousTime)) {
                        // Clear existing data and insert new products
                        categoryDb.categoryDAO().deleteAllData();
                        insertCategory(jsonArray, time);
                    } else {
                        // Log message indicating no new data to insert
                        Log.d("parseCategoryJson", "No new data to insert.");
                    }

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

    private void showCategory(){
        Log.d("Category","Show Category Called");
        categoryNameList.clear();
        categoryIdList.clear();

        List<Category> categoryList = categoryDb.categoryDAO().getAllCategory();
        categoryList = categoryList.subList(0,categoryList.size()/2);
        Collections.shuffle(categoryList);
        for(Category c : categoryList){
            categoryNameList.add(c.getName());
            categoryIdList.add(c.getId());
        }
        categoryRecyclerView.setLayoutManager(linearLayoutManager);
        HomeCategoryHorizontalRecyclerView adapter = new HomeCategoryHorizontalRecyclerView
                (MainActivity.this, categoryNameList,categoryIdList);
        categoryRecyclerView.setAdapter(adapter);

    }

    private void showBrand(){
        Log.d("Brand","Show Brand Called");

        brandImageList.clear();
        brandNameList.clear();

        List<Brand> brandList = brandDB.brandDAO().getAllBrand();
        brandList = brandList.subList(0,brandList.size()/2);
        Collections.shuffle(brandList);
        for(Brand b:brandList){
            brandNameList.add(b.getName());
            brandImageList.add(b.getImage());
        }
        brandRecyclerView.setLayoutManager(gridLayoutManager);
        HomeBrandHorizontalRecyclerView adapter = new HomeBrandHorizontalRecyclerView(
                MainActivity.this,brandList);
        brandRecyclerView.setAdapter(adapter);

    }

    private void insertCategory(JSONArray jsonArray,String createdAt) throws JSONException {
        for(int i=0;i<jsonArray.length();i++){

            JSONObject responseObj = jsonArray.getJSONObject(i);
            int id = Integer.parseInt(responseObj.getString("id"));
            String name = responseObj.getString("name");
            String image = responseObj.getString("image");

            categoryList.add(new Category(id,name,image,createdAt));
        }

        for(Category c:categoryList){
            categoryDb.categoryDAO().insert(c);
        }

//                    mProductAdapter = new ProuctAdapter(ShowAllProduct.this,mProductList);
//                    mRecyclerView.setAdapter(mProductAdapter);
//                    mProductAdapter.setOnItemClickListener(ShowAllProduct.this);
//                    findViewById(R.id.gridLoading).setVisibility(View.GONE);
    }

    private void parseProductJson() {
        mRequestQueue.getCache().clear();

        String URL = "https://res.cloudinary.com/dq5jpef6l/raw/upload/Shopping%20App/products_gf1bid.json";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("product");
                            JSONObject syncTime = response.getJSONObject("syncTime");
                            String time = syncTime.getString("time");

                            // Parse the sync time into LocalDateTime
                            LocalDateTime latestTime = LocalDateTime.parse(time);

                            // Get the previous insert time from the database
                            List<Product> productList = productDb.mainDao().getAllProduct();
                            LocalDateTime previousTime = LocalDateTime.MIN; // Initialize with minimum value

                            // If there are products in the database, get the timestamp of the first product
                            if (!productList.isEmpty()) {
                                previousTime = LocalDateTime.parse(productList.get(0).getCreatedAt());
                            }

                            // Compare timestamps and decide whether to insert new data
                            Log.d("latestTime", latestTime.toString());
                            Log.d("previousTime", previousTime.toString());
                            if (latestTime.isAfter(previousTime)) {
                                // Clear existing data and insert new products
                                productDb.mainDao().deleteAllData();
                                insertProduct(jsonArray, time);
                            } else {
                                // Log message indicating no new data to insert
                                Log.d("parseProductJson", "No new data to insert.");
                            }
                        } catch (JSONException | DateTimeParseException e) {
                            // Handle JSON parsing or DateTime parsing errors
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle network errors
                error.printStackTrace();
            }
        });

        // Add the request to the RequestQueue
        mRequestQueue.add(request);
    }

    private void insertProduct(JSONArray jsonArray,String createdAt) throws JSONException {
        for(int i=0;i<jsonArray.length();i++){
            JSONObject responseObj = jsonArray.getJSONObject(i);
            int id = Integer.parseInt(responseObj.getString("id"));
            String title = responseObj.getString("title");
            String description = responseObj.getString("description");
            String price = responseObj.getString("price");
//            Product product = new Product(1, gson.toJson(yourObject));
//            productDao.insertProduct(product);
//            Price p = new Price();
//            p.setAmazon(price.getString("amazon"));
//            p.setOther(price.getString("other"));
            String link = responseObj.getString("link");
//            Link l = new Link();
//            l.setAmazon(link.getString("amazon"));
//            l.setOther(link.getString("other"));
            String country = responseObj.getString("country");
            String images = responseObj.getString("images");
            String category = responseObj.getString("category");
            String subCategory = responseObj.getString("subCategory");
            String featured = responseObj.getString("featured");
            String rating = responseObj.getString("ratings");
            String occasion = responseObj.getString("occasions");
            String brand = responseObj.getString("brand");
            Gson gson = new Gson();
            productList.add(new Product(id,title,description,price,link,country,images
                    ,category,subCategory,featured,rating,occasion,brand,createdAt));
        }

        for(Product p:productList){
            productDb.mainDao().insert(p);
        }

//                    mProductAdapter = new ProuctAdapter(ShowAllProduct.this,mProductList);
//                    mRecyclerView.setAdapter(mProductAdapter);
//                    mProductAdapter.setOnItemClickListener(ShowAllProduct.this);
//                    findViewById(R.id.gridLoading).setVisibility(View.GONE);
    }

}