package ar.edu.itba.dreamtrip.cityInfo;

import android.content.Context;
import android.widget.TextView;

import java.util.Collection;
import java.util.HashSet;

import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;
import ar.edu.itba.dreamtrip.common.model.City;
import ar.edu.itba.dreamtrip.common.tasks.AsyncTaskInformed;


public class LoadCityInfoTask extends AsyncTaskInformed<Object,Void,City>{

    private Context context;
    TextView text;
    TextView destFrom;
    String id;

    public LoadCityInfoTask(Context context, TextView text, TextView destFrom, String id) {
        this.context = context;
        this.text = text;
        this.id = id;
        this.destFrom = destFrom;
    }

    @Override
    public HashSet<Dependency> getDependencies() {
        HashSet<Dependency> dependencies = new HashSet<>();
        dependencies.add(new Dependency(DependencyType.CITIES));
        return dependencies;
    }

    @Override
    protected City doInBackground(Object... params) {
        DataHolder dataHolder = (DataHolder) params[0];
        Collection<City> arr = dataHolder.getCities();
        for (City c: arr) {
            System.out.println("id: " + c.getID() + " name: " + c.getName());
            if (c.getID().equals(id)) {
                return c;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(City city) {
        text.setText(city.getName().split(",")[0] + "," + city.getName().split(",")[1]);
        destFrom.setText("Destinations from\n" + city.getName().split(",")[0]);
    }

}
