package ar.edu.itba.dreamtrip.common;

import android.content.Context;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

import ar.edu.itba.dreamtrip.TrackedDestinations.TrackedLegCardAdapter;
import ar.edu.itba.dreamtrip.TrackedDestinations.TrackedLegViewModel;
import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.SettingsManager;
import ar.edu.itba.dreamtrip.common.API.dependencies.DealLoadType;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;
import ar.edu.itba.dreamtrip.common.API.dependencies.TrackedLegsDependency;
import ar.edu.itba.dreamtrip.common.model.Airport;
import ar.edu.itba.dreamtrip.common.model.City;
import ar.edu.itba.dreamtrip.common.model.Country;
import ar.edu.itba.dreamtrip.common.model.Deal;
import ar.edu.itba.dreamtrip.common.model.MyCurrency;
import ar.edu.itba.dreamtrip.common.tasks.AsyncTaskInformed;

/**
 * Created by juanfra on 22/11/16.
 */

public class CenterMaper extends AsyncTaskInformed<Object,Void,Void> {

    Context context;
    GoogleMap map;
    String id;
    DependencyType type;

    LatLng pos=null;
    String desc=null;
    Integer zoom=null;


    public CenterMaper(Context context, GoogleMap map,String id,DependencyType type){
        this.context=context;
        this.map=map;
        this.id=id;
        this.type=type;
    }

    @Override
    public HashSet<Dependency> getDependencies() {
        HashSet<Dependency> dependencies = new HashSet<>();
        dependencies.add(new Dependency(DependencyType.AIRPORTS));
        dependencies.add(new Dependency(DependencyType.CITIES));
        dependencies.add(new Dependency(DependencyType.COUNTRIES));
        return dependencies;
    }

    @Override
    protected Void doInBackground(Object... params) {
        DataHolder dataHolder = (DataHolder) params[0];

        switch (type){
            case AIRPORTS:
                Airport a = dataHolder.getAirportById(id);
                pos=new LatLng(a.getLocation().getLatitude(),a.getLocation().getLongitude());
                desc=a.getName();
                zoom=12;
                break;
            case CITIES:
                City c = dataHolder.getCityById(id);
                pos=new LatLng(c.getLocation().getLatitude(),c.getLocation().getLongitude());
                desc=c.getName();
                zoom=10;
                break;
            case COUNTRIES:
                Country co=dataHolder.getCountryById(id);
                pos=new LatLng(co.location.getLatitude(),co.location.getLongitude());
                desc=co.getName();
                zoom=5;
                break;
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        // Add a marker in Sydney and move the camera
        map.addMarker(new MarkerOptions().position(pos).title(desc));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos,zoom));

    }
}
