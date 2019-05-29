package com.example.navigator;

import classes.MyLocationListener;
import classes.Point;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    private ArrayList<Marker> markers = new ArrayList<>();
    private ArrayList<Point> listPoints = new ArrayList<>();
    private final static String FILE_NAME = "contyyt.txt";
    private String text;

    private static final int REQUEST_LOCATION = 2;
    private static final int REQUEST_FILE = 4;
    GoogleMap map;
    MyLocationListener list = new MyLocationListener();
    private LocationManager locationManager;
    private boolean buttonStatus = true;
    LatLng position;
    Marker myLoc = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAddPoint = findViewById(R.id.PointMenu);
        btnAddPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(".AddPoint");
                startActivity(intent);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(!hasPermissions()) {
            requestPerms();
        }
        else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_FILE);
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_FILE);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 10, list);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 10, list);
    }

    @Override
    protected void onPause() {
        super.onPause();
        list.pause();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        list.setMap(map);
        list.setMyLoc(myLoc);
        list.setPosition(position);
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

        permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
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
        permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, REQUEST_FILE);
        }
    }

    public void onButtonLocationClicked(View v){
        if(list.checkEnabled()) {
            if (buttonStatus) {
                onPause();
                buttonStatus = false;
                Toast.makeText(MainActivity.this, "Navigation is just turned off", Toast.LENGTH_SHORT).show();
            } else {
                onResume();
                buttonStatus = true;
                Toast.makeText(MainActivity.this, "Navigation is just turned on", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(MainActivity.this, "You need to put on your geolocation", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }

    public void onButtonUpdateClicked(View v) throws IOException {
        if(markers.size() != 0) {
            for (int i = 0; i < markers.size(); i++) {
                Marker mrk = markers.get(i);
                mrk.remove();
            }
        }
        openText();
        if(listPoints.get(0).getName().length() != 0){
            text = null;
            if (listPoints.size() == 0) {
                Toast.makeText(MainActivity.this, "Error: file is empty", Toast.LENGTH_SHORT).show();
            }else {
                for (int i = 0; i < listPoints.size(); i++) {
                    markers.add(map.addMarker(new MarkerOptions().position(new LatLng(listPoints.get(i).getLattitude(),
                            listPoints.get(i).getLongtitude())).title(listPoints.get(i).getName())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))));
                    text = text + listPoints.get(i).getFlag() + "\n" + listPoints.get(i).getName() + "\n"
                            + listPoints.get(i).getLattitude() + "\n" + listPoints.get(i).getLongtitude() + "\n";
                }
                //saveText();
            }
        }else {
            Toast.makeText(MainActivity.this, "Error: file is empty", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveText(){

        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(text.getBytes());
        }
        catch(IOException ex) {System.out.println(ex);}
        finally{
            try{
                if(fos!=null)
                    fos.close();
            }
            catch(IOException ex){
                System.out.println(ex);
            }
        }
    }

    public void openText() throws IOException {

        FileInputStream fin = openFileInput(FILE_NAME);
        text = null;
        InputStreamReader reader = new InputStreamReader(fin);
        BufferedReader buffer = new BufferedReader(reader);
        StringBuilder str = new StringBuilder();
        while((text = buffer.readLine()) != null) {
            str.append(text).append(" ");
        }
        String[] arr;
        String name;
        int flag;
        double lattitude;
        double longtitude;
        if(str.length() != 0) {
            arr = str.toString().split("@@@   @@@");
            for (int i = 0; i < arr.length; i++){
                flag = Integer.parseInt(arr[i]);
                i++;
                name = arr[i];
                i++;
                lattitude = Double.parseDouble(arr[i]);
                i++;
                longtitude = Double.parseDouble(arr[i]);
                if(flag == 0) {
                    listPoints.add(new Point(0, name, lattitude, longtitude));
                }else{
                    listPoints.add(new Point(0, name, list.getMyLocationLattitude(), list.getMyLocationLongitude()));
                }
            }
        }
        else{
            Toast.makeText(MainActivity.this, "Error: file", Toast.LENGTH_SHORT).show();
        }
    }
}
