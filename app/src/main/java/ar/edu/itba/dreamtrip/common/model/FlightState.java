package ar.edu.itba.dreamtrip.common.model;

import ar.edu.itba.dreamtrip.R;

/**
 * Created by Julian Benitez on 11/22/2016.
 */

public class FlightState {
    private String flightIdentifier;
    private FlightStateAtLocation origin;
    private FlightStateAtLocation destination;
    private FlightStatus status;

    public FlightState(String flightIdentifier, FlightStateAtLocation origin, FlightStateAtLocation destination, FlightStatus status) {
        this.flightIdentifier = flightIdentifier;
        this.origin = origin;
        this.destination = destination;
        this.status = status;
    }

    public String getIdentifier() {
        return flightIdentifier;
    }

    public FlightStateAtLocation getOrigin() {
        return origin;
    }

    public FlightStateAtLocation getDestination() {
        return destination;
    }

    public FlightStatus getStatus() {
        return status;
    }



}
