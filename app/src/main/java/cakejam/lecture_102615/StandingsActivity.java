package cakejam.lecture_102615;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class StandingsActivity extends AppCompatActivity {
    private double accelTHRESH, speedTHRESH, brakeTHRESH, swSharpTHRESH, speedSharpTHRESH;
    private double swSlightTHRESH, speedSlightTHRESH;
    int freqScoreCollection, freqSpeedDeduction, freqCorneringDeduction, freqPosScore, startScoreCollection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        accelTHRESH=4; speedTHRESH=.004; brakeTHRESH=-.004; swSharpTHRESH=30; speedSharpTHRESH = 17;
        swSlightTHRESH=15; speedSlightTHRESH=30;

        freqScoreCollection = 5000;// 1 minute (1,000 = 1 second)
        freqSpeedDeduction = 250;
        freqCorneringDeduction = 500;
        freqPosScore = 10000;
        startScoreCollection = 10000;



        final RecordActivity recordActivity = new RecordActivity();

        final SeekBar accelSB = (SeekBar) findViewById(R.id.accel_threshold);
        final SeekBar brakeSB = (SeekBar) findViewById(R.id.brake_threshold);
        final SeekBar speedSB = (SeekBar) findViewById(R.id.speed_threshold);
        final SeekBar swSharpSB = (SeekBar) findViewById(R.id.swAngleSharp_threshold);
        final SeekBar speedSharpSB = (SeekBar) findViewById(R.id.swSpeedSharp_threshold);
        final SeekBar swSlightSB = (SeekBar) findViewById(R.id.swAngleSlight_threshold);
        final SeekBar speedSlightSB = (SeekBar) findViewById(R.id.swSpeedSlight_threshold);
        final SeekBar startScoreCollSB = (SeekBar) findViewById(R.id.startScoring_threshold);
        final SeekBar scoreCollFreqSB = (SeekBar) findViewById(R.id.scoreColl_freq);
        final SeekBar posScoreFreqSB = (SeekBar) findViewById(R.id.posScore_freq);
        final SeekBar speedDeductFreqSB = (SeekBar) findViewById(R.id.speedDeduct_freq);
        final SeekBar cornerDeductFreqSB = (SeekBar) findViewById(R.id.cornerDeduct_freq);
        final Button deleteButton = (Button) findViewById(R.id.deleteDB);

        final TextView accelTV = (TextView) findViewById(R.id.accel_val);
        final TextView brakeTV = (TextView) findViewById(R.id.brake_thresh_val);
        final TextView speedTV = (TextView) findViewById(R.id.speed_thresh_val);
        final TextView swSharpTV = (TextView) findViewById(R.id.swSharp_thresh_val);
        final TextView speedSharpTV = (TextView) findViewById(R.id.speedSharp_thresh_val);
        final TextView swSlightTV = (TextView) findViewById(R.id.swSlight_thresh_val);
        final TextView speedSlightTV = (TextView) findViewById(R.id.speedSlight_thresh_val);
        final TextView startScoreCollTV = (TextView) findViewById(R.id.startScoring_thresh_val);
        final TextView scoreCollFreqTV = (TextView) findViewById(R.id.scoreColl_freq_val);
        final TextView posScoreFreqTV = (TextView) findViewById(R.id.posScore_freq_val);
        final TextView speedDeductFreqTV = (TextView) findViewById(R.id.speedDeduct_freq_val);
        final TextView cornerDeductTV = (TextView) findViewById(R.id.cornerDeduct_freq_val);

//        accelTV.setText(accelTV.getText()+": "+accelSB.getProgress());
//        brakeTV.setText(brakeTV.getText()+": "+brakeSB.getProgress());
//        speedTV.setText(speedTV.getText()+": "+speedSB.getProgress());
//        swSharpTV.setText(swSharpTV.getText()+": "+swSharpSB.getProgress());
//        speedSharpTV.setText(speedSharpTV.getText()+": "+speedSharpSB.getProgress());
//        swSlightTV.setText(swSlightTV.getText()+": "+swSlightSB.getProgress());
//        speedSlightTV.setText(speedSlightTV.getText()+": "+speedSlightSB.getProgress());


        accelSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                accelTV.setText(""+progress);
                accelTHRESH = (double) progress*0.5;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        brakeSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brakeTV.setText(""+progress);
                double progDub = (double) progress;
                brakeTHRESH = progDub * (-.0002);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        speedSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speedTV.setText(""+progress);
                double progDub = (double) progress;
                speedTHRESH = progDub * .0002;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        swSharpSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                swSharpTV.setText(""+progress);
                swSharpTHRESH = (double) progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        speedSharpSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speedSharpTV.setText(""+progress);
                speedSharpTHRESH = (double) progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        swSlightSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                swSlightTV.setText(""+progress);
                swSlightTHRESH = (double) progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        speedSlightSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speedSlightTV.setText(""+progress);
                speedSlightTHRESH = (double) progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        startScoreCollSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                startScoreCollTV.setText(""+progress);
                startScoreCollection = 10000* progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        scoreCollFreqSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                scoreCollFreqTV.setText(""+progress);
                freqScoreCollection = 10000* progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        posScoreFreqSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                posScoreFreqTV.setText(""+progress);
                freqPosScore = 10000* progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        speedDeductFreqSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speedDeductFreqTV.setText(""+progress);
                freqSpeedDeduction = 10* progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        cornerDeductFreqSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cornerDeductTV.setText(""+progress);
                freqCorneringDeduction = 10* progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

//        Intent intent = this.getIntent();
//        Bundle bundle = intent.getExtras();
//        int x = bundle.getInt("x");
//        String str = bundle.getString("str");
//
//        TextView textView = (TextView) findViewById(R.id.TV);
//        textView.setText("str: " + str + " x: " + x);
    }

    public double getAccelTHRESH() {
        return accelTHRESH;
    }

    public void setAccelTHRESH(double accelTHRESH) {
        this.accelTHRESH = accelTHRESH;
    }

    public double getSpeedTHRESH() {
        return speedTHRESH;
    }

    public void setSpeedTHRESH(double speedTHRESH) {
        this.speedTHRESH = speedTHRESH;
    }

    public double getBrakeTHRESH() {
        return brakeTHRESH;
    }

    public void setBrakeTHRESH(double brakeTHRESH) {
        this.brakeTHRESH = brakeTHRESH;
    }

    public double getSwSharpTHRESH() {
        return swSharpTHRESH;
    }

    public void setSwSharpTHRESH(double swSharpTHRESH) {
        this.swSharpTHRESH = swSharpTHRESH;
    }

    public double getSpeedSharpTHRESH() {
        return speedSharpTHRESH;
    }

    public void setSpeedSharpTHRESH(double speedSharpTHRESH) {
        this.speedSharpTHRESH = speedSharpTHRESH;
    }

    public double getSwSlightTHRESH() {
        return swSlightTHRESH;
    }

    public void setSwSlightTHRESH(double swSlightTHRESH) {
        this.swSlightTHRESH = swSlightTHRESH;
    }

    public double getSpeedSlightTHRESH() {
        return speedSlightTHRESH;
    }

    public void setSpeedSlightTHRESH(double speedSlightTHRESH) {
        this.speedSlightTHRESH = speedSlightTHRESH;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void buttonHandler(View view){
        final DataHandler handler =  new DataHandler(getBaseContext());
        handler.deleteTable();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_Record:
                // User chose the "Settings" item, show the app settings UI...
                Intent recordIntent = new Intent(this, RecordActivity.class);
                Bundle myBundle = new Bundle();
                myBundle.putDouble("Accel Thresh", accelTHRESH);
                myBundle.putDouble("Brake Thresh", brakeTHRESH);
                myBundle.putDouble("Speed Thresh", speedTHRESH);
                myBundle.putDouble("SW Sharp Thresh", swSharpTHRESH);
                myBundle.putDouble("Speed Sharp Thresh", speedSharpTHRESH);
                myBundle.putDouble("SW Slight Thresh", swSlightTHRESH);
                myBundle.putDouble("Speed Slight Thresh", speedSlightTHRESH);
                myBundle.putInt("Start Score Collection", startScoreCollection);
                myBundle.putInt("Score Collection Freq", freqScoreCollection);
                myBundle.putInt("Positive Score Freq", freqPosScore);
                myBundle.putInt("Speed Deduct Freq", freqSpeedDeduction);
                myBundle.putInt("Corner Deduct Freq", freqCorneringDeduction);
                recordIntent.putExtras(myBundle);
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
//    public void buttonHandler(View v) {
//        Intent myIntent = new Intent(this, SummaryActivity.class);
//
//
//        Bundle myBundle = new Bundle();
//        myBundle.putInt("x",5);
//        myBundle.putString("str","hello");
//        myIntent.putExtras(myBundle);
//
//        startActivity(myIntent);
//    }
//    public void buttonViewAction(View v) {
//        Intent myIntent = new Intent(Intent.ACTION_VIEW);
//        myIntent.setData(Uri.parse("http://www.umich.edu"));
//        startActivity(myIntent);
//    }

}
