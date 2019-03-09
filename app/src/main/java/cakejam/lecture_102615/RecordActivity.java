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
import android.widget.Toast;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class RecordActivity extends AppCompatActivity {
    /* -------------------------------------------------------------------------------------------
       ------------------------------ Begin: DECLARATIONS some initializations --------------------------------
       -------------------------------------------------------------------------------------------*/

    String NAME_OF_FILE = "index.html";//DataHandler.DATA_BASE_NAME;

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

    //Scoring
    TextView scoreTV;
    ArrayList<Double> queue;
    public int tempColor;
    public double speedScore,brakeScore,cornerScore,totalScore, fuelScore;
    ArrayList<Integer> timeArray = new ArrayList<>();
    ArrayList<Integer> speedArray = new ArrayList<>();
    ArrayList<Integer> brakeArray = new ArrayList<>();
    ArrayList<Integer> cornerArray = new ArrayList<>();
    ArrayList<Integer> fuelArray = new ArrayList<>();
    ArrayList<Integer> overallArray = new ArrayList<>();
    // timeArray, speedArray, brakeArray, cornerArray, fuelArray, overallArray;
    //ArrayList<ArrayList<Double>> scoresArray = new ArrayList<>();
    long normToMilli = 1000000;
    int freqScoreCollection;// 1 minute (1,000 = 1 second)
    int freqSpeedDeduction;
    int freqCorneringDeduction;
    int freqFuelDeduction = 250;
    int freqPosScore;
    int startScoreCollection;
    //int posSpeedScoreTime=0, posBrakeScoreTime=0, posCornerScoreTime=0, posFuelScoreTime=0;
    int prevScoreUpdateTime =0, speedDeductionTime=0,brakeDeductionTime=0,cornerDeductionTime=0,fuelDeductionTime=0;
    int minuteCount = 0;
    public String speedDeduction = "Harsh Acceleration";
    public String brakeDeduction = "Harsh Brake";
    public String cornerDeduction = "Harsh Corner";
    public String fuelDeduction = "Inefficient Fuel";
    double accelTHRESHOLD;// = 4;
    double speedTHRESHOLD;// = 0.004;
    double brakeTHRESHOLD;// = -0.004;
    double swSharpTHRESHOLD;// = 30;
    double speedSharpTHRESHOLD;// = 16;
    double swSlightTHRESHOLD;// = 13;
    double speedSlightTHRESHOLD;// = 30;

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
    public static ArrayList<Trip> trips = new ArrayList<Trip>();
    int numOfTrip;
    TextView recapTV;
    int startTime, endTime, tripTimeSecs;

    //Stopwatch
    TextView timeView;
    public Chronometer myChrono;
    String startTimeFormatted;

    /* -------------------------------------------------------------------------------------------
       ------------------------------ End: DECLARATIONS some initializations --------------------------------
       -------------------------------------------------------------------------------------------*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final StandingsActivity standingsActivity = new StandingsActivity();

/* --------------------------------------------------------------------------------------------------------------------------------------
   ------------------------------ Begin: INITIALIZATION ---------------------------------------------------------------------------------
   --------------------------------------------------------------------------------------------------------------------------------------*/

        // TextViews Initialization and setup
        startTimeString = "has not started";
        endTimeString = "has not ended";
        timeView = (TextView) findViewById(R.id.time);
        scoreTV = (TextView) findViewById(R.id.score_info);
        toggle = (ToggleButton) findViewById(R.id.record_toggle);
        myChrono = (Chronometer) findViewById(R.id.stopwatch);
        //timeArray.add(0.0); speedArray.add(0.0); brakeArray.add(0.0); cornerArray.add(0.0); fuelArray.add(0.0); overallArray.add(0.0);

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

        // Acceleration Scoring Eval. Initialization
        speedVal = 0;
        speedPrevVal = 0;
        swAngleVal = 0;
        brakeVal = "off";
        totalScore = 100;
        currTime=prevTime =speedCurrTime=speedPrevTime=swCurrTime=swPrevTime=pedalPrevTime=pedalCurrTime= 0;
        currAccel = 0;
        speedDiff =0;
        timeDiff = 0;

//        accelTHRESHOLD = standingsActivity.getAccelTHRESH();
//        brakeTHRESHOLD = standingsActivity.getBrakeTHRESH();
//        speedTHRESHOLD = standingsActivity.getSpeedTHRESH();
//        swSharpTHRESHOLD = standingsActivity.getSwSharpTHRESH();
//        speedSharpTHRESHOLD = standingsActivity.getSpeedSharpTHRESH();
//        swSlightTHRESHOLD = standingsActivity.getSwSlightTHRESH();
//        speedSharpTHRESHOLD = standingsActivity.getSpeedSlightTHRESH();


        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
//        boolean empty = bundle.isEmpty();
//        double blah = bundle.getDouble("Accel Thresh");
        if(bundle == null)
        {
            accelTHRESHOLD=4; speedTHRESHOLD=.004; brakeTHRESHOLD=-.004;
            swSharpTHRESHOLD=30; speedSharpTHRESHOLD = 17;
            swSlightTHRESHOLD=15; speedSlightTHRESHOLD=30;
            freqScoreCollection = 5000;// 1 minute (1,000 = 1 second)
            freqSpeedDeduction = 250;
            freqCorneringDeduction = 500;
            freqPosScore = 10000;
            startScoreCollection = 10000;
        }
        else{
            accelTHRESHOLD = bundle.getDouble("Accel Thresh");
            brakeTHRESHOLD = bundle.getDouble("Brake Thresh");
            speedTHRESHOLD = bundle.getDouble("Speed Thresh");
            swSharpTHRESHOLD = bundle.getDouble("SW Sharp Thresh");
            speedSharpTHRESHOLD = bundle.getDouble("Speed Sharp Thresh");
            swSlightTHRESHOLD = bundle.getDouble("SW Slight Thresh");
            speedSlightTHRESHOLD = bundle.getDouble("Speed Slight Thresh");
            startScoreCollection = bundle.getInt("Start Score Collection");
            freqScoreCollection = bundle.getInt("Score Collection Freq");
            freqPosScore = bundle.getInt("Positive Score Freq");
            freqSpeedDeduction = bundle.getInt("Speed Deduct Freq");
            freqCorneringDeduction = bundle.getInt("Corner Deduct Freq");

        }


        queue = new ArrayList<>(10);
        while(queue.size() < 10) queue.add(0.0);

        v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        // DATABASE setting up Trip Class
        final DataHandler handler =  new DataHandler(getBaseContext());
        //handler.deleteTable();
        trips = handler.getAllTrips();
        if(trips.size()==0){
            numOfTrip =1;
        }
        else {
            numOfTrip = trips.get(trips.size() - 1).getTripNum();
        }

        // Clear memory
        //numOfTrip = 1;
        //handler.deleteTable();

/* -----------------------------------------------------------------------------------------------------------------------------------------
   ------------------------------ End: INITIALIZATION ----------------------------------------------------------------------------------------
   -----------------------------------------------------------------------------------------------------------------------------------------*/

/* ######################################################################################################################################
   ########################### Toggle Enabled RECORD ####################################################################################
   ######################################################################################################################################*/
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
//                    boolean toggleCheck = toggle.isChecked();


//                    accelTHRESHOLD = standingsActivity.getAccelTHRESH();
//                    speedTHRESHOLD = standingsActivity.getSpeedTHRESH();
//                    brakeTHRESHOLD = standingsActivity.getBrakeTHRESH();
//                    speedSharpTHRESHOLD = standingsActivity.getSpeedSharpTHRESH();
//                    swSharpTHRESHOLD = standingsActivity.getSwSharpTHRESH();
//                    speedSlightTHRESHOLD = standingsActivity.getSpeedSlightTHRESH();
//                    swSlightTHRESHOLD = standingsActivity.getSwSlightTHRESH();

                    toggleStatus = true;
                    speedScore = 100;
                    brakeScore = 100;
                    cornerScore = 100;
                    fuelScore = 100;
                    latPoints = new ArrayList<Double>();
                    longPoints = new ArrayList<Double>();
                    timePoints = new ArrayList<Integer>();
                    deductionPoint = new ArrayList<String>();
                    odomStart = odometer;
                    scoreTV.setText("Speed: " + speedScore +
                            "\nBrake: " + brakeScore +
                            "\nCorner: " + cornerScore);
                    myChrono.setBase(SystemClock.elapsedRealtime());
                    myChrono.start();
                    startTime = (int) (System.nanoTime()/normToMilli);

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

                    prevTime = speedPrevTime=swPrevTime=pedalPrevTime = (int) (System.nanoTime()/normToMilli);
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

/* ######################################################################################################################################
   ########################### Toggle DISABLED STOP RECORDING ###########################################################################
   ######################################################################################################################################*/
                } else {
                    // The toggle is disabled
                    toggleStatus = false;
                    numOfTrip++;
                    totalScore = (speedScore + brakeScore + cornerScore) / 3;
                    myChrono.stop();
                    myChrono.getBase();
                    odomEnd = odometer;
                    endTime = (int) (System.nanoTime()/normToMilli);
                    tripTimeSecs = (endTime-startTime)/1000;
                    String endTimeFormatted = DateFormat.getDateTimeInstance().format(endTime);
                    sm.unregisterListener(camSL2);
                    //camSL2 = null;

                    fuelEfficiency = (odomEnd - odomStart) / (0.264172059*fuelConsumed);
                    fuelScore = 100;// * fuelEfficiency / 30;

                    Trip tripRecorded = new Trip(numOfTrip, startTimeFormatted, endTimeFormatted, myChrono.getText().toString(), tripTimeSecs,
                            speedScore, brakeScore, cornerScore, totalScore,
                            timePoints, latPoints, longPoints, deductionPoint);
                    Trip tripRecorded2 = new Trip(numOfTrip, startTimeFormatted, endTimeFormatted, myChrono.getText().toString(), tripTimeSecs,
                            speedScore, brakeScore, cornerScore, fuelScore, totalScore,
                            timeArray, speedArray, brakeArray,
                            cornerArray,fuelArray, overallArray,
                            timePoints, latPoints, longPoints, deductionPoint);
                    if(tripTimeSecs>freqScoreCollection/1000) {
                        //trips.add(tripRecorded);
                        trips.add(tripRecorded2);
                        //Saves Data to Database
                        handler.insertData(tripRecorded2);
                        uploadFile();
                    }
                    else{Toast.makeText(getBaseContext(),"Trip was too short to record", Toast.LENGTH_LONG).show();}
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
    public void posScoreUpdate(int prevDeductionTime, String deductionType) {
        int updateTime = (int) (System.nanoTime() / normToMilli);
        boolean toggleCheck = toggle.isChecked();
        if ((updateTime - prevDeductionTime) > freqPosScore && toggleStatus) {
            if(deductionType == speedDeduction) {
                if(speedScore <100) {
                    speedScore++;
                    scoreTV.setBackgroundColor(Color.GREEN);
                    scoreTV.setText("Speed: " + speedScore +
                            "\nBrake: " + brakeScore +
                            "\nCorner: " + cornerScore);
                }
                speedDeductionTime = updateTime;
            }
            else if(deductionType == brakeDeduction) {
                if(brakeScore<100) {
                    brakeScore++;
                    scoreTV.setBackgroundColor(Color.GREEN);
                    scoreTV.setText("Speed: " + speedScore +
                            "\nBrake: " + brakeScore +
                            "\nCorner: " + cornerScore);
                }
                brakeDeductionTime = updateTime;
            }
            else if(deductionType == cornerDeduction) {
                if(cornerScore<100) {
                    cornerScore++;
                    scoreTV.setBackgroundColor(Color.GREEN);
                    scoreTV.setText("Speed: " + speedScore +
                            "\nBrake: " + brakeScore +
                            "\nCorner: " + cornerScore);
                }
                cornerDeductionTime = updateTime;
            }
            else{
                if(fuelScore<100) {
                    fuelScore++;
                    scoreTV.setBackgroundColor(Color.GREEN);
                    scoreTV.setText("Speed: " + speedScore +
                            "\nBrake: " + brakeScore +
                            "\nCorner: " + cornerScore);
                }
                fuelDeductionTime = updateTime;
            }

        }
    }

    public void minuteScoresUpdate(){
//        long updateTimeNano =  System.nanoTime();
//        long toMillis = updateTimeNano/normToMilli;
//        int updateTime = (int) toMillis;
        int updateTime = (int) (System.nanoTime()/normToMilli);
        int startTimeInt = (int) startTime;
        if(updateTime-prevScoreUpdateTime>freqScoreCollection && toggle.isChecked() && (updateTime-startTimeInt)>startScoreCollection)
        {
            if(minuteCount==0){
                timeArray.add(minuteCount+1);
                speedArray.add(100);
                brakeArray.add(100);
                cornerArray.add(100);
                fuelArray.add(100);
                //double overallScore = (speedScore+brakeScore+cornerScore+fuelScore)/4;
                overallArray.add(100);
                prevScoreUpdateTime = updateTime;
                minuteCount = 1;
            }
            else{
                timeArray.add(minuteCount+1);
                speedArray.add((int) speedScore);
                brakeArray.add((int) brakeScore);
                cornerArray.add((int) cornerScore);
                fuelArray.add((int) fuelScore);
                double overallScore = (speedScore+brakeScore+cornerScore+fuelScore)/4;
                overallArray.add((int) overallScore);
                prevScoreUpdateTime = updateTime;
            }

        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

/* LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL
    LLLLLLLLLLLLLLLLLLLLLLLLLLLLLL accelerometer listener LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL
    LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL*/
    SensorEventListener camSL2 = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            currTime = (int) (System.nanoTime()/normToMilli);

            accelXDouble = (double) event.values[0];
            accelYDouble = (double) event.values[1];
            accelZDouble = (double) event.values[2];
            accelMag = Math.sqrt(Math.pow(accelXDouble, 2) + Math.pow(accelYDouble, 2) + Math.pow(accelZDouble, 2));
            //speedVal2 = recordActivity.speedVal;

                accelTV.setText("X: " +String.format("%.2f",accelXDouble) +
                        "\nY: " + String.format("%.2f",accelYDouble) +
                        "\nZ: " + String.format("%.2f",accelZDouble) +
                        "\nAccel Magnitude:  " + String.format("%.2f",accelMag));

            if (Math.abs(accelMag - prevaccelMag) > accelTHRESHOLD  && toggle.isChecked()) {
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
                        deductionPoint.add(cornerDeduction);
                        cornerDeductionTime = currTime;
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
                            deductionPoint.add(speedDeduction);
                            speedDeductionTime = currTime;
                        }
                        else {
                            brakeScore= brakeScore -5;

                            scoreTV.setText("Speed: " + speedScore +
                                    "\nBrake: " + brakeScore +
                                    "\nCorner: " + cornerScore);

                            latPoints.add(latitude);
                            longPoints.add(longitude);
                            timePoints.add(currTime);
                            deductionPoint.add(brakeDeduction);
                            brakeDeductionTime = currTime;
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

/* LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL
   LLLLLLLLLLLLLLLLLLLLLLLLL vehicle listeners LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL
   LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL*/


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
                    speedCurrTime = (int) (System.nanoTime()/normToMilli);
                    speedVal = 0.621371 *speed.getValue().doubleValue();
                    mVehicleSpeedView.setText(String.format("Speed: %.2f", speedVal));//+"\n"+currAccel);
                    minuteScoresUpdate();
                    posScoreUpdate(speedDeductionTime, speedDeduction);
                    posScoreUpdate(brakeDeductionTime, brakeDeduction);
                    posScoreUpdate(cornerDeductionTime, cornerDeduction);
                    posScoreUpdate(fuelDeductionTime, fuelDeduction);


                    if((speedCurrTime-speedPrevTime)>freqSpeedDeduction && toggle.isChecked()) {

                        speedDiff= speedVal - speedPrevVal;
                        timeDiff =  speedCurrTime - speedPrevTime;
                        currAccel = speedDiff/timeDiff;
                        //mVehicleSpeedView.setText( String.format("Speed: %.2f", speedVal));//+"\n"+currAccel);
//                        double sumElements=0, accelAvg, idx0, idx1, idx2, idx3, idx4;
//                        boolean goodAvg = true;
//
//                        idx0 = queue.get(0);
//                        idx1 = queue.get(1);
//                        idx2 = queue.get(2);
//                        idx3 = queue.get(3);
//                        queue.set(1,idx0);
//                        queue.set(2,idx1);
//                        queue.set(3,idx2);
//                        queue.set(4,idx3);
//
//
//                        queue.set(0,currAccel);
//                        for(int i = 0; i < queue.size(); i++){
//                            if(queue.get(i) == 0.0){
//                                goodAvg = false;
//                            }
//                            sumElements += queue.get(i);
//                        }
//                        accelAvg = sumElements/queue.size();

                        if(/*accelAvg*/currAccel > speedTHRESHOLD){
                            scoreTV.setBackgroundColor(tempColor);
                            speedScore--;
                            scoreTV.setText("Speed: " + speedScore +
                                    "\nBrake: " + brakeScore +
                                    "\nCorner: " + cornerScore);

                            latPoints.add(latitude);
                            longPoints.add(longitude);
                            timePoints.add(speedCurrTime);
                            deductionPoint.add(speedDeduction);
                            speedPrevTime = speedCurrTime;
                            speedDeductionTime = speedCurrTime;
                            speedPrevVal = speedVal;
                        }
                        else{scoreTV.setBackgroundColor(Color.WHITE);}
                        if(/*accelAvg*/currAccel < brakeTHRESHOLD){
                            scoreTV.setBackgroundColor(tempColor);
                            brakeScore--;

                            scoreTV.setText("Speed: " + speedScore +
                                    "\nBrake: " + brakeScore +
                                    "\nCorner: " + cornerScore);

                            latPoints.add(latitude);
                            longPoints.add(longitude);
                            timePoints.add(speedCurrTime);
                            deductionPoint.add(brakeDeduction);
                            brakeDeductionTime = speedCurrTime;
                            speedPrevTime = speedCurrTime;
                            speedPrevVal = speedVal;
                        }
                        else{scoreTV.setBackgroundColor(Color.WHITE);}

                    }
                }
            });
        }
    };

    BrakePedalStatus.Listener mBrakeListener = new BrakePedalStatus.Listener() {
        public void receive(Measurement measurement) {
            final BrakePedalStatus brake = (BrakePedalStatus) measurement;

            RecordActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    brakeVal = brake.getValue().toString();
                    brakeTV.setText("Brake: "+brakeVal);

                }
            });
        }
    };

    SteeringWheelAngle.Listener mSWListener = new SteeringWheelAngle.Listener() {
        public void receive(Measurement measurement) {
            final SteeringWheelAngle swAngle= (SteeringWheelAngle) measurement;
            RecordActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    swCurrTime = (int) (System.nanoTime()/normToMilli);
                    swAngleVal = swAngle.getValue().doubleValue();
                    swTV.setText(String.format("SW angle: %.2f", swAngleVal));
                    if (swCurrTime - swPrevTime  > freqCorneringDeduction  && toggle.isChecked()) {


                        if (Math.abs(swAngleVal) > swSharpTHRESHOLD && speedVal > speedSharpTHRESHOLD) {
                            cornerScore--;
                            scoreTV.setText("Speed: " + speedScore +
                                    "\nBrake: " + brakeScore +
                                    "\nCorner: " + cornerScore);
                            Log.v(TAG, "" + Math.abs(swAngleVal));
                            latPoints.add(latitude);
                            longPoints.add(longitude);
                            timePoints.add(swCurrTime);
                            deductionPoint.add(cornerDeduction);
                            swPrevTime = swCurrTime;
                            cornerDeductionTime = swCurrTime;

                        }

                        if (Math.abs(swAngleVal) > swSlightTHRESHOLD && speedVal > speedSlightTHRESHOLD) {
                            cornerScore--;
                            scoreTV.setText("Speed: " + speedScore +
                                    "\nBrake: " + brakeScore +
                                    "\nCorner: " + cornerScore);
                            Log.v(TAG, "" + Math.abs(swAngleVal));
                            latPoints.add(latitude);
                            longPoints.add(longitude);
                            timePoints.add(swCurrTime);
                            deductionPoint.add(cornerDeduction);
                            swPrevTime = swCurrTime;
                            cornerDeductionTime = swPrevTime;
                        }


                    }
                }
            });
        }
    };

    Latitude.Listener mLatListener = new Latitude.Listener() {
        public void receive(Measurement measurement) {
            final Latitude lat= (Latitude) measurement;

            RecordActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    latitude = lat.getValue().doubleValue();
                    latitudeTV.setText( String.format("Latitude: %.4f", latitude));
                }
            });
        }
    };

    Longitude.Listener mLongListener = new Longitude.Listener() {
        public void receive(Measurement measurement) {
            final Longitude lng= (Longitude) measurement;

            RecordActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    longitude = lng.getValue().doubleValue();
                    longitudeTV.setText(String.format("Longitude: %.4f", longitude));

                }
            });
        }
    };

    AcceleratorPedalPosition.Listener mAcceleratorPedalPosition = new AcceleratorPedalPosition.Listener() {
        public void receive(Measurement measurement) {

            final AcceleratorPedalPosition aPP= (AcceleratorPedalPosition) measurement;

            RecordActivity.this.runOnUiThread(new Runnable() {
                public void run() {

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
            final FuelConsumed fc= (FuelConsumed) measurement;

            RecordActivity.this.runOnUiThread(new Runnable() {
                public void run() {

                    fuelConsumed = fc.getValue().doubleValue();
                    fuelConsumedTV.setText( String.format("Fuel Consumed: %.2f", fuelConsumed));

                }
            });
        }
    };

    Odometer.Listener mOdometer = new Odometer.Listener() {
        public void receive(Measurement measurement) {

            final Odometer od= (Odometer) measurement;

            RecordActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    odometer = od.getValue().doubleValue();
                    odometerTV.setText( String.format("Odometer: %.2f", odometer));

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
                //toggle.setChecked(false);
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
                toggle.setChecked(false);
                Intent standingsIntent = new Intent(this, StandingsActivity.class);

//                Bundle myBundle = new Bundle();
//                myBundle.putInt("x", 5);
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
    public double getSpeedVal(){
        return speedVal;
    }

    public double getSWAngleVal(){
        return swAngleVal;
    }

    public String getBrakeVal(){
        return brakeVal;
    }

    public void uploadFile(){
        try {
            FileInputStream fis =this.openFileInput(NAME_OF_FILE);
            HttpFileUploader htfu = new HttpFileUploader("ece535-mysql.its.umd.umich.edu/~wcleeper/ServerFileUpload.php","noparamshere", NAME_OF_FILE);
            htfu.doStart(fis);
        } catch (FileNotFoundException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
