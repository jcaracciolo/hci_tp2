package ar.edu.itba.dreamtrip.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import ar.edu.itba.dreamtrip.common.API.dependencies.FlightDealsDependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.TrackedFlightsDependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.TrackedLegsDependency;
import ar.edu.itba.dreamtrip.common.model.Airport;
import ar.edu.itba.dreamtrip.common.model.City;
import ar.edu.itba.dreamtrip.common.model.Deal;
import ar.edu.itba.dreamtrip.common.model.Flight;
import ar.edu.itba.dreamtrip.common.model.FlightState;
import ar.edu.itba.dreamtrip.common.model.FlightStatus;
import ar.edu.itba.dreamtrip.common.tasks.AsyncTaskInformed;
import ar.edu.itba.dreamtrip.flightInfo.FlightInfo;

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

    public HashMap<String,Trace> animated=new HashMap<>();

    CheckBox cbAirpots;
    CheckBox cbCities;
    CheckBox cbFlights;
    CheckBox cbDeals;

    public PopulateMaps(Context context, GoogleMap map,LatLng location,AppCompatActivity view) {
        this.context = context;
        this.map = map;
        this.location=location;
        cbAirpots=(CheckBox)view.findViewById(R.id.airport_check);
        cbCities=(CheckBox)view.findViewById(R.id.cities_check);
        cbFlights=(CheckBox)view.findViewById(R.id.tracked_flights_check);
        cbDeals=(CheckBox)view.findViewById(R.id.deals_check);

        cbAirpots.setEnabled(false);
        cbCities.setEnabled(false);
        cbDeals.setEnabled(false);
        cbFlights.setEnabled(false);
    }

    @Override
    public HashSet<Dependency> getDependencies() {
        HashSet<Dependency> dependencies = new HashSet<>();
        dependencies.add(new Dependency(DependencyType.AIRPORTS));
        dependencies.add(new TrackedFlightsDependency(context,5));
        dependencies.add(new TrackedLegsDependency(context,false, DealLoadType.LAST_MINUTE_DEALS));
        return dependencies;
    }

    @Override
    protected ArrayList<Collection<? extends Object>> doInBackground(Object... params) {
        //OBJECTS - AIRPORTS - CITIES - TRACKEDFLIGHTS
        DataHolder dataHolder = (DataHolder) params[0];
        ArrayList<Collection<? extends Object>> ans = new ArrayList<>();
        ans.add(dataHolder.getAirports());
        ans.add(dataHolder.getCities());
        ans.add(dataHolder.getTrackedFlightStates());
        ans.add(dataHolder.getTrackedDeals());
        return ans;
    }

    @Override
    protected void onPostExecute(ArrayList<Collection<? extends Object>> arrayLists) {
        final Collection<Airport> airports = (Collection<Airport>)arrayLists.get(0);
        Collection<City> cities = (Collection<City>)arrayLists.get(1);
        Collection<FlightState> flights = (Collection<FlightState>)arrayLists.get(2);
        Collection<Deal> deals = (Collection<Deal>)arrayLists.get(3);

        DataHolder data= DataHolder.getInstance(context);

        for (Airport a: airports) {
            MarkerOptions marker = new MarkerOptions().position(new LatLng(a.getLocation().getLatitude(),a.getLocation().getLongitude()));
            marker.title(a.getName());
            marker.icon(getMarkerIconFromDrawable(context.getDrawable(R.mipmap.ic_airport),0.5));
            jumper.put(marker.getTitle(),new ToNewActivity(AirportInfo.class,context,a.getID(),RESULT_ID_KEY));
            mOpAirports.add(marker);
        }

        for (City c: cities) {
            MarkerOptions marker = new MarkerOptions().position(new LatLng(c.getLocation().getLatitude(),c.getLocation().getLongitude()));
            marker.title(c.getName());
            marker.icon(getMarkerIconFromDrawable(context.getDrawable(R.drawable.ic_city),1.3));
            jumper.put(marker.getTitle(),new ToNewActivity(CityInfo.class,context,c.getID(),RESULT_ID_KEY));
            mOpCities.add(marker);
        }

        for(FlightState f: flights){
            Airport origin=data.getAirportById(f.getOrigin().getLocationID());
            Airport dest=data.getAirportById(f.getDestination().getLocationID());
            LatLng origLat=latLng(origin.getLocation());
            LatLng destLat=latLng(dest.getLocation());

            PolylineOptions route = new PolylineOptions().add(origLat).add(destLat);
            route.color(TrackedFlightCardView.getColor(context,f.getStatus()));
            lOpFLights.add(route);

            MarkerOptions marker = new MarkerOptions();
            double percentage=getPercentage(f.getStatus());
            marker.position(getLatLngAtPercentage(origLat,destLat,percentage));
            marker.position(origLat);
            marker.title(f.getIdentifier());
            marker.anchor(0.5f,0.5f);
            marker.icon(getMarkerIconFromDrawable(context.getDrawable(R.drawable.ic_airplane_l),1.5));
            jumper.put(marker.getTitle(),new ToNewActivity(FlightInfo.class,context,f.getIdentifier(),RESULT_ID_KEY));
            mOpFLights.add(marker);

            if(f.getStatus()==FlightStatus.ACTIVE){
                animated.put(marker.getTitle(),new Trace(origLat,destLat));
            }
        }

        double maxPrice=1;
        double minPrice=1;
        if(!deals.isEmpty()){
            maxPrice=Collections.max(deals, new Comparator<Deal>() {
                @Override
                public int compare(Deal d1, Deal d2) {
                    return Double.compare(d1.getPrice(),d2.getPrice());
                }
            }).getPrice();

            minPrice=Collections.min(deals, new Comparator<Deal>() {
                @Override
                public int compare(Deal d1, Deal d2) {
                    return Double.compare(d1.getPrice(),d2.getPrice());
                }
            }).getPrice();
        }

        for(Deal d: deals) {
            City origin = data.getCityById(d.getOriginCityID());
            City dest = data.getCityById(d.getDestinationCityID());
            LatLng origLat = latLng(origin.getLocation());
            LatLng destLat = latLng(dest.getLocation());

            PolylineOptions route = new PolylineOptions().add(origLat).add(destLat);
            route.width(1f);
            if(maxPrice!=minPrice) {
                route.color(getDealColor((maxPrice - d.getPrice()) / (maxPrice - minPrice)));
            }else{
                route.color(Color.rgb(0,255,0));
            }
            lOpDeals.add(route);

            MarkerOptions marker = new MarkerOptions();
            marker.position(destLat);
            marker.title(d.getDestinationDescription());
            marker.anchor(0.5f, 0.5f);
            marker.icon(getMarkerIconFromDrawable(context.getDrawable(R.drawable.ic_city), 0.5));
            mOpDeals.add(marker);
            jumper.put(marker.getTitle(),new ToNewActivity(CityInfo.class,context,d.getDestinationCityID(),RESULT_ID_KEY));
        }

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                jumper.get(marker.getTitle()).jump();
            }
        });

        cbAirpots.setEnabled(true);
        cbCities.setEnabled(true);
        cbDeals.setEnabled(true);
        cbFlights.setEnabled(true);

        cbAirpots.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    addMarkers(mOpAirports,mAirports);
                }else{
                    deleteMarkers(mAirports);
                }
            }
        });

        cbCities.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    addMarkers(mOpCities,mCities);
                }else{
                    deleteMarkers(mCities);
                }
            }
        });

        cbFlights.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    addMarkers(mOpFLights,mFLights);
                    addLines(lOpFLights,lFLights);
                }else{
                    deleteMarkers(mFLights);
                    deleteLines(lFLights);
                }
            }
        });

        cbDeals.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    addMarkers(mOpDeals,mDeals);
                    addLines(lOpDeals,lDeals);
                }else{
                    deleteMarkers(mDeals);
                    deleteLines(lDeals);
                }
            }
        });


        cbCities.toggle();
        cbAirpots.toggle();
        cbDeals.toggle();
        cbFlights.toggle();
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
            Trace t=animated.get(o.getTitle());
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

    public Integer getDealColor(double percentage){
        float hsl[]=new float[3];
        hsl[0] = (float)Math.floor((100 - percentage*100) * 120 / 100.0);  // go from green to red
        hsl[1] = 0.6f;   // fade to white as it approaches 50
        hsl[2] = 0.5f;

        return ColorUtils.HSLToColor(hsl);
    }

}
