package cakejam.lecture_102615;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

//import com.google.android.gms.maps.*;
//import com.google.android.gms.maps.model.*;
//import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
//import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MyTripsMapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    Toolbar toolbar;
    private ArrayList<LatLng> points;

    LocationManager locationManager;
    String PROVIDER = LocationManager.GPS_PROVIDER;
    Location location;
    CamLocationListener camLL;
    double lat, lng;

    private static final LatLng LOWER_MANHATTAN = new LatLng(40.722543,
            -73.998585);
    private static final LatLng TIMES_SQUARE = new LatLng(40.7577, -73.9857);
    private static final LatLng BROOKLYN_BRIDGE = new LatLng(40.7057, -73.9964);

    private GoogleMap googleMap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if(!isGooglePlayServicesAvailable()) finish();
        setContentView(R.layout.activity_my_trips_maps);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        lat = 40.722543;
        lng = -73.998585;

        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(lat,lng))
                .radius(5)
                .fillColor(Color.BLUE)
                .strokeWidth(1)
                .strokeColor(50);
//        lng = location.getLongitude();
//        lat = location.getLatitude();
        LatLng position = new LatLng(lat,lng);
        camLL = new CamLocationListener(lat,lng);
        mMap.addCircle(circleOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16));
        Log.v("MTM_activity lat: ", Double.toString(lat));
        Log.v("MTM_activity long: ", Double.toString(lng));
        //setSupportActionBar(toolbar);


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

               // ActivityCompat.requestPermissions(this,
               //         new String[]{Manifest.permission.READ_CONTACTS},
               //         MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        location = locationManager.getLastKnownLocation(PROVIDER);

        points = new ArrayList<>();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        checkCallingOrSelfPermission(permission);
        locationManager.removeUpdates(camLL);
    }

    protected void onResume(){
        super.onResume();
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        checkCallingOrSelfPermission(permission);
        locationManager.requestLocationUpdates(PROVIDER,0, 0, camLL);
    }
    private void setUpMapIfNeeded() {
        // check if we have got the googleMap already
        if (googleMap == null) {
            googleMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
            if (googleMap != null) {
                addLines();
            }
        }
    }
    private void addLines() {

        googleMap
                .addPolyline((new PolylineOptions())
                        .add(TIMES_SQUARE, BROOKLYN_BRIDGE, LOWER_MANHATTAN,
                                TIMES_SQUARE).width(5).color(Color.BLUE)
                        .geodesic(true));
        // move camera to zoom on map
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LOWER_MANHATTAN,
                13));
    }





    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        setUpMapIfNeeded();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_Record:
                // User chose the "Settings" item, show the app settings UI...
                Intent recordIntent = new Intent(this, RecordActivity.class);

                startActivity(recordIntent);
                return true;

            case R.id.action_Summary:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Intent summaryIntent = new Intent(this, SummaryActivity.class);
                startActivity(summaryIntent);
                return true;

            case R.id.action_MyTrips:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Intent myTripsIntent = new Intent(this, MyTripsActivity.class);
                startActivity(myTripsIntent);
                return true;
            case R.id.action_Standings:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Intent standingsIntent = new Intent(this, StandingsActivity.class);

                Bundle myBundle = new Bundle();
                myBundle.putInt("x",5);
                myBundle.putString("str", "hello");
                standingsIntent.putExtras(myBundle);

                startActivity(standingsIntent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
