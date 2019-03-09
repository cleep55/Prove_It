package cakejam.lecture_102615;
/* -----------------------------------------------------------------------------------------
 * ---------------------------- BELOW provided with map template ---------------------------
 * -----------------------------------------------------------------------------------------*/
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/* -----------------------------------------------------------------------------------------
 * ---------------------------- ABOVE provided with map template ---------------------------
 * -----------------------------------------------------------------------------------------*/


public class MyTripsMapsActivity extends AppCompatActivity/*FragmentActivity*/ implements OnMapReadyCallback {

    private GoogleMap mMap;
    RecordActivity recordActivity = new RecordActivity();
    GridLayout trip1Button, trip2Button, trip3Button, trip4Button, trip5Button;


    ArrayList<Trip> trips = recordActivity.trips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* -----------------------------------------------------------------------------------------
         * ---------------------------- BELOW provided with map template ---------------------------
         * -----------------------------------------------------------------------------------------*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trips_maps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        /* -----------------------------------------------------------------------------------------
         * ---------------------------- ABOVE provided with map template ---------------------------
         * -----------------------------------------------------------------------------------------*/

        TextView date1 = (TextView) findViewById(R.id.b1_date);
        TextView date2 = (TextView) findViewById(R.id.b2_date);
        TextView date3 = (TextView) findViewById(R.id.b3_date);
        TextView date4 = (TextView) findViewById(R.id.b4_date);
        TextView date5 = (TextView) findViewById(R.id.b5_date);

        TextView overall1 = (TextView) findViewById(R.id.b1_overall);
        TextView overall2 = (TextView) findViewById(R.id.b2_overall);
        TextView overall3 = (TextView) findViewById(R.id.b3_overall);
        TextView overall4 = (TextView) findViewById(R.id.b4_overall);
        TextView overall5 = (TextView) findViewById(R.id.b5_overall);

        TextView triptime1 = (TextView) findViewById(R.id.b1_triptime);
        TextView triptime2 = (TextView) findViewById(R.id.b2_triptime);
        TextView triptime3 = (TextView) findViewById(R.id.b3_triptime);
        TextView triptime4 = (TextView) findViewById(R.id.b4_triptime);
        TextView triptime5 = (TextView) findViewById(R.id.b5_triptime);

        TextView speed1 = (TextView) findViewById(R.id.b1_speed);
        TextView speed2 = (TextView) findViewById(R.id.b2_speed);
        TextView speed3 = (TextView) findViewById(R.id.b3_speed);
        TextView speed4 = (TextView) findViewById(R.id.b4_speed);
        TextView speed5 = (TextView) findViewById(R.id.b5_speed);

        TextView brake1 = (TextView) findViewById(R.id.b1_brake);
        TextView brake2 = (TextView) findViewById(R.id.b2_brake);
        TextView brake3 = (TextView) findViewById(R.id.b3_brake);
        TextView brake4 = (TextView) findViewById(R.id.b4_brake);
        TextView brake5 = (TextView) findViewById(R.id.b5_brake);

        TextView corner1 = (TextView) findViewById(R.id.b1_corner);
        TextView corner2 = (TextView) findViewById(R.id.b2_corner);
        TextView corner3 = (TextView) findViewById(R.id.b3_corner);
        TextView corner4 = (TextView) findViewById(R.id.b4_corner);
        TextView corner5 = (TextView) findViewById(R.id.b5_corner);

        trip1Button = (GridLayout) findViewById(R.id.trip1);
        trip2Button = (GridLayout) findViewById(R.id.trip2);
        trip3Button = (GridLayout) findViewById(R.id.trip3);
        trip4Button = (GridLayout) findViewById(R.id.trip4);
        trip5Button = (GridLayout) findViewById(R.id.trip5);

        if(RecordActivity.trips.size()>=1 && !RecordActivity.trips.get(trips.size()-1).getStartTime().equals(null)){
            date1.setText(RecordActivity.trips.get(trips.size()-1).getStartTime());
            overall1.setText("Overall: "+String.format("%.2f", RecordActivity.trips.get(trips.size() - 1).getTripTotalScore()));
            triptime1.setText(RecordActivity.trips.get(trips.size()-1).getTripDuration());
            speed1.setText("Speed: "+String.format("%.2f", RecordActivity.trips.get(trips.size() - 1).getSpeedScore()));
            brake1.setText("Brake: "+String.format("%.2f", RecordActivity.trips.get(trips.size()-1).getBrakeScore()));
            corner1.setText("Corner: " + String.format("%.2f", RecordActivity.trips.get(trips.size() - 1).getCornerScore()));
        }

        if(RecordActivity.trips.size()>=2 && !RecordActivity.trips.get(trips.size() - 2).getStartTime().equals(null)) {
            date2.setText(RecordActivity.trips.get(trips.size()-2).getStartTime());
            overall2.setText("Overall: "+String.format("%.2f", RecordActivity.trips.get(trips.size() - 2).getTripTotalScore()));
            triptime2.setText(RecordActivity.trips.get(trips.size()-1).getTripDuration());
            speed2.setText("Speed: "+String.format("%.2f", RecordActivity.trips.get(trips.size() - 2).getSpeedScore()));
            brake2.setText("Brake: "+String.format("%.2f", RecordActivity.trips.get(trips.size()-2).getBrakeScore()));
            corner2.setText("Corner: "+String.format("%.2f",RecordActivity.trips.get(trips.size()-2).getCornerScore()));
        }

        if(RecordActivity.trips.size()>=3 && !RecordActivity.trips.get(trips.size()-3).getStartTime().equals(null)){
            date3.setText(RecordActivity.trips.get(trips.size() - 3).getStartTime());
            overall3.setText("Overall: "+String.format("%.2f", RecordActivity.trips.get(trips.size() - 3).getTripTotalScore()));
            triptime3.setText(RecordActivity.trips.get(trips.size()-3).getTripDuration());
            speed3.setText("Speed: "+String.format("%.2f", RecordActivity.trips.get(trips.size() - 3).getSpeedScore()));
            brake3.setText("Brake: "+String.format("%.2f", RecordActivity.trips.get(trips.size()-3).getBrakeScore()));
            corner3.setText("Corner: "+String.format("%.2f",RecordActivity.trips.get(trips.size()-3).getCornerScore()));
        }

        if(RecordActivity.trips.size()>=4 && !RecordActivity.trips.get(trips.size()-4).getStartTime().equals(null)){
            date4.setText(RecordActivity.trips.get(trips.size()-4).getStartTime());
            overall4.setText("Overall: "+String.format("%.2f", RecordActivity.trips.get(trips.size() - 4).getTripTotalScore()));
            triptime4.setText(RecordActivity.trips.get(trips.size()-4).getTripDuration());
            speed4.setText("Speed: "+String.format("%.2f", RecordActivity.trips.get(trips.size() - 4).getSpeedScore()));
            brake4.setText("Brake: "+String.format("%.2f", RecordActivity.trips.get(trips.size()-4).getBrakeScore()));
            corner4.setText("Corner: "+String.format("%.2f",RecordActivity.trips.get(trips.size()-4).getCornerScore()));
        }

        if (RecordActivity.trips.size()>=5 && !RecordActivity.trips.get(trips.size()-5).getStartTime().equals(null)) {
            date5.setText(RecordActivity.trips.get(trips.size()-5).getStartTime());
            overall5.setText("Overall: "+String.format("%.2f", RecordActivity.trips.get(trips.size() - 5).getTripTotalScore()));
            triptime5.setText(RecordActivity.trips.get(trips.size()-5).getTripDuration());
            speed5.setText("Speed: " + String.format("%.2f", RecordActivity.trips.get(trips.size() - 5).getSpeedScore()));
            brake5.setText("Brake: "+String.format("%.2f", RecordActivity.trips.get(trips.size()-5).getBrakeScore()));
            corner5.setText("Corner: "+String.format("%.2f",RecordActivity.trips.get(trips.size()-5).getCornerScore()));
        }

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
        /* -----------------------------------------------------------------------------------------
         * ---------------------------- BELOW provided with map template ---------------------------
         * -----------------------------------------------------------------------------------------*/
        mMap = googleMap;
        if(mMap!=null) {
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            mMap.setMyLocationEnabled(true);
            mMap.animateCamera(CameraUpdateFactory.zoomBy(5));
        }

//        /* -----------------------------------------------------------------------------------------
//         * ---------------------------- ABOVE provided with map template ---------------------------
//         * -----------------------------------------------------------------------------------------*/


        // SETTING MAP TYPES
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void buttonHandler(View v) {
//        Intent myIntent = new Intent(this, SummaryActivity.class);
//
//
//        Bundle myBundle = new Bundle();
//        myBundle.putInt("x",5);
//        myBundle.putString("str","hello");
//        myIntent.putExtras(myBundle);
//
//        startActivity(myIntent);
        if(v.getId() == trip1Button.getId() && RecordActivity.trips.size()>=1){
            mMap.clear();
            populateTripOnMap(trips.get(trips.size() - 1), mMap);
        }
        else if(v.getId() == trip2Button.getId() && RecordActivity.trips.size()>=2){
            mMap.clear();
            populateTripOnMap(trips.get(trips.size()-2), mMap);
        }
        else if(v.getId() == trip3Button.getId() && RecordActivity.trips.size()>=3){
            mMap.clear();
            populateTripOnMap(trips.get(trips.size()-3), mMap);
        }
        else if(v.getId() == trip4Button.getId() && RecordActivity.trips.size()>=4){
            mMap.clear();
            populateTripOnMap(trips.get(trips.size()-4), mMap);
        }
        else if(v.getId() == trip5Button.getId()&&RecordActivity.trips.size()>=5){
                mMap.clear();
                populateTripOnMap(trips.get(trips.size() - 5), mMap);


        }
    }

    public void populateTripOnMap(Trip trip, GoogleMap map){
        map.clear();
        for(int i=0;i<trip.getLatPoints().size();i++){
            LatLng currMarker = new LatLng(trip.getLatPoints().get(i),trip.getLongPoints().get(i));
            //map.addMarker(new MarkerOptions().position(currMarker));
            if(trip.getDeductionType().get(i)== recordActivity.speedDeduction)
            {
                //create a speed deduction marker
                map.addMarker(new MarkerOptions()
                        .title(trip.getDeductionType().get(i)+"\n"+trip.getTimePoints().get(i))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        .position(currMarker)).showInfoWindow();
            }
            else if(trip.getDeductionType().get(i)== recordActivity.brakeDeduction)
            {
                //create a brake deduction marker
                map.addMarker(new MarkerOptions()
                        .title(trip.getDeductionType().get(i))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        .position(currMarker)).showInfoWindow();
            }
            else if(trip.getDeductionType().get(i)== recordActivity.cornerDeduction)
            {
                //create a corner deduction marker
                map.addMarker(new MarkerOptions()
                        .title(trip.getDeductionType().get(i))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                        .position(currMarker)).showInfoWindow();
            }
            else{
                map.addMarker(new MarkerOptions()
                        .title(trip.getDeductionType().get(i))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                        .position(currMarker)).showInfoWindow();
            }

            map.moveCamera(CameraUpdateFactory.newLatLng(currMarker));
            map.animateCamera(CameraUpdateFactory.zoomBy(10));
        }

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

            /*case R.id.action_MyTrips:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Intent myTripsIntent = new Intent(this, MyTripsActivity.class);
                startActivity(myTripsIntent);
                return true;*/
            case R.id.action_Standings:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Intent standingsIntent = new Intent(this, StandingsActivity.class);

//                Bundle myBundle = new Bundle();
//                myBundle.putInt("x",5);
//                myBundle.putString("str", "hello");
//                standingsIntent.putExtras(myBundle);

                startActivity(standingsIntent);
                return true;
            case R.id.action_MyTripsMaps:
                // User chose the "Settings" item, show the app settings UI...
                Intent myTripsMapIntent = new Intent(this, MyTripsMapsActivity.class);
                startActivity(myTripsMapIntent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
