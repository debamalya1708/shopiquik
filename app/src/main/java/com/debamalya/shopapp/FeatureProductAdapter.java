package com.debamalya.shopapp;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FeatureProductAdapter extends RecyclerView.Adapter<FeatureProductAdapter.productViewHolder> implements Filterable {

    private Context mContext;
    private List<Product> mFeatureProductList;
    private List<Product> mALlProduct;
    private OnItemClickListener mListener;
    private FavoriteDB favoriteDB;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public FeatureProductAdapter (Context context,List<Product> productList){
        mContext = context;
        mFeatureProductList = productList;
        mALlProduct = new ArrayList<>(productList);

    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<Product> filteredList = new ArrayList<>();

            if (charSequence.toString().isEmpty()){
                filteredList.addAll(mALlProduct);
            }
            else {
                for (Product product:mALlProduct){

                    if (product.equals(charSequence.toString().toLowerCase())){
                        filteredList.add(product);
                    }
                }
            }


            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            mFeatureProductList.clear();
            mFeatureProductList.addAll((Collection<? extends Product>) filterResults.values);
            notifyDataSetChanged();

        }
    };

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public productViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.product_card,parent,false);
        return new productViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull productViewHolder holder, int position) {
        // Initialize database instance
        favoriteDB = FavoriteDB.getInstance(mContext);

        final Product currentItem = mFeatureProductList.get(position);

        String productId = currentItem.getId();
        String imageUrl = currentItem.getImages();
        String productName = currentItem.getTitle().substring(0,Math.min(13,currentItem.getTitle().length()));
        String productBrand = currentItem.getBrand();
//        String productDescription = currentItem.getDescription().substring(0,10)+"...";
        String productRating = currentItem.getRating()+ " ★";

        holder.mProductName.setText(productName);
        holder.mProductBrand.setText(productBrand);
//        holder.mProductDescription.setText(productDescription);
        holder.mProductRating.setText(productRating);
        Glide.with(mContext).load(imageUrl).into(holder.mImageView);
        List<String> favoriteList = getAllFavourite();
        if(favoriteList.contains(productId)){
            holder.mFavoriteButton.setImageResource(R.drawable.ic_baseline_favorite_24); // Change to your filled favorite icon
        }

        holder.mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.scale_up);
                v.startAnimation(anim);
                Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator != null) {
                    vibrator.vibrate(50); // Vibrate for 100 milliseconds
                }
                Favorite favorite = favoriteDB.favoriteDAO().getFavoriteByProductId(productId);
                if(favorite!=null){
                    Favorite removeFavorite = new Favorite();
                    removeFavorite.setProductId(productId);
                    removeFavorite.setId(favorite.getId());
                    favoriteDB.favoriteDAO().delete(removeFavorite);
                    holder.mFavoriteButton.setImageResource(R.drawable.ic_baseline_favorite_border_24); // Change to your filled favorite icon
                    Toast.makeText(mContext,"Removed from Fav",Toast.LENGTH_SHORT).show();
                }
                else{
                    Favorite addFavorite = new Favorite();
                    addFavorite.setProductId(productId);
                    favoriteDB.favoriteDAO().insert(addFavorite);
                    holder.mFavoriteButton.setImageResource(R.drawable.ic_baseline_favorite_24); // Change to your filled favorite icon
                    Toast.makeText(mContext,"Added to Fav",Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.scale_up);
//                view.startAnimation(anim);
                //Toast.makeText(mcontext,"Click",Toast.LENGTH_LONG).show();

                Intent intent = new Intent(mContext, ProductDescription.class);
                intent.putExtra("productId", productId);
                mContext.startActivity(intent);
            }
        });
    }

    public List<String> getAllFavourite(){
        List<String> favoriteList = favoriteDB.favoriteDAO().getAllFavorites();
        if (favoriteList.size()>0)
            return favoriteList;
        return new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return mFeatureProductList.size();
    }

    public class productViewHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView mProductName;
        public TextView mProductBrand;
//        public TextView mProductDescription;
        public TextView mProductRating;
        private ImageButton mFavoriteButton;


        public productViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.product_image);
            mProductName = itemView.findViewById(R.id.product_title);
            mProductBrand = itemView.findViewById(R.id.product_brand);
//            mProductDescription = itemView.findViewById(R.id.product_desc);
            mProductRating = itemView.findViewById(R.id.product_rating);
            mFavoriteButton = itemView.findViewById(R.id.favorite_button);


        }
    }
}
