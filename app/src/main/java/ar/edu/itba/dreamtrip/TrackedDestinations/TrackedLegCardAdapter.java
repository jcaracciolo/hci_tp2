package ar.edu.itba.dreamtrip.TrackedDestinations;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.ArrayList;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.common.API.SettingsManager;

/**
 * Created by juanfra on 22/11/16.
 */

public class TrackedLegCardAdapter extends BaseSwipeAdapter {
    Context context;
    ArrayList<TrackedLegViewModel> legsCards;
    BroadcastReceiver deleteDest;

    public TrackedLegCardAdapter(Context context, ArrayList<TrackedLegViewModel> legsCards){
        this.context=context;
        this.legsCards = legsCards;
        deleteDest=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String lID = intent.getExtras().getString(context.getResources().getString(R.string.RetrieveDeleteDealIntent));
                SettingsManager.getInstance(context).untrackLeg(lID);
                Intent it = new Intent(context.getResources().getString(R.string.UpdateTrackedDeals));
                context.sendBroadcast(it);
            }
        };
        context.registerReceiver(deleteDest,new IntentFilter(context.getResources().getString(R.string.deleteDealIntent)));
    }

    @Override
    public int getCount() {
        return legsCards.size();
    }

    @Override
    public TrackedLegViewModel getItem(int i) {
        return legsCards.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_track_deal;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.fragment_swipeable_tracked_destinations_card, parent, false);
    }

    @Override
    public void fillValues(int position, View view) {
        TrackedLegCardView.fillView(context,getItem(position),view);
    }

    public void unregister(){
        context.unregisterReceiver(deleteDest);
    }

}
