package com.smdproject.easygrocery.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.smdproject.easygrocery.R;
import com.smdproject.easygrocery.models.Product;

import java.util.ArrayList;
import java.util.List;

public class CategoryRvAdapter extends RecyclerView.Adapter<CategoryRvAdapter.RvAdapterCategoryViewHolder> {

    private List<Product> categoryList = new ArrayList<>();

    private Context context;

    public CategoryRvAdapter(Context context){
        this.context = context;
    }

    public void addItem(Product category){
        this.categoryList.add(category);
        notifyDataSetChanged();
    }


    @Override
    public RvAdapterCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_layout, parent, false);
        RvAdapterCategoryViewHolder viewHolder = new RvAdapterCategoryViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RvAdapterCategoryViewHolder holder, int position) {

        Product categoryItem = this.categoryList.get(position);

        holder.mCategoryName.setText(categoryItem.getItemName());
        holder.mCategoryTypes.setText(categoryItem.getItemDescription());
        Glide.with(holder.itemView)
                .load(categoryItem.getImgUri())
                .fitCenter()
                .into(holder.mImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Clicked on Item..", Toast.LENGTH_SHORT).show();
                //System.out.println(storesList.get(0).getClass().getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.categoryList.size();
    }


    public class RvAdapterCategoryViewHolder extends RecyclerView.ViewHolder{

        ImageView mImageView;
        TextView mCategoryName, mCategoryTypes;
        public RvAdapterCategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.category_itemIv);
            mCategoryName = itemView.findViewById(R.id.category_item_name);
            mCategoryTypes = itemView.findViewById(R.id.category_item_types);
        }
    }
}
