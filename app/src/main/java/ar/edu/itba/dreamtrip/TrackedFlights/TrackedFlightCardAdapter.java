package ar.edu.itba.dreamtrip.TrackedFlights;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import java.util.ArrayList;

import ar.edu.itba.dreamtrip.R;

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
            view= TrackedFlightCardView.getView(context,LayoutInflater.from(context),parent,card);
        }else{
            view=convertView;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"ASD",Toast.LENGTH_LONG);
            }
        });

        return view;
    }
}
