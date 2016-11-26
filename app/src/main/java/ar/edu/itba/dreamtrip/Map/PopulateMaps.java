package ar.edu.itba.dreamtrip.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.TrackedDestinations.TrackedLegViewModel;
import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.dependencies.DealLoadType;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;
import ar.edu.itba.dreamtrip.common.API.dependencies.TrackedLegsDependency;
import ar.edu.itba.dreamtrip.common.model.Airport;
import ar.edu.itba.dreamtrip.common.model.City;
import ar.edu.itba.dreamtrip.common.model.Deal;
import ar.edu.itba.dreamtrip.common.tasks.AsyncTaskInformed;

/**
 * Created by juanfra on 26/11/16.
 */

public class PopulateMaps extends AsyncTaskInformed<Object,Void,ArrayList<Collection<? extends Object>>> {

    public Context context;
    public GoogleMap map;
    public LatLng location;

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
        return dependencies;
    }

    @Override
    protected ArrayList<Collection<? extends Object>> doInBackground(Object... params) {
        //OBJECTS - AIRPORTS - CITIES - TRACKEDFLIGHTS
        DataHolder dataHolder = (DataHolder) params[0];
        ArrayList<Collection<? extends Object>> ans = new ArrayList<>();
        ans.add(dataHolder.getAirports());
        ans.add(dataHolder.getCities());

        return ans;
    }

    @Override
    protected void onPostExecute(ArrayList<Collection<? extends Object>> arrayLists) {
        Collection<Airport> airports = (Collection<Airport>)arrayLists.get(0);
        Collection<City> cities = (Collection<City>)arrayLists.get(1);

        for (Airport a: airports) {
            MarkerOptions marker = new MarkerOptions().position(new LatLng(a.getLocation().getLatitude(),a.getLocation().getLongitude()));
            marker.title(a.getName());
            map.addMarker(marker);
        }

        for (City c: cities) {
            MarkerOptions marker = new MarkerOptions().position(new LatLng(c.getLocation().getLatitude(),c.getLocation().getLongitude()));
            marker.title(c.getName());
            marker.icon(getMarkerIconFromDrawable(context.getDrawable(R.drawable.ic_arrow_right)));
            map.addMarker(marker);
            marker.title("AAA");
        }
    }
    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap;
        bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
