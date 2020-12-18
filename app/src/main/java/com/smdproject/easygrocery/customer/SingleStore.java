package com.smdproject.easygrocery.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smdproject.easygrocery.R;
import com.smdproject.easygrocery.adapters.CategoryRvAdapter;
import com.smdproject.easygrocery.adapters.RecyclerViewAdapter;
import com.smdproject.easygrocery.models.GroceryStore;
import com.smdproject.easygrocery.models.Product;
import com.smdproject.easygrocery.models.ProductCategory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SingleStore extends AppCompatActivity implements CategoryRvAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CategoryRvAdapter adapter;

    private TextView mStoreName, mRatingReviews;
    private ImageView mBgImage, mBackArrow;

    private ArrayList<ProductCategory> categoryList;

    private DatabaseReference databaseReference;

    private String categoryId, categoryName, image;
    private String storeName, storeId, imgUri;
    private int storeReviews, deliveryCharges;
    private double storeRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_store);

        recyclerView = findViewById(R.id.single_storeRv);
        mBgImage = findViewById(R.id.single_bgImg);
        mStoreName = findViewById(R.id.single_storeNameTv);
        mRatingReviews = findViewById(R.id.single_reviewsTv);
        mBackArrow = findViewById(R.id.store_backArrow);

        categoryList = new ArrayList<>();

        //Get data from the previous intent
        getIntentStrings();

        adapter = new CategoryRvAdapter(this, this, "categoryRV");
        getStoresDetails();
        initFirstRecyclerView();

        onBackArrowPressed();
    }

    public void onBackArrowPressed(){
        mBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initFirstRecyclerView() {

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        try {
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            System.out.println("ERROR:  " + e);
        }
    }

    public void getIntentStrings(){
        Intent intent = getIntent();
        storeName = intent.getStringExtra("storeName");
        deliveryCharges = intent.getIntExtra("storeDeliveryCharges", 0);
        storeRating = intent.getDoubleExtra("storeRating", 0.0);
        storeReviews = intent.getIntExtra("storeReviews", 0);
        imgUri = intent.getStringExtra("image");
        storeId = intent.getStringExtra("storeId");

        // Set Store info in header.
        mStoreName.setText(storeName);
        mRatingReviews.setText(String.valueOf(storeRating) + "(" + String.valueOf(storeReviews) + ")");

        Picasso.with(this)
                .load(imgUri)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(mBgImage);
    }

    private void getStoresDetails() {

        System.out.println("Store ID: " + storeId);
        // get details of currently signed in user from database
        databaseReference = FirebaseDatabase.getInstance().getReference("Categories");
        Query query = databaseReference.child(storeId).orderByChild("categoryId");
        //Query query = databaseReference.orderByChild("storeId").equalTo(storeId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //check until required data get
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //get Data
                    categoryName = "" + ds.child("categoryType").getValue();
                    categoryId = "" + ds.child("categoryId").getValue();
                    image = "" + ds.child("image").getValue();

                    System.out.println("Category Name: " + categoryName);

                    ProductCategory productCategory = new ProductCategory();
                    productCategory.setCategoryName(categoryName);
                    productCategory.setCategoryId(categoryId);
                    productCategory.setImgUri(image);

                    categoryList.add(productCategory);
                    adapter.addCategoryItem(productCategory);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SingleStore.this, "Data loading failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Uri rawImageToUri(int imgId){
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + getResources().getResourcePackageName(imgId)
                + '/' + getResources().getResourceTypeName(imgId)
                + '/' + getResources().getResourceEntryName(imgId) );

        return imageUri;
    }

    @Override
    public void onItemClick(int position, String tag) {
        Toast.makeText(this, "Item clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SingleStore.this, SingleStoreSubcategory.class);
        intent.putExtra("categoryId", categoryList.get(position).getCategoryId());
        intent.putExtra("categoryName", categoryList.get(position).getCategoryName());
        intent.putExtra("storeId", storeId);
        intent.putExtra("storeName", storeName);
        startActivity(intent);
    }

}