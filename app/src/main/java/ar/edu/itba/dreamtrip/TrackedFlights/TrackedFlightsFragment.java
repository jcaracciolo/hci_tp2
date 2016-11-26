package ar.edu.itba.dreamtrip.TrackedFlights;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.TrackedDestinations.TrackedLegCardAdapter;
import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.SettingsManager;

/**
 * Created by Julian Benitez on 11/17/2016.
 */

public class TrackedFlightsFragment extends ListFragment {

    ProgressBar progressBar;
    private BroadcastReceiver listUpdater;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listUpdater = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateList();
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tracked_flights, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar = (ProgressBar) getView().findViewById(R.id.loading_trackers);
        getListView().setEmptyView(progressBar);
    }

    @Override
    public void onResume() {
        super.onResume();
        final DataHolder dataholder = DataHolder.getInstance(getContext());
        dataholder.waitForIt(new PopulateFlightTrackers(getContext(), getListView()));
        getContext().registerReceiver(listUpdater,new IntentFilter(getContext().getResources().getString(R.string.UpdateTrackedFlights)));
        if(getListView().getAdapter()!=null){
            ((TrackedFlightCardAdapter)getListView().getAdapter()).register();
        }
    }

    public void updateList() {
        final DataHolder dataholder = DataHolder.getInstance(getContext());
        dataholder.waitForIt(new PopulateFlightTrackers(getContext(), getListView()));
    }

    @Override
    public void onPause(){
        super.onPause();
        getContext().unregisterReceiver(listUpdater);
        if(getListView().getAdapter()!=null){
            ((TrackedFlightCardAdapter)getListView().getAdapter()).unregister();
        }
    }

}
