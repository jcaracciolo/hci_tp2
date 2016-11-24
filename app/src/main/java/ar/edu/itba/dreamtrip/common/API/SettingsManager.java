package ar.edu.itba.dreamtrip.common.API;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import ar.edu.itba.dreamtrip.R;

/**
 * Created by Julian Benitez on 11/23/2016.
 */
public class SettingsManager {
    private static SettingsManager ourInstance ;
    private Context context;

    public static SettingsManager getInstance(Context context){
        if (ourInstance== null) {
            ourInstance= new SettingsManager(context.getApplicationContext());
        }
        return ourInstance;
    }

    private SettingsManager(Context context) {
        this.context = context;
    }

    public boolean trackFlight(String identifier) {
        String prefsString = context.getString(R.string.tracked_flights_pref);
        String baseFlightString = context.getString(R.string.tracked_flight);
        String amountString = context.getString(R.string.tracked_flight_amount);
        String regex = context.getString(R.string.valid_identifier_regex);

        return track(identifier,regex,prefsString,amountString,baseFlightString);
    }
    public boolean untrackFlight(String identifier) {
        String prefsString = context.getString(R.string.tracked_flights_pref);
        String baseFlightString = context.getString(R.string.tracked_flight);
        String amountString = context.getString(R.string.tracked_flight_amount);
        String regex = context.getString(R.string.valid_identifier_regex);

        return untrack(identifier,regex,prefsString,amountString,baseFlightString);
    }

    public ArrayList<String> getTrackedFlights() {
        String prefsString = context.getString(R.string.tracked_flights_pref);
        String baseFlightString = context.getString(R.string.tracked_flight);
        String amountString = context.getString(R.string.tracked_flight_amount);

        return getTracked(prefsString,amountString,baseFlightString);
    }


    public void clearAllTracked(){
        String prefsString = context.getString(R.string.tracked_flights_pref);
        context.getSharedPreferences(prefsString, 0).edit().clear().commit();
        prefsString = context.getString(R.string.tracked_airports_pref);
        context.getSharedPreferences(prefsString, 0).edit().clear().commit();
    }

    public boolean trackAirport(String airportID){
        String prefsString = context.getString(R.string.tracked_airports_pref);
        String baseFlightString = context.getString(R.string.tracked_airport);
        String amountString = context.getString(R.string.tracked_airport_amount);
        String regex = context.getString(R.string.valid_airport_regex);

        return track(airportID,regex,prefsString,amountString,baseFlightString);
    }
    public boolean untrackAirport(String airportID){
        String prefsString = context.getString(R.string.tracked_airports_pref);
        String baseFlightString = context.getString(R.string.tracked_airport);
        String amountString = context.getString(R.string.tracked_airport_amount);
        String regex = context.getString(R.string.valid_airport_regex);

        return untrack(airportID,regex,prefsString,amountString,baseFlightString);
    }
    public ArrayList<String> getTrackedAirports(){
        String prefsString = context.getString(R.string.tracked_airports_pref);
        String baseFlightString = context.getString(R.string.tracked_airport);
        String amountString = context.getString(R.string.tracked_airport_amount);
        String regex = context.getString(R.string.valid_airport_regex);

        return getTracked(prefsString,amountString,baseFlightString);
    }

    public boolean track(String identifier, String regex, String prefsString, String amountString,
                         String baseString) {
        if(!identifier.matches(regex)){
            return false;
        }

        SharedPreferences sharedPref = context.getSharedPreferences(prefsString, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        Integer trackedObjAmount= sharedPref.getInt(amountString,0);
        for (int i = 0; i < trackedObjAmount; i++) {
            String savedIdentifier = sharedPref.getString(baseString+i,null);
            if(savedIdentifier == null) throw new RuntimeException("A null identifier has been found");
            if(savedIdentifier.equals(identifier.toUpperCase())) return false;
        }
        editor.putString(baseString+trackedObjAmount,identifier.toUpperCase());
        editor.putInt(amountString,trackedObjAmount+1);
        editor.commit();
        return true;
    }

    //Returns true if deleted something
    public boolean untrack(String identifier, String regex, String prefsString, String amountString,
                           String baseString) {
        if(!identifier.matches(regex)) return false;

        SharedPreferences sharedPref = context.getSharedPreferences(prefsString, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        Integer trackedObjAmount= sharedPref.getInt(amountString,0);

        for (int i = 0; i < trackedObjAmount; i++) {
            String savedIdentifier = sharedPref.getString(baseString+i,null);
            if(savedIdentifier == null) throw new RuntimeException("A null identifier has been found");
            if(savedIdentifier.equals(identifier)) {
                editor.remove(baseString+i);
                if(i < trackedObjAmount - 1){
                    String lastIdentifier = sharedPref.getString(baseString+(trackedObjAmount-1),null);
                    if(lastIdentifier == null) throw new RuntimeException("A null identifier has been found");
                    editor.remove(baseString+(trackedObjAmount-1));
                    editor.putString(baseString+i,lastIdentifier);
                }
                trackedObjAmount -= 1;
                editor.remove(amountString);
                editor.putInt(amountString,trackedObjAmount);
                editor.commit(); //cambiarlo a commit si se caga algo
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> getTracked(String prefsString, String amountString,
                                        String baseString) {

        SharedPreferences sharedPref = context.getSharedPreferences(prefsString, Context.MODE_PRIVATE);

        Integer trackedobjAmount= sharedPref.getInt(amountString,0);
        ArrayList<String> identifiers = new ArrayList<>();

        for (int i = 0; i < trackedobjAmount; i++) {
            String identifier = sharedPref.getString(baseString+i,null);
            if(identifier == null) throw new RuntimeException("A null identifier has been found");
            identifiers.add(identifier);
        }
        return identifiers;
    }
}
