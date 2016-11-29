package ar.edu.itba.dreamtrip.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import ar.edu.itba.dreamtrip.Map.MapsActivity;
import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.Settings;

/**
 * Created by juanfra on 22/11/16.
 */

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    final static String OPEN_QR = "open_qr_in_search";

    public DrawerLayout mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_base_navdrawer);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        boolean alreadyonmain = false;

        if (this.getClass().toString().contains("FlightTracker")) {
            alreadyonmain = true;
        }

        //TODO: Hacer que al tocar las tabs se haga highlight en el nav drawer
        //TODO: Hacer que si se crea una nueva activity se haga highlight el tab del nav drawer que acabas de clickear

        switch (id) {
            case R.id.drawer_flights: {
                if (!alreadyonmain) {
                    Intent intent = new Intent(this, FlightTracker.class);
                    intent.putExtra("selectedTab", 1);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
                    viewPager.setCurrentItem(1);
                }
                break;
            }
            case R.id.drawer_destinations: {
                if (!alreadyonmain) {
                    Intent intent = new Intent(this, FlightTracker.class);
                    intent.putExtra("selectedTab", 2);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
                    viewPager.setCurrentItem(2);
                }
                break;
            }
            case R.id.drawer_search: {
                if (!alreadyonmain) {
                    Intent intent = new Intent(this, FlightTracker.class);
                    intent.putExtra("selectedTab", 0);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
                    viewPager.setCurrentItem(0);
                }
                break;
            }
            case R.id.drawer_qr: {
                Intent intent = new Intent(this, FlightTracker.class);
                intent.putExtra("selectedTab", 0);
                intent.putExtra(OPEN_QR, true);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            }
            case R.id.drawer_map: {
                Intent intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.drawer_settings: {
                Intent intent = new Intent(this, Settings.class);
                startActivity(intent);
                break;
            }
            case R.id.drawer_help: {
                break;
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void setNavSelected(int position) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(position).setChecked(true);
        onNavigationItemSelected(navigationView.getMenu().getItem(position));

    }

    public void setupView(int layout) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(layout, null, false);
        mDrawer.addView(contentView, 0);
    }

}
