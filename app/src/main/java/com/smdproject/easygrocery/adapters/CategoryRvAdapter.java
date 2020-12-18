package com.smdproject.easygrocery.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.smdproject.easygrocery.R;
import com.smdproject.easygrocery.models.Product;
import com.smdproject.easygrocery.models.ProductCategory;

import java.util.ArrayList;
import java.util.List;

public class CategoryRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Product> productsList ;
    private List<ProductCategory> categoryList ;

    private Context context;
    private OnItemClickListener mOnItemClickListener;
    private String mTag;

    public CategoryRvAdapter(Context context, OnItemClickListener onItemClickListener, String tag){
        this.context = context;
        this.mOnItemClickListener = onItemClickListener;
        this.mTag = tag;

        productsList = new ArrayList<>();
        categoryList = new ArrayList<>();
    }

    public void addCategoryItem(ProductCategory category){
        this.categoryList.add(category);
        notifyDataSetChanged();
    }

    public void addProductItem(Product category){
        this.productsList.add(category);
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        RvAdapterCategoryViewHolder viewHolder = null;
//        RvAdapterProductViewHolder pViewHolder = null;
        if(mTag == "categoryRV"){
            //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_layout, parent, false);
            //viewHolder = new RvAdapterCategoryViewHolder(view, mOnItemClickListener);
            return new RvAdapterCategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_layout, parent, false), mOnItemClickListener);
        }
       // View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
        //pViewHolder = new RvAdapterProductViewHolder(view, mOnItemClickListener);
        return new RvAdapterProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false), mOnItemClickListener);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        switch (mTag){
            case "categoryRV":
                ((RvAdapterCategoryViewHolder) holder).bind(position);
                break;
            case "subcategoryRV":
                ((RvAdapterProductViewHolder) holder).bind(position);
                break;
        }

    }

    @Override
    public int getItemCount() {
        if(mTag == "categoryRV"){
            return this.categoryList.size();
        }
        return this.productsList.size();

    }


    public class RvAdapterCategoryViewHolder extends ViewHolder{

        ImageView mImageView;
        TextView mCategoryName, mCategoryTypes;
        OnItemClickListener onItemClickListener;
        public RvAdapterCategoryViewHolder(@NonNull View itemView, final OnItemClickListener onItemClickListener) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.category_itemIv);
            mCategoryName = itemView.findViewById(R.id.category_item_name);
            mCategoryTypes = itemView.findViewById(R.id.category_item_types);

            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(getAdapterPosition(), mTag);
                }
            });
        }

        public void bind(int position){
            ProductCategory categoryItem = categoryList.get(position);
            mCategoryName.setText(categoryItem.getCategoryName());
            //holder.mCategoryTypes.setText(categoryItem.getItemDescription());
            Glide.with(itemView)
                    .load(categoryItem.getImgUri())
                    .fitCenter()
                    .into(mImageView);
        }
    }

    public class RvAdapterProductViewHolder extends ViewHolder {

        ImageView mProductImageView;
        TextView mProductName, mProductPrice;
        ImageButton mAddItemImgBtn;

        OnItemClickListener onItemClickListener;
        public RvAdapterProductViewHolder(@NonNull View itemView, final OnItemClickListener onItemClickListener) {
            super(itemView);

            this.onItemClickListener = onItemClickListener;
            mProductImageView = itemView.findViewById(R.id.product_imageV);
            mProductName = itemView.findViewById(R.id.product_name);
            mProductPrice = itemView.findViewById(R.id.product_price);
            mAddItemImgBtn = itemView.findViewById(R.id.product_cartImgBtn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(getAdapterPosition(), mTag);
                }
            });
        }

        public void bind(int position){
            Product product = productsList.get(position);
            mProductName.setText(product.getItemName());
            mProductPrice.setText("Rs. "+ String.valueOf(product.getItemPrice()));
            Glide.with(itemView)
                    .load(product.getImgUri())
                    .fitCenter()
                    .into(mProductImageView);

            mAddItemImgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "item Added to cart", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, String tag);
    }
}
