package com.debamalya.shopapp;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

public class ProductPropertyAdapter extends RecyclerView.Adapter< ProductPropertyAdapter.propertyViewHolder>{

    private Context mContext;
    private List<ProductProperty> mProductPropertyList;

    public ProductPropertyAdapter(Context mContext, List<ProductProperty> mProductPropertyList) {
        this.mContext = mContext;
        this.mProductPropertyList = mProductPropertyList;
    }

    @NonNull
    @Override
    public  ProductPropertyAdapter.propertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.product_property,parent,false);
        return new ProductPropertyAdapter.propertyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull  ProductPropertyAdapter.propertyViewHolder holder, int position) {
        final ProductProperty productProperty = mProductPropertyList.get(position);
        String price = productProperty.getPrice();
        String marketPlaceName = productProperty.getMarketPlaceName();
        String link = productProperty.getLink();

        holder.marketPlaceName.setText(marketPlaceName);
        holder.marketPlacePrice.setText("MRP ₹ "+formatPrice(price));
        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent();
                String shareMssg = "View this Product from " +marketPlaceName+"\n\n"+link;
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT,shareMssg+"\n\n");
                shareIntent.setType("text/plain");
                mContext.startActivity(Intent.createChooser(shareIntent,"Share via"));
            }
        });
        holder.buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(link));
                mContext.startActivity(intent);
            }
        });
    }

    private String formatPrice(String price) {
        double newPrice = Double.parseDouble(price);

        // Create a DecimalFormat object with the pattern "#,###.##"
        DecimalFormat formatter = new DecimalFormat("#,###.##");

        // Format the price using the DecimalFormat object
        String formattedPrice = formatter.format(newPrice);
        // Print the formatted price
        return formattedPrice;
    }

    @Override
    public int getItemCount() {
        return mProductPropertyList.size();
    }

    public class propertyViewHolder extends RecyclerView.ViewHolder{

        public ImageView shareButton;
        public TextView marketPlaceName;
        public TextView marketPlacePrice;
        private Button buyButton;


        public propertyViewHolder(@NonNull View itemView) {
            super(itemView);
            shareButton = itemView.findViewById(R.id.shareProduct);
            marketPlaceName = itemView.findViewById(R.id.product_market_place);
            marketPlacePrice = itemView.findViewById(R.id.market_place_price);
            buyButton = itemView.findViewById(R.id.buy_product);
        }
    }

}
