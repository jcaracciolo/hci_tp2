package ar.edu.itba.dreamtrip.Deals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import ar.edu.itba.dreamtrip.TrackedFlights.TrackedFlightViewModel;

/**
 * Created by juanfra on 23/11/16.
 */

public class DealsAdapter  extends BaseAdapter {
    Context context;
    ArrayList<DealViewModel> deals;

    public DealsAdapter(Context context,ArrayList<DealViewModel> deals){
        this.context=context;
        this.deals=deals;
    }

    @Override
    public int getCount() {
        return deals.size();
    }

    @Override
    public DealViewModel getItem(int i) {
        return deals.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        DealViewModel deal=this.getItem(i);
        View view=convertView;

        if(view==null){
            view=DealView.getView(context,LayoutInflater.from(context),parent,deal);
        }

        return view;
    }
}
