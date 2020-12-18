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
import com.smdproject.easygrocery.models.Product;
import com.smdproject.easygrocery.models.ProductCategory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SingleStoreSubcategory extends AppCompatActivity implements CategoryRvAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CategoryRvAdapter adapter;

    private TextView mStoreName, mCategory;
    private ImageView mBackArrow;

    private DatabaseReference databaseReference;

    private String categoryName, categoryId, storeId, storeName;

    private ArrayList<Product> productsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_store_subcategory);

        recyclerView = findViewById(R.id.subcategory_Rv);
        mStoreName = findViewById(R.id.subcategory_actionbar_text);
        mCategory = findViewById(R.id.subcategory_categoryTv);
        mBackArrow = findViewById(R.id.subcategory_back_arrow);

        productsList = new ArrayList<>();

        onBackArrowPressed();
        adapter = new CategoryRvAdapter(this, this, "subcategoryRV");
        getIntentStrings();
        getCategoryProducts();
        initFirstRecyclerView();

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

    public Uri rawImageToUri(int imgId){
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + getResources().getResourcePackageName(imgId)
                + '/' + getResources().getResourceTypeName(imgId)
                + '/' + getResources().getResourceEntryName(imgId) );

        return imageUri;
    }


    public void getIntentStrings(){
        Intent intent = getIntent();
        categoryId = intent.getStringExtra("categoryId");
        categoryName = intent.getStringExtra("categoryName");
        storeId = intent.getStringExtra("storeId");
        storeName = intent.getStringExtra("storeName");

        // Set Store info in header.
        mStoreName.setText(storeName);
        mCategory.setText(categoryName);
    }

    private void getCategoryProducts() {

        System.out.println("Store ID: " + storeId);
        // get details of currently signed in user from database
        databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        Query query = databaseReference.child(storeId).child(categoryId).orderByChild("itemId");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //check until required data get
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //get Data
                    String itemName = "" + ds.child("itemName").getValue();
                    String description = "" + ds.child("description").getValue();
                    String itemId = "" + ds.child("itemId").getValue();
                    String price = "" + ds.child("price").getValue();
                    String image = "" + ds.child("image").getValue();

                    Product product = new Product();
                    product.setItemName(itemName);
                    product.setItemDescription(description);
                    product.setItemPrice(Double.parseDouble(price));
                    product.setItemId(itemId);
                    product.setImgUri(image);

                    productsList.add(product);

                    adapter.addProductItem(product);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SingleStoreSubcategory.this, "Data loading failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(int position, String tag) {

    }
}