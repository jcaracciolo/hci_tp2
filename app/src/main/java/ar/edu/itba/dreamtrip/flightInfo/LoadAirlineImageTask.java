package ar.edu.itba.dreamtrip.flightInfo;

import android.content.Context;
import android.widget.ImageView;

import java.util.Collection;
import java.util.HashSet;

import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;
import ar.edu.itba.dreamtrip.common.model.Airline;
import ar.edu.itba.dreamtrip.common.tasks.AsyncTaskInformed;


public class LoadAirlineImageTask extends AsyncTaskInformed<Object,Void,Airline>{

    private Context context;
    String airlineID;
    ImageView img;

    public LoadAirlineImageTask(Context context, String airlineID, ImageView img) {
        this.context = context;
        this.airlineID = airlineID;
        this.img = img;
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
        Collection<Airline> arr = dataHolder.getAirlines();
        for (Airline a: arr) {
            if (a.getID().equals(airlineID)) {
                return a;
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Airline a) {
        img.setImageBitmap(a.getLogo());
    }

}
