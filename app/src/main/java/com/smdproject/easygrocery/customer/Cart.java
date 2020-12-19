package com.smdproject.easygrocery.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class Cart extends AppCompatActivity implements CategoryRvAdapter.OnItemClickListener{


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CategoryRvAdapter adapter;

    private TextView mSubTotal, mDeliveryFee, mTotal;
    private ImageView mBackArrow;
    private Button mPlaceOrder;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cartRv);
        mSubTotal = findViewById(R.id.cart_subtotal);
        mTotal = findViewById(R.id.cart_total);
        mBackArrow = findViewById(R.id.subcategory_back_arrow);
        mDeliveryFee = findViewById(R.id.cart_deliveryFee);
        mPlaceOrder = findViewById(R.id.cart_placeOrderBtn);
        mBackArrow = findViewById(R.id.cart_back_arrow);

        mAuth = FirebaseAuth.getInstance();

        onBackArrowPressed();
        adapter = new CategoryRvAdapter(this, this, "cartRV");
        getCartProducts();
        initFirstRecyclerView();

    }

    public void onPlaceOrder(){
        mPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Cart.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
            }
        });
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

    private void getCartProducts() {

        // get details of currently signed in user from database
        FirebaseUser user = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Cart");
        Query query = databaseReference.child(user.getUid()).orderByChild("itemId");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //check until required data get
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //get Data
                    String itemName = "" + ds.child("itemName").getValue();
                    String itemId = "" + ds.child("itemId").getValue();
                    String price = "" + ds.child("price").getValue();
                    String image = "" + ds.child("image").getValue();
                    String quantity = "" + ds.child("quantity").getValue();

                    CartItem cartItem = new CartItem();
                    cartItem.setItemName(itemName);
                    cartItem.setItemPrice(Double.parseDouble(price));
                    cartItem.setItemId(itemId);
                    cartItem.setImgUri(image);
                    cartItem.setQuantity(Integer.parseInt(quantity));

                    //productsList.add(product);

                    adapter.addCartItem(cartItem);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Cart.this, "Data loading failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(int position, String tag) {

    }
}