package ar.edu.itba.dreamtrip.common.model;

import android.location.Location;

import ar.edu.itba.dreamtrip.common.interfaces.Identificable;

/**
 * Created by Julian Benitez on 11/12/2016.
 */

public class Airport implements Identificable{
    private String ID;
    private String name;

    private City city;
    private Location location;

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public City getCity() {
        return city;
    }

    public Location getLocation() {
        return location;
    }

    public Airport(String ID, String name, City city, Double latitude, Double longitude) {
        this.ID = ID;
        this.name = name;
        this.city = city;
        location = new Location("Airport" + ID);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
    }
}
