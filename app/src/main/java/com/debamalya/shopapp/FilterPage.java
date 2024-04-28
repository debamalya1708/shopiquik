package com.debamalya.shopapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilterPage extends AppCompatActivity {
    private RoomDB productDb;
    private String category,gender,amountType = "";
    private double amount = -1;
    String noSelection = "None" ;
    private RecyclerView searchProductRecyclerView;
    List<Product> productResult = new ArrayList<>();
    private TextView favoriteCount;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FavoriteDB favoriteDB;
    private ImageView infoIcon, favIcon,homeIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_page);
        productDb=RoomDB.getInstance(this);
        favoriteDB = FavoriteDB.getInstance(this);
        setFavoriteCount();

        Intent intent = getIntent();
        if(intent.getStringExtra("categoryValue") != null){
            String getCategoryValue = intent.getStringExtra("categoryValue");
            if (getCategoryValue.equals(noSelection)){
                category= "";
            } else{
                category = getCategoryValue;
            }
        }
        if(intent.getStringExtra("genderValue") != null){
            String getGenderValue = intent.getStringExtra("genderValue");
            if (getGenderValue.equals(noSelection)){
                gender = "";
            } else{
                gender = getGenderValue;
            }
        }
        if(intent.getStringExtra("amountType") != null){
            String getAmountType = intent.getStringExtra("amountType");
            if(!getAmountType.equals(noSelection)){
                String getAmount = intent.getStringExtra("enteredAmount");
                amount = Double.parseDouble(getAmount);
                amountType = getAmountType;
            }
        }
        favIcon = findViewById(R.id.favIconV2);


        favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterPage.this, FavoriteProducts.class);
                FilterPage.this.startActivity(intent);
            }
        });

        infoIcon = findViewById(R.id.infoIconV2);

        infoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterPage.this, MoreActivity.class);
                FilterPage.this.startActivity(intent);
            }
        });

        homeIcon= findViewById(R.id.homeIconV2);

        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterPage.this, MainActivity.class);
                FilterPage.this.startActivity(intent);
            }
        });
        Log.d("category",category);
        Log.d("gender",gender);
        Log.d("amount",Double.toString(amount));
        Log.d("amountType",amountType);
        searchProductRecyclerView = findViewById(R.id.filter_product_recyclerView);
        searchProductRecyclerView.setHasFixedSize(true);
        searchProductRecyclerView.setLayoutManager(new GridLayoutManager(FilterPage.this,2));
        productResult = getAllFilterProducts(category,gender,amount,amountType);
        Collections.shuffle(productResult);
        Log.d("productFetchList.length",Integer.toString(productResult.size()));
        searchProductRecyclerView.setAdapter(new FeatureProductAdapter
                (FilterPage.this, productResult));

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do your refresh logic here
                // For example, fetch new data from a network request
                // Once the refresh is complete, call setRefreshing(false) to stop the animation
                setFavoriteCount();
                productResult = getAllFilterProducts(category,gender,amount,amountType);
                Collections.shuffle(productResult);
                Log.d("productFetchList.length",Integer.toString(productResult.size()));
                searchProductRecyclerView.setAdapter(new FeatureProductAdapter
                        (FilterPage.this, productResult));
                swipeRefreshLayout.setRefreshing(false);
            }
        });

//        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
//        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
//        Spinner spinner3 = (Spinner) findViewById(R.id.spinner3);
//        Spinner spinner4 = (Spinner) findViewById(R.id.spinner4);
//// Create an ArrayAdapter using the string array and a default spinner layout.
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
//                this,
//                R.array.gender_array,
//                R.layout.spinner_layout
//        );
//// Specify the layout to use when the list of choices appears.
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//// Apply the adapter to the spinner.
//        spinner1.setAdapter(adapter);
//        spinner2.setAdapter(adapter);
//        spinner3.setAdapter(adapter);
//        spinner4.setAdapter(adapter);
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

    public List<Product> getAllFilterProducts(String category, String gender, double price,
                                              String amountType) {

        List<Product> products = productDb.mainDao().getAllProduct();
        List<Product> filteredProducts = new ArrayList<>();

        for (Product product : products) {
            try {
                JSONObject priceJson = new JSONObject(product.getPrice());

                Gson gson = new Gson();
                Price productPrice = gson.fromJson(String.valueOf(priceJson), Price.class);

//                double amazonPrice = Double.parseDouble(productPrice.getAmazon());
//                double otherPrice = Double.parseDouble(productPrice.getOther());

                double amazonPrice = Double.parseDouble("5333");
                double otherPrice = Double.parseDouble("89767");

                if(amountType.equals("Above")){
                        if ((category.isEmpty() || product.getCategory().equals(category)) &&
                                (gender.isEmpty() || product.getGender().equals(gender)) &&
                                ((price == -1) || (amazonPrice >= price || otherPrice >= price))) {
                            filteredProducts.add(product);
                        }
                    }
                else{
                    if ((category.isEmpty() || product.getCategory().equals(category)) &&
                            (gender.isEmpty() || product.getGender().equals(gender)) &&
                            ((price == -1) || (amazonPrice <= price || otherPrice <= price))) {
                        filteredProducts.add(product);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return filteredProducts;
    }
}