package com.smdproject.easygrocery.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smdproject.easygrocery.R;
import com.smdproject.easygrocery.adapters.RecyclerViewAdapter;
import com.smdproject.easygrocery.adapters.SliderAdapter;
import com.smdproject.easygrocery.models.GroceryStore;
import com.smdproject.easygrocery.models.SliderItem;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class Home extends AppCompatActivity implements RecyclerViewAdapter.OnItemListener {


    private SliderView sliderView;
    private SliderAdapter sliderAdapter;
    private RecyclerView mFeaturedRv, mNewStoresRv;
    private RecyclerViewAdapter featuredRvAdapter, newStoresRvAdapter;
    private RecyclerView.LayoutManager layoutManager, layoutManager2;

    private DatabaseReference databaseReference, databaseReference2;
    private StorageReference storageReference;

    private String storeName, deliveryCharges, storeRating, storeReviews, storeId, imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sliderView = findViewById(R.id.imageSlider);
        mFeaturedRv = findViewById(R.id.home_featuredRv);
        mNewStoresRv = findViewById(R.id.home_newStoresRv);

        databaseReference = FirebaseDatabase.getInstance().getReference("Stores");
        databaseReference2 = FirebaseDatabase.getInstance().getReference("Products");

        featuredRvAdapter = new RecyclerViewAdapter(this, this, "featured");
        newStoresRvAdapter = new RecyclerViewAdapter(this, this, "newStores");

        /*for(int i=0; i<8; i++){
            //when user Registered store user info in realtime Database too
            storeId = String.valueOf(i + 100);
            HashMap<Object, String> userHashMap = new HashMap<>();
            userHashMap.put("storeId", storeId);
            //userHashMap.put("uid", uId);
            userHashMap.put("image", "");
            userHashMap.put("StoreName", "");
            userHashMap.put("deliveryCharges", "");
            userHashMap.put("rating", "");
            userHashMap.put("reviews", "");

            databaseReference.child(storeId).setValue(userHashMap);
        }*/

        /*for(int i=0; i<5; i++){
            //when user Registered store user info in realtime Database too
            storeId = String.valueOf(i + 100);

            for(int j=0; j < 5; j++){
                String categoryId = String.valueOf(j + 1000);

                for(int k =0; k<4 ; k++){
                    String categoryId2 = String.valueOf(k + 10000);
                    HashMap<Object, String> userHashMap = new HashMap<>();
                    userHashMap.put("itemId", categoryId2);
                    userHashMap.put("itemName", "");
                    userHashMap.put("image", "");
                    userHashMap.put("description", "");
                    userHashMap.put("price", "");

                    databaseReference2.child(storeId).child(categoryId).child(categoryId2).setValue(userHashMap);
                }

            }

        }*/


        populateSliderData();
        //populateRvData();
        getStoresDetails();

        //Set slider adapter, cycle delay time and auto scroll
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();
    }

    public Uri rawImageToUri(int imgId){
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + getResources().getResourcePackageName(imgId)
                + '/' + getResources().getResourceTypeName(imgId)
                + '/' + getResources().getResourceEntryName(imgId) );

        return imageUri;
    }

    /**
     * This function populate sample data in the Slider adapter.
     * */
    public void populateSliderData(){
        sliderAdapter = new SliderAdapter(this);

        SliderItem sliderItem = new SliderItem();
        sliderItem.setDescription("D Watson");
        sliderItem.setImageUri(rawImageToUri(R.raw.img1));

        SliderItem sliderItem2 = new SliderItem();
        sliderItem2.setDescription("Madni Cash & Carry");
        sliderItem2.setImageUri(rawImageToUri(R.raw.img2));

        SliderItem sliderItem3 = new SliderItem();
        sliderItem3.setDescription("Super Mart");
        sliderItem3.setImageUri(rawImageToUri(R.raw.img3));

        sliderAdapter.addItem(sliderItem);
        sliderAdapter.addItem(sliderItem2);
        sliderAdapter.addItem(sliderItem3);
    }

    private void getStoresDetails() {

        initFirstRecyclerView();
        initSecondRecyclerView();

        // get details of currently signed in user from database
        Query query = databaseReference.orderByChild("storeId");
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
    }

    private void initFirstRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        try {
            mFeaturedRv.setLayoutManager(layoutManager);
            mFeaturedRv.setAdapter(featuredRvAdapter);
        } catch (Exception e) {
            System.out.println("ERROR:  " + e);
        }
    }

    private void initSecondRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        try {
            mNewStoresRv.setLayoutManager(layoutManager);
            mNewStoresRv.setAdapter(newStoresRvAdapter);
        } catch (Exception e) {
            System.out.println("ERROR:  " + e);
        }
    }

    @Override
    public void onItemClick(int position, String tag) {

        Toast.makeText(this, "Featured Clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Home.this, SingleStore.class);
        intent.putExtra("storeName", storeName);
        intent.putExtra("storeDeliveryCharges", deliveryCharges);
        intent.putExtra("storeRating", storeRating);
        intent.putExtra("storeReviews", storeReviews);
        intent.putExtra("image", imgUri);
        intent.putExtra("storeId", storeId);
        startActivity(intent);

    }
}