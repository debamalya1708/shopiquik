package com.debamalya.shopapp;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ElectronicsTabPagerAdapter extends FragmentStateAdapter {
    private String[] categories = {"watch", "mobile", "headphones", "laptop"};

    public ElectronicsTabPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return ElectronicsFragment.newInstance(categories[position]);
    }

    @Override
    public int getItemCount() {
        return categories.length;
    }
}
