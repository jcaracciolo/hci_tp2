package ar.edu.itba.dreamtrip.flightInfo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import java.util.HashSet;

import ar.edu.itba.dreamtrip.Map.LatLngInterpolator;
import ar.edu.itba.dreamtrip.Map.MarkerAnimation;
import ar.edu.itba.dreamtrip.Map.PopulateMaps;
import ar.edu.itba.dreamtrip.Map.ToNewActivity;
import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.TrackedFlights.TrackedFlightCardView;
import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;
import ar.edu.itba.dreamtrip.common.API.dependencies.FlightDependency;
import ar.edu.itba.dreamtrip.common.model.Airport;
import ar.edu.itba.dreamtrip.common.model.Flight;
import ar.edu.itba.dreamtrip.common.model.FlightStatus;
import ar.edu.itba.dreamtrip.common.model.StatusSearch;
import ar.edu.itba.dreamtrip.common.tasks.AsyncTaskInformed;
import ar.edu.itba.dreamtrip.main.ResultElement;

/**
 * Created by Pedro on 28/11/2016.
 */

public class CenterFlight extends AsyncTaskInformed<Object,Void,Flight>{

    GoogleMap map;
    String id;
    Context context;

    public CenterFlight(Context context,GoogleMap map, String id) {
        this.map = map;
        this.id = id;
        this.context=context;
    }


    @Override
    protected Flight doInBackground(Object... objects) {

        DataHolder data=DataHolder.getInstance(context);
        for (Flight f : data.getFlights()) {
            if (f.getIdentifier().equals(id)) {
                return f;
            }
        }

        return null;
    }

    @Override
    public HashSet<Dependency> getDependencies() {
        HashSet<Dependency> dependencies = new HashSet<>();
        dependencies.add(new FlightDependency(new StatusSearch(id.split(" ")[0],Integer.parseInt(id.split(" ")[1]))));
        return dependencies;
    }

    @Override
    protected void onPostExecute(Flight f) {
        if(f==null) return;
        DataHolder data=DataHolder.getInstance(context);
        Airport origin=data.getAirportById(f.getOrigin().getID());
        Airport dest=data.getAirportById(f.getDestination().getID());
        LatLng origLat=latLng(origin.getLocation());
        LatLng destLat=latLng(dest.getLocation());

        PolylineOptions route = new PolylineOptions().add(origLat).add(destLat);
        route.color(Color.rgb(255,0,0));
        map.addPolyline(route);

        MarkerOptions marker = new MarkerOptions();
        marker.position(origLat);
        marker.title(f.getIdentifier());
        marker.anchor(0.5f,0.5f);
        marker.icon(getMarkerIconFromDrawable(context.getDrawable(R.drawable.ic_airplane_l),1.5));

        Marker m =map.addMarker(marker);
        m.showInfoWindow();
        m.setRotation((float) SphericalUtil.computeHeading(origLat, destLat));
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(origLat).include(destLat);
        LatLngBounds bounds = builder.build();
        MarkerAnimation.animateMarkerToHC(m, destLat, new LatLngInterpolator.LinearFixed());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(origLat,5));

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
