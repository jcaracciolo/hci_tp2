package ar.edu.itba.dreamtrip.common.tasks;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import ar.edu.itba.dreamtrip.TrackedDestinations.AutocompleteElement;
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


public class PopulateAutocompleteTask extends AsyncTaskInformed<Object,Void,ArrayList<String>>{

    private Context context;
    private AutoCompleteTextView autocompleteView;
    private String filter;
    ArrayList<AutocompleteElement> searchResults = new ArrayList<>();

    public PopulateAutocompleteTask(Context context, AutoCompleteTextView autocompleteView, String filter) {
        this.context = context;
        this.autocompleteView = autocompleteView;
        this.filter = filter.trim();
    }

    @Override
    public HashSet<Dependency> getDependencies() {
        HashSet<Dependency> dependencies = new HashSet<>();
        dependencies.add(new Dependency(DependencyType.CITIES));
        return dependencies;
    }

    @Override
    protected ArrayList<String> doInBackground(Object... params) {
        DataHolder dataHolder = (DataHolder) params[0];
        ArrayList<String> strings = new ArrayList<>();

        if (filter.equals("")) {
            return strings;
        }

        Collection<City> cities = dataHolder.getCities();
        for (City c: cities) {
            if (c.getName().split(",")[0].toLowerCase().contains(filter.toLowerCase())) {
                strings.add("(" + c.getID() + ") " + c.getName().split(",")[0]);
                searchResults.add(new AutocompleteElement(c.getID(), c.getName()));
            }
        }

        return strings;
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, result);
        autocompleteView.setAdapter(adapter);
        autocompleteView.showDropDown();
    }

    public void toast(String str) {
        Toast.makeText(context, str,
                Toast.LENGTH_SHORT).show();
    }
}

