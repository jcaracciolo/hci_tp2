package ar.edu.itba.dreamtrip.main.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;
import ar.edu.itba.dreamtrip.common.model.Airline;
import ar.edu.itba.dreamtrip.main.ResultElement;

/**
 * Created by Julian Benitez on 11/19/2016.
 */

public class ResultAdapter extends BaseAdapter {
    Context context;
    ArrayList<ResultElement> data;
    private static LayoutInflater inflater = null;

    public ResultAdapter(Context context, ArrayList<ResultElement> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null) {
            vi = inflater.inflate(R.layout.result_row, null);
        }

        TextView text = (TextView) vi.findViewById(R.id.result_description);
        text.setText(data.get(position).getElement().getName());

        ImageView img = (ImageView) vi.findViewById(R.id.result_image);
        if (data.get(position).getDepType() == DependencyType.AIRLINES) {
            Airline airline = (Airline)data.get(position).getElement();
            img.setImageBitmap(airline.getLogo());
        } else {
            img.setImageResource(getIconByResult(data.get(position)));
        }

        return vi;
    }

    private int getIconByResult(ResultElement elem) {
        if (elem.getDepType() == DependencyType.CITIES) {
            return R.mipmap.ic_city;
        } else if (elem.getDepType() == DependencyType.AIRPORTS) {
            return R.mipmap.ic_airport;
        } else if (elem.getDepType() == DependencyType.COUNTRIES) {
            return R.mipmap.ic_place;
        } else if (elem.getDepType() == DependencyType.FLIGHT_SEARCH) {
            return R.drawable.ic_flight_takeoff;
        } else{
            return -1;
        }
    }
}
