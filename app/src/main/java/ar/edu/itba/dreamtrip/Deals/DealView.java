package ar.edu.itba.dreamtrip.Deals;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.cityInfo.CityInfo;

/**
 * Created by juanfra on 23/11/16.
 */

public class DealView {
    public final static String RESULT_ID_KEY = "main_activity.go_to_info_id";

    public static View getView(final Context context, LayoutInflater inflater, ViewGroup container, final DealViewModel deal){

        View dealView=inflater.inflate(R.layout.fragment_deal, container, false);
        ((TextView)dealView.findViewById(R.id.deal_to)).setText(deal.to);
        ((TextView)dealView.findViewById(R.id.deal_price)).setText(deal.price.toString());
        ImageButton destButton=((ImageButton)dealView.findViewById(R.id.deal_img));
        destButton.setBackground(new BitmapDrawable(deal.image));
        destButton.setTag(deal.idTo);
        destButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id=(String)view.getTag();
                Intent intent = new Intent(context, CityInfo.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(RESULT_ID_KEY,id);
                context.startActivity(intent);
            }
        });

        dealView.findViewById(R.id.deals_follow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"FOLLOW " + deal.to,Toast.LENGTH_SHORT).show();
            }
        });

        return dealView;
    }
}
