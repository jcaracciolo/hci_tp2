package ar.edu.itba.dreamtrip.airportInfo;

import android.content.Context;
import android.widget.TextView;

import java.util.Collection;
import java.util.HashSet;

import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;
import ar.edu.itba.dreamtrip.common.model.Airport;
import ar.edu.itba.dreamtrip.common.tasks.AsyncTaskInformed;


public class LoadAirportInfoTask extends AsyncTaskInformed<Object,Void,Airport>{

    private Context context;
    TextView text;
    TextView destFrom;
    String id;

    public LoadAirportInfoTask(Context context, TextView text, TextView destFrom,  String id) {
        this.context = context;
        this.text = text;
        this.id = id;
        this.destFrom = destFrom;
    }

    @Override
    public HashSet<Dependency> getDependencies() {
        HashSet<Dependency> dependencies = new HashSet<>();
        dependencies.add(new Dependency(DependencyType.AIRPORTS));
        return dependencies;
    }

    @Override
    protected Airport doInBackground(Object... params) {
        DataHolder dataHolder = (DataHolder) params[0];
        Collection<Airport> arr = dataHolder.getAirports();
        for (Airport a: arr) {
            System.out.println("id: " + a.getID() + " name: " + a.getName());
            if (a.getID().equals(id)) {
                return a;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Airport airport) {
        text.setText(airport.getName().split(",")[0] + "," + airport.getName().split(",")[1]);
        destFrom.setText("Destinations from\n" + airport.getID());
    }

}
