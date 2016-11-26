package ar.edu.itba.dreamtrip.Deals;

import android.content.Context;
import android.graphics.Bitmap;

import java.net.URL;

import ar.edu.itba.dreamtrip.common.model.Deal;

/**
 * Created by juanfra on 23/11/16.
 */

public class DealViewModel {

    public String to;
    public String idTo;
    public String image;
    public Double price;
    public String fromID;

    public DealViewModel(Context context, Deal deal){
        to=deal.getDestinationDescription();
        idTo=deal.getDestinationCityID();
        price=deal.getPrice();
        fromID=deal.getOriginCityID();
        image =deal.getImageUrl();
    }
}
