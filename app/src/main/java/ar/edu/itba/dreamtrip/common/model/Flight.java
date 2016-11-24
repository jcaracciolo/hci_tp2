package ar.edu.itba.dreamtrip.common.model;

import android.icu.util.DateInterval;

import java.util.Date;

import ar.edu.itba.dreamtrip.common.interfaces.Identificable;

/**
 * Created by Julian Benitez on 11/13/2016.
 */

public class Flight implements Identificable{


    private Airport origin;
    private Airport destination;
    private PriceData priceData;

    private Date arrivalDate;
    private Date departureDate;

    private Integer id;
    private Integer number;
    private CabinType cabinType;

    private Airline airline;
    private String duration;

    public Flight(Airport origin, Airport destination, PriceData priceData,
                  Date arrivalDate, Date departureDate, Integer id, Integer number,
                  CabinType cabinType, Airline airline, String duration) {
        this.origin = origin;
        this.destination = destination;
        this.priceData = priceData;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.id = id;
        this.number = number;
        this.cabinType = cabinType;
        this.airline = airline;
        this.duration = duration;
    }

    public Airport getOrigin() {
        return origin;
    }

    public Airport getDestination() {
        return destination;
    }

    public PriceData getPriceData() {
        return priceData;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public String getID() {
        return getFlightIDCode().toString();
    }

    public Integer getFlightIDCode() {
        return id;
    }

    @Override
    public String getName() {
        return getAirline().getName() + " " + getNumber();
    }

    public Integer getNumber() {
        return number;
    }

    public CabinType getCabinType() {
        return cabinType;
    }

    public Airline getAirline() {
        return airline;
    }

    public String getDuration() {
        return duration;
    }
}
