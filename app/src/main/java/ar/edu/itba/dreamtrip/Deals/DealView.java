package ar.edu.itba.dreamtrip.Deals;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import ar.edu.itba.dreamtrip.R;

/**
 * Created by juanfra on 23/11/16.
 */

public class DealView {

    public static View getView(final Context context, LayoutInflater inflater, ViewGroup container, final DealViewModel deal){

        View dealView=inflater.inflate(R.layout.fragment_deal, container, false);
        ((TextView)dealView.findViewById(R.id.deal_to)).setText(deal.to);
        ((TextView)dealView.findViewById(R.id.deal_price)).setText(deal.price.toString());
        ImageButton destButton=((ImageButton)dealView.findViewById(R.id.deal_img));
        destButton.setBackground(new BitmapDrawable(deal.image));
        destButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,deal.to,Toast.LENGTH_SHORT);
            }
        });

        return dealView;
    }
}
