package ar.edu.itba.dreamtrip.countryInfo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.cityInfo.CityDetailsFragment;
import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;
import ar.edu.itba.dreamtrip.common.CenterMaper;
import ar.edu.itba.dreamtrip.common.model.Country;
import ar.edu.itba.dreamtrip.main.BaseActivity;

public class CountryInfo extends BaseActivity implements CountryDetailsFragment.OnFragmentInteractionListener, OnMapReadyCallback {

    private GoogleMap mMap;
    private Country country;
    public final static String RESULT_ID_KEY = "main_activity.go_to_info_id";
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView(R.layout.activity_country_info);

        Intent intent = getIntent();
        this.id = intent.getStringExtra(RESULT_ID_KEY);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.activity_country_info, new CountryDetailsFragment()).commit();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        DataHolder.getInstance(getBaseContext()).waitForIt(new CenterMaper(getBaseContext(),mMap,id, DependencyType.COUNTRIES));


    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
