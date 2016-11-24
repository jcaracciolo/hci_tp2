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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import ar.edu.itba.dreamtrip.R;
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
    public void onListItemClick(ListView l, View v, int position, long id) {
        TrackedFlightViewModel flight = (TrackedFlightViewModel)getListView().getAdapter().getItem(position);
        toast("YOU CLICKED " + flight.getIdentifier());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar = (ProgressBar) getView().findViewById(R.id.loading_trackers);
        getListView().setEmptyView(progressBar);



//        SettingsManager settingsManager = SettingsManager.getInstance(getContext());
//        settingsManager.clearAllTracked();
//        settingsManager.trackFlight("8R 8700");
//        settingsManager.trackFlight("BA 2282");
//        System.out.println(settingsManager.getTrackedFlights());
//        settingsManager.untrackFlight("BA 2282");
//        System.out.println(settingsManager.getTrackedFlights());
//        settingsManager.trackFlight("BA 2282");
//        System.out.println(settingsManager.getTrackedFlights());
        SettingsManager settingsManager = SettingsManager.getInstance(getContext());

        settingsManager.trackLeg("EZE LON");
        settingsManager.trackLeg("LON PAR");

    }

    @Override
    public void onResume() {
        super.onResume();
        final DataHolder dataholder = DataHolder.getInstance(getContext());
        dataholder.waitForIt(new PopulateFlightTrackers(getContext(), getListView()));
        getActivity().registerReceiver(listUpdater,new IntentFilter("ar.edu.itba.dreamtrip.UPDATE_LIST_TRACKER"));
    }

    public void toast(String str) {
        Toast.makeText(getActivity(), str,
                Toast.LENGTH_SHORT).show();
    }

    public void updateList() {
        final DataHolder dataholder = DataHolder.getInstance(getContext());
        dataholder.waitForIt(new PopulateFlightTrackers(getContext(), getListView()));
    }

    @Override
    public void onPause(){
        super.onPause();
        getActivity().unregisterReceiver(listUpdater);
    }

}
