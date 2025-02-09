package com.debamalya.shopapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RecyclerView categoryRecyclerView;
    private RecyclerView brandRecyclerView;
    private RecyclerView featureProductRecyclerView,featureProductRecyclerView1,featureProductRecyclerView2;
    private List<Product> productList = new ArrayList<>();
    private List<Product> featureProductList = new ArrayList<>();
    private List<Brand> brandList = new ArrayList<>();
    private RoomDB productDb;
    private CategoryDB categoryDb;
    private SubCategoryDB subCategoryDb;
    private FavoriteDB favoriteDB;
    private BrandDB brandDB;
    private ImageButton allCategoryButton;
    private MainAdapter adapter;
    private RequestQueue mRequestQueue;
    LinearLayoutManager linearLayoutManager;
    private List<Category> categoryList = new ArrayList<>();
    private List<SubCategory> subCategoryList = new ArrayList<>();
    private ArrayList<String> categoryNameList = new ArrayList<>();
    private ArrayList<Integer> categoryIdList = new ArrayList<>();
    GridLayoutManager gridLayoutManager;
    GridLayoutManager featureProductGridLayoutManager;
    private ArrayList<String> brandNameList = new ArrayList<>();
    private ArrayList<String> brandImageList = new ArrayList<>();
    private TextView viewAllBrands;
    private ImageView infoIcon, favIcon;
    private EditText dialogAmount,searchInput;
    private FloatingActionButton floatingActionButton;
    private Spinner categorySpinner, genderSpinner,priceSpinner;
    private ArrayAdapter<CharSequence> genderArrayAdapter,priceArrayAdapter;
    private long pressedTime;
    private TextView favoriteCount;
    private ViewFlipper viewFlipper1,viewFlipperCover,viewFlipperCover1,viewFlipperCover2;
    private ImageButton mBtnPrevious,btn_previousCover,btn_previousCover1,btn_previousCover2,searchProduct;
    private ImageButton mBtnNext,btn_nextCover,btn_nextCover1,btn_nextCover2;

    private SwipeRefreshLayout swipeRefreshLayout;
    private NestedScrollView nestedScrollView;
    private Button fabButton;
    private LinearLayout searchLayout;
    private TextView appTitle;
    ImageView searchButton,removeIcon;

    private int lastScrollY = 0;

    private Animation fadeInAnimation;
    private Animation fadeOutAnimation;
    private List<String> categories = Arrays.asList("Men", "Women", "Boys", "Girls");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

// Clear the cache and print result
        boolean isCacheCleared = CacheCleaner.clearCache(this);
        if (isCacheCleared) {
            Log.d("Cache: ", "Cache cleared successfully.");
        } else {
            Log.d("Cache: ", "Failed to clear cache.");
        }
        productDb=RoomDB.getInstance(this);

        categoryDb = CategoryDB.getInstance(this);

        subCategoryDb = SubCategoryDB.getInstance(this);

        favoriteDB = FavoriteDB.getInstance(this);

        brandDB = BrandDB.getInstance(this);

        nestedScrollView = findViewById(R.id.scrollContainer);

        fabButton = findViewById(R.id.back_to_top_button);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > lastScrollY && fabButton.getVisibility() == View.VISIBLE || scrollY == 0) {
                    // Scrolling down, hide FAB
                    fabButton.setVisibility(View.GONE);
                } else if (scrollY < lastScrollY) {
                    // Scrolling up or at the top, show FAB
                    fabButton.setVisibility(View.VISIBLE);
                }

                lastScrollY = scrollY;
            }
        });

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Scroll to the top of the NestedScrollView when FAB is clicked
                nestedScrollView.smoothScrollTo(0, 0);
            }
        });

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);

        TabPagerAdapter adapter = new TabPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) ->
                tab.setText(categories.get(position))
        ).attach();

        tabLayout.getTabAt(0).select(); // Select first tab by default

        mRequestQueue = Volley.newRequestQueue(MainActivity.this);

        linearLayoutManager = new LinearLayoutManager(MainActivity.this,
                LinearLayoutManager.HORIZONTAL,false);

        categoryRecyclerView = findViewById(R.id.category_recyclerView);

        viewFlipper1 = findViewById(R.id.viewFlipper1);
        viewFlipperCover = findViewById(R.id.viewFlipperCover);
//        viewFlipperCover1 = findViewById(R.id.viewFlipperCover1);
//        viewFlipperCover2 = findViewById(R.id.viewFlipperCover2);
        searchProduct = findViewById(R.id.searchProduct);
        searchInput = findViewById(R.id.searchInput);

        mBtnPrevious = findViewById(R.id.btn_previous);
        mBtnNext = findViewById(R.id.btn_next);

        btn_previousCover = findViewById(R.id.btn_previousCover);
        btn_nextCover = findViewById(R.id.btn_nextCover);

//        btn_previousCover1 = findViewById(R.id.btn_previousCover1);
//        btn_nextCover1 = findViewById(R.id.btn_nextCover1);
//
//        btn_previousCover2 = findViewById(R.id.btn_previousCover2);
//        btn_nextCover2 = findViewById(R.id.btn_nextCover2);

        viewAllBrands = findViewById(R.id.viewAllBrands);

        allCategoryButton = findViewById(R.id.allCategoryButton);

        searchProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = searchInput.getText().toString();
                Intent intent = new Intent(MainActivity.this, SearchProduct.class);
                intent.putExtra("SearchItem", searchText);
                intent.putExtra("Event", "Global");
                MainActivity.this.startActivity(intent);
            }
        });

        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // Perform your search or any other action here
                    String searchText = searchInput.getText().toString();
                    Intent intent = new Intent(MainActivity.this, SearchProduct.class);
                    intent.putExtra("SearchItem", searchText);
                    intent.putExtra("Event", "Global");
                    MainActivity.this.startActivity(intent);
                    return true; // Return true to indicate you've handled the action
                }
                return false; // Return false to let the system handle the action
            }
        });

        floatingActionButton = findViewById(R.id.filter_fab_Icon);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.scale_up);
                v.startAnimation(anim);
                Vibrator vibrator = (Vibrator) MainActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator != null) {
                    vibrator.vibrate(50); // Vibrate for 100 milliseconds
                }
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
        favIcon = findViewById(R.id.fav_Icon);


        favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FavoriteProducts.class);
                MainActivity.this.startActivity(intent);
            }
        });

        infoIcon = findViewById(R.id.infoIcon);

        infoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MoreActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        gridLayoutManager = new GridLayoutManager(MainActivity.this, 3);

        brandRecyclerView = findViewById(R.id.brand_recyclerView);

        featureProductRecyclerView = findViewById(R.id.feature_product_recyclerView);

        featureProductRecyclerView.setHasFixedSize(true);

        featureProductRecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,2));

//        featureProductRecyclerView1 = findViewById(R.id.feature_product_recyclerView1);
//
//        featureProductRecyclerView1.setHasFixedSize(true);
//
//        featureProductRecyclerView1.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
//
//        featureProductRecyclerView2 = findViewById(R.id.feature_product_recyclerView2);
//
//        featureProductRecyclerView2.setHasFixedSize(true);
//
//        featureProductRecyclerView2.setLayoutManager(new GridLayoutManager(MainActivity.this,2));

        parseProductJson();
        showProduct();

        parseCategoryJson();
        showCategory();

        parseSubCategoryJson();

        parseBrandJson();
        showBrand();

        setFavoriteCount();

        setFeatureFlipper();
        setViewFlipperCover();
//        setViewFlipperCover1();
//        setViewFlipperCover2();

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do your refresh logic here
                // For example, fetch new data from a network request
                // Once the refresh is complete, call setRefreshing(false) to stop the animation
//                parseProductJson();
//                parseCategoryJson();
//                parseSubCategoryJson();
//                parseBrandJson();
                showProduct();
                showCategory();
                showBrand();
                setFavoriteCount();
                setFeatureFlipper();
                setViewFlipperCover();
//                setViewFlipperCover1();
//                setViewFlipperCover2();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        searchLayout = findViewById(R.id.searchLayout);
        appTitle = findViewById(R.id.appBarTitle);

        searchButton = findViewById(R.id.search_Icon);
        removeIcon = findViewById(R.id.remove_Icon);
        // Load animations
        fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        removeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSearchView();
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSearchView();
            }
        });
    }

    private void toggleSearchView() {
        if (appTitle.getVisibility() == View.VISIBLE) {
            appTitle.setVisibility(View.GONE);
            searchLayout.setVisibility(View.VISIBLE);
            searchButton.setVisibility(View.GONE);
            removeIcon.setVisibility(View.VISIBLE);
            favIcon.setVisibility(View.GONE);
            infoIcon.setVisibility(View.GONE);
            // Fade in search view
            removeIcon.setVisibility(View.VISIBLE);
//            removeIcon.startAnimation(fadeInAnimation);

            searchLayout.setVisibility(View.VISIBLE);
//            searchLayout.startAnimation(fadeInAnimation);
        } else {
            appTitle.setVisibility(View.VISIBLE);
            searchLayout.setVisibility(View.GONE);
            searchButton.setVisibility(View.VISIBLE);
            removeIcon.setVisibility(View.GONE);
            favIcon.setVisibility(View.VISIBLE);
            infoIcon.setVisibility(View.VISIBLE);

            appTitle.setVisibility(View.VISIBLE);
            appTitle.startAnimation(fadeInAnimation);

            searchButton.setVisibility(View.VISIBLE);
            searchButton.startAnimation(fadeInAnimation);

            favIcon.setVisibility(View.VISIBLE);
            favIcon.startAnimation(fadeInAnimation);

            infoIcon.setVisibility(View.VISIBLE);
            infoIcon.startAnimation(fadeInAnimation);
        }
    }

    private void setFeatureFlipper(){
        String URL = "https://res.cloudinary.com/dq5jpef6l/raw/upload/Shopping%20App/image_albp25.json";
        setFeaturedImageSlider(URL,viewFlipper1,mBtnPrevious,mBtnNext);
    }

    private void setViewFlipperCover(){
        String URL = "https://res.cloudinary.com/dq5jpef6l/raw/upload/Shopping%20App/image_albp25.json";
        setFeaturedImageSlider(URL,viewFlipperCover,btn_previousCover,btn_nextCover);
    }

//    private void setViewFlipperCover1(){
//        String URL = "https://res.cloudinary.com/dq5jpef6l/raw/upload/Shopping%20App/image_albp25.json";
//        setFeaturedImageSlider(URL,viewFlipperCover1,btn_previousCover1,btn_nextCover1);
//    }
//
//    private void setViewFlipperCover2(){
//        String URL = "https://res.cloudinary.com/dq5jpef6l/raw/upload/Shopping%20App/image_albp25.json";
//        setFeaturedImageSlider(URL,viewFlipperCover2,btn_previousCover2,btn_nextCover2);
//    }

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

                    ImageSliderAdapter adapter = new ImageSliderAdapter(MainActivity.this, viewFlipper, imageSliderList);
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

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        return mGestureDetector.onTouchEvent(event);
//    }
//
//    private class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {
//
//        private static final int SWIPE_THRESHOLD = 100;
//        private static final int SWIPE_VELOCITY_THRESHOLD = 100;
//
//        @Override
//        public boolean onDown(MotionEvent event) {
//            return true;
//        }
//
//        @Override
//        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
//            float diffX = event2.getX() - event1.getX();
//            float diffY = event2.getY() - event1.getY();
//
//            if (Math.abs(diffX) > Math.abs(diffY) &&
//                    Math.abs(diffX) > SWIPE_THRESHOLD &&
//                    Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
//                if (diffX > 0) {
//                    viewFlipper.showPrevious();
//                } else {
//                    viewFlipper.showNext();
//                }
//                return true;
//            }
//            return false;
//        }
//    }
//    private void fetchImagesFromApi() {
//        mRequestQueue.getCache().clear();
//        String URL = "https://res.cloudinary.com/dq5jpef6l/raw/upload/Shopping%20App/image_albp25.json";
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
//
//        List<SliderImageModel> slideModels = new ArrayList<>();
//
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONArray jsonArray = response.getJSONArray("items");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject hit = jsonArray.getJSONObject(i);
//                        slideModels.add(new SliderImageModel(hit.get("image").toString()
//                                ,hit.get("action").toString()));
//                    }
//
//                    ImagePagerAdapter adapter = new ImagePagerAdapter(MainActivity.this,slideModels);
//                    viewPager.setAdapter(adapter);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//        mRequestQueue.add(request);
//    }

    private void setFavoriteCount(){
        int favoriteItemCount = favoriteDB.favoriteDAO().getAllFavorites().size();
        favoriteCount = findViewById(R.id.favoriteCount);
        if(favoriteItemCount<9){
            favoriteCount.setText(String.valueOf(favoriteItemCount));
        }else{
            favoriteCount.setText("9+");
        }
    }

    public void showCustomDialog() {

        // Inflate the custom layout
        View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog_layout, null);

        categorySpinner = dialogView.findViewById(R.id.category_spinner);
        genderSpinner = dialogView.findViewById(R.id.gender_spinner);
        priceSpinner = dialogView.findViewById(R.id.price_spinner);
        dialogAmount = dialogView.findViewById(R.id.dialog_amount);
        String noSelection = "None" ;
        List<String> categoryNameList = categoryDb.categoryDAO().getAllCategoryName();
        categoryNameList.add(0,noSelection);

//        List<String> brandNameList = brandDB.brandDAO().getAllBrandName();
//        brandNameList.add(noSelection);

//        ArrayAdapter<String> brandArrayAdapter = new ArrayAdapter<>(this,
//                R.layout.spinner_layout, brandNameList);

//        brandArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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
        genderSpinner.setAdapter(genderArrayAdapter);
        priceSpinner.setAdapter(priceArrayAdapter);
        priceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedValue = (String) parentView.getItemAtPosition(position);

                // Assuming you want to show the EditText when a specific value is selected
                if (!selectedValue.equals(noSelection)) {
                    dialogAmount.setVisibility(View.VISIBLE);
                } else {
                    dialogAmount.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle case where nothing is selected
            }
        });
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

        Button searchButton = dialogView.findViewById(R.id.search_dialog_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected value from spinner1
                String categorySpinnerValue = categorySpinner.getSelectedItem().toString();

                // Get the selected value from spinner2
                String genderSpinnerValue = genderSpinner.getSelectedItem().toString();

                // Get the selected value from spinner3
                String priceSpinnerValue = priceSpinner.getSelectedItem().toString();

                String enteredAmount = dialogAmount.getText().toString();

                if(!priceSpinnerValue.equals(noSelection)){
                    if(TextUtils.isEmpty(dialogAmount.getText())){
                        dialogAmount.setError("Enter the field");
                        return;
                    }
                }

                Intent intent = new Intent(MainActivity.this, FilterPage.class);
                intent.putExtra("categoryValue", categorySpinnerValue);
                intent.putExtra("genderValue", genderSpinnerValue);
                intent.putExtra("amountType", priceSpinnerValue);
                intent.putExtra("enteredAmount", enteredAmount);
                MainActivity.this.startActivity(intent);

            }
        });

        // Show the dialog
        dialog.show();
    }

    private void showProduct() {
        Log.d("Product","Show Product Called");
        featureProductList.clear();

        featureProductList = productDb.mainDao().getAllFeatureProduct();
        List<Product> featureList = new ArrayList<>();
        featureProductList = featureProductList.subList(0,Math.min(featureProductList.size(),24));
        Collections.shuffle(featureProductList);
//        Collections.shuffle(featureList);
        FeatureProductAdapter adapter = new FeatureProductAdapter
                (MainActivity.this, featureProductList);
        featureProductRecyclerView.setAdapter(adapter);
//        showProductV1(featureProductList);
    }

//    private void showProductV1(List<Product> featureProductList) {
//        Log.d("Product", "Show Product1 Called");
//        int startIndex = featureProductList.size() / 3;
//        int endIndex = (2 * featureProductList.size()) / 3;
//        if (featureProductList.size() > 3) {
//            // Calculate start and end indices for the sublist
//
//            // Get the sublist
//            List<Product> featureList = featureProductList.subList(startIndex, endIndex);
//
//            // Shuffle the sublist
//            Collections.shuffle(featureList);
//
//            // Set up the adapter with the shuffled sublist
//            FeatureProductAdapter adapter = new FeatureProductAdapter(MainActivity.this, featureList);
//            featureProductRecyclerView1.setAdapter(adapter);
//        } else {
//            List<Product> featureList = featureProductList.subList(endIndex, featureProductList.size());
//
//            // If the list size is 3 or less, shuffle the original list
//            Collections.shuffle(featureProductList);
//
//            // Set up the adapter with the shuffled original list
//            FeatureProductAdapter adapter = new FeatureProductAdapter(MainActivity.this, featureProductList);
//            featureProductRecyclerView1.setAdapter(adapter);
//        }
//        showProductV2(featureProductList);
//    }
//
//
//    private void showProductV2(List<Product> featureProductList) {
//        Log.d("Product", "Show Product2 Called");
//        int startIndex = (2 * featureProductList.size()) / 3;
//        if (featureProductList.size() > 3) {
//            // Calculate start and end indices for the sublist
//
//
//            // Get the sublist
//            List<Product> featureList = featureProductList.subList(startIndex, featureProductList.size());
//
//            // Shuffle the sublist
//            Collections.shuffle(featureList);
//
//            // Set up the adapter with the shuffled sublist
//            FeatureProductAdapter adapter = new FeatureProductAdapter(MainActivity.this, featureList);
//            featureProductRecyclerView2.setAdapter(adapter);
//        } else {
//            List<Product> featureList = featureProductList.subList(startIndex, featureProductList.size());
//
//            // If the list size is 3 or less, shuffle the original list
//            Collections.shuffle(featureList);
//
//            // Set up the adapter with the shuffled original list
//            FeatureProductAdapter adapter = new FeatureProductAdapter(MainActivity.this, featureList);
//            featureProductRecyclerView2.setAdapter(adapter);
//        }
//    }
//

    private void parseBrandJson(){
        mRequestQueue.getCache().clear();
        String URL = "https://res.cloudinary.com/dq5jpef6l/raw/upload/v1/Shopping%20App/brands_rqtfnt.json";
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
                    Log.d("parseBrandJson", "Function called");
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

    private void parseSubCategoryJson(){
        mRequestQueue.getCache().clear();
        String URL = "https://res.cloudinary.com/dq5jpef6l/raw/upload/v1/Shopping%20App/subCategory_pmqlor.json";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("subCategory");
                    JSONObject syncTime = response.getJSONObject("syncTime");
                    String time = syncTime.getString("time");

                    // Parse the sync time into LocalDateTime
                    LocalDateTime latestTime = LocalDateTime.parse(time);

                    // Get the previous insert time from the database
                    List<SubCategory> subCategoryList = subCategoryDb.subCategoryDAO().getAllSubCategory();
                    LocalDateTime previousTime = LocalDateTime.MIN; // Initialize with minimum value

                    // If there are products in the database, get the timestamp of the first product
                    if (!subCategoryList.isEmpty()) {
                        previousTime = LocalDateTime.parse(subCategoryList.get(0).getCreatedAt());
                    }

                    // Compare timestamps and decide whether to insert new data
                    Log.d("latestTime", latestTime.toString());
                    Log.d("previousTime", previousTime.toString());

                    Log.d("parseSubCategoryJson", "Function called");

                    if (latestTime.isAfter(previousTime)) {
                        // Clear existing data and insert new products
                        subCategoryDb.subCategoryDAO().deleteAllData();
                        insertSubCategory(jsonArray, time);
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
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Cache-Control", "no-cache");
                headers.put("Pragma", "no-cache");
                return headers;
            }
        };
        mRequestQueue.add(request);

    }

    private void parseCategoryJson(){
        mRequestQueue.getCache().clear();
        String URL = "https://res.cloudinary.com/dq5jpef6l/raw/upload/v1/Shopping%20App/category_wu9sa7.json";
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

                    Log.d("parseCategoryJson", "Function called");

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
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Cache-Control", "no-cache");
                headers.put("Pragma", "no-cache");
                return headers;
            }
        };
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
        int lastIndex = 6;
        List<Brand> brandList = brandDB.brandDAO().getAllBrand();
        Collections.shuffle(brandList);
        brandList = brandList.subList(0,Math.min(brandList.size(), lastIndex));
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


    private void insertSubCategory(JSONArray jsonArray,String createdAt) throws JSONException {
        for(int i=0;i<jsonArray.length();i++){

            JSONObject responseObj = jsonArray.getJSONObject(i);
            int id = Integer.parseInt(responseObj.getString("id"));
            String name = responseObj.getString("name");
            String image = responseObj.getString("image");
            String gender = responseObj.getString("gender");
            String category = responseObj.getString("category");

            subCategoryList.add(new SubCategory(id,name,image, gender, category, createdAt));
        }

        for(SubCategory c:subCategoryList){
            subCategoryDb.subCategoryDAO().insert(c);
        }

//                    mProductAdapter = new ProuctAdapter(ShowAllProduct.this,mProductList);
//                    mRecyclerView.setAdapter(mProductAdapter);
//                    mProductAdapter.setOnItemClickListener(ShowAllProduct.this);
//                    findViewById(R.id.gridLoading).setVisibility(View.GONE);
    }

    private void parseProductJson() {
        mRequestQueue.getCache().clear();

        String URL = "https://res.cloudinary.com/dq5jpef6l/raw/upload/v1/Shopping%20App/products_gf1bid.json";
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
            String id = responseObj.getString("id");
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
            String gender = responseObj.getString("gender");
            String subCategory = responseObj.getString("subCategory");
            String featured = responseObj.getString("featured");
            String rating = responseObj.getString("ratings");
            String occasion = responseObj.getString("occasions");
            String brand = responseObj.getString("brand");
            Gson gson = new Gson();
            productList.add(new Product(id,title,description,price,link,country,images
                    ,category,gender,subCategory,featured,rating,occasion,brand,createdAt));
        }

        for(Product p:productList){
            productDb.mainDao().insert(p);
        }

//                    mProductAdapter = new ProuctAdapter(ShowAllProduct.this,mProductList);
//                    mRecyclerView.setAdapter(mProductAdapter);
//                    mProductAdapter.setOnItemClickListener(ShowAllProduct.this);
//                    findViewById(R.id.gridLoading).setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
            doublePressBack();
    }

    public void doublePressBack(){
        if (pressedTime + 3000 > System.currentTimeMillis()) {
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

}