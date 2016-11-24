package ar.edu.itba.dreamtrip.main.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import ar.edu.itba.dreamtrip.main.viewModels.TrackedFlightViewModel;
import ar.edu.itba.dreamtrip.main.TrackedFlightCardView;

/**
 * Created by juanfra on 22/11/16.
 */

public class TrackedFlightCardAdapter extends BaseAdapter {
    Context context;
    ArrayList<TrackedFlightViewModel> flightCards;

    public TrackedFlightCardAdapter(Context context,ArrayList<TrackedFlightViewModel> flightCards){
        this.context=context;
        this.flightCards=flightCards;

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

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        TrackedFlightViewModel card=this.getItem(i);
        View view;

        if(convertView==null){
            TrackedFlightCard tempCard= TrackedFlightCard.newInstance(card);
            tempCard.context=context;
            view=tempCard.onCreateView(LayoutInflater.from(context),parent,null);
        }else{
            view=convertView;
        }

        return view;
    }
}
