package ar.edu.itba.dreamtrip.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.TrackedDestinations.TrackedLegViewModel;
import ar.edu.itba.dreamtrip.TrackedFlights.TrackedFlightCardView;
import ar.edu.itba.dreamtrip.airportInfo.AirportInfo;
import ar.edu.itba.dreamtrip.cityInfo.CityInfo;
import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.dependencies.DealLoadType;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;
import ar.edu.itba.dreamtrip.common.API.dependencies.TrackedFlightsDependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.TrackedLegsDependency;
import ar.edu.itba.dreamtrip.common.model.Airport;
import ar.edu.itba.dreamtrip.common.model.City;
import ar.edu.itba.dreamtrip.common.model.Deal;
import ar.edu.itba.dreamtrip.common.model.Flight;
import ar.edu.itba.dreamtrip.common.model.FlightState;
import ar.edu.itba.dreamtrip.common.model.FlightStatus;
import ar.edu.itba.dreamtrip.common.tasks.AsyncTaskInformed;

/**
 * Created by juanfra on 26/11/16.
 */

public class PopulateMaps extends AsyncTaskInformed<Object,Void,ArrayList<Collection<? extends Object>>> {

    public final static String RESULT_ID_KEY = "main_activity.go_to_info_id";

    public Context context;
    public GoogleMap map;
    public LatLng location;

    public Collection<Marker> mAirports=new ArrayList<>();
    public Collection<Marker> mCities=new ArrayList<>();
    public Collection<Marker> mFLights=new ArrayList<>();
    public Collection<Marker> mDeals=new ArrayList<>();

    public Collection<MarkerOptions> mOpAirports=new ArrayList<>();
    public Collection<MarkerOptions> mOpCities=new ArrayList<>();
    public Collection<MarkerOptions> mOpFLights=new ArrayList<>();
    public Collection<MarkerOptions> mOpDeals=new ArrayList<>();

    public Collection<PolylineOptions> lOpFLights=new ArrayList<>();
    public Collection<PolylineOptions> lOpDeals=new ArrayList<>();

    public Collection<Polyline> lFLights=new ArrayList<>();;
    public Collection<Polyline> lDeals=new ArrayList<>();

    public HashMap<String,ToNewActivity> jumper=new HashMap<>();

    public HashMap<MarkerOptions,Trace> animated=new HashMap<>();

    public PopulateMaps(Context context, GoogleMap map, LatLng location) {
        this.context = context;
        this.map = map;
        this.location = location;
    }

    @Override
    public HashSet<Dependency> getDependencies() {
        HashSet<Dependency> dependencies = new HashSet<>();
        dependencies.add(new Dependency(DependencyType.AIRPORTS));
        dependencies.add(new Dependency(DependencyType.CITIES));
        dependencies.add(new TrackedFlightsDependency(context,5));
        return dependencies;
    }

    @Override
    protected ArrayList<Collection<? extends Object>> doInBackground(Object... params) {
        //OBJECTS - AIRPORTS - CITIES - TRACKEDFLIGHTS
        DataHolder dataHolder = (DataHolder) params[0];
        ArrayList<Collection<? extends Object>> ans = new ArrayList<>();
        ans.add(dataHolder.getAirports());
        ans.add(dataHolder.getCities());
        ans.add(dataHolder.getFlightStates());

        return ans;
    }

    @Override
    protected void onPostExecute(ArrayList<Collection<? extends Object>> arrayLists) {
        Collection<Airport> airports = (Collection<Airport>)arrayLists.get(0);
        Collection<City> cities = (Collection<City>)arrayLists.get(1);
        Collection<FlightState> flights = (Collection<FlightState>)arrayLists.get(2);

        for (Airport a: airports) {
            MarkerOptions marker = new MarkerOptions().position(new LatLng(a.getLocation().getLatitude(),a.getLocation().getLongitude()));
            marker.title(a.getName());
            marker.icon(getMarkerIconFromDrawable(context.getDrawable(R.drawable.ic_place),1.5));
            jumper.put(a.getName(),new ToNewActivity(AirportInfo.class,context,a.getID(),RESULT_ID_KEY));
            mOpAirports.add(marker);
        }

        for (City c: cities) {
            MarkerOptions marker = new MarkerOptions().position(new LatLng(c.getLocation().getLatitude(),c.getLocation().getLongitude()));
            marker.title(c.getName());
            marker.icon(getMarkerIconFromDrawable(context.getDrawable(R.drawable.ic_city),0.5));
            jumper.put(c.getName(),new ToNewActivity(CityInfo.class,context,c.getID(),RESULT_ID_KEY));
            mOpCities.add(marker);
        }

        DataHolder data= DataHolder.getInstance(context);

        for(FlightState f: flights){
            Airport origin=data.getAirportById(f.getOrigin().getLocationID());
            Airport dest=data.getAirportById(f.getDestination().getLocationID());
            LatLng origLat=latLng(origin.getLocation());
            LatLng destLat=latLng(dest.getLocation());

            PolylineOptions route = new PolylineOptions().add(origLat).add(destLat);
            route.color(TrackedFlightCardView.getColor(context,f.getStatus()));
            map.addPolyline(route);

            MarkerOptions marker = new MarkerOptions();
            double percentage=getPercentage(f.getStatus());
            marker.position(getLatLngAtPercentage(origLat,destLat,percentage));
            marker.position(origLat);
            marker.title(f.getIdentifier());
            marker.anchor(0.5f,0.5f);
            marker.icon(getMarkerIconFromDrawable(context.getDrawable(R.drawable.ic_airplane_l),1.5));
            jumper.put(f.getIdentifier(),new ToNewActivity(AirportInfo.class,context,f.getIdentifier(),RESULT_ID_KEY));
            mOpFLights.add(marker);

            if(f.getStatus()==FlightStatus.ACTIVE){
                animated.put(marker,new Trace(origLat,destLat));
            }
        }

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                jumper.get(marker.getTitle()).jump();
            }
        });
    }

    public void deleteMarkers(Collection<Marker> markers){
        for (Marker m:markers) {
            m.remove();
        }
    }

    public void addMarkers(Collection<MarkerOptions> moptions,Collection<Marker> markers){
        markers.clear();
        for(MarkerOptions o: moptions){
            Marker m=map.addMarker(o);
            markers.add(m);
            Trace t=animated.get(o);
            if(t!=null){
                m.showInfoWindow();
                m.setRotation((float) SphericalUtil.computeHeading(t.orig, t.dest));
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(t.orig).include(t.dest);
                LatLngBounds bounds = builder.build();
                MarkerAnimation.animateMarkerToHC(m, t.dest, new LatLngInterpolator.LinearFixed());
            }
        }
    }

    public void addLines(Collection<PolylineOptions> pOptions,Collection<Polyline> lines){
        lines.clear();
        for(PolylineOptions l: pOptions){
            lines.add(map.addPolyline(l));
        }
    }

    public void deleteLines(Collection<Polyline> lines){
        for(Polyline p: lines){
            p.remove();
        }
    }

    public BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable,double size) {
        Canvas canvas = new Canvas();
        Integer width=(Double.valueOf(drawable.getIntrinsicWidth()*size)).intValue();
        Integer height=(Double.valueOf(drawable.getIntrinsicHeight()*size)).intValue();
        Bitmap bitmap;
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, width,height);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public LatLng latLng(Location loc){
        return new LatLng(loc.getLatitude(),loc.getLongitude());
    }

    public LatLng getLatLngAtPercentage(LatLng start,LatLng finish,double percentage){
        double newX=start.latitude * (1-percentage) + finish.latitude*percentage;
        double newY=start.longitude * (1-percentage) + finish.longitude*percentage;
        return new LatLng(newX,newY);

    }

    private double getPercentage(FlightStatus status){
        double percentage=0.05;
        switch (status) {
            case SCHEDULED:
            case DELAYED:
            case CANCELLED:
                percentage=0.05;
            case ACTIVE:
                percentage=0;
            case LANDED:
                percentage=0.95;
        }
        return percentage;
    }

    private class Trace{
        public LatLng orig;
        public LatLng dest;

        public Trace(LatLng orig, LatLng dest) {
            this.orig = orig;
            this.dest = dest;
        }
    }

}
