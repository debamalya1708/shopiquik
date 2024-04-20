package com.debamalya.shopapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class ImagePagerAdapter extends PagerAdapter {

    private Context context;
    private List<SliderImageModel> sliderImageModelList;
    List<String> imageStrings = new ArrayList<>();
    List<String> actionStrings = new ArrayList<>();

    public ImagePagerAdapter(Context context, List<SliderImageModel> imageUrls) {
        this.context = context;
        this.sliderImageModelList = imageUrls;
    }

    @Override
    public int getCount() {
        return sliderImageModelList.size();
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_image, container, false);
        ImageView imageView = itemView.findViewById(R.id.slider_imageView);

        for (SliderImageModel sliderImageModel : sliderImageModelList){
            imageStrings.add(sliderImageModel.getImage());
            actionStrings.add(sliderImageModel.getAction());
        }
        // Load image from URL using Glide
        Glide.with(context)
                .load(sliderImageModelList.get(position).getImage())
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(imageView);

        // Handle click event
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click action here
                Toast.makeText(context, "Image " + (position + 1) + " clicked", Toast.LENGTH_SHORT).show();
            }
        });

        container.addView(itemView);
        return itemView;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
