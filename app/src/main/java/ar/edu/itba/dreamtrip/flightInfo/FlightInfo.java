package ar.edu.itba.dreamtrip.flightInfo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.tasks.LoadFlightsTask;
import ar.edu.itba.dreamtrip.main.BaseActivity;

public class FlightInfo extends BaseActivity {

    public final static String RESULT_ID_KEY = "main_activity.go_to_info_id";

    FlightStateView flightStateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView(R.layout.activity_flight_info);

        fillFlightStateView();

        String id = getIntent().getExtras().getString(RESULT_ID_KEY);

        DataHolder dataHolder = DataHolder.getInstance(this);
        dataHolder.waitForIt(new LoadFlightStatusTask(getBaseContext(), id, flightStateView));
        
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

        TextView status = (TextView) findViewById(R.id.flight_status_message);
        TextView estimatedDepTime = (TextView) findViewById(R.id.flight_estimated_departure);
        TextView estimatedArrTime = (TextView) findViewById(R.id.flight_estimated_arrival);

        TextView terminal = (TextView) findViewById(R.id.flight_terminal);
        TextView gate = (TextView) findViewById(R.id.flight_gate);
        TextView luggage = (TextView) findViewById(R.id.flight_lgate);

        flightStateView = new FlightStateView(airlineLogo, flightID, originID, originName, departureTime,
                destinationID, destinationName, arrivalTime, status, estimatedDepTime, estimatedArrTime,
                terminal, gate, luggage);
    }

}
