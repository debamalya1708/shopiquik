package com.debamalya.shopapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class HomeBrandHorizontalRecyclerView extends
        RecyclerView.Adapter<HomeBrandHorizontalRecyclerView.ViewHolder> implements Filterable {

    private static final String TAG = "HomeBrandHorizontalRecyclerView";
    private List<String> brandNames = new ArrayList<>();
    private List<String> brandImages = new ArrayList<>();
    private List<Brand> brandArrayList;
    private Context context;
    private OnItemClickListener mListener;


    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<Brand> filteredList = new ArrayList<>();

            if (charSequence.toString().isEmpty()){
                filteredList.addAll(brandArrayList);
            }
            else {
                for (Brand b:brandArrayList){

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

            brandArrayList.clear();
            brandArrayList.addAll((Collection<? extends Brand>) filterResults.values);
            notifyDataSetChanged();

        }
    };


    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public HomeBrandHorizontalRecyclerView(Context mContext, List<Brand> mbrandArrayList) {
        this.brandArrayList = mbrandArrayList;
        this.context = mContext;
    }

    @NonNull
    @Override
    public HomeBrandHorizontalRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.home_brand_card,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeBrandHorizontalRecyclerView.ViewHolder holder, int position) {

        final Brand currentItem = brandArrayList.get(position);

        String imageUrl = currentItem.getImage();
        String brandName = currentItem.getName();

        holder.name.setText(brandName);
        Glide.with(context).load(imageUrl).into(holder.imageView);
        Log.d(TAG, "onBindViewHolder: called.");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String brandName = brandImages.get(position);

                if (brandName.equals("")) {
                    Log.d(TAG, "No Data");
//                    Intent intent = new Intent(context, ShowAllProduct.class);
//                    context.startActivity(intent);
                }
                else {
                    Intent intent = new Intent(context, SearchProduct.class);
                    intent.putExtra("SearchItem", brandName);
                    intent.putExtra("Event", "Brand");
                    context.startActivity(intent);
                }

            }

        });
    }

    @Override
    public int getItemCount() {
        return brandArrayList.size();
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
