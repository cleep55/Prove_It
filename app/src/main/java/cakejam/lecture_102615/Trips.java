package cakejam.lecture_102615;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;

/**
 * Created by WLEEPER on 11/22/2015.
 */
public class Trips {

    ArrayList<Integer> timePoints;
    ArrayList<Double> latPoints;
    ArrayList<Double> longPoints;
    ArrayList<String> deductionType;
    double speedScore, brakeScore, cornerScore, tripTotalScore;
    double driverSpeedScore=0, driverBrakeScore=0, driverCornerScore=0, driverTotalScore=0;
    String endTime, startTime, tripDuration;
    int tripNum;
    long tripTime;
    double driverTotalTime;



    Trips(int tripNum_in, String startTime_in, String endTime_in, double speedScore_in, double brakeScore_in,
          double cornerScore_in, double tripTotalScore_in, String tripDuration_in,
          ArrayList<Integer> timePoints_in, ArrayList<Double> latPoints_in,
          ArrayList<Double> longPoints_in, ArrayList<String> deductionType_in, long tripTime_in){
        tripNum = tripNum_in;
        startTime = startTime_in;
        endTime = endTime_in;
        speedScore = speedScore_in;
        brakeScore = brakeScore_in;
        cornerScore = cornerScore_in;
        tripTotalScore = tripTotalScore_in;
        tripDuration = tripDuration_in;
        tripTime = tripTime_in;

        timePoints = timePoints_in;
        latPoints = latPoints_in;
        longPoints = longPoints_in;
        deductionType = deductionType_in;

    }
    public double getDriverTotalTime(ArrayList<Trips> allTrips){
        double sum = 0;
        for(int i=0; i<allTrips.size(); i++){
            sum += ((double) allTrips.get(i).tripTime);

        }
        return sum;
    }
    public double getDriverTotalScore(ArrayList<Trips> allTrips){
        double sum = 0;
        for(int i=0; i<allTrips.size(); i++){
            sum += allTrips.get(i).tripTotalScore*allTrips.get(i).tripTime/getDriverTotalTime(allTrips);

        }
        //driverTotalScore = sum/allTrips.size();
        return sum;
    }
    public double getDriverSpeedScore(ArrayList<Trips> allTrips){
        double sum=0;
        for(int i=0; i<allTrips.size(); i++){
            sum += allTrips.get(i).speedScore*allTrips.get(i).tripTime/getDriverTotalTime(allTrips);;

        }
        //driverSpeedScore = sum/allTrips.size();
        return sum;
    }
    public double getDriverBrakeScore(ArrayList<Trips> allTrips){
        double sum = 0;
        for(int i=0; i<allTrips.size(); i++){
            sum += allTrips.get(i).getBrakeScore()*allTrips.get(i).tripTime/getDriverTotalTime(allTrips);;
        }
        //driverBrakeScore = sum/allTrips.size();
        return sum;
    }
    public double getDriverCornerScore(ArrayList<Trips> allTrips){
        double sum = 0;
        for(int i=0; i<allTrips.size(); i++){
            sum += allTrips.get(i).getCornerScore()*allTrips.get(i).tripTime/getDriverTotalTime(allTrips);;
        }
        //driverCornerScore = sum/allTrips.size();
        return sum;
    }

    public ArrayList<Integer> getTimePoints() {
        return timePoints;
    }

    public ArrayList<Double> getLatPoints() {
        return latPoints;
    }

    public ArrayList<Double> getLongPoints() {
        return longPoints;
    }

    public ArrayList<String> getDeductionType() {
        return deductionType;
    }

    public double getSpeedScore() {
        return speedScore;
    }

    public double getBrakeScore() {
        return brakeScore;
    }

    public double getCornerScore() {
        return cornerScore;
    }

    public double getTripTotalScore() {
        return tripTotalScore;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getStartTime() {
        //String startTimeFormatted = DateFormat.getDateTimeInstance().format(startTime);
        return startTime;
    }

    public String getTripDuration() {
        return tripDuration;
    }

    public int getTripNum() {
        return tripNum;
    }
    public long getTripTime() {
        return tripTime;
    }

    public void setTimePoints(ArrayList<Integer> timePoints) {
        this.timePoints = timePoints;
    }

    public void setLatPoints(ArrayList<Double> latPoints) {
        this.latPoints = latPoints;
    }

    public void setLongPoints(ArrayList<Double> longPoints) {
        this.longPoints = longPoints;
    }

    public void setDeductionType(ArrayList<String> deductionType) {
        this.deductionType = deductionType;
    }

    public void setSpeedScore(double speedScore) {
        this.speedScore = speedScore;
    }

    public void setBrakeScore(double brakeScore) {
        this.brakeScore = brakeScore;
    }

    public void setCornerScore(double cornerScore) {
        this.cornerScore = cornerScore;
    }

    public void setTripTotalScore(double tripTotalScore) {
        this.tripTotalScore = tripTotalScore;
    }

    public void setDriverSpeedScore(double driverSpeedScore) {
        this.driverSpeedScore = driverSpeedScore;
    }

    public void setDriverBrakeScore(double driverBrakeScore) {
        this.driverBrakeScore = driverBrakeScore;
    }

    public void setDriverCornerScore(double driverCornerScore) {
        this.driverCornerScore = driverCornerScore;
    }

    public void setDriverTotalScore(double driverTotalScore) {
        this.driverTotalScore = driverTotalScore;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setTripDuration(String tripDuration) {
        this.tripDuration = tripDuration;
    }

    public void setTripNum(int tripNum) {
        this.tripNum = tripNum;
    }

    public void setTripTime(long tripTime) {
        this.tripTime = tripTime;
    }

    public void setDriverTotalTime(double driverTotalTime) {
        this.driverTotalTime = driverTotalTime;
    }
}
