package ar.edu.itba.dreamtrip.common.API;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import ar.edu.itba.dreamtrip.common.API.dependencies.AirlinesReviewDependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.FlightDependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.ImageDependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.FlightDealsDependency;
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
import ar.edu.itba.dreamtrip.common.tasks.AsyncTaskInformed;

public class DataHolder {

    private HashMap<String,City> cities = new HashMap<>();
    private HashMap<String,Airport> airports = new HashMap<>();
    private HashMap<String,Country> countries = new HashMap<>();
    private HashMap<String,MyCurrency> currencies = new HashMap<>();
    private HashMap<String,FlightState> flightStates = new HashMap<>();
    private HashMap<String,HashSet<Review>> reviews = new HashMap<>();
    private HashMap<String,Deal> deals = new HashMap<>();


    public Collection<Airline> getAirlines() {
        return airlines.values();
    }
    private VolleyRequestQueue queue ;
    private static DataHolder dataHolder;

    private HashMap<String,Airline> airlines = new HashMap<>();

    private HashMap<Integer,Flight> flights = new HashMap<>();
    private HashMap<Country,Flight> flightsFrom = new HashMap<>();
    private HashMap<Country,Flight> flightsTo = new HashMap<>();

    private HashSet<Dependency> loaded = new HashSet<>();
    private HashSet<Dependency> requested = new HashSet<>();
    private HashSet<Dependency> waiting = new HashSet<>();

    private HashMap<String , Bitmap> images =  new HashMap<>();

    private ConcurrentHashMap<HashSet<Dependency>, ArrayList<AsyncTask>> tasks = new ConcurrentHashMap<>();

    private Context context;

    private DataHolder(Context context) {
        // Instantiate the RequestQueue.
        queue = VolleyRequestQueue.getInstance(context);
        this.context = context;
    }

    public static DataHolder getInstance(Context context){
        if (dataHolder== null) {

            dataHolder= new DataHolder(context.getApplicationContext());
        }
        return dataHolder;
    }

    public boolean waitForIt(AsyncTaskInformed task) {
        HashSet<Dependency> deps = task.getDependencies();
        return waitForIt(task,task.getDependencies());
    }

    public boolean waitForIt(AsyncTask task, HashSet<Dependency> dependencies){
        HashSet<Dependency> deps =  new HashSet<>();
        System.out.println("Waiting for  " + dependencies);

        for (Dependency dep: dependencies){
            deps.addAll(dep.getFlattenedDependencies());
        }

        if(loaded.containsAll(deps)){
            System.out.println("Straight execute");
            task.execute(this);
            return true;
        }
        addDependencies(deps);


        if(tasks.containsKey(dependencies)){
            tasks.get(dependencies).add(task);
        } else {
            ArrayList<AsyncTask> tempTask = new ArrayList<>();
            tempTask.add(task);
            tasks.put(dependencies,tempTask);
        }
        return false;
    }

    private boolean addDependencies(HashSet<Dependency> dependencies){
        for (Dependency dep : dependencies){
            if(!loaded.contains(dep)){
                if( !requested.contains(dep)){
                    if(! waiting.contains(dep)){
                        if(dep.dependenciesLoaded(loaded)) loadDependency(dep);
                        else waiting.add(dep);
                    }
                }
            }
        }

        findDependencies();
        return false;
    }

    private void findDependencies() {
        HashSet<Dependency> finished = new HashSet<>();
        Dependency dep[] = waiting.toArray( new Dependency[waiting.size()] );

        for(int i=0;i<dep.length;i++){
            if(dep[i].dependenciesLoaded(loaded)) {
//                loadDependency(dep[i]);
                finished.add(dep[i]);
                waiting.remove(dep[i]);
//                toRemove.add(dep[i]);
            }
        }
        for (Dependency dependency: finished){
            loadDependency(dependency);
        }
//        waiting.removeAll(toRemove);
    }

    void addToVolleyQueue(Request request){
        queue.addToRequestQueue(request);
    }

    private void loadDependency(Dependency dependency) {
        requested.add(dependency);
        System.out.println("Requesting " + dependency);
        switch (dependency.getDependencyType()){
            case AIRLINES_DATA:
                DependencyLoader.loadAirlineData(this,airlines);
                break;
            case AIRLINES_LOGOS:
                DependencyLoader.loadAirlineLogos(this,dependency);
                break;
            case COUNTRIES:
                DependencyLoader.loadCountries(this,countries);
                break;
            case CITIES:
                DependencyLoader.loadCities(this,countries,cities);
                break;
            case AIRPORTS:
                DependencyLoader.loadAirports(this, countries,cities,airports);
                break;
            case CURRENCIES:
                DependencyLoader.loadCurrencies(this,currencies);
                break;
            case FLIGHTS:
                DependencyLoader.loadFlight(this,airlines,airports,flights,(FlightDependency) dependency);
                break;
            case FLIGHT_SEARCH:
                DependencyLoader.loadStatusSearch(this,(StatusSearchDependency) dependency);
                break;
            case FLIGHT_STATUS:
                DependencyLoader.loadFlightState(this,(StatusDependency) dependency,flightStates);
                break;
            case AIRLINE_REVIEWS:
                DependencyLoader.loadAirlineReviews(this,(AirlinesReviewDependency) dependency,reviews);
                break;
            case LAST_MINUTE_DEALS_DATA:
                DependencyLoader.loadFlightDealsData(this,(FlightDealsDependency) dependency,deals,true);
                break;
            case DEALS_DATA:
                DependencyLoader.loadFlightDealsData(this,(FlightDealsDependency) dependency,deals,false);
                break;
            case LAST_MINUTE_DEALS_IMAGES:
                DependencyLoader.loadDealsImages(this,deals,dependency);
                break;
            case DEALS_IMAGES:
                DependencyLoader.loadDealsImages(this,deals,dependency);
                break;
            case LAST_MINUTE_DEALS_IMAGES_URLS:
                DependencyLoader.loadDealsImagesURLS(this,deals,dependency);
                break;
            case DEALS_IMAGES_URLS:
                DependencyLoader.loadDealsImagesURLS(this,deals,dependency);
                break;
            case AIRLINE_WIKI_INFO:
                DependencyLoader.loadAirlineWikiData(this,dependency);
                break;
            case AIRLINES:
            case TRACKED_FLIGHTS:
            case LAST_MINUTE_DEALS:
            case DEALS:
            case TRACKED_LEGS:

                somethingLoaded(dependency);
                break;
        }
    }

    synchronized void  somethingLoaded(Dependency dependency){
        System.out.println("Loaded asda: " + dependency);
        if(loaded.contains(dependency)) return;
        else {
            loaded.add(dependency);
            requested.remove(dependency);
            ArrayList<ArrayList<AsyncTask>> toRemove = new ArrayList<>();
            for (HashSet<Dependency> dep : tasks.keySet()){
                if (loaded.containsAll(dep)) {
                    for (AsyncTask task : tasks.get(dep)){
                        System.out.println("Executing");
                        task.execute(this);
                    }
                    toRemove.add(tasks.get(dep));
                }
            }

            Iterator<ArrayList<AsyncTask>> it = tasks.values().iterator();

            while (it.hasNext())
            {
                ArrayList<AsyncTask> task = it.next();
                if (toRemove.contains(task)){
                    it.remove();
                }
            }
        }
        findDependencies();
    }

    void handleNetworkError(VolleyError error, Dependency dependency){
        requested.remove(dependency);
        waiting.add(dependency);
        System.out.println("There is not enough internet to call" + dependency);
    }

    public boolean removeDependency(Dependency dependency){
        if(requested.contains(dependency)) return false;
        if(waiting.contains(dependency)) return false;
        if(loaded.contains(dependency)) return loaded.remove(dependency);
        return false;
    }

    private void setImage(ImageDependency imageDependency, Bitmap bitmap){
        images.put(imageDependency.getUrl(),bitmap);
        switch (imageDependency.getImageType()){
            case AIRLINE_LOGO:
                getAirlineByUrl(imageDependency.getUrl()).setLogo(bitmap);
                break;
        }
    }

    private ArrayList<HashSet<Dependency>> getDependenciesWithDependencies(Dependency dependency) {
        ArrayList<Dependency> dependencies = new ArrayList<>();
        dependencies.add(dependency);
        return getDependenciesWithDependencies(dependencies);
    }
    private ArrayList<HashSet<Dependency>> getDependenciesWithDependencies(ArrayList<Dependency> dependencies){
        ArrayList<HashSet<Dependency>> validDependencies = new ArrayList<>();
        for (HashSet<Dependency> taskDependencies: tasks.keySet()){
            if(taskDependencies.containsAll(dependencies)){
                validDependencies.add(taskDependencies);
            }
        }
        return validDependencies;
    }

    private Airline getAirlineByUrl(String url){
        for (Airline airline:airlines.values()){
            if(airline.getLogoUrl().equals(url)) return airline;
        }
        return null;
    }

    public City getCityById(String id){
        return cities.containsKey(id)? cities.get(id) : null;
    }

    public Airport getAirportById(String id){
        return airports.containsKey(id)? airports.get(id) : null;
    }

    public Country getCountryById(String id){
        return countries.containsKey(id)? countries.get(id) : null;
    }

    public Collection<Country> getCountries(){
        return countries.values();
    }

    public Collection<City> getCities() {
        return cities.values();
    }

    public Collection<Airport> getAirports() {
        return airports.values();
    }

    public Collection<MyCurrency> getCurrencies() {
        return currencies.values();
    }

    public Collection<Flight> getFlights() {
        return flights.values();
    }

    public Collection<FlightState> getFlightStates() {
        return flightStates.values();
    }

    public Collection<Review> getFlightReviews(String airlineID, Integer number) {
        String identifier = airlineID.trim() + " " + number;
        return getFlightReviews(identifier);
    }
    public Collection<Review> getFlightReviews(String identifier) {
        return reviews.get(identifier.trim().toUpperCase());
    }

    public Collection<Review> getAirlineReviews(String airlineID){
        HashSet<Review> airlineReviews = new HashSet<>();
        for (String identifier: reviews.keySet()){
            if(identifier.contains(airlineID.toUpperCase().trim())){
                airlineReviews.addAll(reviews.get(identifier));
            }
        }
        return airlineReviews;
    }

    public Collection<Deal> getDeals() {
        return deals.values();
    }
    public Collection<Deal> getTrackedDeals() {
        Collection<Deal> trackedDeals = new ArrayList<>();
        for (String s: SettingsManager.getInstance(context).getTrackedLegs()){
            if(deals.containsKey(s)){
                trackedDeals.add(deals.get(s));
            }
        }
        return trackedDeals;
    }

    public boolean loadImageIntoView(final ImageView imageView, final String url){
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        imageView.setBackground(new BitmapDrawable(context.getResources(),bitmap));
                        return;
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        //TODO Not Found
                    }
                });
        dataHolder.addToVolleyQueue(request);
        return true;
    }
}
