package ar.edu.itba.dreamtrip.common.model;

import android.graphics.Bitmap;

import java.util.Objects;

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
    private boolean isLastMinute;

    public Deal(String destinationCityID, String originCityID, String destinationDescription, Double price) {
        this.destinationCityID = destinationCityID;
        this.originCityID = originCityID;
        this.price = price;
        this.destinationDescription = destinationDescription;
        isLastMinute = false;
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

    public boolean isLastMinute() {
        return isLastMinute;
    }

    public void setLastMinute(boolean lastMinute) {
        isLastMinute = lastMinute;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deal deal = (Deal) o;
        return Objects.equals(destinationCityID, deal.destinationCityID) &&
                Objects.equals(originCityID, deal.originCityID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(destinationCityID, originCityID);
    }

    @Override
    public String toString(){
        return (isLastMinute? "lastMin " : "")+originCityID + " to " + destinationCityID;
    }

    public String getDealIdentifier(){
        return originCityID + " " + destinationCityID;
    }
}
