package com.debamalya.shopapp;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchTabPagerAdapter extends FragmentStateAdapter {
    private String[] categories = {"All", "Male", "Female"};
    private List<Product> products = new ArrayList<>();

    public SearchTabPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<Product> productList) {
        super(fragmentActivity);
        this.products = productList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return SearchFragment.newInstance(categories[position],products);
    }

    @Override
    public int getItemCount() {
        return categories.length;
    }
}
