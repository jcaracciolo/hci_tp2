package ar.edu.itba.dreamtrip.main.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ar.edu.itba.dreamtrip.TrackedDestinations.TrackedLegFragment;
import ar.edu.itba.dreamtrip.main.SearchFragment;
import ar.edu.itba.dreamtrip.TrackedFlights.TrackedFlightsFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                SearchFragment tab1 = new SearchFragment();
                return tab1;
            case 1:
                TrackedFlightsFragment tab2 = new TrackedFlightsFragment();
                return tab2;
            case 2:
                TrackedLegFragment tab3 = new TrackedLegFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}