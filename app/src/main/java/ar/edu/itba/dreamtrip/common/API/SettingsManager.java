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
        if(!identifier.matches("^\\w{2} \\d{3,4}$"))
            throw new RuntimeException("Illegal flight identifier" + identifier);
        String prefsString = context.getString(R.string.tracked_flights_pref);
        String baseFlightString = context.getString(R.string.tracked_flight);
        String amountString = context.getString(R.string.tracked_flight_amount);

        SharedPreferences sharedPref = context.getSharedPreferences(prefsString, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        Integer trackedFlightsAmount= sharedPref.getInt(amountString,0);


        for (int i = 0; i < trackedFlightsAmount; i++) {
            String savedIdentifier = sharedPref.getString(baseFlightString+i,null);
            if(savedIdentifier == null) throw new RuntimeException("A null identifier has been found");
            if(savedIdentifier.equals(identifier.toUpperCase())) return false;
        }
        editor.putString(baseFlightString+trackedFlightsAmount,identifier.toUpperCase());
        editor.putInt(amountString,trackedFlightsAmount+1);
        editor.commit();
        return true;
    }

    public boolean untrackFlight(String identifier) {
        if(!identifier.matches("^\\w{2} \\d{3,4}$"))
            throw new RuntimeException("Illegal flight identifier" + identifier);
        String prefsString = context.getString(R.string.tracked_flights_pref);
        String baseFlightString = context.getString(R.string.tracked_flight);
        String amountString = context.getString(R.string.tracked_flight_amount);

        SharedPreferences sharedPref = context.getSharedPreferences(prefsString, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        Integer trackedFlightsAmount= sharedPref.getInt(amountString,0);

        for (int i = 0; i < trackedFlightsAmount; i++) {
            String savedIdentifier = sharedPref.getString(baseFlightString+i,null);
            if(savedIdentifier == null) throw new RuntimeException("A null identifier has been found");
            if(savedIdentifier.equals(identifier)) {
                editor.remove(baseFlightString+i);
                if(i < trackedFlightsAmount - 1){
                    String lastIdentifier = sharedPref.getString(baseFlightString+(trackedFlightsAmount-1),null);
                    if(lastIdentifier == null) throw new RuntimeException("A null identifier has been found");
                    editor.remove(baseFlightString+(trackedFlightsAmount-1));
                    editor.putString(baseFlightString+i,lastIdentifier);
                }
                trackedFlightsAmount -= 1;
                editor.remove(amountString);
                editor.putInt(amountString,trackedFlightsAmount);
                editor.commit(); //cambiarlo a commit si se caga algo
            }
        }
        return true;
    }
    public ArrayList<String> getTrackedFlights() {
        String prefsString = context.getString(R.string.tracked_flights_pref);
        String baseFlightString = context.getString(R.string.tracked_flight);
        String amountString = context.getString(R.string.tracked_flight_amount);

        SharedPreferences sharedPref = context.getSharedPreferences(prefsString, Context.MODE_PRIVATE);

        Integer trackedFlightsAmount= sharedPref.getInt(amountString,0);
        ArrayList<String> identifiers = new ArrayList<>();

        for (int i = 0; i < trackedFlightsAmount; i++) {
            String identifier = sharedPref.getString(baseFlightString+i,null);
            if(identifier == null) throw new RuntimeException("A null identifier has been found");
            identifiers.add(identifier);
        }
        return identifiers;
    }


    public void clearTrackedFlights(){
        String prefsString = context.getString(R.string.tracked_flights_pref);
        context.getSharedPreferences(prefsString, 0).edit().clear().commit();
    }
}
