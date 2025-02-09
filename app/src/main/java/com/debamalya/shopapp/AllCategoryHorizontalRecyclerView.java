package com.debamalya.shopapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AllCategoryHorizontalRecyclerView extends
        RecyclerView.Adapter<AllCategoryHorizontalRecyclerView.ViewHolder> implements Filterable {

    private static final String TAG = "AllCategoryHorizontalRecyclerView";
    private List<String> categoryNames = new ArrayList<>();
    private List<String> categoryImages = new ArrayList<>();
    private List<Category> categoryArrayList;
    private Context context;
    private OnItemClickListener mListener;


    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<Category> filteredList = new ArrayList<>();

            if (charSequence.toString().isEmpty()){
                filteredList.addAll(categoryArrayList);
            }
            else {
                for (Category b:categoryArrayList){

                    if (b.equals(charSequence.toString().toLowerCase())){
                        filteredList.add(b);
                    }
                }
            }


            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            categoryArrayList.clear();
            categoryArrayList.addAll((Collection<? extends Category>) filterResults.values);
            notifyDataSetChanged();

        }
    };


    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public AllCategoryHorizontalRecyclerView(Context mContext, List<Category> mCategoryArrayList) {
        this.categoryArrayList = mCategoryArrayList;
        this.context = mContext;
    }

    @NonNull
    @Override
    public AllCategoryHorizontalRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.all_category_card,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllCategoryHorizontalRecyclerView.ViewHolder holder, int position) {

        final Category currentItem = categoryArrayList.get(position);

        String imageUrl = currentItem.getImage();
        String CategoryName = currentItem.getName();

        holder.name.setText(CategoryName);
        Glide.with(context).load(imageUrl).into(holder.imageView);
        Log.d(TAG, "onBindViewHolder: called.");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Animation anim = AnimationUtils.loadAnimation(context, R.anim.scale_up);
                view.startAnimation(anim);
//                String CategoryName = CategoryImages.get(position);

                if (CategoryName.equals("")) {
                    Log.d(TAG, "No Data");
//                    Intent intent = new Intent(context, ShowAllProduct.class);
//                    context.startActivity(intent);
                }
                else {
                    Intent intent = new Intent(context, SubCategoryProducts.class);
                    intent.putExtra("SearchItem", CategoryName);
                    intent.putExtra("Event", "Category");
                    context.startActivity(intent);
                }

            }

        });
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.Character_image_view);
            name = itemView.findViewById(R.id.characterName);
        }
    }

}
