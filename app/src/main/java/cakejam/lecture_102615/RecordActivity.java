package cakejam.lecture_102615;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.Chronometer;

import com.openxc.VehicleManager;
import com.openxc.measurements.BrakePedalStatus;
import com.openxc.measurements.Latitude;
import com.openxc.measurements.Longitude;
import com.openxc.measurements.Measurement;
import com.openxc.measurements.SteeringWheelAngle;
import com.openxc.measurements.VehicleSpeed;
import com.openxc.measurements.AcceleratorPedalPosition;
import com.openxc.measurements.FuelConsumed;
import com.openxc.measurements.Odometer;

import java.text.DateFormat;
import java.util.ArrayList;

public class RecordActivity extends AppCompatActivity {
    // OpenXC Variables
    TextView mVehicleSpeedView, brakeTV, swTV, pedalPosTV, fuelConsumedTV, odometerTV, latitudeTV, longitudeTV;
    private static final String TAG = "MainActivity";
    private VehicleManager mVehicleManager;
    public double speedVal, speedPrevVal, swAngleVal, latitude, longitude, odometer, acceleratorPedalPosition, fuelConsumed;
    public String brakeVal;

    //Accelerometer Variables
    TextView accelTV;
    Sensor accelerometer;
    SensorManager sm;
    public double accelMag;
    public double accelXDouble;
    public double accelYDouble;
    public double accelZDouble;
    public double prevaccelMag;
    //CamSenseListener camSL;//, camSL2;
    public Vibrator v;

    TextView scoreTV;
    ArrayList<Double> queue;
    public int tempColor;
    public double speedScore,brakeScore,cornerScore,totalScore, fuelScore;

    //Toggle Values
    ToggleButton toggle;
    boolean toggleStatus;
    public double currAccel, speedDiff,timeDiff;
    public double odomStart, odomEnd, fuelEfficiency;
    String startTimeString;
    String endTimeString;
    //public double prevaccelXDouble;
    //public double prevaccelYDouble;
    //public double prevaccelZDouble;
    int currTime, prevTime, speedCurrTime, speedPrevTime, swCurrTime, swPrevTime, pedalCurrTime, pedalPrevTime;
    ArrayList<Double> latPoints = new ArrayList<Double>();
    ArrayList<Double> longPoints = new ArrayList<Double>();
    ArrayList<Integer> timePoints = new ArrayList<Integer>();
    ArrayList<String> deductionPoint = new ArrayList<String>();
    public static ArrayList<Trips> trips = new ArrayList<Trips>();
    int numOfTrip;
    TextView recapTV;
    long startTime, endTime, tripTimeSecs;

    TextView timeView;
    public Chronometer myChrono;
    String startTimeFormatted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        totalScore = 100;
        currTime=prevTime =speedCurrTime=speedPrevTime=swCurrTime=swPrevTime=pedalPrevTime=pedalCurrTime= 0;

        //OpenXC Initialization
        mVehicleSpeedView = (TextView) findViewById(R.id.vehicle_speed);
        brakeTV = (TextView) findViewById(R.id.vehicle_brake);
        swTV = (TextView) findViewById(R.id.vehicle_SW);
        pedalPosTV= (TextView) findViewById(R.id.pedal_pos);
        fuelConsumedTV= (TextView) findViewById(R.id.fuel_consumed);
        odometerTV= (TextView) findViewById(R.id.odometer);
        latitudeTV= (TextView) findViewById(R.id.latitude);
        longitudeTV= (TextView) findViewById(R.id.longitude);

        recapTV = (TextView) findViewById(R.id.recap);


        speedVal = 0;
        speedPrevVal = 0;
        swAngleVal = 0;
        brakeVal = "off";

        currAccel = 0;
        speedDiff =0;
        timeDiff = 0;
        queue = new ArrayList<>(10);
        while(queue.size() < 10) queue.add(0.0);

        startTimeString = "has not started";
        endTimeString = "has not ended";
        timeView = (TextView) findViewById(R.id.time);
        scoreTV = (TextView) findViewById(R.id.score_info);
        toggle = (ToggleButton) findViewById(R.id.record_toggle);
        myChrono = (Chronometer) findViewById(R.id.stopwatch);

        v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);


        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    toggleStatus = true;
                    speedScore = 100;
                    brakeScore = 100;
                    cornerScore = 100;
                    fuelScore = 100;
                    odomStart = odometer;
                    scoreTV.setText("Speed: " + speedScore +
                            "\nBrake: " + brakeScore +
                            "\nCorner: " + cornerScore);
                    myChrono.setBase(SystemClock.elapsedRealtime());
                    myChrono.start();
                    startTime = System.currentTimeMillis();

                    //.Timestamp startTemp = new Timestamp(startTime);
                    //startTemp.setTime(System.currentTimeMillis());
                    // SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
                    //Date resultDate = new Date(startTime);
                    startTimeFormatted = DateFormat.getDateTimeInstance().format(startTime);

                    //long elapsed;
                    //elapsed = ((System.currentTimeMillis() - startTime) / 1000);

                    startTimeString = String.format("%02d:%02d:%02d", startTime / 3600, (startTime % 3600) / 60, (startTime % 60));
                    //startTimeString = startTemp.toString();
                    timeView.setText("Recording Started at: " + /*startTimeString resultDate*/ startTimeFormatted + "\nRecording Ended at: " + endTimeString);

                    prevTime = (int) (System.currentTimeMillis());
                    sm = (SensorManager) getSystemService(SENSOR_SERVICE);
                    accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                    accelTV = (TextView) findViewById(R.id.accel_info);
                    tempColor = getResources().getColor(R.color.colorAccent);


                    //camSL = new CamSenseListener(accelTV,scoreTV, tempColor, speedScore,
                    //        brakeScore, cornerScore, speedVal, brakeVal, swAngleVal);

//                    Intent intent = new Intent(mVehicleManager, VehicleManager.class);
//                    mVehicleManager.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

                    if (sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {

                        // success! we have an accelerometer
                        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                        sm.registerListener(camSL2, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                        //vibrateThreshold = accelerometer.getMaximumRange() / 2;
                    } else {
                        // fai! we dont have an accelerometer!
                    }
                     /*%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
                     * /*%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
                     * /*%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
                     * /*%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
                     * /*%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
                     * /*%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%*/


                } else {
                    // The toggle is disabled
                    toggleStatus = false;
                    numOfTrip++;
                    totalScore = (speedScore + brakeScore + cornerScore) / 3;
                    myChrono.stop();
                    myChrono.getBase();
                    odomEnd = odometer;
                    endTime = System.currentTimeMillis();
                    tripTimeSecs = (endTime-startTime)/1000;
                    String endTimeFormatted = DateFormat.getDateTimeInstance().format(endTime);
                    sm.unregisterListener(camSL2);
                    //camSL2 = null;

                    fuelEfficiency = (odomEnd - odomStart) / (0.264172059*fuelConsumed);
                    fuelScore = 100 * fuelEfficiency / 30;

                    Trips tripRecorded = new Trips(numOfTrip, startTimeFormatted, endTimeFormatted, speedScore,
                            brakeScore, cornerScore, totalScore, myChrono.getText().toString(), timePoints,
                            latPoints, longPoints, deductionPoint, tripTimeSecs);
                    trips.add(tripRecorded);
                    int sizer = trips.size();
                    recapTV.setText(String.format("Trip Score:  %.2f", totalScore)
                                    +String.format("\nDistance Traveled: %.2f", (odomEnd-odomStart))
                                    +String.format("\nFuel Efficiency: %.2f", fuelEfficiency)
                            //+String.format("\nFuel Score: %.2f", fuelScore)
                            );
                    timeView.setText("Recording Started at: " + startTimeFormatted + "\nRecording Ended at: " + endTimeFormatted);
//                    Intent myTripRecordIntent = new Intent(this, MyTripsActivity.class);
//
//                    Bundle myBundle = new Bundle();
//                    //myBundle.putDoubleArray("Deduction Lat Pts", latPoints);
//                    //myBundle.putDoubleArray("Deduction Long Pts", longPoints);
//                    myBundle.putIntegerArrayList("Deduction Time", timePoints);
//                    myBundle.putStringArrayList("Deduction Name", deductionPoint);
//                    myBundle.putc
//                    //trips.add(myBundle);
//                    myTripRecordIntent.putExtras(myBundle);
//                    //myTripRecordIntent.putExtras(trips);
//
//                    startActivity(myTripRecordIntent);

//                    mVehicleManager.unbindService(mConnection);
//                    mSpeedListener = null;
//                    mBrakeListener = null;
//                    mSWListener = null;
                }

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    SensorEventListener camSL2 = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            currTime =  (int) (System.currentTimeMillis());
            accelXDouble = (double) event.values[0];
            accelYDouble = (double) event.values[1];
            accelZDouble = (double) event.values[2];
            accelMag = Math.sqrt(Math.pow(accelXDouble, 2) + Math.pow(accelYDouble, 2) + Math.pow(accelZDouble, 2));
            //speedVal2 = recordActivity.speedVal;

                accelTV.setText("X: " +accelXDouble +
                        "\nY: " + accelYDouble +
                        "\nZ: " + accelZDouble +
                        "\nAccel Magnitude:  " + accelMag);

            if (Math.abs(accelMag - prevaccelMag) > 4) {
                if(currTime-prevTime>500) {

                    accelTV.setBackgroundColor(tempColor);

                    if (Math.abs(swAngleVal) > 10) {
                        cornerScore = cornerScore-5;
                        scoreTV.setText("Speed: " + speedScore +
                                    "\nBrake: " + brakeScore +
                                    "\nCorner: " + cornerScore);

                        Log.v(TAG, "" + Math.abs(swAngleVal));
                        latPoints.add(latitude);
                        longPoints.add(longitude);
                        timePoints.add(currTime);
                        deductionPoint.add("Harsh Corner");
                    }
                    else {
                        if (brakeVal.equals("off")) {
                            speedScore = speedScore -5;

                                scoreTV.setText("Speed: " + speedScore +
                                        "\nBrake: " + brakeScore +
                                        "\nCorner: " + cornerScore);

                            latPoints.add(latitude);
                            longPoints.add(longitude);
                            timePoints.add(currTime);
                            deductionPoint.add("Harsh Acceleration");
                        }
                        else {
                            brakeScore= brakeScore -5;

                                scoreTV.setText("Speed: " + speedScore +
                                        "\nBrake: " + brakeScore +
                                        "\nCorner: " + cornerScore);

                            latPoints.add(latitude);
                            longPoints.add(longitude);
                            timePoints.add(currTime);
                            deductionPoint.add("Harsh Brake");
                        }

                    }
                }
                prevTime = currTime;
            }
            else accelTV.setBackgroundColor(Color.WHITE);

            prevaccelMag = accelMag;

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

    };

    ///////////////////////////9999999999999999999999999999999999999999999999999

    VehicleSpeed.Listener mSpeedListener = new VehicleSpeed.Listener() {
        public void receive(Measurement measurement) {
            // When we receive a new VehicleSpeed value from the car, we want to
            // update the UI to display the new value. First we cast the generic
            // Measurement back to the type we know it to be, an VehicleSpeed.
            final VehicleSpeed speed = (VehicleSpeed) measurement;
            // In order to modify the UI, we have to make sure the code is
            // running on the "UI thread" - Google around for this, it's an
            // important concept in Android.


            RecordActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    // Finally, we've got a new value and we're running on the
                    // UI thread - we set the text of the VehicleSpeed view to
                    // the latest value
                    speedCurrTime =  (int) (System.currentTimeMillis());
                    speedVal = 0.621371 *speed.getValue().doubleValue();
                    mVehicleSpeedView.setText( String.format("Speed: %.2f", speedVal));//+"\n"+currAccel);
                    if(speedCurrTime-speedPrevTime>250) {
                        speedDiff= speedVal - speedPrevVal;
                        timeDiff =  speedCurrTime - speedPrevTime;
                        currAccel = speedDiff/timeDiff;
                        mVehicleSpeedView.setText( String.format("Speed: %.2f", speedVal));//+"\n"+currAccel);
                        double sumElements=0, accelAvg, idx0, idx1, idx2, idx3, idx4;
                        boolean goodAvg = true;

                        idx0 = queue.get(0);
                        idx1 = queue.get(1);
                        idx2 = queue.get(2);
                        idx3 = queue.get(3);
                        queue.set(1,idx0);
                        queue.set(2,idx1);
                        queue.set(3,idx2);
                        queue.set(4,idx3);


                        queue.set(0,currAccel);
                        for(int i = 0; i < queue.size(); i++){
                            if(queue.get(i) == 0.0){
                                goodAvg = false;
                            }
                            sumElements += queue.get(i);
                        }
                        accelAvg = sumElements/queue.size();

                        if(/*accelAvg*/currAccel > 0.0025){
                            scoreTV.setBackgroundColor(tempColor);
                            speedScore--;
                            scoreTV.setText("Speed: " + speedScore +
                                        "\nBrake: " + brakeScore +
                                        "\nCorner: " + cornerScore);

                            latPoints.add(latitude);
                            longPoints.add(longitude);
                            timePoints.add(currTime);
                            deductionPoint.add("Harsh Acceleration");

                        }
                        else{scoreTV.setBackgroundColor(Color.WHITE);}
                        if(/*accelAvg*/currAccel < -0.0025){
                            scoreTV.setBackgroundColor(tempColor);
                            brakeScore--;

                                scoreTV.setText("Speed: " + speedScore +
                                        "\nBrake: " + brakeScore +
                                        "\nCorner: " + cornerScore);

                            latPoints.add(latitude);
                            longPoints.add(longitude);
                            timePoints.add(currTime);
                            deductionPoint.add("Harsh Brake");
                        }
                        else{scoreTV.setBackgroundColor(Color.WHITE);}
                        speedPrevTime = speedCurrTime;
                        speedPrevVal = speedVal;
                    }
                }
            });
        }
    };

    BrakePedalStatus.Listener mBrakeListener = new BrakePedalStatus.Listener() {
        public void receive(Measurement measurement) {
            // When we receive a new VehicleSpeed value from the car, we want to
            // update the UI to display the new value. First we cast the generic
            // Measurement back to the type we know it to be, an VehicleSpeed.
            final BrakePedalStatus brake = (BrakePedalStatus) measurement;
            // In order to modify the UI, we have to make sure the code is
            // running on the "UI thread" - Google around for this, it's an
            // important concept in Android.

            RecordActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    // Finally, we've got a new value and we're running on the
                    // UI thread - we set the text of the VehicleSpeed view to
                    // the latest value
                    brakeVal = brake.getValue().toString();
                    brakeTV.setText("Brake: "+brakeVal);

                }
            });
        }
    };

    SteeringWheelAngle.Listener mSWListener = new SteeringWheelAngle.Listener() {
        public void receive(Measurement measurement) {
            // When we receive a new VehicleSpeed value from the car, we want to
            // update the UI to display the new value. First we cast the generic
            // Measurement back to the type we know it to be, an VehicleSpeed.
            final SteeringWheelAngle swAngle= (SteeringWheelAngle) measurement;
            // In order to modify the UI, we have to make sure the code is
            // running on the "UI thread" - Google around for this, it's an
            // important concept in Android.

            RecordActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    // Finally, we've got a new value and we're running on the
                    // UI thread - we set the text of the VehicleSpeed view to
                    // the latest value
                    swCurrTime = (int) (System.currentTimeMillis());
                    swAngleVal = swAngle.getValue().doubleValue();
                    swTV.setText(String.format("SW angle: %.2f", swAngleVal));
                    if (swCurrTime - swPrevTime  > 500) {


                        if (Math.abs(swAngleVal) > 10 && speedVal > 17) {
                            cornerScore--;
                            scoreTV.setText("Speed: " + speedScore +
                                    "\nBrake: " + brakeScore +
                                    "\nCorner: " + cornerScore);
                            Log.v(TAG, "" + Math.abs(swAngleVal));
                            latPoints.add(latitude);
                            longPoints.add(longitude);
                            timePoints.add(currTime);
                            deductionPoint.add("Harsh Corner");

                        }
                        if (Math.abs(swAngleVal) > 15 && speedVal > 30) {
                            cornerScore--;
                            scoreTV.setText("Speed: " + speedScore +
                                    "\nBrake: " + brakeScore +
                                    "\nCorner: " + cornerScore);
                            Log.v(TAG, "" + Math.abs(swAngleVal));
                            latPoints.add(latitude);
                            longPoints.add(longitude);
                            timePoints.add(currTime);
                            deductionPoint.add("Harsh Corner");
                        }
                        swPrevTime = swCurrTime;

                    }
                }
            });
        }
    };

    Latitude.Listener mLatListener = new Latitude.Listener() {
        public void receive(Measurement measurement) {
            // When we receive a new VehicleSpeed value from the car, we want to
            // update the UI to display the new value. First we cast the generic
            // Measurement back to the type we know it to be, an VehicleSpeed.
            final Latitude lat= (Latitude) measurement;
            // In order to modify the UI, we have to make sure the code is
            // running on the "UI thread" - Google around for this, it's an
            // important concept in Android.

            RecordActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    // Finally, we've got a new value and we're running on the
                    // UI thread - we set the text of the VehicleSpeed view to
                    // the latest value
                    //swAngleVal = swAngle.getValue().doubleValue();
                    latitude = lat.getValue().doubleValue();
                    latitudeTV.setText( String.format("Latitude: %.4f", latitude));
                }
            });
        }
    };

    Longitude.Listener mLongListener = new Longitude.Listener() {
        public void receive(Measurement measurement) {
            // When we receive a new VehicleSpeed value from the car, we want to
            // update the UI to display the new value. First we cast the generic
            // Measurement back to the type we know it to be, an VehicleSpeed.
            final Longitude lng= (Longitude) measurement;
            // In order to modify the UI, we have to make sure the code is
            // running on the "UI thread" - Google around for this, it's an
            // important concept in Android.

            RecordActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    // Finally, we've got a new value and we're running on the
                    // UI thread - we set the text of the VehicleSpeed view to
                    // the latest value
                    //swAngleVal = swAngle.getValue().doubleValue();
                    longitude = lng.getValue().doubleValue();
                    longitudeTV.setText(String.format("Longitude: %.4f", longitude));
//                    swTV.setText("Steering Wheel angle: "
//                            + swAngleVal +"\n"+ lng.getValue());
                }
            });
        }
    };

    AcceleratorPedalPosition.Listener mAcceleratorPedalPosition = new AcceleratorPedalPosition.Listener() {
        public void receive(Measurement measurement) {
            // When we receive a new VehicleSpeed value from the car, we want to
            // update the UI to display the new value. First we cast the generic
            // Measurement back to the type we know it to be, an VehicleSpeed.
            final AcceleratorPedalPosition aPP= (AcceleratorPedalPosition) measurement;
            // In order to modify the UI, we have to make sure the code is
            // running on the "UI thread" - Google around for this, it's an
            // important concept in Android.

            RecordActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    // Finally, we've got a new value and we're running on the
                    // UI thread - we set the text of the VehicleSpeed view to
                    // the latest value
                    //swAngleVal = swAngle.getValue().doubleValue();
                    //pedalCurrTime =  (int) (System.currentTimeMillis());
                    acceleratorPedalPosition = aPP.getValue().doubleValue();
                    pedalPosTV.setText( String.format("Pedal Pos: %.2f", acceleratorPedalPosition));
//                    if(pedalCurrTime-speedPrevTime>250) {
//                        if (acceleratorPedalPosition > 20) {
//                            speedScore--;
//                            scoreTV.setText("Speed: " + speedScore +
//                                    "\nBrake: " + brakeScore +
//                                    "\nCorner: " + cornerScore);
//
//                            latPoints.add(latitude);
//                            longPoints.add(longitude);
//                            timePoints.add(currTime);
//                            deductionPoint.add("Harsh Acceleration");
//
//                        }
//                        pedalPrevTime = pedalCurrTime;
////                    swTV.setText("Steering Wheel angle: "
////                            + swAngleVal +"\n"+ lng.getValue());
//                    }
                }
            });
        }
    };

    FuelConsumed.Listener mFuelConsumed = new FuelConsumed.Listener() {
        public void receive(Measurement measurement) {
            // When we receive a new VehicleSpeed value from the car, we want to
            // update the UI to display the new value. First we cast the generic
            // Measurement back to the type we know it to be, an VehicleSpeed.
            final FuelConsumed fc= (FuelConsumed) measurement;
            // In order to modify the UI, we have to make sure the code is
            // running on the "UI thread" - Google around for this, it's an
            // important concept in Android.

            RecordActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    // Finally, we've got a new value and we're running on the
                    // UI thread - we set the text of the VehicleSpeed view to
                    // the latest value
                    //swAngleVal = swAngle.getValue().doubleValue();
                    fuelConsumed = fc.getValue().doubleValue();
                    fuelConsumedTV.setText( String.format("Fuel Consumed: %.2f", fuelConsumed));
//                    swTV.setText("Steering Wheel angle: "
//                            + swAngleVal +"\n"+ lng.getValue());
                }
            });
        }
    };

    Odometer.Listener mOdometer = new Odometer.Listener() {
        public void receive(Measurement measurement) {
            // When we receive a new VehicleSpeed value from the car, we want to
            // update the UI to display the new value. First we cast the generic
            // Measurement back to the type we know it to be, an VehicleSpeed.
            final Odometer od= (Odometer) measurement;
            // In order to modify the UI, we have to make sure the code is
            // running on the "UI thread" - Google around for this, it's an
            // important concept in Android.

            RecordActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    // Finally, we've got a new value and we're running on the
                    // UI thread - we set the text of the VehicleSpeed view to
                    // the latest value
                    //swAngleVal = swAngle.getValue().doubleValue();
                    odometer = od.getValue().doubleValue();
                    odometerTV.setText( String.format("Odometer: %.2f", odometer));
//                    swTV.setText("Steering Wheel angle: "
//                            + swAngleVal +"\n"+ lng.getValue());
                }
            });
        }
    };

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

            // We want to receive updates whenever the VehicleSpeed changes. We
            // have an VehicleSpeed.Listener (see above, mSpeedListener) and here
            // we request that the VehicleManager call its receive() method
            // whenever the VehicleSpeed changes
            mVehicleManager.addListener(VehicleSpeed.class, mSpeedListener);
            mVehicleManager.addListener(BrakePedalStatus.class, mBrakeListener);
            mVehicleManager.addListener(SteeringWheelAngle.class, mSWListener);
            mVehicleManager.addListener(Latitude.class, mLatListener);
            mVehicleManager.addListener(Longitude.class, mLongListener);
            mVehicleManager.addListener(AcceleratorPedalPosition.class, mAcceleratorPedalPosition);
            mVehicleManager.addListener(FuelConsumed.class, mFuelConsumed);
            mVehicleManager.addListener(Odometer.class, mOdometer);
        }

        // Called when the connection with the service disconnects unexpectedly
        public void onServiceDisconnected(ComponentName className) {
            Log.w(TAG, "VehicleManager Service  disconnected unexpectedly");
            mVehicleManager = null;
        }
    };

    public void onResume() {
        super.onResume();
        // When the activity starts up or returns from the background,
        // re-connect to the VehicleManager so we can receive updates.
        if(mVehicleManager == null) {
            Intent intent = new Intent(this, VehicleManager.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
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
                toggle.setChecked(false);
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
                toggle.setChecked(false);
                Intent standingsIntent = new Intent(this, StandingsActivity.class);

                Bundle myBundle = new Bundle();
                myBundle.putInt("x", 5);
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
    public double getSpeedVal(){
        return speedVal;
    }

    public double getSWAngleVal(){
        return swAngleVal;
    }

    public String getBrakeVal(){
        return brakeVal;
    }


}
