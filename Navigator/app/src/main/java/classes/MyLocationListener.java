package classes;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

public class MyLocationListener implements LocationListener {

    private double myLocationLongitude;
    private double myLocationLattitude;
    private LocationManager locationManager;


    public MyLocationListener(double myLocationLattitude, double myLocationLongitude) {
        this.myLocationLongitude = myLocationLongitude;
        this.myLocationLattitude = myLocationLattitude;
    }

    public MyLocationListener() {
        this.myLocationLongitude = 0;
        this.myLocationLattitude = 0;
    }

    public void setLocationManager(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    public double getMyLocationLongitude() {
        return myLocationLongitude;
    }

    public double getMyLocationLattitude() {
        return myLocationLattitude;
    }

    public void start(){
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
//        }
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 10, this);
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 10, 10, this);
        checkEnabled();
    }

    public void pause(){
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        showLocation(location);
    }

    @Override
    public void onProviderDisabled(String provider) {
        checkEnabled();
    }

    @Override
    public void onProviderEnabled(String provider) {
        checkEnabled();
        //showLocation(locationManager.getLastKnownLocation(provider));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    private void showLocation(Location location) {
        if (location == null)
            return;
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            myLocationLattitude = location.getLatitude();
            myLocationLongitude = location.getLongitude();
        } else if (location.getProvider().equals(
                LocationManager.NETWORK_PROVIDER)) {
            myLocationLattitude = location.getLatitude();
            myLocationLongitude = location.getLongitude();
        }
    }

    public void checkEnabled() {
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
        !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) return;
    }
}