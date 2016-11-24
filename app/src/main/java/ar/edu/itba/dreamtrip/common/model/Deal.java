package ar.edu.itba.dreamtrip.common.model;

import android.graphics.Bitmap;

/**
 * Created by Julian Benitez on 11/24/2016.
 */

public class Deal {
    public String getOriginCityID() {
        return originCityID;
    }

    public Double getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Bitmap getImage() {
        return image;

    }

    public String getDestinationDescription() {
        return destinationDescription;
    }

    private String destinationCityID;
    private String originCityID;
    private Double price;
    private Bitmap image;
    private String imageUrl;
    private String destinationDescription;

    public Deal(String destinationCityID, String originCityID, String destinationDescription, Double price) {
        this.destinationCityID = destinationCityID;
        this.originCityID = originCityID;
        this.price = price;
        this.destinationDescription = destinationDescription;
    }

    public void setImage(Bitmap image){
        this.image = image;
    }

    public void setImageUrl(String url){
        this.imageUrl = url;
    }

    public String getDestinationCityID() {
        return destinationCityID;
    }
}
