package ar.edu.itba.dreamtrip.Deals;

import android.content.Context;
import android.graphics.Bitmap;

import java.net.URL;
import java.text.DecimalFormat;

import ar.edu.itba.dreamtrip.common.model.Deal;
import ar.edu.itba.dreamtrip.common.model.MyCurrency;

/**
 * Created by juanfra on 23/11/16.
 */

public class DealViewModel {

    public String to;
    public String idTo;
    public String image;
    public Double price;
    public String fromID;
    public Double ratio;
    public String formatedPrice;
    public String currencySimbol;

    public DealViewModel(Context context, Deal deal, MyCurrency currency){
        to=deal.getDestinationDescription();
        idTo=deal.getDestinationCityID();
        price=deal.getPrice();
        fromID=deal.getOriginCityID();
        image =deal.getImageUrl();
        if(currency != null) {
            currencySimbol = currency.getSymbol();
            ratio = currency.getRatio();
        } else {
            currencySimbol = "U$S";
            ratio = 1.0;
        }
    }


    public String getFormatedPrice(){
        return currencySimbol + new DecimalFormat("#.##").format(price/ratio);
    }
}
