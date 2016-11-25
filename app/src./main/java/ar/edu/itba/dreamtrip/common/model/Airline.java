package ar.edu.itba.dreamtrip.common.model;

import android.graphics.Bitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import ar.edu.itba.dreamtrip.common.interfaces.Identificable;

/**
 * Created by Julian Benitez on 11/13/2016.
 */

public class Airline implements Identificable{
    private String id;
    private String name;
    private String logoUrl;
    private Bitmap logo;
    private String wikiData;

    public String getWikiData() {
        return wikiData;
    }

    public void setWikiData(String wikiData) {
        this.wikiData = wikiData;
    }

    public Bitmap getLogo() {
        return logo;
    }

    public void setLogo(Bitmap logo) {
        this.logo = logo;
    }

    public Airline(String id, String name, String logoUrl) {
        this.id = id;
        this.name = name;
        this.logoUrl = logoUrl;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public String getName() {
        return name;
    }

    public String getID() {
        return id;
    }

    @Override
    public String toString(){
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Airline airline = (Airline) o;

        if (id != null ? !id.equals(airline.id) : airline.id != null) return false;
        if (name != null ? !name.equals(airline.name) : airline.name != null) return false;
        return logoUrl != null ? logoUrl.equals(airline.logoUrl) : airline.logoUrl == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
