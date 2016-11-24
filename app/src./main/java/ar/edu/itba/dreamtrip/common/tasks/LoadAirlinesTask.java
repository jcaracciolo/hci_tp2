package ar.edu.itba.dreamtrip.common.tasks;

import java.util.Collection;
import java.util.HashSet;

import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;
import ar.edu.itba.dreamtrip.common.model.Airline;


public class LoadAirlinesTask extends AsyncTaskInformed<Object,Void,Collection<Airline>>{

    @Override
    public HashSet<Dependency> getDependencies() {
        HashSet<Dependency> dependencies = new HashSet<>();
        dependencies.add(new Dependency(DependencyType.AIRLINES));
        return dependencies;
    }

    @Override
    protected Collection<Airline> doInBackground(Object... params) {
        DataHolder dataHolder = (DataHolder) params[0];
        return dataHolder.getAirlines();
    }

    @Override
    protected void onPostExecute(Collection<Airline> result) {
        String msg =  "charged " + result.size() + " arilines";
    }

}
