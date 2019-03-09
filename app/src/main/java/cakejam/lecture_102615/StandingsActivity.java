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
import android.widget.TextView;

public class StandingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standings);
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


        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        int x = bundle.getInt("x");
        String str = bundle.getString("str");

        TextView textView = (TextView) findViewById(R.id.TV);
        textView.setText("str: " + str + " x: " + x);
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
    public void buttonHandler(View v) {
        Intent myIntent = new Intent(this, SummaryActivity.class);


        Bundle myBundle = new Bundle();
        myBundle.putInt("x",5);
        myBundle.putString("str","hello");
        myIntent.putExtras(myBundle);

        startActivity(myIntent);
    }
    public void buttonViewAction(View v) {
        Intent myIntent = new Intent(Intent.ACTION_VIEW);
        myIntent.setData(Uri.parse("http://www.umich.edu"));
        startActivity(myIntent);
    }

}
