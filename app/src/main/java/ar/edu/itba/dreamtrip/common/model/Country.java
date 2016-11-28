package ar.edu.itba.dreamtrip.common.model;

import android.location.Location;

import ar.edu.itba.dreamtrip.common.interfaces.Identificable;

/**
 * Created by Julian Benitez on 11/12/2016.
 */

public class Country implements Identificable{
    private String ID;
    private String name;

    public Location location;

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public Country(String id, String name, Double latitude, Double longitude) {
        ID = id;
        this.name = name;
        location = new Location("countries");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
    }

}
