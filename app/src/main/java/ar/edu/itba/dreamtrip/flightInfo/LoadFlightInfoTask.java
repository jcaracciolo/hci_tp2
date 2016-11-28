package ar.edu.itba.dreamtrip.flightInfo;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;
import java.util.HashSet;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;
import ar.edu.itba.dreamtrip.common.API.dependencies.StatusDependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.StatusSearchDependency;
import ar.edu.itba.dreamtrip.common.model.Flight;
import ar.edu.itba.dreamtrip.common.model.FlightState;
import ar.edu.itba.dreamtrip.common.model.StatusSearch;
import ar.edu.itba.dreamtrip.common.tasks.AsyncTaskInformed;


public class LoadFlightInfoTask extends AsyncTaskInformed<Object, Void, Flight> {

    private Context context;
    String flightID;
    TextView duration;
    TextView cityOrigin;
    TextView cityDestination;

    public LoadFlightInfoTask(Context context, String flightID, TextView duration, TextView cityOrigin, TextView cityDestination) {
        this.context = context;
        this.flightID = flightID;
        this.duration = duration;
        this.cityOrigin = cityOrigin;
        this.cityDestination = cityDestination;
    }

    @Override
    public HashSet<Dependency> getDependencies() {
        HashSet<Dependency> dependencies = new HashSet<>();
        dependencies.add(new StatusSearchDependency(new StatusSearch(flightID.split(" ")[0],
                Integer.parseInt(flightID.split(" ")[1]))));
        return dependencies;
    }

    @Override
    protected Flight doInBackground(Object... params) {
        DataHolder dataHolder = (DataHolder) params[0];
        Collection<Flight> arr = dataHolder.getFlights();
        for (Flight f : arr) {
            if (f.getIdentifier().equals(flightID)) {
                return f;
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Flight f) {
        if (f != null) {
            duration.setText(f.getDuration());
        } else {
            duration.setText("");
        }
        cityOrigin.setText(f.getOrigin().getCity().getName().split(",")[0]);
        cityDestination.setText(f.getDestination().getCity().getName().split(",")[0]);
    }

}
