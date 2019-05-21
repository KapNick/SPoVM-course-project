package classes;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

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
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    public void showLocation(Location location) {
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

    private boolean checkEnabled() {
        if(
        !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
        !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        ) return false;
        return true;
    }
}