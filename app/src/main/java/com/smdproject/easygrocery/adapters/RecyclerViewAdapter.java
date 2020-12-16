package com.smdproject.easygrocery.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.smdproject.easygrocery.R;
import com.smdproject.easygrocery.models.GroceryStore;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RvAdapterHomeViewHolder> {

    private List<GroceryStore> storesList = new ArrayList<>();
    //private List<Product> categoryList = new ArrayList<>();
    private String mFeatured ;
    private String mTag;

    private  OnItemListener onItemListener;

    private Context context;
    //private int VIEW_TYPE_HOME = 1;
    //private int VIEW_TYPE_CATEGORY = 2;

    public RecyclerViewAdapter(Context context, OnItemListener onItemListener, String tag){
        this.context = context;
        this.mTag = tag;
        this.onItemListener = onItemListener;
    }

    public void addItem(GroceryStore store){
        this.storesList.add(store);
        notifyDataSetChanged();
    }


    @Override
    public RvAdapterHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        /*if(viewType == VIEW_TYPE_HOME){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
            RecyclerView.ViewHolder viewHolder = new RvAdapterHomeViewHolder(view);
            return viewHolder;
        }*/
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        RvAdapterHomeViewHolder viewHolder = new RvAdapterHomeViewHolder(view, onItemListener);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RvAdapterHomeViewHolder holder, int position) {

        GroceryStore storeItem = this.storesList.get(position);

        holder.mStoreNameTv.setText(storeItem.getStoreName());
        holder.mRatingTv.setText(Double.toString(storeItem.getRatingValue()) + "(" +Integer.toString(storeItem.getNumOfReviews()) + ")");
        holder.mDeliveryTv.setText("Rs. " + Integer.toString(storeItem.getDeliveryCharges()) + " Delivery fee");
        Glide.with(holder.itemView)
                .load(storeItem.getImgUri())
                .fitCenter()
                .into(holder.mItemImg);

        //Picasso.with(context).load(storeItem.getImgUri()).into(holder.mItemImg);

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context, "Clicked on Item..", Toast.LENGTH_SHORT).show();
//                //System.out.println(storesList.get(0).getClass().getName());
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return this.storesList.size();
    }

    /*@Override
    public int getItemViewType(int position) {

        if(storesList.get(position).getType().equals("GroceryStore")){
            return VIEW_TYPE_HOME;
        }
        else if()
        return VIEW_TYPE_CATEGORY;

    }*/

    public class RvAdapterHomeViewHolder extends RecyclerView.ViewHolder {

        ImageView mItemImg;
        TextView mStoreNameTv, mDeliveryTv, mRatingTv;

        OnItemListener onItemListener;

        public RvAdapterHomeViewHolder(@NonNull View itemView, final OnItemListener onItemListener) {
            super(itemView);

            mItemImg = itemView.findViewById(R.id.item_imgView);
            mStoreNameTv = itemView.findViewById(R.id.item_storeNameTv);
            mDeliveryTv = itemView.findViewById(R.id.item_deliveryTv);
            mRatingTv = itemView.findViewById(R.id.item_ratingTv);

            this.onItemListener = onItemListener;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemListener.onItemClick(getAdapterPosition(), mTag);
                }
            });
        }
    }

    public interface OnItemListener {
        public void onItemClick(int position, String tag);
    }

    /*public class RvAdapterCategoryViewHolder extends ViewHolder{

        ImageView mImageView;
        TextView mCategoryName, mCategoryTypes;
        public RvAdapterCategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.category_itemIv);
            mCategoryName = itemView.findViewById(R.id.category_item_name);
            mCategoryTypes = itemView.findViewById(R.id.category_item_types);
        }
    }*/


}
