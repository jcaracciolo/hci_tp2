package ar.edu.itba.dreamtrip.common.API;

import android.graphics.Bitmap;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;

import ar.edu.itba.dreamtrip.common.API.dependencies.AirlinesReviewDependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;
import ar.edu.itba.dreamtrip.common.API.dependencies.FlightDealsDependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.FlightDependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.StatusDependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.StatusSearchDependency;
import ar.edu.itba.dreamtrip.common.model.Airline;
import ar.edu.itba.dreamtrip.common.model.Airport;
import ar.edu.itba.dreamtrip.common.model.City;
import ar.edu.itba.dreamtrip.common.model.Country;
import ar.edu.itba.dreamtrip.common.model.Deal;
import ar.edu.itba.dreamtrip.common.model.Flight;
import ar.edu.itba.dreamtrip.common.model.FlightState;
import ar.edu.itba.dreamtrip.common.model.MyCurrency;
import ar.edu.itba.dreamtrip.common.model.Review;

/**
 * Created by Julian Benitez on 11/22/2016.
 */

public abstract class DependencyLoader {

    private static Integer loadedAirlinesLogos ;
    private static Integer totalAirlinesLogos ;
    private static Integer totalDealImages ;
    private static Integer loadedDealImages ;
    private static Integer totalDealImagesURLS ;
    private static Integer loadedDealImagesURLS ;
    static boolean loadFlightState(final DataHolder dataHolder,
                                   final StatusDependency dependency,
                                   final HashMap<String,FlightState> flightStates){
        String url ="http://hci.it.itba.edu.ar/v1/api/status.groovy?method=getflightstatus"
                + "&airline_id=" + dependency.getAirlineID()+
                "&flight_number=" + dependency.getFlightNumber();
        // Request a string response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONParser.fillFlightState(response,flightStates);
                        dataHolder.somethingLoaded(dependency);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dataHolder.handleNetworkError(error,dependency);
                    }
                });
        // Add the request to the RequestQueue.
        dataHolder.addToVolleyQueue(jsObjRequest);
        return true;
    }

    static boolean loadStatusSearch(final DataHolder dataHolder,
                                   final StatusSearchDependency dependency){
        String url ="http://hci.it.itba.edu.ar/v1/api/status.groovy?method=getflightstatus"
                + "&airline_id=" + dependency.getStatusSearch().getAirlineID() +
                "&flight_number=" + dependency.getStatusSearch().getFlightNumber();
        // Request a string response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(! JSONParser.parseStatusSearch(response,dependency.getStatusSearch())){
                            dependency.getStatusSearch().flightNotFound();
                        }
                        dataHolder.somethingLoaded(dependency);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dataHolder.handleNetworkError(error,dependency);
                    }
                });
        // Add the request to the RequestQueue.
        dataHolder.addToVolleyQueue(jsObjRequest);
        return true;
    }

    static boolean loadAirlineLogos(final DataHolder dataHolder){
        totalAirlinesLogos =  dataHolder.getAirlines().size();
        loadedAirlinesLogos = 0;
        final Dependency dependency = new Dependency(DependencyType.AIRLINES_LOGOS);

        for(final Airline airline: dataHolder.getAirlines()){
            ImageRequest request = new ImageRequest(airline.getLogoUrl(),
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            airline.setLogo(bitmap);
                            loadedAirlinesLogos++;
                            if(loadedAirlinesLogos == totalAirlinesLogos) {
                                dataHolder.somethingLoaded(dependency);
                            }
                        }
                    }, 0, 0, null,
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            loadedAirlinesLogos++;
                            if(loadedAirlinesLogos == totalAirlinesLogos) {
                                dataHolder.somethingLoaded(dependency);
                            }
                        }
                    });
            dataHolder.addToVolleyQueue(request);
        }

        return true;
    }

    static void loadDealsImages(final DataHolder dataHolder, HashSet<Deal> deals, final Dependency dependency){
        totalDealImages =  deals.size();
        loadedDealImages = 0;

        for(final Deal deal: deals){
            String url = deal.getImageUrl();

            if(url!=null){
                ImageRequest request = new ImageRequest(url,
                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap bitmap) {
                                deal.setImage(bitmap);
                                loadedDealImages++;
                                if(loadedDealImages == totalDealImages) {
                                    dataHolder.somethingLoaded(dependency);
                                }
                            }
                        }, 0, 0, null,
                        new Response.ErrorListener() {
                            public void onErrorResponse(VolleyError error) {
                                loadedDealImages++;
                                if(loadedDealImages == totalDealImages) {
                                    dataHolder.somethingLoaded(dependency);
                                }
                            }
                        });
                dataHolder.addToVolleyQueue(request);
            }
        }
    }
    static void loadDealsImagesURLS(final DataHolder dataHolder, HashSet<Deal> deals, final Dependency dependency){
        totalDealImagesURLS =  deals.size();
        loadedDealImagesURLS = 0;

        for(final Deal deal: deals){
            String url =  "https://api.flickr.com/services/rest/?method=flickr.photos.search" +
                    "&api_key=" + "9d6b40067159b728b85feeaac2bf7f6f" +
                    "&tags=landscape" +
                    "&text=" + deal.getDestinationDescription().replace(' ','+') +
                    "&sort=interestingness-desc&format=json&nojsoncallback=1";

            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String imageUrl = JSONParser.parseFlickerImageUrl(response);
                            deal.setImageUrl(imageUrl);
                            loadedDealImagesURLS++;
                            if(loadedDealImagesURLS == totalDealImagesURLS) {
                                dataHolder.somethingLoaded(dependency);
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loadedDealImagesURLS++;
                            if(loadedDealImagesURLS == totalDealImagesURLS) {
                                dataHolder.somethingLoaded(dependency);
                            }
                        }
                    });
            dataHolder.addToVolleyQueue(jsObjRequest);
        }
    }

    static boolean loadAirlineData(final DataHolder dataHolder,
                                final HashMap<String, Airline> airlines){
        final Dependency dependency = new Dependency(DependencyType.AIRLINES_DATA);
        String url ="http://hci.it.itba.edu.ar/v1/api/misc.groovy?method=getairlines";
        // Request a string response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        airlines.clear();
                        JSONParser.fillAirlinesHashmap(response,airlines);
                        dataHolder.somethingLoaded(dependency);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dataHolder.handleNetworkError(error,dependency);
                    }
                });
        // Add the request to the RequestQueue.
        dataHolder.addToVolleyQueue(jsObjRequest);
        return true;
    }
    static void loadAirlineReviews(final DataHolder dataHolder,
                                   final AirlinesReviewDependency dependency,
                                   final HashMap<String, HashSet<Review>> reviews){
        String url ="http://hci.it.itba.edu.ar/v1/api/review.groovy?method=getairlinereviews"
                + "&airline_id="+dependency.getAirlineID();
        // Request a string response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONParser.fillReviewsHashmap(response,reviews);
                        dataHolder.somethingLoaded(dependency);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dataHolder.handleNetworkError(error,dependency);
                    }
                });
        // Add the request to the RequestQueue.
        dataHolder.addToVolleyQueue(jsObjRequest);
    }


    static boolean loadFlight(final DataHolder dataHolder,
                              final HashMap<String, Airline> airlines,
                              final HashMap<String, Airport> airports,
                              final HashMap<Integer, Flight> flights,
                              final FlightDependency dependency){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        if(dependency.isValid() != null && dependency.isValid() != true) {
            dataHolder.somethingLoaded(dependency);
            return false;
        }
        String test = format.format(dependency.getDepartureDate());

        String url ="http://hci.it.itba.edu.ar/v1/api/booking.groovy?method=getonewayflights"+
                "&from="+ dependency.getOriginID() +
                "&to=" + dependency.getDestinationID() +
                "&dep_date=" + format.format(dependency.getDepartureDate()) +
                "&adults=1&children=0&infants=0";
        System.out.println("Searching " + url);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONParser.fillFlightsHashmap(response,flights, airlines, airports);
                        dataHolder.somethingLoaded(dependency);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dataHolder.handleNetworkError(error,dependency);
                    }
                });
        dataHolder.addToVolleyQueue(jsObjRequest);
        return true;
    }


    static boolean loadAirports(final DataHolder dataHolder, final HashMap<String, Country> countries,
                                final HashMap<String, City> cities,
                                 final HashMap<String, Airport> airports){
        String url ="http://hci.it.itba.edu.ar/v1/api/geo.groovy?method=getairports&page_size=1000";
        final Dependency dependency = new Dependency(DependencyType.AIRPORTS);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        airports.clear();
                        JSONParser.fillAirportsHashmap(response,airports,cities);
                        dataHolder.somethingLoaded(dependency);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dataHolder.handleNetworkError(error,dependency);
                    }
                });
        dataHolder.addToVolleyQueue(jsObjRequest);
        return true;
    }


    static boolean loadCities(final DataHolder dataHolder, final HashMap<String, Country> countries,
                              final HashMap<String, City> cities){
        String url ="http://hci.it.itba.edu.ar/v1/api/geo.groovy?method=getcities&page_size=1000";
        final Dependency dependency = new Dependency(DependencyType.CITIES);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        cities.clear();
                        JSONParser.fillCitiesHashmap(response,cities,countries);
                        dataHolder.somethingLoaded(dependency);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dataHolder.handleNetworkError(error,dependency);
                    }
                });
        dataHolder.addToVolleyQueue(jsObjRequest);
        return true;
    }

    static boolean loadCountries(final DataHolder dataHolder, final HashMap<String, Country> countries){
        final Dependency dependency = new Dependency(DependencyType.COUNTRIES);
        String url ="http://hci.it.itba.edu.ar/v1/api/geo.groovy?method=getcountries";
        // Request a string response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        countries.clear();
                        JSONParser.fillCountriesHashmap(response,countries);
                        dataHolder.somethingLoaded(dependency);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dataHolder.handleNetworkError(error,dependency);
                    }
                });
        // Add the request to the RequestQueue.
        dataHolder.addToVolleyQueue(jsObjRequest);
        return true;
    }

    static boolean loadCurrencies(final DataHolder dataHolder, final HashMap<String,MyCurrency> currencies){
        String url ="http://hci.it.itba.edu.ar/v1/api/misc.groovy?method=getcurrencies&page_size=1000";
        final Dependency dependency = new Dependency(DependencyType.CURRENCIES);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONParser.fillCurrenciesHashmap(response,currencies);
                        dataHolder.somethingLoaded(dependency);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dataHolder.handleNetworkError(error,dependency);
                    }
                });
        dataHolder.addToVolleyQueue(jsObjRequest);
        return true;
    }

    static boolean loadFlightDealsData(final DataHolder dataHolder,
                                       final FlightDealsDependency dependency,
                                       final HashSet<Deal> deals,final boolean lastMinute){
        String url ="http://hci.it.itba.edu.ar/v1/api/booking.groovy?method=get"
            + (lastMinute? "lastminute":"") + "flightdeals" +
                "&from=" + dependency.getOriginID();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        deals.clear();

                        JSONParser.fillDeals(response,deals,dependency.getOriginID());
                        dataHolder.somethingLoaded(dependency);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dataHolder.handleNetworkError(error,dependency);
                    }
                });
        dataHolder.addToVolleyQueue(jsObjRequest);
        return true;
    }


}
