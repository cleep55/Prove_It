package cakejam.lecture_102615;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by WLEEPER on 11/21/2015.
 */
public class CamLocationListener implements LocationListener {
    double latVal;
    double lngVal;

    CamLocationListener(double lat_in, double long_in){
        latVal= lat_in;
        lngVal= long_in;
    }


    @Override
    public void onLocationChanged(Location location) {
        if(location == null){
            //Toast.makeText(, "Your Location is not found"),0).show();
        }
        else{
            latVal = location.getLatitude();
            lngVal = location.getLongitude();
            LatLng position = new LatLng(latVal, lngVal);
            Log.v("lat: ", Double.toString(latVal));
            Log.v("long: ", Double.toString(lngVal));
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
