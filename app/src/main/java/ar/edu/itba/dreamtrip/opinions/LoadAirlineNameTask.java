package ar.edu.itba.dreamtrip.opinions;

import android.content.Context;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;
import ar.edu.itba.dreamtrip.common.API.dependencies.FlightDependency;
import ar.edu.itba.dreamtrip.common.model.Airline;
import ar.edu.itba.dreamtrip.common.model.Airport;
import ar.edu.itba.dreamtrip.common.tasks.AsyncTaskInformed;


public class LoadAirlineNameTask extends AsyncTaskInformed<Object,Void,Airline>{

    private Context context;
    private String id;
    TextView title;

    public LoadAirlineNameTask(Context context, String id, TextView title) {
        this.context = context;
        this.id = id;
        this.title = title;
    }

    @Override
    public HashSet<Dependency> getDependencies() {
        HashSet<Dependency> dependencies = new HashSet<>();
        dependencies.add(new Dependency(DependencyType.AIRLINES));
        return dependencies;
    }

    @Override
    protected Airline doInBackground(Object... params) {
        DataHolder dataHolder = (DataHolder) params[0];
        Collection<Airline> airports = dataHolder.getAirlines();
        for (Airline a: airports) {
            if (a.getID().equals(id)) {
                return a;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Airline a) {
        title.setText(context.getResources().getString(R.string.opinions_of_title) + " " + a.getName());
    }

}
