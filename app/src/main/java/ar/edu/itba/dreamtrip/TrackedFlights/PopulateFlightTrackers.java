package ar.edu.itba.dreamtrip.TrackedFlights;

import android.content.Context;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.TrackedFlightsDependency;
import ar.edu.itba.dreamtrip.common.model.FlightState;
import ar.edu.itba.dreamtrip.common.tasks.AsyncTaskInformed;

/**
 * Created by juanfra on 22/11/16.
 */

public class PopulateFlightTrackers extends AsyncTaskInformed<Object,Void,ArrayList<TrackedFlightViewModel>> {

    Context context;
    ListView list;
    static TrackedFlightCardAdapter flightCardAdapter;

    public PopulateFlightTrackers(Context context, ListView list){
        this.context=context;
        this.list=list;
    }

    @Override
    public HashSet<Dependency> getDependencies() {
        HashSet<Dependency> dependencies = new HashSet<>();
        dependencies.add(new TrackedFlightsDependency(context,5));
        return dependencies;
    }

    @Override
    protected ArrayList<TrackedFlightViewModel> doInBackground(Object... params) {
        DataHolder dataHolder = (DataHolder) params[0];
        ArrayList<TrackedFlightViewModel> flightCards = new ArrayList<>();

        Collection<FlightState> flightStates = dataHolder.getFlightStates();
        for (FlightState flightState: flightStates) {
            flightCards.add(new TrackedFlightViewModel(context,flightState));
        }

        return flightCards;
    }

    @Override
    protected void onPostExecute(ArrayList<TrackedFlightViewModel> flightCards) {
        list.setAdapter(new TrackedFlightCardAdapter(context, flightCards));
    }
}
