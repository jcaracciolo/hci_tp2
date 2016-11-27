package ar.edu.itba.dreamtrip.TrackedFlights;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.ArrayList;
import java.util.zip.Inflater;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.common.API.SettingsManager;
import ar.edu.itba.dreamtrip.common.model.Flight;

/**
 * Created by juanfra on 22/11/16.
 */

public class TrackedFlightCardAdapter extends BaseSwipeAdapter {

    Context context;
    ArrayList<TrackedFlightViewModel> flightCards;
    private BroadcastReceiver deleteFlight;

    public TrackedFlightCardAdapter(Context context,ArrayList<TrackedFlightViewModel> flightCards){
        this.context=context;
        this.flightCards=flightCards;
        deleteFlight=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String fID = intent.getExtras().getString(context.getResources().getString(R.string.RetrieveDeleteFlightIntent));
                SettingsManager.getInstance(context).untrackFlight(fID);
                Intent it = new Intent(context.getResources().getString(R.string.UpdateTrackedFlights));
                context.sendBroadcast(it);
            }
        };

        context.registerReceiver(deleteFlight,new IntentFilter(context.getResources().getString(R.string.deleteFlightIntent)));
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_track_flight;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.fragment_swipeable_tracked_flight_card, parent, false);
    }

    @Override
    public void fillValues(int position,final View view) {
        final TrackedFlightViewModel card=this.getItem(position);
        TrackedFlightCardView.fillView(context,LayoutInflater.from(context),card,view);
    }

    @Override
    public int getCount() {
        return flightCards.size();
    }

    @Override
    public TrackedFlightViewModel getItem(int i) {
        return flightCards.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void unregister(){
        try{
            context.unregisterReceiver(deleteFlight);
        }catch (Exception e){

        }
    }

    public void register(){
        context.registerReceiver(deleteFlight,new IntentFilter(context.getResources().getString(R.string.deleteFlightIntent)));
    }


}
