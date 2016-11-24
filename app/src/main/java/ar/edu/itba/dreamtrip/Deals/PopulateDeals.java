package ar.edu.itba.dreamtrip.Deals;

import android.content.Context;
import android.widget.AbsListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;
import ar.edu.itba.dreamtrip.common.model.Deal;
import ar.edu.itba.dreamtrip.common.tasks.AsyncTaskInformed;

/**
 * Created by juanfra on 23/11/16.
 */

public class PopulateDeals extends AsyncTaskInformed<Object,Void,ArrayList<DealViewModel>>  {

    Context context;
    AbsListView listView;
    static DealsAdapter dealsAdapter;

    public PopulateDeals(Context context,AbsListView listView){
        this.listView = listView;
        this.context=context;
    }

    public HashSet<Dependency> getDependencies() {
        HashSet<Dependency> dependencies = new HashSet<>();
        dependencies.add(new Dependency(DependencyType.AIRPORTS));
        return dependencies;
    }

    @Override
    protected ArrayList<DealViewModel> doInBackground(Object... params){
        DataHolder dataHolder = (DataHolder) params[0];
        ArrayList<DealViewModel> deals = new ArrayList<>();

        Collection<Deal> dealsModels = dataHolder.getDeals();
        for (Deal d: dealsModels) {
            deals.add(new DealViewModel(context,d));
        }

        return deals;

    }

    @Override
    protected void onPostExecute(ArrayList<DealViewModel> deals) {
       listView.setAdapter(new DealsAdapter(context, deals));
    }
}
