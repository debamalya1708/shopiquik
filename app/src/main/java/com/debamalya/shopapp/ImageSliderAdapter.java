package com.debamalya.shopapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ImageSliderAdapter{

    private Context mContext;
    private ViewFlipper mViewFlipper;
    private List<SliderItem> sliderImageList;

    public ImageSliderAdapter(Context context, ViewFlipper viewFlipper, List<SliderItem> imageUrls) {
        mContext = context;
        mViewFlipper = viewFlipper;
        sliderImageList = imageUrls;
    }

    public void loadImages() {
        for (SliderItem imageUrl : sliderImageList) {
            ImageView imageView = new ImageView(mContext);
            Glide.with(mContext)
                    .load(imageUrl.getImage())
                    .into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle click event for this image
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse(imageUrl.getAction()));
                        mContext.startActivity(intent);
                    }
            });
            mViewFlipper.addView(imageView);
        }

        // Set animation
        mViewFlipper.setInAnimation(mContext, android.R.anim.fade_in);
        mViewFlipper.setOutAnimation(mContext, android.R.anim.fade_out);

        // Set flipping interval
        mViewFlipper.setFlipInterval(5000); // 3 seconds
        mViewFlipper.setAutoStart(true); // Start flipping
    }
}