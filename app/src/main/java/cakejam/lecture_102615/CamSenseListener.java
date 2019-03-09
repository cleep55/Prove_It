package cakejam.lecture_102615;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import com.openxc.VehicleManager;
import com.openxc.measurements.BrakePedalStatus;
import com.openxc.measurements.Measurement;
import com.openxc.measurements.SteeringWheelAngle;
import com.openxc.measurements.VehicleSpeed;

/**
 * Created by wleeper on 11/19/2015.
 */
public class CamSenseListener implements SensorEventListener {
    TextView accelTV;
    TextView scoreTV;
    public double accelMag;
    public double accelXDouble;
    public double accelYDouble;
    public double accelZDouble;
    public double prevaccelMag;
    public double prevaccelXDouble;
    public double prevaccelYDouble;
    public double prevaccelZDouble;
    public int tempColor;
    double sScore;
    double bScore;
    double cScore;
    public double speedVal, swAngleVal, speedVal2;
    public String brakeVal;
    private static final String TAG = "MainActivity";
    int currTime, prevTime;
    RecordActivity recordActivity;


    CamSenseListener(TextView acceltv_in, TextView scoretv_in, int color_in, double speedScore_in,
                     double brakeScore_in, double cornerScore_in, double speedVal_in,
                     String brakeVal_in, double swVal_in){

        accelTV = acceltv_in;
        scoreTV = scoretv_in;
        tempColor = color_in;
        sScore = speedScore_in;
        bScore = brakeScore_in;
        cScore = cornerScore_in;
        speedVal = speedVal_in;
        swAngleVal = swVal_in;
        brakeVal = brakeVal_in;
        prevaccelXDouble = 0;
        prevaccelYDouble = 0;
        prevaccelXDouble = 0;
        prevaccelMag = 0;
        currTime = 0;
        prevTime = 0;
        //speedVal2 = recordActivity.getSpeedVal();

    }
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
                    "\nAccel Magnitude:  " + accelMag);// +"\n"+speedVal+"\n"+brakeVal+"\n"+swAngleVal);

        //RecordActivity.class.get


        if (Math.abs(accelMag - prevaccelMag) > 1)
        {
            if(currTime-prevTime>500) {

                accelTV.setBackgroundColor(tempColor);

                if (Math.abs(speedVal) > 5) {
                    cScore--;
                    scoreTV.setText("Speed: " + sScore +
                            "\nBrake: " + bScore +
                            "\nCorner: " + cScore);
                    Log.v(TAG, "" + Math.abs(swAngleVal));
                }
                else {
                    if (brakeVal == "off") {
                        sScore--;
                        scoreTV.setText("Speed: " + sScore +
                                "\nBrake: " + bScore +
                                "\nCorner: " + cScore);
                    }
                    else {
                        bScore--;
                        scoreTV.setText("Speed: " + sScore +
                                "\nBrake: " + bScore +
                                "\nCorner: " + cScore);
                    }

                }
            }
            prevTime = currTime;
        }
        else accelTV.setBackgroundColor(Color.WHITE);

        prevaccelMag = accelMag;



//        if(accelMag > 11)
//        {
//            accelTV.setBackgroundColor(tempColor);
//            if(accelXDouble<-5){
//                sScore--;
//                scoreTV.setText("Speed: "+sScore+
//                "\nBrake: "+bScore+
//                "\nCorner: "+cScore);
//            }
//            else{
//                bScore--;
//                scoreTV.setText("Speed: "+sScore+
//                        "\nBrake: "+bScore+
//                        "\nCorner: "+cScore);
//            }
//            if(Math.sqrt(Math.pow(accelYDouble,2))>1.1){
//                cScore--;
//                scoreTV.setText("Speed: "+sScore+
//                        "\nBrake: "+bScore+
//                        "\nCorner: "+cScore);
//            }
//
//        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}
