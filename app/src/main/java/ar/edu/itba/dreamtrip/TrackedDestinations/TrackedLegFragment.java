package ar.edu.itba.dreamtrip.TrackedDestinations;

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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.SettingsManager;

import static ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType.COUNTRIES;

/**
 * Created by Julian Benitez on 11/17/2016.
 */

public class TrackedLegFragment extends ListFragment {

    public static boolean isActive = false;
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
        View v = inflater.inflate(R.layout.fragment_tracked_destinations, container, false);

        return v;

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
        dataholder.waitForIt(new PopulateLegTrackers(getContext(), getListView()));
        getContext().registerReceiver(listUpdater,new IntentFilter(getContext().getString(R.string.UpdateTrackedDeals)));
        if(getListView().getAdapter()!=null){
            ((TrackedLegCardAdapter)getListView().getAdapter()).register();
        }
        isActive = true;

        progressBar = (ProgressBar) getView().findViewById(R.id.loading_trackers);
        View arrow = getView().findViewById(R.id.help_arrow);
        if (SettingsManager.getInstance(getContext()).getTrackedLegs().size() > 0) {
            getListView().setEmptyView(progressBar);
            arrow.setVisibility(View.INVISIBLE);
        } else {
            getListView().setEmptyView(getView().findViewById(R.id.bullshit));
            progressBar.setVisibility(View.INVISIBLE);
            arrow.setVisibility(View.VISIBLE);
        }

    }

    public void updateList() {
        final DataHolder dataholder = DataHolder.getInstance(getContext());
        dataholder.waitForIt(new PopulateLegTrackers(getContext(), getListView()));

        progressBar = (ProgressBar) getView().findViewById(R.id.loading_trackers);
        View arrow = getView().findViewById(R.id.help_arrow);
        if (SettingsManager.getInstance(getContext()).getTrackedLegs().size() > 0) {
            getListView().setEmptyView(progressBar);
            arrow.setVisibility(View.INVISIBLE);
        } else {
            getListView().setEmptyView(getView().findViewById(R.id.bullshit));
            progressBar.setVisibility(View.INVISIBLE);
            arrow.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if(getListView().getAdapter()!=null){
            ((TrackedLegCardAdapter)getListView().getAdapter()).unregister();
        }
        isActive = false;

        getContext().unregisterReceiver(listUpdater);

    }

}
