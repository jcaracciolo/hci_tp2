package ar.edu.itba.dreamtrip.TrackedDestinations;

import android.content.Context;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.SettingsManager;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;
import ar.edu.itba.dreamtrip.common.API.dependencies.FlightDealsDependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.TrackedFlightsDependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.TrackedLegsDependency;
import ar.edu.itba.dreamtrip.common.model.Deal;
import ar.edu.itba.dreamtrip.common.model.FlightState;
import ar.edu.itba.dreamtrip.common.model.StatusSearch;
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
        SettingsManager settingsManager = SettingsManager.getInstance(context);

        settingsManager.clearAllTracked();
        settingsManager.trackLeg("EZE LON");
        settingsManager.trackLeg("LON PAR");


        System.out.println(settingsManager.getTrackedLegs());
    }

    @Override
    public HashSet<Dependency> getDependencies() {
        HashSet<Dependency> dependencies = new HashSet<>();
        dependencies.add(new TrackedLegsDependency(context,true));
        return dependencies;
    }

    @Override
    protected ArrayList<TrackedLegViewModel> doInBackground(Object... params) {
        DataHolder dataHolder = (DataHolder) params[0];
        ArrayList<TrackedLegViewModel> dealCards = new ArrayList<>();

        Collection<Deal> deals = dataHolder.getTrackedDeals();
        //No me acuerdo si aca tenian que aparecer todos los deals o solo los deals nuevos que estan
        //trackeados. Descomenta lo de abajo si queres todos
//        Collection<Deal> deals = dataHolder.getDeals();

        for(Deal deal: deals){
            dealCards.add(new TrackedLegViewModel(context,deal));
        }

        return dealCards;
    }

    @Override
    protected void onPostExecute(ArrayList<TrackedLegViewModel> flightCards) {
        list.setAdapter(new TrackedLegCardAdapter(context, flightCards));
    }
}
