package ar.edu.itba.dreamtrip.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import ar.edu.itba.dreamtrip.Deals.DealViewModel;
import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.TrackedFlights.TrackedFlightCardView;
import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.dependencies.DealLoadType;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;
import ar.edu.itba.dreamtrip.common.API.dependencies.FlightDealsDependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.TrackedLegsDependency;
import ar.edu.itba.dreamtrip.common.model.Airport;
import ar.edu.itba.dreamtrip.common.model.Deal;
import ar.edu.itba.dreamtrip.common.tasks.AsyncTaskInformed;

/**
 * Created by juanfra on 28/11/16.
 */

public class PopulateDealsMap extends AsyncTaskInformed<Object,Void,ArrayList<Deal>> {
    public Context context;
    public GoogleMap map;
    public String originID;
    public boolean isCity;

    public PopulateDealsMap(Context context, GoogleMap map, String originID, boolean isCity) {
        this.context = context;
        this.map = map;
        this.originID = originID;
        this.isCity = isCity;
    }

    @Override

    protected ArrayList<Deal> doInBackground(Object... params) {
        DataHolder dataHolder = (DataHolder) params[0];
        ArrayList<Deal> deals = new ArrayList<>();

        Collection<Deal> dealsModels = dataHolder.getDeals();
        for (Deal d: dealsModels) {
            deals.add(d);
        }

        return deals;
    }

    @Override
    public HashSet<Dependency> getDependencies() {
        HashSet<Dependency> dependencies = new HashSet<>();
        if(isCity){
            dependencies.add(new Dependency(DependencyType.CITIES));
        }else {
            dependencies.add(new Dependency(DependencyType.AIRPORTS));
        }
        dependencies.add(new FlightDealsDependency(originID,false, false));

        return dependencies;
    }

    @Override
    protected void onPostExecute(ArrayList<Deal> deals) {
        DataHolder data= DataHolder.getInstance(context);

        for (Deal d :deals) {
            Airport origin=data.getAirportById(d.getOriginCityID());
            Airport dest=data.getAirportById(d.getDestinationCityID());
            LatLng origLat=latLng(origin.getLocation());
            LatLng destLat=latLng(dest.getLocation());

            PolylineOptions route = new PolylineOptions().add(origLat).add(destLat);
            map.addPolyline(route);
            MarkerOptions marker = new MarkerOptions();
            marker.position(destLat);
            marker.title(d.getDestinationDescription());
            marker.icon(getMarkerIconFromDrawable(context.getDrawable(R.drawable.ic_city),0.5));
            map.addMarker(marker);
        }
    }

    public LatLng latLng(Location loc){
        return new LatLng(loc.getLatitude(),loc.getLongitude());
    }



    public BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable, double size) {
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

}
