package com.smdproject.easygrocery.customer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smdproject.easygrocery.R;
import com.smdproject.easygrocery.adapters.CategoryRvAdapter;
import com.smdproject.easygrocery.models.CartItem;
import com.smdproject.easygrocery.models.Product;
import com.smdproject.easygrocery.models.ProductCategory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class SingleStoreSubcategory extends AppCompatActivity implements CategoryRvAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CategoryRvAdapter adapter;

    private TextView mStoreName, mCategory, mCartCount;
    private ImageView mBackArrow;
    private ImageButton mCart;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListner;

    boolean flag = false;

    private String categoryName, categoryId, storeId, storeName;

    private ArrayList<Product> productsList;
    private ArrayList<CartItem> cartItemsList;
    int count =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_store_subcategory);

        recyclerView = findViewById(R.id.subcategory_Rv);
        mStoreName = findViewById(R.id.subcategory_actionbar_text);
        mCategory = findViewById(R.id.subcategory_categoryTv);
        mBackArrow = findViewById(R.id.subcategory_back_arrow);
        mCart = findViewById(R.id.subcategory_cartImgBtn);
        mCartCount = findViewById(R.id.subcategory_cartCount);

        mAuth = FirebaseAuth.getInstance();
        authStateListener();

        mCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSelectedItemsToCart();
                Intent intent = new Intent(SingleStoreSubcategory.this, Cart.class);
                startActivity(intent);
            }
        });

        productsList = new ArrayList<>();
        cartItemsList = new ArrayList<>();

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

    @Override
    public void onItemClick(int position, String tag) {
        if(flag){
            count++;
            mCartCount.setText(String.valueOf(count));

            CartItem cartItem = new CartItem();
            cartItem.setImgUri(productsList.get(position).getImgUri());
            cartItem.setItemId(productsList.get(position).getItemId());
            cartItem.setItemName(productsList.get(position).getItemName());
            cartItem.setItemPrice(productsList.get(position).getItemPrice());
            cartItem.setQuantity(1);

            cartItemsList.add(cartItem);
        }else{
            showDialogue();
        }
    }

    public void addSelectedItemsToCart(){
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Cart");

        for(int i=0; i<cartItemsList.size(); i++){
            HashMap<Object, String> userHashMap = new HashMap<>();
            userHashMap.put("itemId", cartItemsList.get(i).getItemId());
            userHashMap.put("itemName", cartItemsList.get(i).getItemName());
            userHashMap.put("image", cartItemsList.get(i).getImgUri());
            userHashMap.put("price", String.valueOf(cartItemsList.get(i).getItemPrice()));
            userHashMap.put("quantity", String.valueOf(cartItemsList.get(i).getQuantity()));

            reference.child(uid).child(cartItemsList.get(i).getItemId()).setValue(userHashMap);
        }

    }


    @SuppressLint("ResourceType")
    private void showDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SingleStoreSubcategory.this);
        builder.setTitle("To add items to cart You must need to Login first.");

        //set Layout of dailogue
        LinearLayout layout = new LinearLayout(SingleStoreSubcategory.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        //add in dailogue
        builder.setView(layout);

        //add buttons in dailogue
        builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(SingleStoreSubcategory.this, Login.class);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //progressDialog.dismiss();
            }
        });

        builder.create().show();
    }
}