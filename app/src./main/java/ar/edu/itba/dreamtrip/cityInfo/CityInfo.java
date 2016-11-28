package ar.edu.itba.dreamtrip.cityInfo;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.airportInfo.AirportDetailsFragment;
import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;
import ar.edu.itba.dreamtrip.common.CenterMaper;
import ar.edu.itba.dreamtrip.main.BaseActivity;

public class CityInfo extends BaseActivity implements CityDetailsFragment.OnFragmentInteractionListener, OnMapReadyCallback {

    private GoogleMap mMap;
    public final static String RESULT_ID_KEY = "main_activity.go_to_info_id";
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView(R.layout.activity_city_info);

        Intent intent = getIntent();
        this.id = intent.getStringExtra(RESULT_ID_KEY);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.activity_city_info, new CityDetailsFragment()).commit();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
      //  findViewById(R.id.)
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        DataHolder.getInstance(getBaseContext()).waitForIt(new CenterMaper(getBaseContext(),mMap,id,DependencyType.CITIES));

    }
}
