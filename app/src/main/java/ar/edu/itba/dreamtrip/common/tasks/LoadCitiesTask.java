package ar.edu.itba.dreamtrip.common.tasks;

import android.content.Context;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;

import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;
import ar.edu.itba.dreamtrip.common.API.dependencies.FlightDependency;


public class LoadCitiesTask extends AsyncTaskInformed<Object,Void,ArrayList<String>>{

    private Context context;
    private HashSet<FlightDependency> flightDependencies;

    public LoadCitiesTask(Context context, HashSet<FlightDependency> flights) {
        flightDependencies = flights;
        this.context = context;
    }

    public LoadCitiesTask(Context context, String originID, String destID, String depDate) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        flightDependencies = new HashSet<>();
        try {
            flightDependencies.add(new FlightDependency(originID,destID,format.parse(depDate)));
        } catch (ParseException e) {
            throw new RuntimeException("Invalid departure date");
        }
        this.context = context;
    }

    @Override
    public HashSet<Dependency> getDependencies() {
        HashSet<Dependency> dependencies = new HashSet<>();
        dependencies.add(new Dependency(DependencyType.CITIES));
        return dependencies;
    }

    @Override
    protected ArrayList<String> doInBackground(Object... params) {
        DataHolder dataHolder = (DataHolder) params[0];
        ArrayList<String> strings = new ArrayList<>();
        strings.add("Loaded " + dataHolder.getCountries().size() + " countries." );
        strings.add("Loaded " + dataHolder.getCities().size() + " cities." );
        strings.add("Loaded " + dataHolder.getAirports().size() + " airports." );
        strings.add("Loaded " + dataHolder.getCurrencies().size() + " currencies." );
        strings.add("Loaded " + dataHolder.getAirlines().size() + " airlines." );
        strings.add("Loaded " + dataHolder.getFlights().size() + " flights." );
        return strings;
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
    }

}
