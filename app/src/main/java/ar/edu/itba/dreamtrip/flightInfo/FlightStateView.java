package ar.edu.itba.dreamtrip.flightInfo;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Familia Balaguer on 24/11/2016.
 */

public class FlightStateView {
    private ImageView airlineLogo;
    private TextView flightID;

    private TextView originID;
    private TextView originName;
    private TextView departureTime;

    private TextView destinationID;
    private TextView destinationName;
    private TextView arrivalTime;

    private TextView status;
    private TextView estimatedDepTime;
    private TextView estimatedArrTime;

    private TextView terminal;
    private TextView gate;
    private TextView luggage;

    public FlightStateView(ImageView airlineLogo, TextView flightID, TextView originID,
                           TextView originName, TextView departureTime, TextView destinationID,
                           TextView destinationName, TextView arrivalTime, TextView status,
                           TextView estimatedDepTime, TextView estimatedArrTime, TextView terminal,
                           TextView gate, TextView luggage) {
        this.airlineLogo = airlineLogo;
        this.flightID = flightID;
        this.originID = originID;
        this.originName = originName;
        this.departureTime = departureTime;
        this.destinationID = destinationID;
        this.destinationName = destinationName;
        this.arrivalTime = arrivalTime;
        this.status = status;
        this.estimatedDepTime = estimatedDepTime;
        this.estimatedArrTime = estimatedArrTime;
        this.terminal = terminal;
        this.gate = gate;
        this.luggage = luggage;
    }

    public ImageView getAirlineLogo() {
        return airlineLogo;
    }

    public TextView getFlightID() {
        return flightID;
    }

    public TextView getOriginID() {
        return originID;
    }

    public TextView getOriginName() {
        return originName;
    }

    public TextView getDepartureTime() {
        return departureTime;
    }

    public TextView getDestinationID() {
        return destinationID;
    }

    public TextView getDestinationName() {
        return destinationName;
    }

    public TextView getArrivalTime() {
        return arrivalTime;
    }

    public TextView getStatus() {
        return status;
    }

    public TextView getEstimatedDepTime() {
        return estimatedDepTime;
    }

    public TextView getEstimatedArrTime() {
        return estimatedArrTime;
    }

    public TextView getTerminal() {
        return terminal;
    }

    public TextView getGate() {
        return gate;
    }

    public TextView getLuggage() {
        return luggage;
    }
}
