package ar.edu.itba.dreamtrip.TrackedDestinations;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.common.API.SettingsManager;
import ar.edu.itba.dreamtrip.common.model.Deal;
import ar.edu.itba.dreamtrip.common.model.FlightState;
import ar.edu.itba.dreamtrip.common.model.FlightStatus;
import ar.edu.itba.dreamtrip.common.model.MyCurrency;

/**
 * Created by juanfra on 22/11/16.
 */



public class TrackedLegViewModel implements Serializable{

    public String image;

    public String originID;
    public String originDescription;
    public String destinationID;
    public String destinationDescription;
    public boolean isLastMinute;
    public Double price;
    public boolean selected;
    public String dealID;
    public String currencySimbol;

    public TrackedLegViewModel(Context context, Deal deal, MyCurrency currency) {
        originID=deal.getOriginCityID();
        originDescription=deal.getOriginCityID();
        destinationID=deal.getDestinationCityID();
        destinationDescription=deal.getDestinationDescription();
        isLastMinute=deal.isLastMinute();
        image=deal.getImageUrl();
        price=deal.getPrice();
        dealID=deal.getDealIdentifier();
        if(currency != null) {
            currencySimbol = currency.getSymbol();
            price = price / currency.getRatio();
        } else currencySimbol = "$";
    }

    public String getFormatedPrice(){
        return currencySimbol + new DecimalFormat("#").format(price);
    }
}
