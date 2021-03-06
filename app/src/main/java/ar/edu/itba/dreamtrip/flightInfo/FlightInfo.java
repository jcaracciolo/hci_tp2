package ar.edu.itba.dreamtrip.flightInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.SettingsManager;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;
import ar.edu.itba.dreamtrip.common.CenterMaper;
import ar.edu.itba.dreamtrip.common.tasks.TrackFlightTask;
import ar.edu.itba.dreamtrip.main.BaseActivity;
import ar.edu.itba.dreamtrip.main.FlightTracker;

public class FlightInfo extends BaseActivity implements OnMapReadyCallback{

    public final static String RESULT_ID_KEY = "main_activity.go_to_info_id";

    GoogleMap mMap;
    FlightStateView flightStateView;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView(R.layout.activity_flight_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fillFlightStateView();

        id = getIntent().getExtras().getString(RESULT_ID_KEY);
        ImageView logo_big = (ImageView) findViewById(R.id.flight_airlinelogo_big);
        TextView duration = (TextView) findViewById(R.id.flight_duration);
        TextView cityOrigin = (TextView) findViewById(R.id.flight_origin_city);
        TextView cityDestination = (TextView) findViewById(R.id.flight_destination_city);

        DataHolder dataHolder = DataHolder.getInstance(this);
        dataHolder.waitForIt(new LoadFlightStatusTask(getBaseContext(), id, flightStateView));
        dataHolder.waitForIt(new LoadAirlineImageTask(getBaseContext(), id.split(" ")[0], logo_big));
        dataHolder.waitForIt(new LoadFlightInfoTask(getBaseContext(), id, duration, cityOrigin, cityDestination));

        ArrayList<String> trackedFlights = SettingsManager.getInstance(getApplicationContext()).getTrackedFlights();
        if (trackedFlights.contains(id)) {
            Button followBtn = (Button) findViewById(R.id.follow_flight_btn);
            followBtn.setText(R.string.unfollow);
        }

        String isLandTablet = getBaseContext().getResources().getString(R.string.isTablet);
        if (isLandTablet.equals("true")) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

    }

    public void followFlight(View v) {
        ArrayList<String> trackedFlights = SettingsManager.getInstance(getApplicationContext()).getTrackedFlights();
        if (trackedFlights.contains(id)) {
            SettingsManager.getInstance(getApplicationContext()).untrackFlight(id);
            Button followBtn = (Button) findViewById(R.id.follow_flight_btn);
            followBtn.setText(R.string.follow);
        } else {
            DataHolder.getInstance(getApplicationContext()).waitForIt(
                    new TrackFlightTask(getApplicationContext(), id));

            Intent intent = new Intent(getBaseContext(), FlightTracker.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void fillFlightStateView() {
        ImageView airlineLogo = (ImageView) findViewById(R.id.flight_airlinelogo_big);
        TextView flightID = (TextView) findViewById(R.id.flight_name);

        TextView originID = (TextView) findViewById(R.id.flight_origin_airport);
        TextView originName = (TextView) findViewById(R.id.flight_origin_city);
        TextView departureTime = (TextView) findViewById(R.id.flight_departure_time);

        TextView destinationID = (TextView) findViewById(R.id.flight_destination_airport);
        TextView destinationName = (TextView) findViewById(R.id.flight_destination_city);
        TextView arrivalTime = (TextView) findViewById(R.id.flight_arrival_time);

        ImageView statusBar = (ImageView) findViewById(R.id.flight_details_statuscolor);
        TextView status = (TextView) findViewById(R.id.flight_status_message);
        TextView estimatedDepTime = (TextView) findViewById(R.id.flight_estimated_departure);
        TextView estimatedArrTime = (TextView) findViewById(R.id.flight_estimated_arrival);

        TextView terminal = (TextView) findViewById(R.id.flight_terminal);
        TextView gate = (TextView) findViewById(R.id.flight_gate);
        TextView luggage = (TextView) findViewById(R.id.flight_lgate);

        flightStateView = new FlightStateView(airlineLogo, flightID, originID, originName, departureTime,
                destinationID, destinationName, arrivalTime, statusBar, status, estimatedDepTime, estimatedArrTime,
                terminal, gate, luggage);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        DataHolder.getInstance(getBaseContext()).waitForIt(new CenterFlight(getBaseContext(),mMap,id));

    }

}