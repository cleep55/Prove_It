package cakejam.lecture_102615;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.openxc.VehicleManager;
import com.openxc.measurements.EngineSpeed;
import com.openxc.measurements.Measurement;
import com.openxc.measurements.VehicleSpeed;
import com.openxc.measurements.WindshieldWiperStatus;
import com.openxc.messages.Command;
import com.openxc.messages.SimpleVehicleMessage;
import com.openxc.messages.VehicleMessage;

import java.util.ArrayList;

public class SummaryActivity extends AppCompatActivity {


    private static final String TAG = "SummaryActivity";

    private VehicleManager mVehicleManager;
    //private TextView mEngineSpeedView;
    //private TextView mVehicleSpeedView;
    private TextView pbAccelTV, pbBrakeTV, pbCornerTV, overallScoreTV,fuelTV;
    private Measurement mrMeasure;

    LineChart mpChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // INITIALIZING TEXT VIEWS
        pbAccelTV = (TextView) findViewById(R.id.pbAccelTV);
        pbBrakeTV = (TextView) findViewById(R.id.pbBrakeTV);
        pbCornerTV = (TextView) findViewById(R.id.pbCornerTV);
        overallScoreTV = (TextView) findViewById(R.id.OverallScore);
        fuelTV = (TextView) findViewById(R.id.pbFuelTV);

        // INITIALIZING PROGRESS BARS to their respective object in the layout
        ProgressBar pbAccel = (ProgressBar) findViewById(R.id.pb_accel);
        ProgressBar pbBrake = (ProgressBar) findViewById(R.id.pb_brake);
        ProgressBar pbCorner = (ProgressBar) findViewById(R.id.pb_corner);
        ProgressBar pbFuel = (ProgressBar) findViewById(R.id.pb_fuel_efficiency);
        ProgressBar pbLKA = (ProgressBar) findViewById(R.id.pb_LKA);
        ProgressBar pbDistFromObj = (ProgressBar) findViewById(R.id.pb_dist_from_objects);
        //ProgressBar pbBlindSpot = (ProgressBar) findViewById(R.id.pb_BLIS);

        if(RecordActivity.trips.size()>=1){
            // SETTING PROGRESS BAR VALUES to the driver's overall scores in each category
            pbAccel.setProgress((int) RecordActivity.trips.get(0).getDriverSpeedScore(RecordActivity.trips));
            pbBrake.setProgress((int) RecordActivity.trips.get(0).getDriverBrakeScore(RecordActivity.trips));
            pbCorner.setProgress((int) RecordActivity.trips.get(0).getDriverCornerScore(RecordActivity.trips));
            pbFuel.setProgress((int) RecordActivity.trips.get(0).getDriverFuelScore(RecordActivity.trips));
            pbLKA.setProgress(79);
            pbDistFromObj.setProgress(88);

            // OBTAINING OVERALL SCORE
            double driverOverallScore = RecordActivity.trips.get(0).getDriverTotalScore(RecordActivity.trips);

            // CREATING STRINGS TO PASSED TO THE TEXT VIEWS
            String overallScoreString = String.format("Overall Score\n%.2f", driverOverallScore);
            String accelString = String.format("Acceleration: %.2f", RecordActivity.trips.get(0).getDriverSpeedScore(RecordActivity.trips));
            String brakeString = String.format("Brake: %.2f", RecordActivity.trips.get(0).getDriverBrakeScore(RecordActivity.trips));
            String cornerString = String.format("Corner: %.2f", RecordActivity.trips.get(0).getDriverCornerScore(RecordActivity.trips));
            String fuelString = String.format("Fuel: %.2f", RecordActivity.trips.get(0).getDriverFuelScore(RecordActivity.trips));

            // PASSING STRINGS INTO THE TEXT STRINGS
            pbAccelTV.setText(accelString);//"Acceleration: "+RecordActivity.trips.get(0).getDriverSpeedScore(RecordActivity.trips));
            pbBrakeTV.setText(brakeString);//"Brake: "+RecordActivity.trips.get(0).getDriverBrakeScore(RecordActivity.trips));
            pbCornerTV.setText(cornerString);//"Corner: "+RecordActivity.trips.get(0).getDriverCornerScore(RecordActivity.trips));
            overallScoreTV.setText(overallScoreString);
            fuelTV.setText(fuelString);
        }


        // CREATE CHART
        mpChart = (LineChart) findViewById(R.id.chart);
        LineDataSet speedData, brakeData, cornerData, fuelData, overallData;
        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<LineDataSet> dataSets = new ArrayList<>();
        //speedData = createLineDataSet(RecordActivity.trips.get(0).getSpeedScoreData(), "Speed Score");
        speedData = createLineDataSet(Trip.getAllSpeedScoreData(RecordActivity.trips), "Speed Score");
        brakeData = createLineDataSet(Trip.getAllBrakeScoreData(RecordActivity.trips), "Brake Score");
        cornerData = createLineDataSet(Trip.getAllCornerScoreData(RecordActivity.trips), "Corner Score");
        fuelData = createLineDataSet(Trip.getAllFuelScoreData(RecordActivity.trips), "Fuel Score");
        overallData = createLineDataSet(Trip.getAllOverallScoreData(RecordActivity.trips), "Overall Score");

        speedData.setColor(Color.GREEN);
        brakeData.setColor(Color.RED);
        cornerData.setColor(Color.CYAN);
        fuelData.setColor(Color.MAGENTA);
        overallData.setColor(Color.rgb(255,209,26));

        speedData.setDrawCircles(false);
        brakeData.setDrawCircles(false);
        cornerData.setDrawCircles(false);
        fuelData.setDrawCircles(false);
        overallData.setDrawCircles(false);

        dataSets.add(overallData);
        dataSets.add(speedData);
        dataSets.add(brakeData);
        dataSets.add(cornerData);
        dataSets.add(fuelData);

        xVals = createXaxisLabels(Trip.getAllTimeData(RecordActivity.trips));

        LineData data = new LineData(xVals, dataSets);
        data.setValueTextColor(Color.rgb(255,209,26));
        //data.setDrawValues(false);
        mpChart.setData(data);
        mpChart.setMaxVisibleValueCount(10);
        mpChart.setBackgroundColor(Color.rgb(125,125,125));
        mpChart.setGridBackgroundColor(R.color.colorPrimaryDark);
        mpChart.setDescriptionColor(Color.rgb(255,209,26));
        mpChart.invalidate();
        mpChart.setDescription("");
        mpChart.setAutoScaleMinMaxEnabled(true);

                //timeArray, speedArray, brakeArray, cornerArray, fuelArray, overallArray;


    }

    public LineDataSet createLineDataSet(ArrayList<Integer> valueArray_in, String dataSetLabel){
        ArrayList<Entry> entryArray_in = new ArrayList<>();
        for(int i =0;i<valueArray_in.size();i++){
            Entry currValEntry = new Entry(valueArray_in.get(i), i);
            entryArray_in.add(currValEntry);
        }
        LineDataSet dataSet = new LineDataSet(entryArray_in,dataSetLabel);
        return dataSet;
    }
    public ArrayList<String> createXaxisLabels(ArrayList<Double> time_in){
        ArrayList<String> timeStrings = new ArrayList<>();
        for(int i=0;i<time_in.size();i++){
            int valToInt = time_in.get(i).intValue();
            String tempString = valToInt+" Min";
            timeStrings.add(tempString);
        }
        return timeStrings;
    }
    public void onResume() {
        super.onResume();
        // When the activity starts up or returns from the background,
        // re-connect to the VehicleManager so we can receive updates.

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
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
