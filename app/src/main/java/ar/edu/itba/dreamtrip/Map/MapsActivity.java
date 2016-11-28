package ar.edu.itba.dreamtrip.Map;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.TrackedDestinations.PopulateLegTrackers;
import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.main.BaseActivity;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback,LocationListener {

    private GoogleMap mMap;
    private LocationManager locManager;
    private String provider;
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private boolean moved=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView(R.layout.fragment_maps);
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
        mMap = googleMap;
        mMap.clear();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                Context context=getBaseContext();
                builder.setMessage(context.getResources().getString(R.string.gps_network_not_enabled));
                final AlertDialog alert = builder.create();
                alert.show();

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            }
        } else {
            getProvider();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getProvider();
                } else {
                    fillMap(null);
                }
                return;
            }
        }
    }

    void moveMapLocation(){
        try {
            startLocationUpdates();
            LatLng lasLatLng=null;


            Location lastLoc = locManager.getLastKnownLocation(provider);
            if(lastLoc != null){
                lasLatLng=new LatLng(lastLoc.getLatitude(),lastLoc.getLongitude());
                MarkerOptions marker=new MarkerOptions().position(lasLatLng);
                marker.title("YOU").snippet("THIS IS YOU");
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_airplane_l));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lasLatLng,10));
                moved=true;
            }
            fillMap(lasLatLng);
        }catch (SecurityException e){

        }

    }

    void fillMap(LatLng lastLatLng){
        final DataHolder dataholder = DataHolder.getInstance(getBaseContext());
        dataholder.waitForIt(new PopulateMaps(getBaseContext(),mMap,lastLatLng));
    }

    void getProvider() {
        locManager = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria crit = new Criteria();
        crit.setAccuracy(Criteria.ACCURACY_FINE);
        crit.setPowerRequirement(Criteria.POWER_LOW);
        provider = locManager.getBestProvider(crit,true);
        if (!locManager.isProviderEnabled(provider)) {
            buildAlertMessageNoGps();
       }else{
            moveMapLocation();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Context context=getBaseContext();
        builder.setMessage(context.getResources().getString(R.string.gps_network_not_enabled))
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.enable), new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(context.getResources().getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        fillMap(null);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng lasLatLng=new LatLng(location.getLatitude(),location.getLongitude());
        MarkerOptions marker=new MarkerOptions().position(lasLatLng);
        marker.title("YOU").snippet("THIS IS YOU");
        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_airplane_l));
        if(!moved){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lasLatLng,10));
            moved=true;
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    protected void startLocationUpdates() {
        try{
            System.out.println("CHANGED");
            locManager.requestLocationUpdates(provider, 400, 1, this);
        }catch (SecurityException e){

        }
    }
}
