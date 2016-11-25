package ar.edu.itba.dreamtrip.TrackedDestinations;

import android.content.Context;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;
import ar.edu.itba.dreamtrip.common.API.dependencies.FlightDealsDependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.TrackedFlightsDependency;
import ar.edu.itba.dreamtrip.common.model.Deal;
import ar.edu.itba.dreamtrip.common.model.FlightState;
import ar.edu.itba.dreamtrip.common.tasks.AsyncTaskInformed;

/**
 * Created by juanfra on 22/11/16.
 */

public class PopulateLegTrackers extends AsyncTaskInformed<Object,Void,ArrayList<TrackedLegViewModel>> {

    Context context;
    ListView list;
    static TrackedLegCardAdapter legsCardAdapter;

    public PopulateLegTrackers(Context context, ListView list){
        this.context=context;
        this.list=list;
    }

    @Override
    public HashSet<Dependency> getDependencies() {
        HashSet<Dependency> dependencies = new HashSet<>();
        dependencies.add(new FlightDealsDependency("BUE",true,false));
        return dependencies;
    }

    @Override
    protected ArrayList<TrackedLegViewModel> doInBackground(Object... params) {
        DataHolder dataHolder = (DataHolder) params[0];
        ArrayList<TrackedLegViewModel> dealCards = new ArrayList<>();

        Collection<Deal> deals = dataHolder.getDeals();
        Collection<Deal> allLastDeals = dataHolder.getDeals();

        for (Deal ld : allLastDeals) {
            if(deals.contains(ld)){
                dealCards.add(new TrackedLegViewModel(context,ld,true));
            }
        }

        for (Deal ld : deals) {
            if(! allLastDeals.contains(deals)){
                dealCards.add(new TrackedLegViewModel(context,ld,false));
            }
        }

        return dealCards;
    }

    @Override
    protected void onPostExecute(ArrayList<TrackedLegViewModel> flightCards) {
        list.setAdapter(new TrackedLegCardAdapter(context, flightCards));
    }
}
