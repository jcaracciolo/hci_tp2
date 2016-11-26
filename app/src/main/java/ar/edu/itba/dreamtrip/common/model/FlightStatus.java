package ar.edu.itba.dreamtrip.common.model;

import android.content.Context;

import ar.edu.itba.dreamtrip.R;

/**
 * Created by juanfra on 22/11/16.
 */

public enum FlightStatus {
    SCHEDULED,
    ACTIVE,
    DELAYED,
    LANDED,
    CANCELLED;

    public static FlightStatus parseFlightStatus(Context context, String string){

        if(context.getString(R.string.scheduled_flight_code).equals(string))
            return FlightStatus.SCHEDULED;
        if(context.getString(R.string.active_flight_code).equals(string))
            return FlightStatus.ACTIVE;
        if(context.getString(R.string.delayed_flight_code).equals(string))
            return FlightStatus.DELAYED;
        if(context.getString(R.string.landed_flight_code).equals(string))
            return FlightStatus.LANDED;
        if(context.getString(R.string.cancelled_flight_code).equals(string))
            return FlightStatus.CANCELLED;
        return null;
    }

    public static String toString(Context context, FlightStatus flightStatus){
        switch (flightStatus){
            case SCHEDULED:
                return context.getString(R.string.scheduled_flight_code);
            case ACTIVE:
                return context.getString(R.string.active_flight_code);
            case DELAYED:
                return context.getString(R.string.delayed_flight_code);
            case LANDED:
                return context.getString(R.string.landed_flight_code);
            case CANCELLED:
                return context.getString(R.string.cancelled_flight_code);
        }
        return null;
    }
}
