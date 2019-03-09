package cakejam.lecture_102615;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MyTripsActivity extends AppCompatActivity {
    Button trip1Button, trip2Button, trip3Button, trip4Button, trip5Button;
    RecordActivity recordActivity = new RecordActivity();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trips);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ArrayList<Trip> trips = recordActivity.trips;

       //trip1Button = (Button) findViewById(R.id.trip1);
//        String testString = "TopLeft TopCenter TopRight BottomLeft BottomCenter BottomRight"
//        Spannable spanString = new SpannableString(testString);
//        spanString.setSpan(new );

       // trip1Button.setText("Date");
        if(RecordActivity.trips.size()>=1 && !RecordActivity.trips.get(trips.size()-1).getStartTime().equals(null)){

            trip1Button = (Button) findViewById(R.id.trip1);
            String htmlText = "<table style='width:100%'>"
                    + "<tr>"
                    + "<td>" +
                    RecordActivity.trips.get(trips.size()-1).getStartTime() + "</td>"
                    + "<td>" + " &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    String.format("%.2f", RecordActivity.trips.get(trips.size()-1).getTripTotalScore())
                    + "</td>"
                    + "<td>" + " &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; &nbsp;"+
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    RecordActivity.trips.get(trips.size()-1).getTripDuration()
                    + "</td>"
                    + "</tr>"
                    + "<br>"
                    + "<tr>"
                    + "<td>" +
                    RecordActivity.trips.get(trips.size()-1).getSpeedScore() + "</td>"
                    + "<td>" + "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    RecordActivity.trips.get(trips.size()-1).getBrakeScore()
                    + "</td>"
                    + "<td>" + "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "+
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    RecordActivity.trips.get(trips.size()-1).getCornerScore()
                    + "</td>"
                    + "</tr>"
                    + "</table>";
            trip1Button.setText(Html.fromHtml(htmlText));
        }

        if(RecordActivity.trips.size()>=2 && !RecordActivity.trips.get(trips.size() - 2).getStartTime().equals(null)) {

                trip2Button = (Button) findViewById(R.id.trip2);
                String htmlText2 = "<table style='width:100%'>"
                        + "<tr>"
                        + "<td>" +
                        RecordActivity.trips.get(trips.size() - 2).getStartTime() + "</td>"
                        + "<td>" + " &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                        "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                        "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                        String.format("%.2f",RecordActivity.trips.get(trips.size() - 2).getTripTotalScore())
                        + "</td>"
                        + "<td>" + " &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                        "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                        "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                        RecordActivity.trips.get(trips.size() - 2).getTripDuration()
                        + "</td>"
                        + "</tr>"
                        + "<br>"
                        + "<tr>"
                        + "<td>" +
                        RecordActivity.trips.get(trips.size() - 2).getSpeedScore() + "</td>"
                        + "<td>" + "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                        "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                        "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                        "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                        RecordActivity.trips.get(trips.size() - 2).getBrakeScore()
                        + "</td>"
                        + "<td>" + "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                        "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                        "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                        "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                        RecordActivity.trips.get(trips.size() - 2).getCornerScore()
                        + "</td>"
                        + "</tr>"
                        + "</table>";
                trip2Button.setText(Html.fromHtml(htmlText2));
        }

        if(RecordActivity.trips.size()>=3 && !RecordActivity.trips.get(trips.size()-3).getStartTime().equals(null)){

            trip3Button = (Button) findViewById(R.id.trip3);
            String htmlText3 = "<table style='width:100%'>"
                    + "<tr>"
                    + "<td>" +
                    RecordActivity.trips.get(trips.size()-3).getStartTime() + "</td>"
                    + "<td>" + " &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    String.format("%.2f",RecordActivity.trips.get(trips.size()-3).getTripTotalScore())
                    + "</td>"
                    + "<td>" + " &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    RecordActivity.trips.get(trips.size()-3).getTripDuration()
                    + "</td>"
                    + "</tr>"
                    + "<br>"
                    + "<tr>"
                    + "<td>" +
                    RecordActivity.trips.get(trips.size()-3).getSpeedScore() + "</td>"
                    + "<td>" + "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    RecordActivity.trips.get(trips.size()-3).getBrakeScore()
                    + "</td>"
                    + "<td>" + "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    RecordActivity.trips.get(trips.size()-3).getCornerScore()
                    + "</td>"
                    + "</tr>"
                    + "</table>";
            trip3Button.setText(Html.fromHtml(htmlText3));
        }

        if(RecordActivity.trips.size()>=4 && !RecordActivity.trips.get(trips.size()-4).getStartTime().equals(null)){

            trip4Button = (Button) findViewById(R.id.trip4);
            String htmlText4 = "<table style='width:100%'>"
                    + "<tr>"
                    + "<td>" +
                    RecordActivity.trips.get(trips.size()-4).getStartTime() + "</td>"
                    + "<td>" + " &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    String.format("%.2f",RecordActivity.trips.get(trips.size()-4).getTripTotalScore())
                    + "</td>"
                    + "<td>" + " &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    RecordActivity.trips.get(trips.size()-4).getTripDuration()
                    + "</td>"
                    + "</tr>"
                    + "<br>"
                    + "<tr>"
                    + "<td>" +
                    RecordActivity.trips.get(trips.size()-4).getSpeedScore() + "</td>"
                    + "<td>" + "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    RecordActivity.trips.get(trips.size()-4).getBrakeScore()
                    + "</td>"
                    + "<td>" + "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    RecordActivity.trips.get(trips.size()-4).getCornerScore()
                    + "</td>"
                    + "</tr>"
                    + "</table>";
            trip4Button.setText(Html.fromHtml(htmlText4));
        }

        if (RecordActivity.trips.size()>=5 && !RecordActivity.trips.get(trips.size()-5).getStartTime().equals(null)) {
            trip5Button = (Button) findViewById(R.id.trip5);
            String htmlText5 = "<table style='width:100%'>"
                    + "<tr>"
                    + "<td>" +
                    RecordActivity.trips.get(trips.size()-5).getStartTime() + "</td>"
                    + "<td>" + " &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    String.format("%.2f",RecordActivity.trips.get(trips.size()-5).getTripTotalScore())
                    + "</td>"
                    + "<td>" + " &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    RecordActivity.trips.get(trips.size()-5).getTripDuration()
                    + "</td>"
                    + "</tr>"
                    + "<br>"
                    + "<tr>"
                    + "<td>" +
                    RecordActivity.trips.get(trips.size()-5).getSpeedScore() + "</td>"
                    + "<td>" + "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    RecordActivity.trips.get(trips.size()-5).getBrakeScore()
                    + "</td>"
                    + "<td>" + "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " +
                    RecordActivity.trips.get(trips.size()-5).getCornerScore()
                    + "</td>"
                    + "</tr>"
                    + "</table>";
            trip5Button.setText(Html.fromHtml(htmlText5));
        }

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
    public void buttonHandler(View v){
        finish();
    }
    public void buttonToMap(View view){
        Intent myIntent = new Intent(Intent.ACTION_VIEW);
        myIntent.setData(Uri.parse("geo:42.31,‐82.16"));
        startActivity(myIntent);
    }

}
