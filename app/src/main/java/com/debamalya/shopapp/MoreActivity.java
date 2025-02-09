package com.debamalya.shopapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MoreActivity extends AppCompatActivity {

    LinearLayout brandLayout,categoryLayout,favLayout,allProductLayout,aboutLayout,policyLayout,ratingLayout,shareLayout,contactLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        brandLayout = findViewById(R.id.brandLayout);
        brandLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoreActivity.this, AllBrand.class);
                MoreActivity.this.startActivity(intent);
            }
        });

        categoryLayout = findViewById(R.id.categoryLayout);
        categoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoreActivity.this, AllCategory.class);
                MoreActivity.this.startActivity(intent);
            }
        });

        favLayout = findViewById(R.id.favLayout);
        favLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoreActivity.this, FavoriteProducts.class);
                MoreActivity.this.startActivity(intent);
            }
        });

        aboutLayout = findViewById(R.id.aboutLayout);
        aboutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoreActivity.this, AboutUs.class);
                MoreActivity.this.startActivity(intent);
            }
        });

        allProductLayout = findViewById(R.id.allProductLayout);
        allProductLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoreActivity.this, AllProduct.class);
                MoreActivity.this.startActivity(intent);
            }
        });

        policyLayout = findViewById(R.id.policyLayout);
        policyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoreActivity.this, PrivacyPolicy.class);
                MoreActivity.this.startActivity(intent);
            }
        });

    }
}