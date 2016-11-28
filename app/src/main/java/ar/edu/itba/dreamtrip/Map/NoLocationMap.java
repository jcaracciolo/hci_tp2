package ar.edu.itba.dreamtrip.Map;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.main.BaseActivity;

/**
 * Created by juanfra on 28/11/16.
 */

public class NoLocationMap extends BaseActivity implements OnMapReadyCallback {
    GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView(R.layout.fragment_nolocation_maps);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        mMap.clear();
        final DataHolder dataholder = DataHolder.getInstance(getBaseContext());
        dataholder.waitForIt(new PopulateDealsMap(getBaseContext(),mMap,"EZE",false));
    }
}
