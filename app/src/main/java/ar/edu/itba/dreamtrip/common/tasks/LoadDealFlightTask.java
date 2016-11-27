package ar.edu.itba.dreamtrip.common.tasks;

import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;
import ar.edu.itba.dreamtrip.common.API.dependencies.FlightDependency;
import ar.edu.itba.dreamtrip.common.model.Airline;
import ar.edu.itba.dreamtrip.common.model.Airport;
import ar.edu.itba.dreamtrip.common.model.City;
import ar.edu.itba.dreamtrip.common.model.Country;
import ar.edu.itba.dreamtrip.common.model.Deal;
import ar.edu.itba.dreamtrip.common.model.Flight;
import ar.edu.itba.dreamtrip.common.model.StatusSearch;
import ar.edu.itba.dreamtrip.main.Adapter.ResultAdapter;
import ar.edu.itba.dreamtrip.main.ResultElement;


public class LoadDealFlightTask extends AsyncTaskInformed<Object,Void,ArrayList<String>>{

    private Context context;
    private Deal deal;

    public LoadDealFlightTask(Context context, Deal deal) {
        this.context = context;
    }

    @Override
    public HashSet<Dependency> getDependencies() {
        HashSet<Dependency> dependencies = new HashSet<>();
        boolean isLastMinute = false;
        dependencies.add(new FlightDependency(deal.getOriginCityID(),deal.getDestinationCityID(),isLastMinute));
        return dependencies;
    }

    @Override
    protected ArrayList<String> doInBackground(Object... params) {
        DataHolder dataHolder = (DataHolder) params[0];
        ArrayList<String> strings = new ArrayList<>();

        Flight flight = dataHolder.getFlightFromDeal(deal);

        return strings;
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
    }

    public void toast(String str) {
        Toast.makeText(context, str,
                Toast.LENGTH_SHORT).show();
    }
}

