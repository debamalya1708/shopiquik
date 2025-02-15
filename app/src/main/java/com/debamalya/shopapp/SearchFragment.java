package com.debamalya.shopapp;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchFragment extends Fragment{

    private static final String ARG_GENDER = "search";
    private String gender;
    private RecyclerView recyclerView;
    private List<Product> featureProductList = new ArrayList<>();

    public static SearchFragment newInstance(String gender, List<Product> products) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_GENDER, gender);
        args.putSerializable("products", (Serializable) products);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            gender = getArguments().getString(ARG_GENDER);
            featureProductList = (List<Product>) getArguments().getSerializable("products");
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        recyclerView = view.findViewById(R.id.search_product_recyclerView);

        fetchProducts(); // Fetch products when the fragment is created
        return view;
    }

    private void fetchProducts() {

        List<Product> filteredList = new ArrayList<>();

        for (Product product : featureProductList) {
            if (product.getGender().equals(gender)) {
                filteredList.add(product);
            }
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        Collections.shuffle(filteredList);
        FeatureProductAdapter adapter;

        if(!gender.equals("All")){
            adapter = new FeatureProductAdapter
                    (getContext(), filteredList);
        }else{
            adapter = new FeatureProductAdapter
                    (getContext(), featureProductList);
        }

        recyclerView.setAdapter(adapter);
    }
}
