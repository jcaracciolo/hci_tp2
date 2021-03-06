package ar.edu.itba.dreamtrip.TrackedFlights;

import android.content.Context;

import java.io.Serializable;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.common.model.FlightState;
import ar.edu.itba.dreamtrip.common.model.FlightStatus;

/**
 * Created by juanfra on 22/11/16.
 */



public class TrackedFlightViewModel implements Serializable{

    public String airlineID;
    public Integer number;

    public FlightStatus status;

    public String date;

    public String originID;
    public String destinationID;

    public String departureHour;
    public String arrivalHour;

    public boolean selected=false;

    public String terminal;
    public Integer gate;
    public Integer luggage;

    public TrackedFlightViewModel(Context context, FlightState flightState) {
        airlineID = flightState.getIdentifier().split(" ")[0];
        number = Integer.parseInt(flightState.getIdentifier().split(" ")[1]);
        status = flightState.getStatus();
        date =  flightState.getOrigin().getScheduledDay();
        originID = flightState.getOrigin().getLocationID();
        destinationID = flightState.getDestination().getLocationID();
        departureHour = flightState.getOrigin().getScheduledHour();
        arrivalHour = flightState.getDestination().getScheduledHour();
        terminal = flightState.getOrigin().getTerminal();
        gate = flightState.getOrigin().getGate();
        luggage = flightState.getDestination().getBaggage();

    }

    public String getIdentifier(){
        return airlineID + " " + number;
    }

    public Integer getStatusString(){
        Integer result = R.string.status_not_found_flight;
        switch (status){
            case DELAYED:
                return R.string.status_not_found_flight;
            case SCHEDULED:
                return R.string.scheduled_flight;
            case ACTIVE:
                return R.string.active_flight;
            case LANDED:
                return R.string.landed_flight;
            case CANCELLED:
                return R.string.cancelled_flight;
        }
        return result;
    }
}
