package ar.edu.itba.dreamtrip.airlineInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.flightInfo.FlightInfo;
import ar.edu.itba.dreamtrip.main.BaseActivity;
import ar.edu.itba.dreamtrip.opinions.Opinions;

public class AirlineInfo extends BaseActivity {

    public final static String RESULT_ID_KEY = "main_activity.go_to_info_id";
    public final static String TO_OPINIONS_KEY = "airline_id_to_opinion";
    public final static String TO_FLIGHT_KEY = "main_activity.go_to_info_id";

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView(R.layout.activity_airline_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        id = intent.getStringExtra(RESULT_ID_KEY);

        ImageView img = (ImageView)findViewById(R.id.airline_logo);
        TextView text = (TextView)findViewById(R.id.airline_name);
        TextView desc = (TextView)findViewById(R.id.airline_description);
        RatingBar ratingBar = (RatingBar)findViewById(R.id.airline_rating);

        DataHolder dataHolder = DataHolder.getInstance(this);
        dataHolder.waitForIt(new LoadAirlineInfoTask(this, img, text, desc, id));
        dataHolder.waitForIt(new LoadAirlineRatingTask(this, id, ratingBar));
    }

    public void searchFlight(View v) {
        String flightID = ((EditText) findViewById(R.id.flight_search_edit_text)).getText().toString();

        if (!flightID.matches("^\\d{3,4}$")) {
            Toast.makeText(v.getContext(), v.getContext().getString(R.string.incorrent_flight_number), Toast.LENGTH_SHORT).show();
            return;
        }

        flightID = id + " " + flightID;

        Intent intent = new Intent(this, FlightInfo.class);
        intent.putExtra(TO_FLIGHT_KEY, flightID);
        startActivity(intent);
    }

    public void seeOpinions(View v) {
        Intent intent = new Intent(this, Opinions.class);
        intent.putExtra(TO_OPINIONS_KEY, id);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
