
package ar.edu.itba.dreamtrip.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.SettingsManager;
import ar.edu.itba.dreamtrip.common.notifications.TrackedChangesManager;
import ar.edu.itba.dreamtrip.common.tasks.TrackFlightTask;
import ar.edu.itba.dreamtrip.main.Adapter.PagerAdapter;

public class FlightTracker extends BaseActivity {

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            SearchFragment search_fragment = (SearchFragment) getSupportFragmentManager().getFragments().get(1);
            search_fragment.onActivityResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int selectedTab = getIntent().getIntExtra("selectedTab", 1); //DEFAULT IS FLIGHTS

        setupView(R.layout.activity_flight_tracker);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        //TODO SE ICONS WHITE
//        Menu menu=toolbar.getMenu();
//        for (int i = 0; i < menu.size(); i++) {
//            MenuItem item = menu.getItem(i);
//            Drawable icon = item.getIcon();
//            if (icon != null) {
//                icon.setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN));
//            }
//        }
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_search));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_flights));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_destinations));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.setCurrentItem(selectedTab);
        SettingsManager.getInstance(getApplicationContext()).clearAllTracked();
        TrackedChangesManager.getInstance(getApplicationContext()).setupChecks();
        SettingsManager.getInstance(getApplicationContext()).trackLeg("EZE LON");

    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)){
            final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        }else {
            final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
            if (viewPager.getCurrentItem() == 1) {
                super.onBackPressed();
            } else {
                viewPager.setCurrentItem(1);
            }
        }
    }

    public void addTrackedFlight(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getLayoutInflater();

        final View textView = inflater.inflate(R.layout.dialog_add_tracked_flight, null);
        builder.setView(textView)
                .setNeutralButton(R.string.dialog_add_tracked_flight_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String flightID = ((TextView) textView.findViewById(R.id.flight_id_dialog)).getText().toString().toUpperCase().trim();

                        if (!flightID.matches("^\\w{2}\\W+\\d{3,4}$")) {

                        } else {
                            flightID = flightID.trim().split("\\W")[0] + " " + flightID.trim().split("\\W")[1];
                            DataHolder.getInstance(getApplicationContext()).waitForIt(
                                    new TrackFlightTask(getApplicationContext(), flightID));

                        }
                    }
                });

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.flight_tracker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Context getActivity() {
        return this;
    }

    public void toast(String str) {
        Toast.makeText(getActivity(), str,
                Toast.LENGTH_LONG).show();
    }

}