package ar.edu.itba.dreamtrip.common.model;

import android.location.Location;

import ar.edu.itba.dreamtrip.common.interfaces.Identificable;

/**
 * Created by Julian Benitez on 11/12/2016.
 */

public class City implements Identificable{
    private String ID;
    private String name;

    private Country country;

    public Location getLocation() {
        return location;
    }

    public Country getCountry() {
        return country;
    }

    public String getName() {
        return name;
    }

    public String getID() {
        return ID;
    }

    private Location location;

    public City(String id, String name, Country country, Double latitude, Double longitude) {
        ID = id;
        this.name = name;
        this.country = country;
        location = new Location("City" + ID);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
    }

    public boolean containsID(String id) {
        if(ID.equals(id)) return true;
        else return false;
    }
}
