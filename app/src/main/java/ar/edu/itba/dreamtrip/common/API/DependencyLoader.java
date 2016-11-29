package ar.edu.itba.dreamtrip.common.API;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.DeadObjectException;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.net.URI;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

import ar.edu.itba.dreamtrip.common.API.dependencies.AirlinesReviewDependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;
import ar.edu.itba.dreamtrip.common.API.dependencies.FlightDealsDependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.FlightDependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.SendReviewDependency;
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

    private static HashMap<Dependency, Integer> dependencyLoaded = new HashMap<>();
    private static HashMap<Dependency, Integer> dependencyMax = new HashMap<>();

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

    static boolean loadAirlineLogos(final DataHolder dataHolder,
                                       final Dependency dependency) {
        setMaxDependencyRequests(dependency,dataHolder.getAirlines().size());

        for(final Airline airline: dataHolder.getAirlines()){
            ImageRequest request = new ImageRequest(airline.getLogoUrl(),
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            airline.setLogo(bitmap);
                            somethingArrived(dataHolder,dependency);
                        }
                    }, 0, 0, null,
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            somethingArrived(dataHolder,dependency);
                        }
                    });
            dataHolder.addToVolleyQueue(request);
        }

        return true;
    }

    static boolean loadAirlineWikiData(final DataHolder dataHolder, final Dependency dependency){
        setMaxDependencyRequests(dependency,dataHolder.getAirlines().size());

        for(final Airline airline: dataHolder.getAirlines()){
            String s = Locale.getDefault().getDisplayLanguage();
            String search = airlineToWiki(airline.getID(),s);
            String url ="https://"+s.substring(0,2)+".wikipedia.org/w/api.php?action=query&format=json&smaxage=0&prop=extracts"
                    +"&titles="+search +"&exsentences=4&exintro=1&explaintext=1&exsectionformat=wiki";
            // Request a string response from the provided URL.
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONParser.fillAirlinesWikiData(response,airline);
                            somethingArrived(dataHolder,dependency);
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            somethingArrived(dataHolder,dependency);

                        }
                    });
            // Add the request to the RequestQueue.
            dataHolder.addToVolleyQueue(jsObjRequest);
        }

        return true;
    }

    private static String airlineToWiki(String id, String s) {

        String res = "";
        if(s.equals("español")){
            switch (id){
                case "IB":
                    return "Iberia_(aerolínea)";
                case "JJ":
                    return "TAM_Líneas_Aéreas";
                case "LA":
                    return "LAN_Airlines";
                case "AV":
                    return "Avianca_El_Salvador";
                case "TA":
                    return "Avianca_El_Salvador";
            }
        }
        switch (id){
            case "BA":
                return "British+Airways";
            case "AF":
                return "Air+France";
            case "JJ":
                return "LATAM+Brasil";
            case "AR":
                return "Aerolíneas+Argentinas";
            case "IB":
                return "Iberia+(airline)";
            case "LA":
                return "LATAM+Chile";
            case "AZ":
                return "Alitalia";
            case "AM":
                return "Aeroméxico";
            case "8R":
                return "Sol_Líneas_Aéreas";
            case "AA":
                return "American+Airlines";
            case "CM":
                return "Copa_Airlines";
            case "AV":
                return "Avianca";
            case "TA":
                return "TACA_Airlines";
        }
        return res;
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
    static void sendFlightReviews(final DataHolder dataHolder,
                                   final SendReviewDependency dependency){
        String url ="http://hci.it.itba.edu.ar/v1/api/review.groovy?method=reviewairline";
        Review review = dependency.getReview();
        String url2 ="http://hci.it.itba.edu.ar/v1/api/review.groovy?method=reviewairline2&review=" +
                "%7b%22flight%22:%7b%22airline%22:%7b%22id%22:%22"+review.getAirlineID()+
                "%22%7d,%22number%22:"+review.getFlightNumber() +
                "%7d,%22rating%22:%7b%22friendliness%22:"+review.getFriendliness()+
                ",%22food%22:"+review.getFood()+
                ",%22punctuality%22:"+review.getPunctuality()+
                ",%22mileage_program%22:"+review.getMilageProgram()+
                ",%22comfort%22:"+review.getConfort()+
                ",%22quality_price%22:"+review.getQualityPriceRatio()+
                "%7d,%22yes_recommend%22:"+ (review.getRecommended()? "true":"false") +
                ",%22comments%22:%22"+ Uri.encode(review.getComment()) +"%22%7d";

        // Request a string response from the provided URL.
//        final JSONObject requestParams =  JSONParser.unparseReview(dependency.getReview());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dataHolder.setLastReviewValid(JSONParser.parseReviewResponse(response));
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
                                       final HashMap<String ,Deal> deals,final boolean lastMinute){
        final String url ="http://hci.it.itba.edu.ar/v1/api/booking.groovy?method=get"
            + (lastMinute? "lastminute":"") + "flightdeals" +
                "&from=" + dependency.getOriginID() +
                "&page_size=1000";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        deals.clear();
                        JSONParser.fillDeals(response,deals,dependency.getOriginID(),lastMinute);
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


    static void loadDealsImagesURLS(final DataHolder dataHolder, HashMap<String ,Deal> deals, final Dependency dependency){
        setMaxDependencyRequests(dependency,deals.size());

        for(final Deal deal: deals.values()){
            if(deal.getImageUrl() == null) {

                String url = "https://api.flickr.com/services/rest/?method=flickr.photos.search" +
                        "&api_key=" + "9d6b40067159b728b85feeaac2bf7f6f" +
                        "&tags=landscape" +
                        "&text=" + deal.getDestinationDescription().replace(' ', '+') +
                        "&sort=interestingness-desc&format=json&nojsoncallback=1";

                JsonObjectRequest jsObjRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                String imageUrl = JSONParser.parseFlickerImageUrl(response);
                                deal.setImageUrl(imageUrl);
                                somethingArrived(dataHolder,dependency);
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                somethingArrived(dataHolder,dependency);
                            }
                        });
                dataHolder.addToVolleyQueue(jsObjRequest);
            } else somethingArrived(dataHolder,dependency);

        }
    }

    static void loadDealsImages(final DataHolder dataHolder,HashMap<String ,Deal> deals, final Dependency dependency){
        setMaxDependencyRequests(dependency,deals.size());


        for(final Deal deal: deals.values()){
            if(deal.getImage() == null && deal.getImageUrl() != null) {
                final String url = deal.getImageUrl();
                ImageRequest request = new ImageRequest(url,
                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap bitmap) {
                                deal.setImage(bitmap);
                                somethingArrived(dataHolder,dependency);
                                System.out.println("loaded image: " + deal + " " + url + " loaded " + dependencyLoaded.get(dependency));
                            }
                        }, 0, 0, null,
                        new Response.ErrorListener() {
                            public void onErrorResponse(VolleyError error) {
                                somethingArrived(dataHolder,dependency);
                            }
                        });
                dataHolder.addToVolleyQueue(request);
                System.out.println("requested image: " + deal + " " + url + " loaded " +  dependencyLoaded.get(dependency));
            } else somethingArrived(dataHolder,dependency);
        }
    }

    private static void somethingArrived(DataHolder dataHolder, Dependency dependency){
        Integer loaded = dependencyLoaded.get(dependency);
        loaded ++;
        if(loaded >= dependencyMax.get(dependency)){
            dataHolder.somethingLoaded(dependency);
        } else {
            dependencyLoaded.put(dependency,loaded);
        }
    }
    private static void setMaxDependencyRequests( Dependency dependency, Integer max){
        dependencyMax.put(dependency,max);
        dependencyLoaded.put(dependency,0);
    }


}
