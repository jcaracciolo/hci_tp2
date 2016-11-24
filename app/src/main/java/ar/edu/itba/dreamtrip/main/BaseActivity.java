package ar.edu.itba.dreamtrip.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import ar.edu.itba.dreamtrip.R;

/**
 * Created by juanfra on 22/11/16.
 */

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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

        //TODO: HAY QUE HACER UN HIGHLIGHT EN LA SECCION DONDE NOS ENCONTRAMOS, Y SI YA ESTA SELECCIONADA QUE NO HAGA UN NEW INTENT.
        //TODO: EN CASO DE QUE SEA ALGUNO DE LOS PRIMEROS TRES, QUE SOLO ABRA LA TAB Y NO HAGA UN INTENT.

        switch (id) {
            case R.id.drawer_flights: {
                Intent intent = new Intent(this, FlightTracker.class);
                startActivity(intent);
                break;
            }
            case R.id.drawer_destinations: {
                break;
            }
            case R.id.drawer_search: {
                break;
            }
            case R.id.drawer_qr: {
                break;
            }
            case R.id.drawer_map: {
                break;
            }
            case R.id.drawer_currency: {
                break;
            }
            case R.id.drawer_settings: {
                break;
            }
            case R.id.drawer_help: {
                break;
            }
        }
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void setupView(int layout){
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(layout, null, false);
        mDrawer.addView(contentView, 0);
    }

}
