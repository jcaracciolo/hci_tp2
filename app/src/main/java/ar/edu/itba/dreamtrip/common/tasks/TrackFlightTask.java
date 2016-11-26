package ar.edu.itba.dreamtrip.common.tasks;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.HashSet;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.SettingsManager;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.StatusDependency;
import ar.edu.itba.dreamtrip.common.model.FlightState;


public class TrackFlightTask extends AsyncTaskInformed<Object,Void,Boolean>{

    private Context context;
    private String identifier;
    private HashSet<Dependency> dependencies;

    public TrackFlightTask(Context context, String identifier) {
        this.context = context;
        this.identifier = identifier.trim().toUpperCase();
        if(! this.identifier.matches(context.getString(R.string.valid_identifier_regex))){
            throw new RuntimeException("Before tracking a flight you should be certain it matches");
        }
        dependencies = new HashSet<>();
        dependencies.add(new StatusDependency(identifier,5));
    }

    @Override
    public HashSet<Dependency> getDependencies() {
        return dependencies;
    }


    @Override
    protected Boolean doInBackground(Object... params) {
        DataHolder dataHolder = (DataHolder) params[0];
        Boolean flightTracked=false;
        for(FlightState flightState : dataHolder.getFlightStates()){
            if(flightState.getIdentifier().equals(identifier)){
                flightTracked = SettingsManager.getInstance(context).trackFlight(identifier);
                SettingsManager.getInstance(context).setFlightStatus(identifier,flightState.getStatus());
            }
        }

        return flightTracked;
    }
    @Override
    protected void onPostExecute(Boolean successfullyTracked) {
        Toast.makeText(context, successfullyTracked? "Successfully tracked!!":"That flight does not exist",
                Toast.LENGTH_LONG).show();
        Intent intent = new Intent(context.getResources().getString(R.string.UpdateTrackedFlights));
        context.sendBroadcast(intent);
    }

}
