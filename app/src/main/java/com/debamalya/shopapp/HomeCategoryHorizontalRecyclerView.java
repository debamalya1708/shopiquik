package com.debamalya.shopapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeCategoryHorizontalRecyclerView extends
        RecyclerView.Adapter<HomeCategoryHorizontalRecyclerView.ViewHolder> {

    private static final String TAG = "HomeCategoryHorizontalRecyclerView";
    private ArrayList<String> categoryNames = new ArrayList<>();
    private ArrayList<Integer> categoryIds = new ArrayList<>();
    private Context context;

    public HomeCategoryHorizontalRecyclerView(Context mContext,ArrayList<String> mCategoryNames,
                                              ArrayList<Integer> mCategoryIds) {
        this.categoryNames = mCategoryNames;
        this.categoryIds = mCategoryIds;
        this.context = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.home_category_card,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final String currentItem = categoryNames.get(position);

        Log.d(TAG, "onBindViewHolder: called.");

        holder.name.setText(currentItem);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String act = categoryNames.get(position);

                if (currentItem.equals("")) {
                    Log.d(TAG, "No Data");
//                    Intent intent = new Intent(context, ShowAllProduct.class);
//                    context.startActivity(intent);
                }
                else {
                    Intent intent = new Intent(context, SearchProduct.class);
                    intent.putExtra("SearchItem", currentItem);
                    intent.putExtra("Event", "Category");
                    context.startActivity(intent);
                }

            }

        });
    }

    @Override
    public int getItemCount() {
        return categoryIds.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView id;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.category_id);
            name = itemView.findViewById(R.id.characterName);
        }
    }

}
