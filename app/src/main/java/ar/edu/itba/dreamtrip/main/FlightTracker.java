
package ar.edu.itba.dreamtrip.main;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.SettingsManager;
import ar.edu.itba.dreamtrip.common.notifications.TrackedChangesManager;
import ar.edu.itba.dreamtrip.common.tasks.PopulateAutocompleteTask;
import ar.edu.itba.dreamtrip.common.tasks.TrackFlightTask;
import ar.edu.itba.dreamtrip.common.tasks.TrackLegTask;
import ar.edu.itba.dreamtrip.main.Adapter.PagerAdapter;

public class FlightTracker extends BaseActivity {
    public static boolean isActive = false;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int selectedTab = getIntent().getIntExtra("selectedTab", 1); //DEFAULT IS FLIGHTS



        setupView(R.layout.activity_flight_tracker);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));


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
            final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), getBaseContext());
            viewPager.setAdapter(adapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                    //HIGHLIGHT NAV DRAWER
                    String tabText = tab.getText().toString();
                    if (tabText.equals(getString(R.string.tab_flights))) {
                        setNavSelected(0);
                    } else if (tabText.equals(getString(R.string.tab_destinations))) {
                        setNavSelected(1);
                    } else if (tabText.equals(getString(R.string.tab_search))) {
                        setNavSelected(2);
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.setCurrentItem(selectedTab);

        TrackedChangesManager.getInstance(getApplicationContext()).setupChecks();
        SettingsManager.getInstance(getApplicationContext()).setFlightNotifications(true);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TrackedChangesManager.getInstance(getApplicationContext()).stopJobs();
    }

    private void setTab(int i) {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setCurrentItem(2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        } else {
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

    public void addTrackedDestination(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.dialog_add_tracked_destinations, null);


        final AutoCompleteTextView originAuto = (AutoCompleteTextView) dialogView.findViewById(R.id.origin_to_track);
        originAuto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                DataHolder dataHolder = DataHolder.getInstance(getBaseContext());
                dataHolder.waitForIt(new PopulateAutocompleteTask(dialogView.getContext(), (AutoCompleteTextView)dialogView.findViewById(R.id.origin_to_track), originAuto.getText().toString()));

            }
        });

        final AutoCompleteTextView destinationAuto = (AutoCompleteTextView) dialogView.findViewById(R.id.destination_to_track);
        destinationAuto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                DataHolder dataHolder = DataHolder.getInstance(getBaseContext());
                dataHolder.waitForIt(new PopulateAutocompleteTask(dialogView.getContext(), (AutoCompleteTextView)dialogView.findViewById(R.id.destination_to_track), destinationAuto.getText().toString()));
            }
        });


        builder.setView(dialogView)
                .setNeutralButton(R.string.dialog_add_tracked_destination_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        toast("trying to track destination");
                        String originID = ((AutoCompleteTextView) dialogView.findViewById(R.id.origin_to_track)).getText().toString();
                        String destinationID = ((AutoCompleteTextView) dialogView.findViewById(R.id.destination_to_track)).getText().toString();

                        if (originID.length() < 4 || DataHolder.getInstance(getBaseContext()).getCityById(originID.substring(1,4)) == null) {
                            toast(getResources().getString(R.string.origin_not_found));
                            return;
                        }
                        if (destinationID.length() < 4 || DataHolder.getInstance(getBaseContext()).getCityById(destinationID.substring(1,4)) == null) {
                            toast(getResources().getString(R.string.destination_not_found));
                            return;
                        }
//                        boolean res=SettingsManager.getInstance(getBaseContext()).trackLeg(originID.substring(1,4) + " " + destinationID.substring(1,4));
                        DataHolder.getInstance(getApplicationContext()).waitForIt(
                                new TrackLegTask(getApplicationContext(),originID.substring(1,4) + " " + destinationID.substring(1,4)));
                    }
                });

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.flight_tracker, menu);
        return false;
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