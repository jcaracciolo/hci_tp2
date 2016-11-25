package ar.edu.itba.dreamtrip.TrackedDestinations;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by juanfra on 22/11/16.
 */

public class TrackedLegCardAdapter extends BaseAdapter {
    Context context;
    ArrayList<TrackedLegViewModel> legsCards;

    public TrackedLegCardAdapter(Context context, ArrayList<TrackedLegViewModel> legsCards){
        this.context=context;
        this.legsCards = legsCards;

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
    public View getView(int i, View convertView, ViewGroup parent) {
        TrackedLegViewModel card=this.getItem(i);
        View view = TrackedLegCardView.getView(context,LayoutInflater.from(context),parent,card);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"ASD",Toast.LENGTH_LONG);
            }
        });

        return view;
    }
}
