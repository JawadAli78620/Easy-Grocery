package com.smdproject.easygrocery.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.smdproject.easygrocery.R;

import java.util.Map;

public class Maps extends AppCompatActivity implements OnMapReadyCallback{

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient fusedClient;
    GoogleMap googleMap;

    boolean locationPermissionGranted = false;
    private Location lastKnownLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

        supportMapFragment.getMapAsync(this);
        //initiallized fused location
        fusedClient = LocationServices.getFusedLocationProviderClient(this);

//        //Check permissions
//        if (ActivityCompat.checkSelfPermission(Maps.this,
//                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//
//            //get Current Location
//            getCurrentLocation();
//        }else{
//            ActivityCompat.requestPermissions(Maps.this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44) ;
//            ActivityCompat.requestPermissions(Maps.this,
//                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 50);
//        }


    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if(requestCode == 44){
//            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                getCurrentLocation();
//            }
//        }else if(requestCode == 50){
//            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                getCurrentLocation();
//            }
//        }
//    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            System.out.println("In Permissions: ");
            return;
        }

        System.out.println("Before Task..");
        Task<Location> task = fusedClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                if(location != null){
                    System.out.println("Inside Success listener..");
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            LatLng latLng = new LatLng(location.getLatitude(), location.getAltitude());
                            MarkerOptions options = new MarkerOptions().position(latLng).title("Your Location");
                            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 50));
                            googleMap.addMarker(options);
                        }
                    });
                }
            }
        });

    }

    private boolean getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        boolean result = ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case 40: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    boolean cameraAccepted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
                    boolean writeStorageAccepted = (grantResults[1] == PackageManager.PERMISSION_GRANTED);

                    if (cameraAccepted && writeStorageAccepted) {
                        //permission enabled
                        //updateLocationUI();
                        locationPermissionGranted = true;
                    } else {
                        //Permission denied
                        Toast.makeText(this, "please enable Camera & storage Permission", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }
        updateLocationUI();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        updateLocationUI();

        getDeviceLocation();
    }

    public void updateLocationUI(){
        if (this.googleMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                this.googleMap.setMyLocationEnabled(true);
                this.googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                this.googleMap.setMyLocationEnabled(false);
                this.googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                //lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), 50));
                            }
                        } else {
//                            Log.d(TAG, "Current location is null. Using defaults.");
//                            Log.e(TAG, "Exception: %s", task.getException());
//                            googleMap.moveCamera(CameraUpdateFactory
//                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }
}