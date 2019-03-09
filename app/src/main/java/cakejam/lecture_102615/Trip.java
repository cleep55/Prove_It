package cakejam.lecture_102615;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by WLEEPER on 11/22/2015.
 */
public class Trip {

    ArrayList<Integer> timePoints;
    ArrayList<Double> latPoints;
    ArrayList<Double> longPoints;
    ArrayList<String> deductionType;

    ArrayList<Integer> timeArray;
    ArrayList<Integer> speedArray;
    ArrayList<Integer> brakeArray;
    ArrayList<Integer> cornerArray;
    ArrayList<Integer> fuelArray;
    ArrayList<Integer> overallArray;


    double speedScore, brakeScore, cornerScore, fuelScore, tripTotalScore;
    double driverSpeedScore=0, driverBrakeScore=0, driverCornerScore=0, driverTotalScore=0;
    String endTime, startTime, tripDuration;
    int tripNum;
    long tripTime;
    double driverTotalTime;


    //Trip(tripNum,startTime,endTime,durationTime,tripTime,tripScore,speedScore,brakeScore,cornerScore,
    //timeDeductions,latitudeDeductions,longitudeDeductions,typeDeductions)
    Trip(int tripNum_in, String startTime_in, String endTime_in, String tripDuration_in, long tripTime_in,
         double speedScore_in, double brakeScore_in, double cornerScore_in, double tripTotalScore_in,
         ArrayList<Integer> timePoints_in, ArrayList<Double> latPoints_in,
         ArrayList<Double> longPoints_in, ArrayList<String> deductionType_in){
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
        //scores = {speedScore_in, brakeScore_in, cornerScore_in, fuelScore_in};

    }



    // Constructor with Fuel Score and Scoring Array for charting
    Trip(int tripNum_in, String startTime_in, String endTime_in, String tripDuration_in, long tripTime_in,
         double speedScore_in, double brakeScore_in, double cornerScore_in, double fuelScore_in,double tripTotalScore_in,
         ArrayList<Integer> timeArray_in, ArrayList<Integer> speedArray_in, ArrayList<Integer> brakeArray_in,
         ArrayList<Integer> cornerArray_in,ArrayList<Integer> fuelArray_in, ArrayList<Integer> overallArray_in,
         ArrayList<Integer> timePoints_in, ArrayList<Double> latPoints_in,
         ArrayList<Double> longPoints_in, ArrayList<String> deductionType_in){
        tripNum = tripNum_in;
        startTime = startTime_in;
        endTime = endTime_in;
        speedScore = speedScore_in;
        brakeScore = brakeScore_in;
        cornerScore = cornerScore_in;
        fuelScore = fuelScore_in;
        tripTotalScore = tripTotalScore_in;
        tripDuration = tripDuration_in;
        tripTime = tripTime_in;

        timePoints = timePoints_in;
        latPoints = latPoints_in;
        longPoints = longPoints_in;
        deductionType = deductionType_in;
        //scores = {speedScore_in, brakeScore_in, cornerScore_in, fuelScore_in};

        timeArray=timeArray_in;
        speedArray = speedArray_in;
        brakeArray = brakeArray_in;
        cornerArray = cornerArray_in;
        fuelArray = fuelArray_in;
        overallArray=overallArray_in;

    }

    public double getDriverTotalTime(ArrayList<Trip> allTrips){
        double sum = 0;
        for(int i=0; i<allTrips.size(); i++){
            sum += ((double) allTrips.get(i).tripTime);

        }
        return sum;
    }
    public double getDriverTotalScore(ArrayList<Trip> allTrips){
        double sum = 0;
        for(int i=0; i<allTrips.size(); i++){
            sum += allTrips.get(i).tripTotalScore*allTrips.get(i).tripTime/getDriverTotalTime(allTrips);

        }
        //driverTotalScore = sum/allTrips.size();
        return sum;
    }
    public double getDriverSpeedScore(ArrayList<Trip> allTrips){
        double sum=0;
        for(int i=0; i<allTrips.size(); i++){
            sum += allTrips.get(i).speedScore*allTrips.get(i).tripTime/getDriverTotalTime(allTrips);;

        }
        //driverSpeedScore = sum/allTrips.size();
        return sum;
    }
    public double getDriverBrakeScore(ArrayList<Trip> allTrips){
        double sum = 0;
        for(int i=0; i<allTrips.size(); i++){
            sum += allTrips.get(i).getBrakeScore()*allTrips.get(i).tripTime/getDriverTotalTime(allTrips);;
        }
        //driverBrakeScore = sum/allTrips.size();
        return sum;
    }
    public double getDriverCornerScore(ArrayList<Trip> allTrips){
        double sum = 0;
        for(int i=0; i<allTrips.size(); i++){
            sum += allTrips.get(i).getCornerScore()*allTrips.get(i).tripTime/getDriverTotalTime(allTrips);;
        }
        //driverCornerScore = sum/allTrips.size();
        return sum;
    }

    public double getDriverFuelScore(ArrayList<Trip> allTrips){
        double sum = 0;
        for(int i=0; i<allTrips.size(); i++){
            sum += allTrips.get(i).getFuelScore()*allTrips.get(i).tripTime/getDriverTotalTime(allTrips);;
        }
        //driverCornerScore = sum/allTrips.size();
        return sum;
    }
    public static ArrayList<Double> getAllTimeData(ArrayList<Trip> allTrips){
        ArrayList<Double> allTimeData = new ArrayList<>();
        double minuteCount = 0;
        for(int i=0; i<allTrips.size(); i++){
            for(int j = 0;j<allTrips.get(i).getSpeedArray().size();j++){
                minuteCount += 1;
                allTimeData.add(minuteCount);
            }

        }

        return allTimeData;
    }
    public static ArrayList<Integer> getAllSpeedScoreData(ArrayList<Trip> allTrips) {
        ArrayList<Integer> allSpeedData = new ArrayList<>();
        for (int i = 0; i < allTrips.size(); i++) {
            for (int j = 0; j < allTrips.get(i).getSpeedArray().size(); j++) {
                allSpeedData.add(allTrips.get(i).getSpeedArray().get(j));
            }
            //driverCornerScore = sum/allTrips.size();

        }
        return allSpeedData;
    }

    public static ArrayList<Integer> getAllBrakeScoreData(ArrayList<Trip> allTrips){
        ArrayList<Integer> allBrakeData = new ArrayList<>();
        for(int i=0; i<allTrips.size(); i++){
            for(int j = 0;j<allTrips.get(i).getBrakeArray().size();j++)
                allBrakeData.add(allTrips.get(i).getBrakeArray().get(j));
        }
        //driverCornerScore = sum/allTrips.size();
        return allBrakeData;
    }
    public static ArrayList<Integer> getAllCornerScoreData(ArrayList<Trip> allTrips){
        ArrayList<Integer> allCornerData = new ArrayList<>();
        for(int i=0; i<allTrips.size(); i++){
            for(int j = 0;j<allTrips.get(i).getCornerArray().size();j++)
                allCornerData.add(allTrips.get(i).getCornerArray().get(j));
        }
        //driverCornerScore = sum/allTrips.size();
        return allCornerData;
    }
    public static ArrayList<Integer> getAllFuelScoreData(ArrayList<Trip> allTrips){
        ArrayList<Integer> allFuelData = new ArrayList<>();
        for(int i=0; i<allTrips.size(); i++){
            for(int j = 0;j<allTrips.get(i).getFuelArray().size();j++)
                allFuelData.add(allTrips.get(i).getFuelArray().get(j));
        }
        //driverCornerScore = sum/allTrips.size();
        return allFuelData;
    }
    public static ArrayList<Integer> getAllOverallScoreData(ArrayList<Trip> allTrips){
        ArrayList<Integer> allOverallData = new ArrayList<>();
        for(int i=0; i<allTrips.size(); i++){
            for(int j = 0;j<allTrips.get(i).getOverallArray().size();j++)
                allOverallData.add(allTrips.get(i).getOverallArray().get(j));
        }
        //driverCornerScore = sum/allTrips.size();
        return allOverallData;
    }

    public ArrayList<Integer> getTimeArray() {
        return timeArray;
    }

    public void setTimeArray(ArrayList<Integer> timeArray) {
        this.timeArray = timeArray;
    }

    public ArrayList<Integer> getSpeedArray() {
        return speedArray;
    }

    public void setSpeedArray(ArrayList<Integer> speedArray) {
        this.speedArray = speedArray;
    }

    public ArrayList<Integer> getBrakeArray() {
        return brakeArray;
    }

    public void setBrakeArray(ArrayList<Integer> brakeArray) {
        this.brakeArray = brakeArray;
    }

    public ArrayList<Integer> getCornerArray() {
        return cornerArray;
    }

    public void setCornerArray(ArrayList<Integer> cornerArray) {
        this.cornerArray = cornerArray;
    }

    public ArrayList<Integer> getFuelArray() {
        return fuelArray;
    }

    public void setFuelArray(ArrayList<Integer> fuelArray) {
        this.fuelArray = fuelArray;
    }

    public ArrayList<Integer> getOverallArray() {
        return overallArray;
    }

    public void setOverallArray(ArrayList<Integer> overallArray) {
        this.overallArray = overallArray;
    }

    public void setFuelScore(double fuelScore) {
        this.fuelScore = fuelScore;
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

    public double getFuelScore() {
        return fuelScore;
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
}
