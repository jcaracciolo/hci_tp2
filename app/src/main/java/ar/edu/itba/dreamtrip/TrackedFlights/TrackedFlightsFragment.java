package ar.edu.itba.dreamtrip.TrackedFlights;

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

    //TODO delete
    //static ArrayList<TrackedFlightViewModel> flightCards = new ArrayList<>();

    ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
//
        //FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        super.onCreate(savedInstanceState);
        toast("hi there!");
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



        final DataHolder dataholder = DataHolder.getInstance(getContext());

        SettingsManager settingsManager = SettingsManager.getInstance(getContext());
        settingsManager.clearTrackedFlights();
        settingsManager.trackFlight("8R 8700");
        settingsManager.trackFlight("BA 2282");
        System.out.println(settingsManager.getTrackedFlights());
        settingsManager.untrackFlight("BA 2282");
        System.out.println(settingsManager.getTrackedFlights());
        settingsManager.trackFlight("BA 2282");
        System.out.println(settingsManager.getTrackedFlights());
        dataholder.waitForIt(new PopulateFlightTrackers(getContext(), getListView()));
    }

    public void toast(String str) {
        Toast.makeText(getActivity(), str,
                Toast.LENGTH_SHORT).show();
    }


}
