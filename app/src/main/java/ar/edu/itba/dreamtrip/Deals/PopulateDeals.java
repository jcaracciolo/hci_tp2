package ar.edu.itba.dreamtrip.Deals;

import android.content.Context;
import android.widget.AbsListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.SettingsManager;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;
import ar.edu.itba.dreamtrip.common.API.dependencies.FlightDealsDependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.TrackedLegsDependency;
import ar.edu.itba.dreamtrip.common.model.Deal;
import ar.edu.itba.dreamtrip.common.model.MyCurrency;
import ar.edu.itba.dreamtrip.common.tasks.AsyncTaskInformed;

/**
 * Created by juanfra on 23/11/16.
 */

public class PopulateDeals extends AsyncTaskInformed<Object,Void,ArrayList<DealViewModel>>  {

    Context context;
    AbsListView listView;
    String originID;
    static DealsAdapter dealsAdapter;
    HashSet<Dependency> dependencies;

    public PopulateDeals(Context context,AbsListView listView,String originID){
        this.listView = listView;
        this.context=context;
        this.originID=originID;
        dependencies = new HashSet<>();
        dependencies.add(new Dependency(DependencyType.AIRPORTS));
        dependencies.add(new FlightDealsDependency(originID,false, false));
    }

    public HashSet<Dependency> getDependencies() {
        return dependencies;
    }

    @Override
    protected ArrayList<DealViewModel> doInBackground(Object... params){
        DataHolder dataHolder = (DataHolder) params[0];
        ArrayList<DealViewModel> deals = new ArrayList<>();
        String validID = originID;
        if(dataHolder.getAirportById(originID) != null) {
            validID = dataHolder.getAirportById(originID).getCity().getID();
        }

        Collection<Deal> dealsModels = dataHolder.getDealsFromOrigin(validID);
        String preferredCurrencyID = SettingsManager.getInstance(context).getPreferredCurrencyID();
        MyCurrency preferredCurrency = dataHolder.getCurrencyByID(preferredCurrencyID);

        for (Deal d: dealsModels) {
            deals.add(new DealViewModel(context,d,preferredCurrency));
        }

        return deals;
    }

    @Override
    protected void onPostExecute(ArrayList<DealViewModel> deals) {
       listView.setAdapter(new DealsAdapter(context, deals));
    }
}
