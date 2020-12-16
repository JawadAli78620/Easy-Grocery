package com.smdproject.easygrocery.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smdproject.easygrocery.R;
import com.smdproject.easygrocery.adapters.CategoryRvAdapter;
import com.smdproject.easygrocery.adapters.RecyclerViewAdapter;
import com.smdproject.easygrocery.models.GroceryStore;
import com.smdproject.easygrocery.models.Product;

public class SingleStore extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CategoryRvAdapter adapter;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_store);

        recyclerView = findViewById(R.id.single_storeRv);

        //populateRvData();
        adapter = new CategoryRvAdapter(this);



        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

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

    /*private void getStoresDetails() {

        initFirstRecyclerView();

        // get details of currently signed in user from database
        Query query = databaseReference.orderByChild("storeId").equalTo("");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //check until required data get
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //get Data
                    storeName = "" + ds.child("StoreName").getValue();
                    storeId = "" + ds.child("storeId").getValue();
                    imgUri = "" + ds.child("image").getValue();
                    storeRating = "" + ds.child("rating").getValue();
                    storeReviews = "" + ds.child("reviews").getValue();
                    deliveryCharges = "" + ds.child("deliveryCharges").getValue();

                    GroceryStore storeItem = new GroceryStore();
                    storeItem.setStoreName(storeName);
                    storeItem.setDeliveryCharges(Integer.parseInt(deliveryCharges));
                    storeItem.setNumOfReviews(Integer.parseInt(storeReviews));
                    storeItem.setRatingValue(Double.parseDouble(storeRating));
                    storeItem.setImgUri(imgUri);

                    featuredRvAdapter.addItem(storeItem);
                    newStoresRvAdapter.addItem(storeItem);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Home.this, "Data loading failed", Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    public Uri rawImageToUri(int imgId){
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + getResources().getResourcePackageName(imgId)
                + '/' + getResources().getResourceTypeName(imgId)
                + '/' + getResources().getResourceEntryName(imgId) );

        return imageUri;
    }

    /*public void populateRvData(){
        adapter = new CategoryRvAdapter(this);

        Product categoryItem = new Product();
        categoryItem.setCategoryName("Cooking Oil");
        categoryItem.setCategoryTypes("Dalda, Sufi, Kisan and others");
        categoryItem.setImgUri(rawImageToUri(R.raw.cooking_oil));

        Product categoryItem2 = new Product();
        categoryItem2.setCategoryName("Dairy Products");
        categoryItem2.setCategoryTypes("Olpers, Yougourt, Milkpack and others");
        categoryItem2.setImgUri(rawImageToUri(R.raw.dairy));

        Product categoryItem3 = new Product();
        categoryItem3.setCategoryName("HouseHolds");
        categoryItem3.setCategoryTypes("washing Powder, Soap, toothpaste etc.");
        categoryItem3.setImgUri(rawImageToUri(R.raw.household));

        Product categoryItem4 = new Product();
        categoryItem4.setCategoryName("Energy Drinks");
        categoryItem4.setCategoryTypes("Sting, Red bull, Rockstar, etc.");
        categoryItem4.setImgUri(rawImageToUri(R.raw.drinks));

        Product categoryItem5 = new Product();
        categoryItem5.setCategoryName("Spices");
        categoryItem5.setCategoryTypes("Salt, Chilli, Masalah, etc.");
        categoryItem5.setImgUri(rawImageToUri(R.raw.spices));

        adapter.addItem(categoryItem);
        adapter.addItem(categoryItem2);
        adapter.addItem(categoryItem3);
        adapter.addItem(categoryItem4);
        adapter.addItem(categoryItem5);

        //Use Linear Layout Manager
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

    }*/
}