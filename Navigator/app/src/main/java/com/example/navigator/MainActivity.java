package com.example.navigator;

import classes.MyLocationListener;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.concurrent.Semaphore;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION = 2;
    GoogleMap map;
    MyLocationListener list = new MyLocationListener();
    private LocationManager locationManager;
    private boolean buttonStatus = false;
    LatLng position;
    Semaphore sem = new Semaphore(1, true);
    Marker myLoc = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        list.setSem(sem);
        if(!hasPermissions()) {
            requestPerms();
        }
        else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
            }
            list.showLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        list.setLocationManager(locationManager);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 10, list);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 10, 10, list);
    }

    @Override
    protected void onPause() {
        super.onPause();
        list.pause();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        position = new LatLng(list.getMyLocationLattitude(), list.getMyLocationLongitude());
        myLoc = map.addMarker(new MarkerOptions().position(position).title("You here"));
        map.moveCamera(CameraUpdateFactory.newLatLng(position));
    }

    private boolean hasPermissions() {
        int res;
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};

        for (String perms : permissions) {
            res = checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)) {
                return false;
            }
        }

        permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION};
        for (String perms : permissions) {
            res = checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)) {
                return false;
            }
        }

        return true;
    }

    private void requestPerms() {
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, REQUEST_LOCATION);
        }
        permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, REQUEST_LOCATION);
        }
    }

    public void onButtonLocationClicked(View v){
        if(list.checkEnabled()) {
            if (buttonStatus) {
                onPause();
                buttonStatus = false;
                position = new LatLng(list.getMyLocationLattitude(), list.getMyLocationLongitude());
                myLoc = map.addMarker(new MarkerOptions().position(position).title("You here"));
                map.moveCamera(CameraUpdateFactory.newLatLng(position));
            } else {
                onResume();
                buttonStatus = true;
                myLoc.remove();
            }
        }else{
            startActivity(new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }
}
