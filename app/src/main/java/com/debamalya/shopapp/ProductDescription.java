package com.debamalya.shopapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class ProductDescription extends AppCompatActivity {

    String productId;
    private RoomDB productDb;

    TextView productTitle, productDetails, productBrand, productAmazonPrice,productBrandPrice,
            productRating,productLikes, productPriceBrand,similarCategoryItem;
    String productCategory, productGender;
    Button buyButtonAmazon,buyProductBrand;
    ImageView productImage, shareProductAmazon,shareOtherBrandProduct;
    private List<Product> featureProductList = new ArrayList<>();
    private RecyclerView searchProductRecyclerView , productPropertyRecyclerView;
    private TextView favoriteCount;
    private FavoriteDB favoriteDB;
    private ImageView infoIcon, favIcon,homeIcon;

    private Button viewMoreButton;
    private boolean isExpanded = false;


    private ImageView favoriteButton;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_description);
        favoriteButton = findViewById(R.id.favorite_button);
//        productPriceBrand = findViewById(R.id.text_view_product_price_brand);
        productTitle = findViewById(R.id.text_view_productName);
        productDetails = findViewById(R.id.text_view_productDesc);
        productBrand = findViewById(R.id.text_view_productBrand);
//        productAmazonPrice = findViewById(R.id.text_view_product_price_amazon);
//        productBrandPrice = findViewById(R.id.text_view_product_price);
        productRating = findViewById(R.id.text_view_productRating);
        productImage = findViewById(R.id.product_Image_detail);
//        buyButtonAmazon = findViewById(R.id.buyProduct_amazon);
//        buyProductBrand = findViewById(R.id.buyProduct_brand);
//        shareProductAmazon = findViewById(R.id.shareProduct_amazon);
//        shareOtherBrandProduct = findViewById(R.id.shareOtherBrandProduct);
        similarCategoryItem = findViewById(R.id.similarItem);
//        DescriptionProductPrice.setPaintFlags(DescriptionProductPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        productLikes = findViewById(R.id.text_view_product_popular);


        viewMoreButton = findViewById(R.id.btn_view_more);

        // Set initial visibility of full text to GONE
        productDetails.setMaxLines(4);  // Show only 2 lines initially
        viewMoreButton.setVisibility(View.VISIBLE);  // Show "View More" button initially

        // Set click listener for "View More" button
        viewMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    // Collapse the text
                    productDetails.setMaxLines(4);  // Show only 2 lines
                    viewMoreButton.setText("View More");
                } else {
                    // Expand the text
                    productDetails.setMaxLines(Integer.MAX_VALUE);  // Show all lines
                    viewMoreButton.setText("View Less");
                }
                isExpanded = !isExpanded;  // Toggle the state
            }
        });


        productDb=RoomDB.getInstance(this);
        favoriteDB = FavoriteDB.getInstance(this);
        List<String> favoriteList = favoriteDB.favoriteDAO().getAllFavorites();
//        setFavoriteCount();
        Intent intent = getIntent();
        searchProductRecyclerView = findViewById(R.id.feature_product_recyclerView);
        searchProductRecyclerView.setHasFixedSize(true);
        searchProductRecyclerView.setLayoutManager(new GridLayoutManager
                (ProductDescription.this,2));

        productPropertyRecyclerView = findViewById(R.id.product_property_recyclerView);
        productPropertyRecyclerView.setLayoutManager(new LinearLayoutManager(ProductDescription.this));


        if(intent.getStringExtra("productId") != null){
            Log.d("Product ID",intent.getStringExtra("productId"));
            productId = intent.getStringExtra("productId");
            showProduct(productId);
        }
        else {
            Toast.makeText(ProductDescription.this, "Coming Soon", Toast.LENGTH_SHORT).show();
        }

        if(favoriteList.contains(productId)){
            favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_24); // Change to your filled favorite icon
        }

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Favorite favorite = favoriteDB.favoriteDAO().getFavoriteByProductId(productId);
                if(favorite!=null){
                    Favorite removeFavorite = new Favorite();
                    removeFavorite.setProductId(productId);
                    removeFavorite.setId(favorite.getId());
                    favoriteDB.favoriteDAO().delete(removeFavorite);
                    favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_border_24); // Change to your filled favorite icon
                    Toast.makeText(ProductDescription.this,"Removed from Fav",Toast.LENGTH_SHORT).show();
//                    setFavoriteCount();
                }
                else{
                    Favorite addFavorite = new Favorite();
                    addFavorite.setProductId(productId);
                    favoriteDB.favoriteDAO().insert(addFavorite);
                    favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_24); // Change to your filled favorite icon
                    Toast.makeText(ProductDescription.this,"Added to Fav",Toast.LENGTH_SHORT).show();
//                    setFavoriteCount();
                }
            }
        });
        favIcon = findViewById(R.id.favIconV2);


        favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDescription.this, FavoriteProducts.class);
                ProductDescription.this.startActivity(intent);
            }
        });

        infoIcon = findViewById(R.id.infoIconV2);

        infoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDescription.this, MoreActivity.class);
                ProductDescription.this.startActivity(intent);
            }
        });

        homeIcon= findViewById(R.id.homeIconV2);

        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDescription.this, MainActivity.class);
                ProductDescription.this.startActivity(intent);
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
//                setFavoriteCount();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
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

    private void similarCategoryProduct(String productId,String productCategory, String gender) {

        String productCategorySearchTerm = "%"+productCategory+"%";
        featureProductList.clear();

        featureProductList = productDb.mainDao().getAllProductByCategoryAndGender(productCategorySearchTerm,gender);

        featureProductList.removeIf(p -> p.getId().equals(productId));
        Collections.shuffle(featureProductList);

        searchProductRecyclerView.setAdapter(new FeatureProductAdapter
                (ProductDescription.this, featureProductList));
    }

    private void showProduct(String productId) {
//        int id = Integer.parseInt(productId);
        Optional<Product> product = productDb.mainDao().getProduct(productId);
        if(product.isPresent()){
            productTitle.setText(product.get().getTitle());
//            productPriceBrand.setText(product.get().getBrand());
            Glide.with(ProductDescription.this).load(product.get().getImages()).into(productImage);
            productDetails.setText(product.get().getDescription());
            productBrand.setText(product.get().getBrand());

            // Parse JSON string to object
            Gson gson = new Gson();
            String priceListString;
            String linkListString;

            ArrayList<Price> priceList = new ArrayList<>();
            ArrayList<Link> linkList = new ArrayList<>();

            List<ProductProperty> productPropertyList = new ArrayList<>();
            priceListString = product.get().getPrice();
            linkListString = product.get().getLink();

            if(!product.get().getPrice().equals("") && !product.get().getLink().equals("")){
                // Define the type of the target data structure using TypeToken
                Type listType = new TypeToken<ArrayList<Price>>() {}.getType();
                // Convert the JSON string to an ArrayList of Item objects
                priceList = gson.fromJson(priceListString, listType);

                Type link_listType = new TypeToken<ArrayList<Link>>() {}.getType();
                // Convert the JSON string to an ArrayList of Item objects
                linkList = gson.fromJson(linkListString, link_listType);

//                priceList = gson.fromJson(product.get().getPrice(), ParentPrice.class);
//                linkList = gson.fromJson(product.get().getLink(), ParentLink.class);

                int size=priceList.size();
                int ind=0;
                while(size>0){
                    String price = priceList.get(ind).getMarketPlacePrice();
                    String marketPlaceName = priceList.get(ind).getMarketPlaceName();
                    String link = linkList.get(ind).getMarketPlaceLink();
                    if(!price.equals("0")){
                        ProductProperty productProperty = new ProductProperty(price,marketPlaceName,link);
                        productPropertyList.add(productProperty);
                    }
                    size--;
                    ind++;
                }
                ProductPropertyAdapter adapter = new ProductPropertyAdapter
                        (ProductDescription.this, productPropertyList);
                productPropertyRecyclerView.setAdapter(adapter);
            }
//
//            if(!product.get().getPrice().equals("")){
//                for(Price p:product.get().getPrice())
//                price =  gson.fromJson(product.get().getPrice(), Price.class);
//                String formattedAmazonPrice="";
//                String formattedBrandPrice ="";
//                if(!price.getAmazon().equals(""))
//                formattedAmazonPrice = formatPrice(price.getAmazon());
//
//                if(!price.getOther().equals(""))
//                formattedBrandPrice = formatPrice(price.getOther());
//
//                productAmazonPrice.setText("MRP ₹ "+formattedAmazonPrice);
//                productBrandPrice.setText("MRP ₹ "+formattedBrandPrice);
//            }

            productRating.setText(product.get().getRating()+" ★");

            productCategory = product.get().getCategory();

            productGender = product.get().getGender();

            Random random = new Random();
            // Generate a random integer between 20 and 100
            int randomNumber = random.nextInt(81) + 20;
            productLikes.setText(randomNumber +"K+");

//            Link link;
//            String buyAmazonLink = "";
//            if(!product.get().getLink().equals("")){
//                link =  gson.fromJson(product.get().getLink(), Link.class);
//                buyAmazonLink = link.getAmazon();
//            }
//            String finalBuyAmazonLink = buyAmazonLink;
//            if(!finalBuyAmazonLink.equals(""))
//            buyButtonAmazon.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent();
//                    intent.setAction(Intent.ACTION_VIEW);
//                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
//                    intent.setData(Uri.parse(finalBuyAmazonLink));
//                    startActivity(intent);
//                }
//            });
//
//            String buyOtherBrandLink = "";
//            if(!product.get().getLink().equals("")){
//                link =  gson.fromJson(product.get().getLink(), Link.class);
//                buyOtherBrandLink = link.getOther();
//            }
//            String finalBuyOtherLink = buyOtherBrandLink;
//
//            if(!finalBuyOtherLink.equals(""))
//            buyProductBrand.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent();
//                    intent.setAction(Intent.ACTION_VIEW);
//                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
//                    intent.setData(Uri.parse(finalBuyOtherLink));
//                    startActivity(intent);
//                }
//            });
//
//            if(!finalBuyAmazonLink.equals(""))
//            shareProductAmazon.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent shareIntent = new Intent();
//                    String shareMssg = product.get().getTitle()+"\n\n"+finalBuyAmazonLink;
//                    shareIntent.setAction(Intent.ACTION_SEND);
//                    shareIntent.putExtra(Intent.EXTRA_TEXT,shareMssg+"\n\n");
//                    shareIntent.setType("text/plain");
//                    startActivity(Intent.createChooser(shareIntent,"Share via"));
//                }
//            });
//
//            if(!finalBuyOtherLink.equals(""))
//            shareOtherBrandProduct.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent shareIntent = new Intent();
//                    String shareMssg = product.get().getTitle()+"\n\n"+finalBuyOtherLink;
//                    shareIntent.setAction(Intent.ACTION_SEND);
//                    shareIntent.putExtra(Intent.EXTRA_TEXT,shareMssg+"\n\n");
//                    shareIntent.setType("text/plain");
//                    startActivity(Intent.createChooser(shareIntent,"Share via"));
//                }
//            });

            similarCategoryItem.setText("More From "+ productCategory+" Category");
            similarCategoryProduct(productId,productCategory,productGender);

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