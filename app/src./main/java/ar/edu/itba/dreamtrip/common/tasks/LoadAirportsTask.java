package ar.edu.itba.dreamtrip.common.tasks;

import android.content.Context;
import android.widget.Toast;

import java.util.Collection;
import java.util.HashSet;

import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;
import ar.edu.itba.dreamtrip.common.model.Airport;


public class LoadAirportsTask extends AsyncTaskInformed<Object,Void,Collection<Airport>>{

    private Context context;
    public LoadAirportsTask(Context context) {
        this.context = context;
    }

    @Override
    public HashSet<Dependency> getDependencies() {
        HashSet<Dependency> dependencies = new HashSet<>();
        dependencies.add(new Dependency(DependencyType.AIRPORTS));
        return dependencies;
    }

    @Override
    protected Collection<Airport> doInBackground(Object... params) {
        DataHolder dataHolder = (DataHolder) params[0];
        return dataHolder.getAirports();
    }

    @Override
    protected void onPostExecute(Collection<Airport> result) {
        Toast.makeText(context, "Downloaded cities: " + result.size(),
                Toast.LENGTH_LONG).show();
    }

}
