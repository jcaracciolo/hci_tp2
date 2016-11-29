package ar.edu.itba.dreamtrip.airlineInfo;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;
import ar.edu.itba.dreamtrip.common.API.dependencies.FlightDependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.StatusDependency;
import ar.edu.itba.dreamtrip.common.model.Airline;
import ar.edu.itba.dreamtrip.common.model.Airport;
import ar.edu.itba.dreamtrip.common.model.City;
import ar.edu.itba.dreamtrip.common.model.Country;
import ar.edu.itba.dreamtrip.common.model.Flight;
import ar.edu.itba.dreamtrip.common.model.FlightState;
import ar.edu.itba.dreamtrip.common.model.StatusSearch;
import ar.edu.itba.dreamtrip.common.tasks.AsyncTaskInformed;
import ar.edu.itba.dreamtrip.flightInfo.FlightInfo;
import ar.edu.itba.dreamtrip.main.Adapter.ResultAdapter;
import ar.edu.itba.dreamtrip.main.ResultElement;

import static ar.edu.itba.dreamtrip.airlineInfo.AirlineInfo.TO_FLIGHT_KEY;


public class FlightExistsInAirlineTask extends AsyncTaskInformed<Object,Void,Boolean>{

    private Context context;
    private String identifier;
    HashSet<Dependency> dependencies = new HashSet<>();

    public FlightExistsInAirlineTask(Context context, String identifier) {
        this.context = context;
        dependencies.add(new StatusDependency(identifier,1));
        this.identifier = identifier;
    }


    @Override
    public HashSet<Dependency> getDependencies() {
        return dependencies;
    }

    @Override
    protected Boolean doInBackground(Object... params) {
        DataHolder dataHolder = (DataHolder) params[0];
        ArrayList<String> strings = new ArrayList<>();
            Collection<FlightState> states = dataHolder.getFlightStates();
            for (FlightState f : states) {
                if(f.getIdentifier().equals(identifier)) return true;
            }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if(result){
            Intent intent = new Intent(context, FlightInfo.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(TO_FLIGHT_KEY, identifier);
            context.startActivity(intent);
        } else {
            toast(context.getString(R.string.incorrect_flight_number));
        }
    }

    public void toast(String str) {
        Toast.makeText(context, str,
                Toast.LENGTH_SHORT).show();
    }
}

