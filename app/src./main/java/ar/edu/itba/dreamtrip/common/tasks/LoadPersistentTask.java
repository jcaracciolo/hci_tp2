package ar.edu.itba.dreamtrip.common.tasks;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;


public class LoadPersistentTask extends AsyncTaskInformed<Object,Void,ArrayList<String>>{

    private Context context;

    public LoadPersistentTask(Context context) {
        this.context = context;
    }

    @Override
    public HashSet<Dependency> getDependencies() {
        HashSet<Dependency> dependencies = new HashSet<>();
        dependencies.add(new Dependency(DependencyType.AIRPORTS));
        dependencies.add(new Dependency(DependencyType.CURRENCIES));
        dependencies.add(new Dependency(DependencyType.AIRLINES));
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
        return strings;
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        for (String s: result){
            System.out.println(s);
            Toast.makeText(context, s,
                    Toast.LENGTH_LONG).show();
        }
    }

}
