package com.smdproject.easygrocery.rider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.smdproject.easygrocery.R;

public class IncomingRide extends AppCompatActivity {
    Button accept,reject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_ride);
        accept=findViewById(R.id.accept);
        reject=findViewById(R.id.reject);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intt1 = new Intent(IncomingRide.this, CurrentlyDelivering.class);
                startActivity(intt1);
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intt1 = new Intent(IncomingRide.this, RiderMain.class);
                startActivity(intt1);
            }
        });

    }
}