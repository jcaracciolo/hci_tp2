package ar.edu.itba.dreamtrip.TrackedDestinations;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.Serializable;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.common.model.Deal;
import ar.edu.itba.dreamtrip.common.model.FlightState;
import ar.edu.itba.dreamtrip.common.model.FlightStatus;

/**
 * Created by juanfra on 22/11/16.
 */



public class TrackedLegViewModel implements Serializable{

    public Bitmap image;

    public String originID;
    public String originDescription;
    public String destinationID;
    public String destinationDescription;
    public boolean isLastMinute;
    public Double price;
    public boolean selected;
    public String dealID;

    public TrackedLegViewModel(Context context, Deal deal) {
        originID=deal.getOriginCityID();
        originDescription=deal.getOriginCityID();
        destinationID=deal.getDestinationCityID();
        destinationDescription=deal.getDestinationDescription();
        isLastMinute=deal.isLastMinute();
        image=deal.getImage();
        price=deal.getPrice();
        dealID=deal.getDealIdentifier();
    }
}
