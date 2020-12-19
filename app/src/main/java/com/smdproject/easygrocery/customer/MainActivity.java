package com.smdproject.easygrocery.customer;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnItemListener {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    private ImageView menuIv;
    private TextView mNavHeaderTxt;
    private TextView mCurrentLocation, mLocation;

    private SliderView sliderView;
    private SliderAdapter sliderAdapter;
    private RecyclerView mFeaturedRv, mNewStoresRv;
    private RecyclerViewAdapter featuredRvAdapter, newStoresRvAdapter;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListner;

    private String storeName, deliveryCharges, storeRating, storeReviews, storeId, imgUri;

    private ArrayList<GroceryStore> storesList;

    boolean flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        menuIv = findViewById(R.id.home_menuIv);

        sliderView = findViewById(R.id.imageSlider);
        mFeaturedRv = findViewById(R.id.home_featuredRv);
        mNewStoresRv = findViewById(R.id.home_newStoresRv);
        mCurrentLocation = findViewById(R.id.home_currentLocationTv);
        mLocation = findViewById(R.id.home_locationTv);

        storesList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Stores");
        mAuth = FirebaseAuth.getInstance();

        featuredRvAdapter = new RecyclerViewAdapter(this, this, "featured");
        newStoresRvAdapter = new RecyclerViewAdapter(this, this, "newStores");

        authStateListener();
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

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_orders, R.id.nav_help, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();

        menuIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(! drawer.isDrawerOpen(GravityCompat.START)){
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        setCurrentLocation();
        setUpDrawerContent(navigationView);
        customizeDrawerHeader();
    }

    private void setCurrentLocation() {
        mCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Maps.class);
                startActivity(intent);
            }
        });

    }

    private void setUpDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.nav_logout:
                        logOut();
                        break;
                    case R.id.nav_help:
                        Toast.makeText(MainActivity.this, "Help is pressed", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_orders:
                        Toast.makeText(MainActivity.this, "All Orders", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }

    private void logOut() {
        try {
            if (flag){
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, Login.class));
                finish();
            }else {
                Toast.makeText(MainActivity.this, "You Need to login first..", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Error Logout, Kindly check your Internet connection..", Toast.LENGTH_SHORT).show();
        }
        return;
    }

    public void authStateListener(){
        firebaseAuthStateListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    flag = true;
                    return;
                }
                else{
                    flag = false;
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListner);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListner);
    }

    public void customizeDrawerHeader(){
        //========================== Header Customization ============================================

        View headerView = navigationView.getHeaderView(0);
        mNavHeaderTxt = headerView.findViewById(R.id.nav_header_text);

        mNavHeaderTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*drawer.closeDrawer(GravityCompat.START);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, new ProfileFragment());
                transaction.commit();*/

                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                //overridePendingTransition(R.anim.slide_out_down, R.anim.slide_in_up);
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
                    storeItem.setStoreId(storeId);

                    storesList.add(storeItem);

                    featuredRvAdapter.addItem(storeItem);
                    newStoresRvAdapter.addItem(storeItem);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Data loading failed", Toast.LENGTH_SHORT).show();
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

        Intent intent = new Intent(MainActivity.this, SingleStore.class);
        intent.putExtra("storeName", storesList.get(position).getStoreName());
        intent.putExtra("storeDeliveryCharges", storesList.get(position).getDeliveryCharges() );
        intent.putExtra("storeRating", storesList.get(position).getRatingValue());
        intent.putExtra("storeReviews", storesList.get(position).getNumOfReviews());
        intent.putExtra("image", storesList.get(position).getImgUri());
        intent.putExtra("storeId", storesList.get(position).getStoreId());
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        if( drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
        return true;
    }
}


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

