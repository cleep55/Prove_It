package cakejam.lecture_102615;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
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

import com.openxc.VehicleManager;
import com.openxc.measurements.EngineSpeed;
import com.openxc.measurements.Measurement;
import com.openxc.measurements.VehicleSpeed;
import com.openxc.measurements.WindshieldWiperStatus;
import com.openxc.messages.Command;
import com.openxc.messages.SimpleVehicleMessage;
import com.openxc.messages.VehicleMessage;

public class SummaryActivity extends AppCompatActivity {


    private static final String TAG = "SummaryActivity";

    private VehicleManager mVehicleManager;
    //private TextView mEngineSpeedView;
    //private TextView mVehicleSpeedView;
    private TextView pbAccelTV, pbBrakeTV, pbCornerTV, overallScoreTV;
    private Measurement mrMeasure;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        ProgressBar pbAccel = (ProgressBar) findViewById(R.id.pb_accel);
        ProgressBar pbBrake = (ProgressBar) findViewById(R.id.pb_brake);
        ProgressBar pbCorner = (ProgressBar) findViewById(R.id.pb_corner);
        ProgressBar pbLKA = (ProgressBar) findViewById(R.id.pb_LKA);
        ProgressBar pbDistFromObj = (ProgressBar) findViewById(R.id.pb_dist_from_objects);
        ProgressBar pbBlindSpot = (ProgressBar) findViewById(R.id.pb_blind_spot);
        pbAccel.setProgress((int) RecordActivity.trips.get(0).getDriverSpeedScore(RecordActivity.trips));
        pbBrake.setProgress((int) RecordActivity.trips.get(0).getDriverBrakeScore(RecordActivity.trips));
        pbCorner.setProgress((int) RecordActivity.trips.get(0).getDriverCornerScore(RecordActivity.trips));

        pbAccelTV = (TextView) findViewById(R.id.pbAccelTV);
        pbBrakeTV = (TextView) findViewById(R.id.pbBrakeTV);
        pbCornerTV = (TextView) findViewById(R.id.pbCornerTV);
        overallScoreTV = (TextView) findViewById(R.id.OverallScore);
        double driverOverallScore = RecordActivity.trips.get(0).getDriverTotalScore(RecordActivity.trips);
        String overallScoreString, accelString, brakeString, cornerString;

            overallScoreString = String.format("Overall Score\n%.2f", driverOverallScore);
            accelString = String.format("Acceleration: %.2f", RecordActivity.trips.get(0).getDriverSpeedScore(RecordActivity.trips));
            brakeString = String.format("Brake: %.2f", RecordActivity.trips.get(0).getDriverBrakeScore(RecordActivity.trips));
            cornerString = String.format("Corner: %.2f", RecordActivity.trips.get(0).getDriverCornerScore(RecordActivity.trips));


        pbAccelTV.setText(accelString);//"Acceleration: "+RecordActivity.trips.get(0).getDriverSpeedScore(RecordActivity.trips));
        pbBrakeTV.setText(brakeString);//"Brake: "+RecordActivity.trips.get(0).getDriverBrakeScore(RecordActivity.trips));
        pbCornerTV.setText(cornerString);//"Corner: "+RecordActivity.trips.get(0).getDriverCornerScore(RecordActivity.trips));
        overallScoreTV.setText(overallScoreString);

        pbLKA.setProgress(79);
        pbDistFromObj.setProgress(88);
        pbBlindSpot.setProgress(68);
        //mEngineSpeedView = (TextView) findViewById(R.id.engine_speed);
        //mVehicleSpeedView = (TextView) findViewById(R.id.vehicle_speed);
        //wiperStatus.equals(false);

        //mResponseView = (TextView) findViewById(R.id.response_view);
        //WindshieldWiperStatus wiperStatus = new WindshieldWiperStatus(false);
        //mResponseView.setText("Vehicle speed (km/h): "
        //        + wiperStatus.getValue());
        //VehicleMessage.Creator
    }
    public void onResume() {
        super.onResume();
        // When the activity starts up or returns from the background,
        // re-connect to the VehicleManager so we can receive updates.
        if(mVehicleManager == null) {
            Intent intent = new Intent(this, VehicleManager.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    EngineSpeed.Listener mEngineSpeedListener = new EngineSpeed.Listener() {
        public void receive(final Measurement measurement) {
            // When we receive a new EngineSpeed value from the car, we want to
            // update the UI to display the new value. First we cast the generic
            // Measurement back to the type we know it to be, an EngineSpeed.
            final EngineSpeed speed = (EngineSpeed) measurement;
            // In order to modify the UI, we have to make sure the code is
            // running on the "UI thread" - Google around for this, it's an
            // important concept in Android.
            SummaryActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    // Finally, we've got a new value and we're running on the
                    // UI thread - we set the text of the EngineSpeed view to
                    // the latest value
                    //mEngineSpeedView.setText("Engine speed (RPM): "
                            //+ speed.getValue().doubleValue());

                }
            });
        }
    };

    VehicleSpeed.Listener mVehicleSpeedListener = new EngineSpeed.Listener() {
        public void receive(Measurement measurement) {
            // When we receive a new Vehicle Speed value from the car, we want to
            // update the UI to display the new value. First we cast the generic
            // Measurement back to the type we know it to be, an VehicleSpeed.
            final VehicleSpeed vehicleSpeed = (VehicleSpeed) measurement;
            // In order to modify the UI, we have to make sure the code is
            // running on the "UI thread" - Google around for this, it's an
            // important concept in Android.
            SummaryActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    // Finally, we've got a new value and we're running on the
                    // UI thread - we set the text of the EngineSpeed view to
                    // the latest value
                    //mVehicleSpeedView.setText("Vehicle speed (km/h): "
                            //vehicleSpeed.getValue().doubleValue());
                }
            });
        }
    };

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
    private ServiceConnection mConnection = new ServiceConnection() {
        // Called when the connection with the VehicleManager service is
        // established, i.e. bound.
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            Log.i(TAG, "Bound to VehicleManager");
            // When the VehicleManager starts up, we store a reference to it
            // here in "mVehicleManager" so we can call functions on it
            // elsewhere in our code.
            mVehicleManager = ((VehicleManager.VehicleBinder) service)
                    .getService();

            // We want to receive updates whenever the EngineSpeed changes. We
            // have an EngineSpeed.Listener (see above, mEngineSpeedListener) and here
            // we request that the VehicleManager call its receive() method
            // whenever the EngineSpeed changes
            mVehicleManager.addListener(EngineSpeed.class, mEngineSpeedListener);
            mVehicleManager.addListener(VehicleSpeed.class, mVehicleSpeedListener);


        }

        // Called when the connection with the service disconnects unexpectedly
        public void onServiceDisconnected(ComponentName className) {
            Log.w(TAG, "VehicleManager Service  disconnected unexpectedly");
            mVehicleManager = null;
        }
    };


}
