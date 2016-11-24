package ar.edu.itba.dreamtrip.common.tasks;

import android.content.Context;
import android.widget.Toast;

import java.util.Collection;
import java.util.HashSet;

import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;
import ar.edu.itba.dreamtrip.common.model.MyCurrency;


public class LoadCurrenciesTask extends AsyncTaskInformed<Object,Void,Collection<MyCurrency>>{

    private Context context;
    public LoadCurrenciesTask(Context context) {
        this.context = context;
    }

    @Override
    public HashSet<Dependency> getDependencies() {
        HashSet<Dependency> dependencies = new HashSet<>();
        dependencies.add(new Dependency(DependencyType.CURRENCIES));
        return dependencies;
    }

    @Override
    protected Collection<MyCurrency> doInBackground(Object... params) {
        DataHolder dataHolder = (DataHolder) params[0];
        return dataHolder.getCurrencies();
    }

    @Override
    protected void onPostExecute(Collection<MyCurrency> result) {
        Toast.makeText(context, "Downloaded currencies: " + result.size(),
                Toast.LENGTH_LONG).show();
    }

}
