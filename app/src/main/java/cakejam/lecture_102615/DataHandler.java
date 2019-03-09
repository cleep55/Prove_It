package cakejam.lecture_102615;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wleeper on 12/2/2015.
 */
public class DataHandler {

    public static final String ID = "_tripNum";
    // 0. WILL ALWAYS BE ASSIGNED TO THE TOTAL TRIP
    // 1. WILL ALWAYS BE THE MOST RECENT TRIP
    // 2. WILL ALWAYS BE THE 2 MOST RECENT TRIP
    // 3. WILL ALWAYS BE THE 3 MOST RECENT TRIP
    // 4. WILL ALWAYS BE THE 4 MOST RECENT TRIP
    // 5. WILL ALWAYS BE THE 5 MOST RECENT TRIP
    public static final String START_TIME = "startTime"; //String value for start time
    public static final String END_TIME = "endTime"; //String value for end time
    public static final String DURATION_TIME = "durationTime"; //string value for how long the trip was
    public static final String TRIP_TIME = "tripTime"; //long value for how long the trip
    public static final String TRIP_SCORE = "tripScore";//double value for trip score
    public static final String SPEED_SCORE = "speedScore";//double value for speed score
    public static final String BRAKE_SCORE = "brakeScore";//double value for brake score
    public static final String CORNER_SCORE = "cornerScore";//double value for corner score
    public static final String FUEL_SCORE = "fuelScore";//double value for fuel score
    public static final String TIME_DEDUCTION = "timeDeduction";//Array of ints for each deduction time
    public static final String LATITUDE_DEDUCTION = "latitudeDeduction";//Array of doubles for each deduction latitude
    public static final String LONGITUDE_DEDUCTION = "longitudeDeduction";//Array of double for each deduction longitude
    public static final String TYPE_DEDUCTION = "typeDeduction";//Array of strings for each deduction type
    public static final String TIME_PROGRESS = "timeProgress"; //Array of minutes completed during trip in which Progressed is tracked
    public static final String SPEED_PROGRESS = "speedProgress"; //Array of speed score tracked for each minute completed during trip
    public static final String BRAKE_PROGRESS = "brakeProgress"; //Array of brake score tracked for each minute completed during trip
    public static final String CORNER_PROGRESS = "cornerProgress"; //Array of corner score tracked for each minute completed during trip
    public static final String FUEL_PROGRESS = "fuelProgress"; //Array of speed fuel tracked for each minute completed during trip
    public static final String OVERALL_PROGRESS = "overallProgress"; //Array of overall score tracked for each minute completed during trip

    public static final String TABLE_NAME = "tripsTable";
    public static final String DATA_BASE_NAME = "tripsDatabase.sqlite"; //mydatabase.db
    public static final int DATABASE_VERSION= 2;//5
    public static final String TABLE_CREATE =
            "CREATE TABLE "+TABLE_NAME+" (" +
                    ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    START_TIME +" TEXT, " +
                    END_TIME +" TEXT, " +
                    DURATION_TIME +" TEXT, " +
                    TRIP_TIME +" TEXT, " +
                    TRIP_SCORE +" TEXT, " +
                    SPEED_SCORE +" TEXT, " +
                    BRAKE_SCORE +" TEXT, " +
                    CORNER_SCORE +" TEXT, " +
                    FUEL_SCORE +" TEXT, " +
                    TIME_DEDUCTION +" TEXT, " +
                    LATITUDE_DEDUCTION +" TEXT, " +
                    LONGITUDE_DEDUCTION +" TEXT, " +
                    TYPE_DEDUCTION +" TEXT, " +
                    TIME_PROGRESS +" TEXT, " +
                    SPEED_PROGRESS +" TEXT, " +
                    BRAKE_PROGRESS +" TEXT, " +
                    CORNER_PROGRESS +" TEXT, " +
                    FUEL_PROGRESS +" TEXT, " +
                    OVERALL_PROGRESS+" TEXT);";

    DataBaseHelper dbHelper;
    Context ctx;
    SQLiteDatabase db;

    public DataHandler(Context ctx_in){
        ctx = ctx_in;
        dbHelper = new DataBaseHelper(ctx);
    }

    private static class DataBaseHelper extends SQLiteOpenHelper{

        public DataBaseHelper(Context ctx_in){
            super(ctx_in,DATA_BASE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try{
                db.execSQL(TABLE_CREATE);
            }
            catch (android.database.SQLException e){
                e.printStackTrace();
            }


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
            onCreate(db);

        }
    }

//    public DataHandler open(){
//        db = dbHelper.getWritableDatabase();
//        return this;
//    }

    //    public void close(){
//        dbHelper.close();
//    }
    public long insertData(Trip recentTrip){
//    public long insertData(Integer tripNum_in, String startTime_in, String endTime_in, String duration_in, long tripTime_in,
//                           double tripScore_in, double speedScore_in, double brakeScore_in, double cornerScore_in,
//                            String timeDeduction_in, String latitudeDeduciton_in, String longitudeDeduction_in, String typeDeduction_in){
//    public long insertData(String arrayList, String name_in, String password_in){
        db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
//        contentValues.put(ARRAYLIST, arrayList);
        contentValues.put(ID, recentTrip.getTripNum());
        contentValues.put(START_TIME, recentTrip.getStartTime());
        contentValues.put(END_TIME, recentTrip.getEndTime());
        contentValues.put(DURATION_TIME,recentTrip.getTripDuration());
        contentValues.put(TRIP_TIME, recentTrip.getTripTime());
        contentValues.put(TRIP_SCORE,recentTrip.getTripTotalScore());
        contentValues.put(SPEED_SCORE,recentTrip.getSpeedScore());
        contentValues.put(BRAKE_SCORE,recentTrip.getBrakeScore());
        contentValues.put(CORNER_SCORE,recentTrip.getCornerScore());
        contentValues.put(FUEL_SCORE,recentTrip.getFuelScore());

        String timeDeductionsString = arrayList2JSONstring(recentTrip.getTimePoints());
        contentValues.put(TIME_DEDUCTION, timeDeductionsString);
        String latitudeDeductionsString = arrayList2JSONstring(recentTrip.getLatPoints());
        contentValues.put(LATITUDE_DEDUCTION,latitudeDeductionsString);
        String longitudeDeductionsString = arrayList2JSONstring(recentTrip.getLongPoints());
        contentValues.put(LONGITUDE_DEDUCTION,longitudeDeductionsString);
        String typeDeductionsString = arrayList2JSONstring(recentTrip.getDeductionType());
        contentValues.put(TYPE_DEDUCTION,typeDeductionsString);

        String minuteStr = arrayList2JSONstring(recentTrip.getTimeArray());
        contentValues.put(TIME_PROGRESS,minuteStr);
        String speedArrayStr = arrayList2JSONstring(recentTrip.getSpeedArray());
        contentValues.put(SPEED_PROGRESS,speedArrayStr);
        String brakeArrayStr = arrayList2JSONstring(recentTrip.getBrakeArray());
        contentValues.put(BRAKE_PROGRESS,brakeArrayStr);
        String cornerArrayStr = arrayList2JSONstring(recentTrip.getCornerArray());
        contentValues.put(CORNER_PROGRESS,cornerArrayStr);
        String fuelArrayStr = arrayList2JSONstring(recentTrip.getFuelArray());
        contentValues.put(FUEL_PROGRESS,fuelArrayStr);
        String overallArrayStr = arrayList2JSONstring(recentTrip.getOverallArray());
        contentValues.put(OVERALL_PROGRESS,overallArrayStr);

// TIME, SPEED, BRAKE, CORNER, FUEL, OVERALL
        long index = db.insertOrThrow(TABLE_NAME, null, contentValues);
        db.close();
        return index;
    }

//    public Cursor returnData(){
//        return db.query(TABLE_NAME,new String[] {START_TIME,END_TIME},null,null,null,null,null);
//    }

    public ArrayList<Trip> getAllTrips(){
        ArrayList<Trip> trips = new ArrayList<>();

        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_NAME, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            // all database information
//            String array = cursor.getString(cursor.getColumnIndex(ARRAYLIST));
            int tripNum = cursor.getInt(cursor.getColumnIndex(ID));
            String startTime= cursor.getString(cursor.getColumnIndex(START_TIME));
            String endTime = cursor.getString(cursor.getColumnIndex(END_TIME));
            String durationTime = cursor.getString(cursor.getColumnIndex(DURATION_TIME));
            long tripTime = cursor.getLong(cursor.getColumnIndex(TRIP_TIME));
            double tripScore = cursor.getDouble(cursor.getColumnIndex(TRIP_SCORE));
            double speedScore = cursor.getDouble(cursor.getColumnIndex(SPEED_SCORE));
            double brakeScore = cursor.getDouble(cursor.getColumnIndex(BRAKE_SCORE));
            double cornerScore = cursor.getDouble(cursor.getColumnIndex(CORNER_SCORE));
            double fuelScore = cursor.getDouble(cursor.getColumnIndex(FUEL_SCORE));

            String timeDeductionString = cursor.getString(cursor.getColumnIndex(TIME_DEDUCTION));
            String latitudeDeducitonString = cursor.getString(cursor.getColumnIndex(LATITUDE_DEDUCTION));
            String longitudeDeductionString = cursor.getString(cursor.getColumnIndex(LONGITUDE_DEDUCTION));
            String typeDeductionString = cursor.getString(cursor.getColumnIndex(TYPE_DEDUCTION));
            ArrayList<Integer> timeDeductions = jsonString2IntArray(timeDeductionString);
            ArrayList<Double> latitudeDeductions = jsonString2DoubleArray(latitudeDeducitonString);
            ArrayList<Double> longitudeDeductions = jsonString2DoubleArray(longitudeDeductionString);
            ArrayList<String> typeDeductions = jsonString2StringArray(typeDeductionString);

            String timeProgressString = cursor.getString(cursor.getColumnIndex(TIME_PROGRESS));
            String speedProgressString = cursor.getString(cursor.getColumnIndex(SPEED_PROGRESS));
            String brakeProgressString = cursor.getString(cursor.getColumnIndex(BRAKE_PROGRESS));
            String cornerProgressString = cursor.getString(cursor.getColumnIndex(CORNER_PROGRESS));
            String fuelProgressString = cursor.getString(cursor.getColumnIndex(FUEL_PROGRESS));
            String overallProgressString = cursor.getString(cursor.getColumnIndex(OVERALL_PROGRESS));
            ArrayList<Integer> timeProgressArray = jsonString2IntArray(timeProgressString);
            ArrayList<Integer> speedProgressArray = jsonString2IntArray(speedProgressString);
            ArrayList<Integer> brakeProgressArray = jsonString2IntArray(brakeProgressString);
            ArrayList<Integer> cornerProgressArray = jsonString2IntArray(cornerProgressString);
            ArrayList<Integer> fuelProgressArray = jsonString2IntArray(fuelProgressString);
            ArrayList<Integer> overallProgressArray = jsonString2IntArray(overallProgressString);


            Trip tripTemp = new Trip(tripNum,startTime,endTime,durationTime,tripTime,
                    speedScore,brakeScore,cornerScore, fuelScore, tripScore,
                    timeProgressArray, speedProgressArray, brakeProgressArray,
                    cornerProgressArray, fuelProgressArray, overallProgressArray,
                    timeDeductions,latitudeDeductions,longitudeDeductions,typeDeductions);
            trips.add(tripTemp);
            cursor.moveToNext();

// TIME, SPEED, BRAKE, CORNER, FUEL, OVERALL
//            Users userTemp = new Users(array,name,password);
//            Trip tripTemp = new Trip(tripNum,startTime,endTime,durationTime,tripTime,
//                    tripScore,speedScore,brakeScore,cornerScore,
//                    timeDeductions,latitudeDeductions,longitudeDeductions,typeDeductions);
//            trips.add(tripTemp);
//            cursor.moveToNext();

        }
        db.close();
        return trips;
    }
    public String getUserPassword(String username){
        db = dbHelper.getWritableDatabase();
        String[] nameArray = {username};
        //query(String table, String[] projection, String selection, String[] selectionArg, groupBy, having, orderBy);
        // table = the name of the table you want to search
        // projection = the columns you want to retrieve
        // selection = category you want to filter by
        // selectionArg = values you want from category
        // groupBy – a filter for grouping the rows
        // having – a filter for which row groups to include
        // orderBy – how the rows should be ordered
        String pw ="";
        Cursor c = db.query(TABLE_NAME, new String[]{START_TIME, END_TIME}, START_TIME + " = ? ", nameArray, null, null, null);

        if(c.moveToFirst()){
            pw = c.getString(c.getColumnIndexOrThrow(END_TIME));
        }
        else {
            pw = "No Such User";
        }

        //using a rawQuery instead
//        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE name Like \'" + username + "\'", null);
//        cursor.moveToFirst();
//        String pw2 = cursor.getString(cursor.getColumnIndex(END_TIME));

        //Practice retrieving array
//        String arrayString = cursor.getString(cursor.getColumnIndex(ARRAYLIST));
//        ArrayList<Double> stringArray = jsonString2DoubleArray(arrayString);
//        String arrayFinal ="\nArrayList: \n1. "+stringArray.get(0)+"\n2. "+stringArray.get(1)+"\n3. "+stringArray.get(2);
//        pw2 = cursor.getString(cursor.getColumnIndex(END_TIME))+arrayFinal;
        db.close();
        return pw;
    }

    public void deleteUser(String username){
        String[] userSearchArg = {username};
        db = dbHelper.getWritableDatabase();
        db.delete(TABLE_NAME,"name = ? ",userSearchArg);
        db.close();
    }

    public void deleteTable(){
        db = dbHelper.getWritableDatabase();
        db.delete(TABLE_NAME,null,null);
        db.close();
    }

    public String arrayList2JSONstring(ArrayList<?> array_in){
        String arrayString = "";
        for(int i=0;i<array_in.size();i++){

        }
//        inputArray.add("first");
//        inputArray.add("second");
//        inputArray.add("third");
        JSONObject json = new JSONObject();
        try{
            json.put("inputArray",new JSONArray(array_in));
        }
        catch (JSONException e){
            throw new RuntimeException(e);
        }
        arrayString = json.toString();
        return arrayString;
    }

    public ArrayList<String> jsonString2StringArray(String jsonArrayString){
        //ArrayList<?> arrayList = new ArrayList<>();
        JSONObject object = null;
        try {
            object = new JSONObject(jsonArrayString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray;
        try {
            jsonArray = object.getJSONArray("inputArray");
        } catch (JSONException e) {
            e.printStackTrace();
            jsonArray = new JSONArray();
        }

        ArrayList<String> stringArray = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                stringArray.add(jsonArray.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return stringArray;
    }

    public ArrayList<Integer> jsonString2IntArray(String jsonArrayString){
        //ArrayList<?> arrayList = new ArrayList<>();
        JSONObject object = null;
        try {
            object = new JSONObject(jsonArrayString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray;
        try {
            jsonArray = object.getJSONArray("inputArray");
        } catch (JSONException e) {
            e.printStackTrace();
            jsonArray = new JSONArray();
        }

        ArrayList<Integer> intArray = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                intArray.add(jsonArray.getInt(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return intArray;
    }

    public ArrayList<Double> jsonString2DoubleArray(String jsonArrayString){
        //ArrayList<?> arrayList = new ArrayList<>();
        JSONObject object = null;
        try {
            object = new JSONObject(jsonArrayString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray;
        try {
            jsonArray = object.getJSONArray("inputArray");
        } catch (JSONException e) {
            e.printStackTrace();
            jsonArray = new JSONArray();
        }

        ArrayList<Double> doubleArray = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                doubleArray.add(jsonArray.getDouble(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return doubleArray;
    }
    //        START_TIME
//        END_TIME
//        DURATION_TIME
//        TRIP_TIME
//        TRIP_SCORE
//        SPEED_SCORE
//        BRAKE_SCORE
//        CORNER_SCORE
//        TIME_DEDUCTION
//        LATITUDE_DEDUCTION
//        LONGITUDE_DEDUCTION
//        TYPE_DEDUCTION

}