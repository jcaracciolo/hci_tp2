package ar.edu.itba.dreamtrip.airlineInfo;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collection;
import java.util.HashSet;

import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;
import ar.edu.itba.dreamtrip.common.model.Airline;
import ar.edu.itba.dreamtrip.common.tasks.AsyncTaskInformed;
import ar.edu.itba.dreamtrip.common.tasks.ImageLoadTask;


public class LoadAirlineInfoTask extends AsyncTaskInformed<Object,Void,Airline>{

    private Context context;
    ImageView img;
    TextView text;
    String id;

    public LoadAirlineInfoTask(Context context, ImageView img, TextView text, String id) {
        this.context = context;
        this.img= img;
        this.text = text;
        this.id = id;
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
            if (a.getID().equals(id)) {
                System.out.println("found: " + a.getID() + " " + a.getName());
                return a;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Airline airline) {

        text.setText(airline.getName());
        System.out.println("id: " + airline.getID() + " name: " + airline.getName());
        new ImageLoadTask(airline.getLogoUrl(), img).execute();
        System.out.println("id: " + airline.getID() + " name: " + airline.getName());
    }

}
