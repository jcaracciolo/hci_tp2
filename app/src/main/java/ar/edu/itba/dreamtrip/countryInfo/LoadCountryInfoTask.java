package ar.edu.itba.dreamtrip.countryInfo;

import android.content.Context;
import android.widget.TextView;

import java.util.Collection;
import java.util.HashSet;

import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;
import ar.edu.itba.dreamtrip.common.model.Country;
import ar.edu.itba.dreamtrip.common.tasks.AsyncTaskInformed;


public class LoadCountryInfoTask extends AsyncTaskInformed<Object,Void,Country>{

    private Context context;
    TextView text;
    TextView destFrom;
    String id;

    public LoadCountryInfoTask(Context context, TextView text, TextView destFrom, String id) {
        this.context = context;
        this.text = text;
        this.id = id;
        this.destFrom = destFrom;
    }

    @Override
    public HashSet<Dependency> getDependencies() {
        HashSet<Dependency> dependencies = new HashSet<>();
        dependencies.add(new Dependency(DependencyType.COUNTRIES));
        return dependencies;
    }

    @Override
    protected Country doInBackground(Object... params) {
        DataHolder dataHolder = (DataHolder) params[0];
        Collection<Country> arr = dataHolder.getCountries();
        for (Country c: arr) {
            System.out.println("id: " + c.getID() + " name: " + c.getName());
            if (c.getID().equals(id)) {
                return c;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Country country) {
        text.setText(country.getName());
        destFrom.setText("Destinations from\n" + country.getName());
    }

}
