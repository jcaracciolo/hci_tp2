package ar.edu.itba.dreamtrip.common.model;

import java.util.Calendar;
import java.util.Date;

import ar.edu.itba.dreamtrip.common.API.JSONParser;

/**
 * Created by Julian Benitez on 11/22/2016.
 */

public class StatusSearch {
    String airlineID;
    Integer flightNumber;
    Date departure;

    String originAirportID;
    String destinationAirportID;

    Boolean valid;

    @Override
    public String toString(){
        return airlineID + " " + flightNumber + " " + valid;
    }

    public StatusSearch(String airlineID, Integer flightNumber) {
        this.airlineID = airlineID;
        this.flightNumber = flightNumber;
        valid = null;
    }


    public void loadData(Date departure, String originAirportID, String destinationAirportID){
        this.departure = departure;
        this.originAirportID = originAirportID;
        this.destinationAirportID = destinationAirportID;
        valid = true;
    }

    public void flightNotFound(){
        valid = false;
    }

    public Boolean getValid() {
        return valid;
    }

    public String getAirlineID() {
        return airlineID;
    }

    public Integer getFlightNumber() {
        return flightNumber;
    }

    public Date getDeparture() {
        return departure;
    }

    public String getOriginAirportID() {
        return originAirportID;
    }

    public String getDestinationAirportID() {
        return destinationAirportID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatusSearch that = (StatusSearch) o;

        if (!airlineID.equals(that.airlineID)) return false;
        return flightNumber.equals(that.flightNumber);

    }

    @Override
    public int hashCode() {
        int result = airlineID.hashCode();
        result = 31 * result + flightNumber.hashCode();
        return result;
    }
}
