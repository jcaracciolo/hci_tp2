package ar.edu.itba.dreamtrip.common.API.dependencies;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.jar.Pack200;

import ar.edu.itba.dreamtrip.R;

/**
 * Created by Julian Benitez on 11/22/2016.
 */

public class TrackedFlightsDependency extends Dependency {

    private Integer timeout;
    private Context context;

    public TrackedFlightsDependency(Context context, Integer timeout) {
        super(DependencyType.TRACKED_FLIGHTS);
        this.timeout = timeout;
        this.context = context;

        for (String identifier: getTrackedFlightsIdentifiers()) {
            addDependency(new StatusDependency(identifier,timeout));
        }
    }

    private ArrayList<String> getTrackedFlightsIdentifiers(){
        ArrayList<String > identifiers = new ArrayList<>();
        String prefsString = context.getString(R.string.tracked_flights_pref);
        String baseFlightString = context.getString(R.string.tracked_flight);
        String amountString = context.getString(R.string.tracked_flight_amount);

        SharedPreferences sharedPref = context.getSharedPreferences(prefsString, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        Integer trackedFlightsAmount= sharedPref.getInt(amountString,0);

        for (int i = 0; i < trackedFlightsAmount; i++) {
            String identifier = sharedPref.getString(baseFlightString+i,null);
            if(identifier != null){
                identifiers.add(identifier);
            }
        }
        return identifiers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TrackedFlightsDependency that = (TrackedFlightsDependency) o;
        return that == this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), timeout);
    }
}
