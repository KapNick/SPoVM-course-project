package classes;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.concurrent.Semaphore;

public class MyLocationListener implements LocationListener {

    private double myLocationLongitude;
    private double myLocationLattitude;
    private LocationManager locationManager;
    Semaphore sem;


    public MyLocationListener(double myLocationLattitude, double myLocationLongitude) {
        this.myLocationLongitude = myLocationLongitude;
        this.myLocationLattitude = myLocationLattitude;
    }

    public MyLocationListener() {
        this.myLocationLongitude = 0;
        this.myLocationLattitude = 0;
    }

    public void setSem(Semaphore sem) {
        this.sem = sem;
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

    public void pause(){
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
//        try {
//            sem.acquire();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        showLocation(location);
//        sem.release();
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

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

    public boolean checkEnabled() {
        if(
        !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
        !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        ) return false;
        return true;
    }
}