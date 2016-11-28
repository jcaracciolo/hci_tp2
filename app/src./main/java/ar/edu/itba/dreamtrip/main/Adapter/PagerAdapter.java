package ar.edu.itba.dreamtrip.main.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.TrackedDestinations.TrackedLegFragment;
import ar.edu.itba.dreamtrip.main.SearchFragment;
import ar.edu.itba.dreamtrip.TrackedFlights.TrackedFlightsFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    Context context;

    public PagerAdapter(FragmentManager fm, int NumOfTabs, Context context) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                String isLandTablet = context.getResources().getString(R.string.isLandTablet);
                if (isLandTablet.equals("false")) {
                    return new SearchFragment();
                } else {
                    return new SearchFragment();
                }
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