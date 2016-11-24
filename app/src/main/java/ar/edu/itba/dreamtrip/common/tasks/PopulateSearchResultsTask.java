package ar.edu.itba.dreamtrip.common.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;
import ar.edu.itba.dreamtrip.common.API.dependencies.FlightDependency;
import ar.edu.itba.dreamtrip.common.model.Airline;
import ar.edu.itba.dreamtrip.common.model.Airport;
import ar.edu.itba.dreamtrip.common.model.City;
import ar.edu.itba.dreamtrip.common.model.Country;
import ar.edu.itba.dreamtrip.common.model.Flight;
import ar.edu.itba.dreamtrip.common.model.StatusSearch;
import ar.edu.itba.dreamtrip.main.Adapter.ResultAdapter;
import ar.edu.itba.dreamtrip.main.ResultElement;


public class PopulateSearchResultsTask extends AsyncTaskInformed<Object,Void,ArrayList<String>>{

    private Context context;
    private ListView listView;
    private String filter;
    private View otherSearchs;
    ArrayList<ResultElement> searchResults = new ArrayList<>();

    public PopulateSearchResultsTask(Context context, ListView list, View otherSearchs, String filter) {
        this.context = context;
        this.listView = list;
        this.filter = filter.trim().toUpperCase();
        this.otherSearchs = otherSearchs;
    }

    private StatusSearch getStatusSearch(String string){
//        return new StatusSearch("BA",2282);

        String[] arr = string.trim().split("\\W");
        if(arr.length != 2) return null;
        if(arr[0].matches("\\d{3,4}") && arr[1].matches("\\w{2}")){
            Integer num = Integer.parseInt(arr[0]);
            if(num == null) return null;
            return new StatusSearch(arr[1].toUpperCase(),num);
        }
        if(arr[0].matches("\\w{2}") && arr[1].matches("\\d{3,4}")){

            Integer num = Integer.parseInt(arr[1]);
            if(num == null) return null;
            return new StatusSearch(arr[0].toUpperCase(),num);
        }
        return null;
    }

    @Override
    public HashSet<Dependency> getDependencies() {
        HashSet<Dependency> dependencies = new HashSet<>();
        dependencies.add(new Dependency(DependencyType.AIRPORTS));
        dependencies.add(new Dependency(DependencyType.AIRLINES));
        StatusSearch statusSearch = getStatusSearch(filter);
        if(statusSearch != null) {
            dependencies.add(new FlightDependency(statusSearch));
        }
        return dependencies;
    }

    @Override
    protected ArrayList<String> doInBackground(Object... params) {
        DataHolder dataHolder = (DataHolder) params[0];
        ArrayList<String> strings = new ArrayList<>();

        if (filter.equals("")) {
            return strings;
        }
        System.out.println("filterting ");
        Collection<Airline> airlines = dataHolder.getAirlines();
        for (Airline a: airlines) {
            if (a.getName().toLowerCase().contains(filter.toLowerCase())) {
                strings.add(a.getName());
                searchResults.add(new ResultElement(a, DependencyType.AIRLINES));
            }
        }

        Collection<Country> countries = dataHolder.getCountries();
        for (Country c: countries) {
            if (c.getName().toLowerCase().contains(filter.toLowerCase())) {
                strings.add(c.getName());
                searchResults.add(new ResultElement(c, DependencyType.COUNTRIES));

            }
        }

        Collection<City> cities = dataHolder.getCities();
        for (City c: cities) {
            if (c.getName().toLowerCase().contains(filter.toLowerCase())) {
                strings.add(c.getName());
                searchResults.add(new ResultElement(c, DependencyType.CITIES));
            }
        }

        Collection<Airport> airports = dataHolder.getAirports();
        for (Airport a: airports) {
            if (a.getName().toLowerCase().contains(filter.toLowerCase())) {
                strings.add(a.getName());
                searchResults.add(new ResultElement(a, DependencyType.AIRPORTS));
            }
        }

        if (filter.matches("^\\w{2}\\W\\d{3,4}$")) {
            Collection<Flight> flights = dataHolder.getFlights();
            for (Flight f : flights) {
                String airlineID = filter.toUpperCase().trim().split("\\W")[0];
                String flightNumber = filter.toUpperCase().trim().split("\\W")[1];
                if (f.getAirline().getID().equals(airlineID)
                        && f.getNumber().equals(Integer.parseInt(flightNumber))) {
                    strings.add(filter.trim().split("\\W")[0] + " " + filter.trim().split("\\W")[1]);
                    searchResults.add(new ResultElement(f, DependencyType.FLIGHT_SEARCH));
                }
            }
        }
//        toast("loaded " + flights.size() + " flights");

        return strings;
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        if (result.isEmpty()) {
            otherSearchs.setVisibility(View.VISIBLE);
        } else {
            otherSearchs.setVisibility(View.INVISIBLE);
        }
        String[] arr = new String[result.size()];

        int i = 0;
        for (String each: result) {
            arr[i++] = each;
        }

        listView.setAdapter(new ResultAdapter(context, searchResults));
    }

    public void toast(String str) {
        Toast.makeText(context, str,
                Toast.LENGTH_SHORT).show();
    }
}

