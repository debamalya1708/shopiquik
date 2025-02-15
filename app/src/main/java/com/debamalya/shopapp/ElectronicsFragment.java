package com.debamalya.shopapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ElectronicsFragment extends Fragment{

    private static final String ARG_CATEGORY = "electronics";
    private String category;
    private RecyclerView recyclerView;
    private List<Product> featureProductList = new ArrayList<>();
    private RoomDB productDb;


    public static ElectronicsFragment newInstance(String category) {
        ElectronicsFragment fragment = new ElectronicsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getString(ARG_CATEGORY);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.electronics_fragment, container, false);
        recyclerView = view.findViewById(R.id.electronics_product_recyclerView);

        fetchProducts(); // Fetch products when the fragment is created
        return view;
    }

    private void fetchProducts() {
        category = category.toLowerCase();
        String productCategorySearchTerm = "%"+category+"%";
        Log.d("productCategorySearchTerm", productCategorySearchTerm);

        featureProductList.clear();

        productDb=RoomDB.getInstance(getContext());

        featureProductList = productDb.mainDao().getAllProductBySubCategory(productCategorySearchTerm,"Electronics");
        Log.d("featureProductList", String.valueOf(featureProductList.size()));

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        featureProductList = featureProductList.subList(0,Math.min(featureProductList.size(),10));
        Collections.shuffle(featureProductList);

        FeatureProductAdapter adapter = new FeatureProductAdapter
                (getContext(), featureProductList);

        recyclerView.setAdapter(adapter);
    }
}
