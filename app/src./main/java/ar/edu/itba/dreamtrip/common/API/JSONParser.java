package ar.edu.itba.dreamtrip.common.API;

import android.icu.util.DateInterval;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import ar.edu.itba.dreamtrip.common.model.Airline;
import ar.edu.itba.dreamtrip.common.model.Airport;
import ar.edu.itba.dreamtrip.common.model.CabinType;
import ar.edu.itba.dreamtrip.common.model.City;
import ar.edu.itba.dreamtrip.common.model.Country;
import ar.edu.itba.dreamtrip.common.model.Deal;
import ar.edu.itba.dreamtrip.common.model.Flight;
import ar.edu.itba.dreamtrip.common.model.FlightState;
import ar.edu.itba.dreamtrip.common.model.FlightStateAtLocation;
import ar.edu.itba.dreamtrip.common.model.FlightStatus;
import ar.edu.itba.dreamtrip.common.model.MyCurrency;
import ar.edu.itba.dreamtrip.common.model.PriceData;
import ar.edu.itba.dreamtrip.common.model.Review;
import ar.edu.itba.dreamtrip.common.model.StatusSearch;

/**
 * Created by Julian Benitez on 11/14/2016.
 */

public class JSONParser {

    public static FlightState fillFlightState(JSONObject object, HashMap<String, FlightState> flightStates) {
        FlightState flightState = null;
        try {
            JSONObject statusObj = object.getJSONObject("status");
            FlightStateAtLocation origin= parseFlightStateAtLocation(statusObj.getJSONObject("departure"));
            FlightStateAtLocation destination= parseFlightStateAtLocation(statusObj.getJSONObject("arrival"));
            FlightStatus status = parseFlightStatus(statusObj);

            StringBuilder identifier = new StringBuilder(statusObj.getJSONObject("airline").getString("id"));
            identifier.append(" ");
            identifier.append(statusObj.getString("number"));

            flightState = new FlightState(identifier.toString(),origin,destination,status);

            flightStates.put(flightState.getIdentifier(),flightState);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return flightState;
    }


    public static FlightStateAtLocation parseFlightStateAtLocation(JSONObject object) {
        FlightStateAtLocation flightStateAtLocation = null;
        try {
            JSONObject airportObj = object.getJSONObject("airport");

            String airportID = safeString(airportObj,"id");
            String terminal = safeString(airportObj,"terminal");
            Integer gate = safeInt(airportObj,"gate");
            Integer baggage = safeInt(airportObj,"baggage");

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date scheduledTime = parseDate(format,object,"scheduled_time");
            Date actualTime = parseDate(format,object,"actual_time");
            Date scheduledGateTime = parseDate(format,object,"scheduled_gate_time");
            Date actualGateTime = parseDate(format,object,"actual_gate_time");
            Integer gateDelay = safeInt(object,"gate_delay");
            Date estimatedRunwayTime = parseDate(format,object,"estimate_runway_time");
            Date actualRunwayTime = parseDate(format,object,"actual_runway_time");
            Integer runwayDelay = safeInt(object,"runway_delay");

            flightStateAtLocation = new FlightStateAtLocation(airportID,terminal,gate,actualTime,
                    scheduledGateTime,actualGateTime,estimatedRunwayTime,actualRunwayTime,
                    baggage,runwayDelay,gateDelay, scheduledTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return flightStateAtLocation;
    }

    public static boolean parseStatusSearch(JSONObject response, StatusSearch statusSearch) {
        try {
            JSONObject statusobj = response.getJSONObject("status");
//            S airline = statusobj.getJSONObject("airline").getString("id"));
            String originID = statusobj.getJSONObject("departure").getJSONObject("airport").getString("id");
            String destID = statusobj.getJSONObject("arrival").getJSONObject("airport").getString("id");

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date depDate = format.parse(statusobj.getJSONObject("departure").getString("scheduled_time"));

            statusSearch.loadData(depDate,originID,destID);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean fillAirlinesHashmap(JSONObject response, HashMap<String, Airline> airlines) {
        try {
            JSONArray airlinesResp = response.getJSONArray("airlines");
            int max = airlinesResp.length();
            for (int i = 0; i < max; i++) {
                JSONObject airlineObj = airlinesResp.getJSONObject(i);
                Airline airline = new Airline(airlineObj.getString("id"),
                        airlineObj.getString("name"),airlineObj.getString("logo"));
                airlines.put(airline.getID(),airline);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean fillCountriesHashmap(JSONObject response, HashMap<String, Country> countries) {
        try {
            JSONArray countriesResp = response.getJSONArray("countries");
            int max = countriesResp.length();
            for (int i = 0; i < max; i++) {
                JSONObject countryObj = countriesResp.getJSONObject(i);
                Country country = new Country(countryObj.getString("id"),
                        countryObj.getString("name"),countryObj.getDouble("latitude"),
                        countryObj.getDouble("longitude"));
                countries.put(country.getID(),country);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean fillCitiesHashmap(JSONObject response, HashMap<String, City> cities,
                                            HashMap<String, Country> countries) {
        try {
            JSONArray citiesResp = response.getJSONArray("cities");
            int max = citiesResp.length();
            for (int i = 0; i < max; i++) {
                JSONObject cityObj = citiesResp.getJSONObject(i);
                Country country = countries.get(cityObj.getJSONObject("country").getString("id"));
                City city= new City( cityObj.getString("id"),
                        cityObj.getString("name"),country,cityObj.getDouble("latitude"),
                        cityObj.getDouble("longitude"));
                cities.put(city.getID(),city);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean fillAirportsHashmap(JSONObject response, HashMap<String, Airport> airports,
                                           HashMap<String, City> cities) {
        try {
            JSONArray airportsResp = response.getJSONArray("airports");
            int max = airportsResp.length();
            for (int i = 0; i < max; i++) {
                JSONObject airportObj = airportsResp.getJSONObject(i);
                City city = cities.get(airportObj.getJSONObject("city").getString("id"));
                Airport airport= new Airport( airportObj.getString("id"),
                        airportObj.getString("description"),city,airportObj.getDouble("latitude"),
                        airportObj.getDouble("longitude"));
                airports.put(airport.getID(),airport);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean fillCurrenciesHashmap(JSONObject response, HashMap<String, MyCurrency> currencies) {
        try {
            JSONArray currenciesResp = response.getJSONArray("currencies");
            int max = currenciesResp.length();
            for (int i = 0; i < max; i++) {
                JSONObject currenciesObj = currenciesResp.getJSONObject(i);
                MyCurrency currency = new MyCurrency(currenciesObj.getString("id"),
                        currenciesObj.getString("description"),currenciesObj.getString("symbol"),
                        currenciesObj.getDouble("ratio"));
                currencies.put(currency.getID(),currency);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean fillFlightsHashmap(JSONObject response, HashMap<Integer, Flight> flights,
                                             HashMap<String, Airline> airlines,
                                             HashMap<String, Airport> airports) {
        try {
            JSONArray flightsResp = response.getJSONArray("flights");
            int max = flightsResp.length();
            for (int i = 0; i < max; i++) {
                JSONObject flightObj = flightsResp.getJSONObject(i);
                PriceData priceData = parsePriceData(flightObj.getJSONObject("price"));
                JSONObject segments2 = flightObj.getJSONArray("outbound_routes").getJSONObject(0);
                JSONObject segments = segments2.getJSONArray("segments").getJSONObject(0);
                String originID = segments.getJSONObject("departure").getJSONObject("airport").getString("id");
                String destinationID = segments.getJSONObject("arrival").getJSONObject("airport").getString("id");
                Integer flightID = segments.getInt("id");
                Integer flightNumber = segments.getInt("number");
                CabinType cabinType = parseCabinType(segments);
                String airlineID = segments.getJSONObject("airline").getString("id");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date depDate = format.parse(segments.getJSONObject("arrival").getString("date"));
                Date arrDate = format.parse(segments.getJSONObject("departure").getString("date"));
                String duration =  segments.getString("duration");

                Flight flight = new Flight(airports.get(originID),airports.get(destinationID), priceData,
                        arrDate,depDate,flightID,flightNumber,cabinType,airlines.get(airlineID),duration);
                flights.put(flight.getFlightIDCode(),flight);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }
    private static CabinType parseCabinType(JSONObject data) throws JSONException {
        switch (data.getString("cabin_type")){
            case "ECONOMY":
                return CabinType.ECONOMY;
            case "BUSINESS":
                return CabinType.BUSINESS;
            case "FIRST_CLASS":
                return CabinType.FIRST_CLASS;
        }
        throw new JSONException("Illegal cabin type");
    }

    private static PriceData parsePriceData(JSONObject priceDataResp) throws JSONException {
        Integer adults=0,children=0,infants=0;
        Double adultFare = 0d,childFare = 0d,infantFare = 0d;
        if(priceDataResp.getString("adults") != "null"){
            adults = priceDataResp.getJSONObject("adults").getInt("quantity");
            adultFare = priceDataResp.getJSONObject("adults").getDouble("base_fare");
        }
        if(priceDataResp.getString("children") != "null") {
            children = priceDataResp.getJSONObject("children").getInt("quantity");
            childFare = priceDataResp.getJSONObject("children").getDouble("base_fare");
        }
        if(priceDataResp.getString("infants") != "null"){
            infants = priceDataResp.getJSONObject("infants").getInt("quantity");
            infantFare = priceDataResp.getJSONObject("infants").getDouble("base_fare");
        }
        return new PriceData(infants,children,adults,infantFare,adultFare,childFare,
                priceDataResp.getJSONObject("total").getDouble("charges"),
                priceDataResp.getJSONObject("total").getDouble("taxes"));
    }

    private static Date parseDate(SimpleDateFormat format, JSONObject obj, String key){
        Date date = null;
        try {
            if(! obj.getString(key).equals("null")) date = format.parse(obj.getString(key));
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private static FlightStatus parseFlightStatus(JSONObject object){
        FlightStatus flightStatus = null;

        try {
            switch (object.getString("status")){
                case "S":
                    flightStatus = FlightStatus.SCHEDULED;
                    break;
                case "A":
                    flightStatus = FlightStatus.ACTIVE;
                    break;
                case "D":
                case "R":
                    flightStatus = FlightStatus.DELAYED;
                    break;
                case "L":
                    flightStatus = FlightStatus.LANDED;
                    break;
                case "C":
                    flightStatus = FlightStatus.CANCELLED;
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return flightStatus;
    }

    private static String safeString(JSONObject object, String key) {
        String string = null;
        try {
            if(! object.getString(key).equals("null")) string = object.getString(key);
        } catch (JSONException e) {
//            e.printStackTrace();
        }
        return string;
    }

    private static Integer safeInt(JSONObject object, String key) {
        Integer integer= null;
        try {
            if(! object.getString(key).equals("null")) integer = object.getInt(key);
        } catch (JSONException e) {
//            e.printStackTrace();
        }
        return integer;
    }

    public static void fillReviewsHashmap(JSONObject response, HashMap<String, HashSet<Review>> reviews) {
        try {
            JSONArray reviewsResp = response.getJSONArray("reviews");
            int max = reviewsResp.length();
            for (int i = 0; i < max; i++) {
                JSONObject reviewObj = reviewsResp.getJSONObject(i);
                JSONObject ratingsObj =  reviewObj.getJSONObject("rating");
                Review review = new Review(reviewObj.getJSONObject("flight").getJSONObject("airline").getString("id"),
                        reviewObj.getJSONObject("flight").getInt("number"),
                        safeInt(ratingsObj,"overall"),safeInt(ratingsObj,"friendliness"),
                        safeInt(ratingsObj,"food"),safeInt(ratingsObj,"punctuality"),
                        safeInt(ratingsObj,"mileage_program"),safeInt(ratingsObj,"comfort"),
                        safeInt(ratingsObj,"quality_price"),reviewObj.getBoolean("yes_recommend"),
                        safeString(reviewObj,"comments"));
                if(reviews.containsKey(review.getIdentifier())){
                    reviews.get(review.getIdentifier()).add(review);
                } else {
                    HashSet<Review> reviewsToSave = new HashSet<>();
                    reviewsToSave.add(review);
                    reviews.put(review.getIdentifier(),reviewsToSave);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void fillDeals(JSONObject response, HashMap<String,Deal> deals, String originID, boolean lastMinute) {
        try {
            JSONArray dealsResp = response.getJSONArray("deals");
            int max = dealsResp.length();
            for (int i = 0; i < max; i++) {
                JSONObject dealObj = dealsResp.getJSONObject(i);
                Deal deal = new Deal(dealObj.getJSONObject("city").getString("id"),
                        originID,dealObj.getJSONObject("city").getString("name"),
                        dealObj.getDouble("price"));
                deal.setLastMinute(lastMinute);
                if(deals.containsKey(deal.getDealIdentifier())){
                    if(lastMinute){
                        deals.get(deal.getDealIdentifier()).setLastMinute(true);
                    }
                } else deals.put(deal.getDealIdentifier(),deal);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String parseFlickerImageUrl(JSONObject response) {
        try {
            JSONObject urlsResp = response.getJSONObject("photos").getJSONArray("photo").getJSONObject(0);
            StringBuilder url = new StringBuilder();
            url.append("https://farm");
            url.append(urlsResp.getInt("farm"));
            url.append(".staticflickr.com/");
            url.append(urlsResp.getString("server"));
            url.append("/");
            url.append(urlsResp.getString("id"));
            url.append("_");
            url.append(urlsResp.getString("secret"));
            url.append("_m.jpg");
            return url.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void fillAirlinesWikiData(JSONObject response, Airline airline) {
        try {
            String pagesResp = response.getJSONObject("query").getString("pages");
            pagesResp = pagesResp.substring( pagesResp.indexOf("extract"));
            pagesResp = pagesResp.substring(10,pagesResp.length()-3);
            airline.setWikiData(pagesResp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static JSONObject unparseReview(Review review) {
        JSONObject obj = new JSONObject();
        try {JSONObject flight = new JSONObject();
            flight.put("airline",new JSONObject().put("id",review.getAirlineID()));
            flight.put("number",review.getFlightNumber());
            JSONObject rating = new JSONObject();
            rating.put("friendliness",review.getFriendliness());
            rating.put("food",review.getFood());
            rating.put("punctuality",review.getPunctuality());
            rating.put("mileage_program",review.getMilageProgram());
            rating.put("comfort",review.getConfort());
            rating.put("quality_price",review.getQualityPriceRatio());
            obj.put("flight",flight);
            obj.put("rating",rating);
            obj.put("yes_recommend",review.getRecommended());
            if(review.getComment() != null) obj.put("comments",review.getComment());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        String st = obj.toString();
        return obj;
    }

    public static Boolean parseReviewResponse(JSONObject response) {
        try {
            return response.getBoolean("review");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
