package com.debamalya.shopapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class ProductDescription extends AppCompatActivity {

    String productId;
    private RoomDB productDb;
    private FavoriteDB favoriteDB;

    TextView productTitle, productDetails, productBrand, productAmazonPrice,productBrandPrice,
            productRating,productLikes, productPriceBrand,similarCategoryItem;
    String productCategory;
    Button buyButtonAmazon,buyProductBrand;
    ImageView productImage, shareProductAmazon,shareOtherBrandProduct;
    private List<Product> featureProductList = new ArrayList<>();
    private RecyclerView searchProductRecyclerView;

    private ImageView favoriteButton;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_description);
        favoriteButton = findViewById(R.id.favorite_button);
        productPriceBrand = findViewById(R.id.text_view_product_price_brand);
        productTitle = findViewById(R.id.text_view_productName);
        productDetails = findViewById(R.id.text_view_productDesc);
        productBrand = findViewById(R.id.text_view_productBrand);
        productAmazonPrice = findViewById(R.id.text_view_product_price_amazon);
        productBrandPrice = findViewById(R.id.text_view_product_price);
        productRating = findViewById(R.id.text_view_productRating);
        productImage = findViewById(R.id.product_Image_detail);
        buyButtonAmazon = findViewById(R.id.buyProduct_amazon);
        buyProductBrand = findViewById(R.id.buyProduct_brand);
        shareProductAmazon = findViewById(R.id.shareProduct_amazon);
        shareOtherBrandProduct = findViewById(R.id.shareOtherBrandProduct);
        similarCategoryItem = findViewById(R.id.similarItem);
//        DescriptionProductPrice.setPaintFlags(DescriptionProductPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        productLikes = findViewById(R.id.text_view_product_popular);

        productDb=RoomDB.getInstance(this);
        favoriteDB = FavoriteDB.getInstance(this);
        List<Integer> favoriteList = favoriteDB.favoriteDAO().getAllFavorites();
        Intent intent = getIntent();
        searchProductRecyclerView = findViewById(R.id.feature_product_recyclerView);
        searchProductRecyclerView.setHasFixedSize(true);
        searchProductRecyclerView.setLayoutManager(new GridLayoutManager
                (ProductDescription.this,2));

        if(intent.getStringExtra("productId") != null){
            Log.d("Product ID",intent.getStringExtra("productId"));
            productId = intent.getStringExtra("productId");
            showProduct(productId);
        }
        else {
            Toast.makeText(ProductDescription.this, "Coming Soon", Toast.LENGTH_SHORT).show();
        }

        if(favoriteList.contains(Integer.parseInt(productId))){
            favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_24); // Change to your filled favorite icon
        }

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Favorite favorite = favoriteDB.favoriteDAO().getFavoriteByProductId(Integer.parseInt(productId));
                if(favorite!=null){
                    Favorite removeFavorite = new Favorite();
                    removeFavorite.setProductId(Integer.parseInt(productId));
                    removeFavorite.setId(favorite.getId());
                    favoriteDB.favoriteDAO().delete(removeFavorite);
                    favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_border_24); // Change to your filled favorite icon
                    Toast.makeText(ProductDescription.this,"Removed from Fav",Toast.LENGTH_SHORT).show();
                }
                else{
                    Favorite addFavorite = new Favorite();
                    addFavorite.setProductId(Integer.parseInt(productId));
                    favoriteDB.favoriteDAO().insert(addFavorite);
                    favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_24); // Change to your filled favorite icon
                    Toast.makeText(ProductDescription.this,"Added to Fav",Toast.LENGTH_SHORT).show();

                }
            }
        });

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do your refresh logic here
                // For example, fetch new data from a network request
                // Once the refresh is complete, call setRefreshing(false) to stop the animation
                showProduct(productId);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void similarCategoryProduct(String productId,String productCategory) {

        String newSearchTerm = "%"+productCategory+"%";
        featureProductList.clear();

        featureProductList = productDb.mainDao().getAllProductByCategory(newSearchTerm);

        featureProductList.removeIf(p -> p.getId() == Integer.parseInt(productId));
        Collections.shuffle(featureProductList);

        searchProductRecyclerView.setAdapter(new FeatureProductAdapter
                (ProductDescription.this, featureProductList));
    }

    private void showProduct(String productId) {
        int id = Integer.parseInt(productId);
        Optional<Product> product = productDb.mainDao().getProduct(id);
        if(product.isPresent()){
            productTitle.setText(product.get().getTitle());
            productPriceBrand.setText(product.get().getBrand());
            Glide.with(ProductDescription.this).load(product.get().getImages()).into(productImage);
            productDetails.setText(product.get().getDescription());
            productBrand.setText(product.get().getBrand());

            // Parse JSON string to object
            Gson gson = new Gson();
            Price price;

            //TODO for check price for every brand, if not exists then have to hide the layout
            if(!product.get().getPrice().equals("")){
                price =  gson.fromJson(product.get().getPrice(), Price.class);
                String formattedAmazonPrice="";
                String formattedBrandPrice ="";
                if(!price.getAmazon().equals(""))
                formattedAmazonPrice = formatPrice(price.getAmazon());

                if(!price.getOther().equals(""))
                formattedBrandPrice = formatPrice(price.getOther());

                productAmazonPrice.setText("MRP ₹ "+formattedAmazonPrice);
                productBrandPrice.setText("MRP ₹ "+formattedBrandPrice);
            }

            productRating.setText(product.get().getRating()+" ★");

            productCategory = product.get().getCategory();

            Random random = new Random();
            // Generate a random integer between 20 and 100
            int randomNumber = random.nextInt(81) + 20;
            productLikes.setText(randomNumber +"K+");

            Link link;
            String buyAmazonLink = "";
            if(!product.get().getLink().equals("")){
                link =  gson.fromJson(product.get().getLink(), Link.class);
                buyAmazonLink = link.getAmazon();
            }
            String finalBuyAmazonLink = buyAmazonLink;
            if(!finalBuyAmazonLink.equals(""))
            buyButtonAmazon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(finalBuyAmazonLink));
                    startActivity(intent);
                }
            });

            String buyOtherBrandLink = "";
            if(!product.get().getLink().equals("")){
                link =  gson.fromJson(product.get().getLink(), Link.class);
                buyOtherBrandLink = link.getOther();
            }
            String finalBuyOtherLink = buyOtherBrandLink;

            if(!finalBuyOtherLink.equals(""))
            buyProductBrand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(finalBuyOtherLink));
                    startActivity(intent);
                }
            });

            if(!finalBuyAmazonLink.equals(""))
            shareProductAmazon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent shareIntent = new Intent();
                    String shareMssg = product.get().getTitle()+"\n\n"+finalBuyAmazonLink;
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT,shareMssg+"\n\n");
                    shareIntent.setType("text/plain");
                    startActivity(Intent.createChooser(shareIntent,"Share via"));
                }
            });

            if(!finalBuyOtherLink.equals(""))
            shareOtherBrandProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent shareIntent = new Intent();
                    String shareMssg = product.get().getTitle()+"\n\n"+finalBuyOtherLink;
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT,shareMssg+"\n\n");
                    shareIntent.setType("text/plain");
                    startActivity(Intent.createChooser(shareIntent,"Share via"));
                }
            });

            similarCategoryItem.setText("More From "+ productCategory+" Category");
            similarCategoryProduct(productId,productCategory);

        }else{
            Toast.makeText(ProductDescription.this, "Coming Soon", Toast.LENGTH_SHORT).show();
        }
    }

    private String formatPrice(String price) {
        double newPrice = Double.parseDouble(price);

        // Create a DecimalFormat object with the pattern "#,###.##"
        DecimalFormat formatter = new DecimalFormat("#,###.##");

        // Format the price using the DecimalFormat object
        String formattedPrice = formatter.format(newPrice);
        // Print the formatted price
        return formattedPrice;
    }
}