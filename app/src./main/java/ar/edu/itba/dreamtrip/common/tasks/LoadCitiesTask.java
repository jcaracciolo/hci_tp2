package ar.edu.itba.dreamtrip.common.tasks;

import android.content.Context;
import android.widget.Toast;

import java.util.Collection;
import java.util.HashSet;

import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;
import ar.edu.itba.dreamtrip.common.model.City;


public class LoadCitiesTask extends AsyncTaskInformed<Object,Void,Collection<City>>{

    private Context context;
    public LoadCitiesTask(Context context) {
        this.context = context;
    }

    @Override
    public HashSet<Dependency> getDependencies() {
        HashSet<Dependency> dependencies = new HashSet<>();
        dependencies.add(new Dependency(DependencyType.CITIES));
        return dependencies;
    }

    @Override
    protected Collection<City> doInBackground(Object... params) {
        DataHolder dataHolder = (DataHolder) params[0];
        return dataHolder.getCities();
    }

    @Override
    protected void onPostExecute(Collection<City> result) {
        Toast.makeText(context, "Downloaded cities: " + result,
                Toast.LENGTH_LONG).show();
        System.out.println("Downloaded " + result + " bytes");
    }

}
