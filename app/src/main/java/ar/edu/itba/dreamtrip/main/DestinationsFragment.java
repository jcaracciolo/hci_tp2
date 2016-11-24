package ar.edu.itba.dreamtrip.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ar.edu.itba.dreamtrip.R;

/**
 * Created by Julian Benitez on 11/17/2016.
 */

public class DestinationsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_destinations, container, false);
    }
}