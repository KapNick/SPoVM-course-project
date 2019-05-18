package classes;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class MyLocationListener implements LocationListener {

    private double myLocationLongitude;
    private double myLocationLattitude;

    public MyLocationListener(double myLocationLattitude, double myLocationLongitude) {
        this.myLocationLongitude = myLocationLongitude;
        this.myLocationLattitude = myLocationLattitude;
    }

    public MyLocationListener() {
        this.myLocationLongitude = 0;
        this.myLocationLattitude = 0;
    }

    public double getMyLocationLongitude() {
        return myLocationLongitude;
    }

    public double getMyLocationLattitude() {
        return myLocationLattitude;
    }

    @Override
    public void onLocationChanged(Location loc) {
        if (loc.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            myLocationLongitude = loc.getLongitude();
            myLocationLattitude = loc.getLatitude();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}