package ar.edu.itba.dreamtrip.common.tasks;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.TrackedDestinations.TrackedLegViewModel;
import ar.edu.itba.dreamtrip.cityInfo.CityInfo;
import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;
import ar.edu.itba.dreamtrip.common.API.dependencies.FlightDependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.FlightsBundleDependency;
import ar.edu.itba.dreamtrip.common.model.Airline;
import ar.edu.itba.dreamtrip.common.model.Airport;
import ar.edu.itba.dreamtrip.common.model.City;
import ar.edu.itba.dreamtrip.common.model.Country;
import ar.edu.itba.dreamtrip.common.model.Deal;
import ar.edu.itba.dreamtrip.common.model.Flight;
import ar.edu.itba.dreamtrip.common.model.StatusSearch;
import ar.edu.itba.dreamtrip.flightInfo.FlightInfo;
import ar.edu.itba.dreamtrip.main.Adapter.ResultAdapter;
import ar.edu.itba.dreamtrip.main.ResultElement;


public class LoadDealFlightTask extends AsyncTaskInformed<Object,Void,ArrayList<String>>{

    private Context context;
    private TrackedLegViewModel deal;

    public LoadDealFlightTask(Context context, TrackedLegViewModel deal) {
        this.context = context;
        this.deal = deal;
        toast(context.getString(R.string.loading_flight));
    }

    @Override
    public HashSet<Dependency> getDependencies() {
        HashSet<Dependency> dependencies = new HashSet<>();
        boolean isLastMinute = false;
        dependencies.add(new FlightsBundleDependency(deal.originID,deal.destinationID,new Date(),isLastMinute));
        return dependencies;
    }

    @Override
    protected ArrayList<String> doInBackground(Object... params) {
        DataHolder dataHolder = (DataHolder) params[0];
        ArrayList<String> strings = new ArrayList<>();

        Flight flight = dataHolder.getFlightFromDeal(new Deal(deal.destinationID,deal.originID,deal.destinationDescription,deal.price));
        if(flight != null) {

            Intent intent = new Intent(context, FlightInfo.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(FlightInfo.RESULT_ID_KEY, flight.getIdentifier());
            context.startActivity(intent);
        }

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

