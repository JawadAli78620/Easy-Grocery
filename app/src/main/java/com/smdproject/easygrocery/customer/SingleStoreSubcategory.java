package com.smdproject.easygrocery.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;

import com.smdproject.easygrocery.R;
import com.smdproject.easygrocery.adapters.CategoryRvAdapter;
import com.smdproject.easygrocery.models.Product;

public class SingleStoreSubcategory extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CategoryRvAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_store_subcategory);

        recyclerView = findViewById(R.id.subcategory_Rv);

        //populateRvData();
    }

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
        categoryItem.setCategoryName("Dalda cooking oil");
        categoryItem.setCategoryTypes("Rs. 250.0/kg");
        categoryItem.setImgUri(rawImageToUri(R.raw.cooking_oil));

        Product categoryItem2 = new Product();
        categoryItem2.setCategoryName("Sufi cooking oil");
        categoryItem2.setCategoryTypes("Rs. 250.0/kg");
        categoryItem2.setImgUri(rawImageToUri(R.raw.dairy));

        Product categoryItem3 = new Product();
        categoryItem3.setCategoryName("Kisan cooking oil");
        categoryItem3.setCategoryTypes("Rs. 250.0/kg");
        categoryItem3.setImgUri(rawImageToUri(R.raw.household));

        Product categoryItem4 = new Product();
        categoryItem4.setCategoryName("Dalda cooking oil");
        categoryItem4.setCategoryTypes("Rs. 250.0/kg");
        categoryItem4.setImgUri(rawImageToUri(R.raw.drinks));

        Product categoryItem5 = new Product();
        categoryItem5.setCategoryName("Kisan cooking oil");
        categoryItem5.setCategoryTypes("Rs. 250.0/kg");
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