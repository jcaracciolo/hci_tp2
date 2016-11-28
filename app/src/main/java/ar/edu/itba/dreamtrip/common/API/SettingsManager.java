package ar.edu.itba.dreamtrip.common.API;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.common.LenguageType;
import ar.edu.itba.dreamtrip.common.model.FlightStatus;

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

    public boolean setFlightStatus(String identifier, FlightStatus flightStatus) {
        String regex = context.getString(R.string.valid_identifier_regex);
        if(!identifier.matches(regex))return false;
        String prefsString = context.getString(R.string.tracked_flights_pref);
        String amountString = context.getString(R.string.tracked_flight_amount);
        String baseString = context.getString(R.string.tracked_flight);
        String extraInfo = FlightStatus.toString(context,flightStatus);

        return setExtraInfo(identifier,extraInfo,prefsString,amountString,baseString);
    }

    public FlightStatus getFlightStatus(String identifier) {
        String regex = context.getString(R.string.valid_identifier_regex);
        if(!identifier.matches(regex))return null;
        String prefsString = context.getString(R.string.tracked_flights_pref);
        String amountString = context.getString(R.string.tracked_flight_amount);
        String baseString = context.getString(R.string.tracked_flight);
        String extraInfo = getExtraInfo(identifier,prefsString,amountString,baseString);
        FlightStatus flightStatus = FlightStatus.parseFlightStatus(context,extraInfo);
        return flightStatus;
    }

    public boolean dealIsShown(String identifier) {
        String regex = context.getString(R.string.valid_leg_regex);
        if(!identifier.matches(regex))return false;
        String prefsString = context.getString(R.string.tracked_legs_pref);
        String amountString = context.getString(R.string.tracked_leg_amount);
        String baseString = context.getString(R.string.tracked_legs);
        String extraInfo = getExtraInfo(identifier,prefsString,amountString,baseString);
        if(("TRUE").equals(extraInfo)) return true;
        else return false;
    }
    public boolean setDealShown(String identifier, boolean shown) {
        String regex = context.getString(R.string.valid_leg_regex);
        if(!identifier.matches(regex))return false;
        String prefsString = context.getString(R.string.tracked_legs_pref);
        String amountString = context.getString(R.string.tracked_leg_amount);
        String baseString = context.getString(R.string.tracked_legs);
        String extraInfo = "TRUE";

        return setExtraInfo(identifier,extraInfo,prefsString,amountString,baseString);
    }



    public boolean clearTrackedFlights(){
        ArrayList<String> tracked = getTrackedFlights();
        for(String string: tracked){
            untrackFlight(string);
        }
        return tracked.size() > 0;
    }

    public boolean clearTrackedLegs(){
        ArrayList<String> tracked = getTrackedLegs();
        for(String string: tracked){
            untrackLeg(string);
        }
        return tracked.size() > 0;
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
        prefsString = context.getString(R.string.tracked_legs_pref);
        context.getSharedPreferences(prefsString, 0).edit().clear().commit();
    }

    //Must be like this "BUE LON"
    public boolean trackLeg(String legID){
        String prefsString = context.getString(R.string.tracked_legs_pref);
        String baseFlightString = context.getString(R.string.tracked_legs);
        String amountString = context.getString(R.string.tracked_leg_amount);
        String regex = context.getString(R.string.valid_leg_regex);

        return track(legID,regex,prefsString,amountString,baseFlightString);
    }

    //Must be like this "BUE EZE"
    public boolean untrackLeg(String legID){
        String prefsString = context.getString(R.string.tracked_legs_pref);
        String baseFlightString = context.getString(R.string.tracked_legs);
        String amountString = context.getString(R.string.tracked_leg_amount);
        String regex = context.getString(R.string.valid_leg_regex);

        return untrack(legID,regex,prefsString,amountString,baseFlightString);
    }

    public ArrayList<String> getTrackedLegs(){
        String prefsString = context.getString(R.string.tracked_legs_pref);
        String baseFlightString = context.getString(R.string.tracked_legs);
        String amountString = context.getString(R.string.tracked_leg_amount);
        String regex = context.getString(R.string.valid_leg_regex);

        return getTracked(prefsString,amountString,baseFlightString);
    }

    public boolean track(String identifier, String regex, String prefsString, String amountString,
                         String baseString) {
        if(!identifier.matches(regex)){
            return false;
        }

        String separator = context.getString(R.string.extra_info_separator);
        SharedPreferences sharedPref = context.getSharedPreferences(prefsString, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        Integer trackedObjAmount= sharedPref.getInt(amountString,0);
        for (int i = 0; i < trackedObjAmount; i++) {
            String savedIdentifier = sharedPref.getString(baseString+i,null);
            savedIdentifier = savedIdentifier.split(separator)[0];
            if(savedIdentifier == null) throw new RuntimeException("A null identifier has been found");
            if(savedIdentifier.equals(identifier.toUpperCase())) return false;
        }
        editor.putString(baseString+trackedObjAmount,identifier.toUpperCase() + separator);
        editor.putInt(amountString,trackedObjAmount+1);
        editor.commit();
        return true;
    }

    //Returns true if deleted something
    private boolean untrack(String identifier, String regex, String prefsString, String amountString,
                            String baseString) {
        if(!identifier.matches(regex)) return false;
        String separator = context.getString(R.string.extra_info_separator);
        SharedPreferences sharedPref = context.getSharedPreferences(prefsString, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        Integer trackedObjAmount= sharedPref.getInt(amountString,0);

        for (int i = 0; i < trackedObjAmount; i++) {
            String savedIdentifier = sharedPref.getString(baseString+i,null).split(separator)[0];
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

    private ArrayList<String> getTracked(String prefsString, String amountString,
                                         String baseString) {

        SharedPreferences sharedPref = context.getSharedPreferences(prefsString, Context.MODE_PRIVATE);
        String separator = context.getString(R.string.extra_info_separator);

        Integer trackedobjAmount= sharedPref.getInt(amountString,0);
        ArrayList<String> identifiers = new ArrayList<>();

        for (int i = 0; i < trackedobjAmount; i++) {
            String identifier = sharedPref.getString(baseString+i,null);
            identifier = identifier.split(separator)[0];
            if(identifier == null) throw new RuntimeException("A null identifier has been found");
            identifiers.add(identifier);
        }
        return identifiers;
    }

    private String getExtraInfo(String identifier, String prefsString, String amountString,
                                String baseString) {

        SharedPreferences sharedPref = context.getSharedPreferences(prefsString, Context.MODE_PRIVATE);
        String separator = context.getString(R.string.extra_info_separator);

        Integer trackedobjAmount= sharedPref.getInt(amountString,0);

        for (int i = 0; i < trackedobjAmount; i++) {
            String savedIdentifier = sharedPref.getString(baseString+i,null);
            String savedExtraInfo = savedIdentifier.split(separator).length >1?savedIdentifier.split(separator)[1]:null;
            savedIdentifier = savedIdentifier.split(separator)[0];
            if(identifier == null) throw new RuntimeException("A null identifier has been found");
            if(savedIdentifier.equals(identifier)) return savedExtraInfo;
        }

        return null;
    }

    private boolean setExtraInfo(String identifier, String extraInfo, String prefsString, String amountString,
                                 String baseString) {

        SharedPreferences sharedPref = context.getSharedPreferences(prefsString, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        String separator = context.getString(R.string.extra_info_separator);

        Integer trackedobjAmount= sharedPref.getInt(amountString,0);

        for (int i = 0; i < trackedobjAmount; i++) {
            String savedIdentifier = sharedPref.getString(baseString+i,null);
            savedIdentifier = savedIdentifier.split(separator)[0];
            if(identifier == null) throw new RuntimeException("A null identifier has been found");
            if(savedIdentifier.equals(identifier)) {
                editor.remove(baseString+i);
                editor.putString(baseString+i,savedIdentifier + separator + extraInfo);
                editor.commit();
                return true;
            }

        }

        return false;
    }

    public boolean setFlightStateCheckInterval(Integer interval){
        if(interval <= 2) return false;
        String prefsString = context.getString(R.string.general_settings_pref);
        String identifier = context.getString(R.string.general_settings_flight_check_interval);
        SharedPreferences sharedPref = context.getSharedPreferences(prefsString, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt(identifier,interval);
        editor.apply();
        return true;
    }

    public Integer getFlightStateCheckInterval(){
        String prefsString = context.getString(R.string.general_settings_pref);
        String identifier = context.getString(R.string.general_settings_flight_check_interval);
        SharedPreferences sharedPref = context.getSharedPreferences(prefsString, Context.MODE_PRIVATE);

        return sharedPref.getInt(identifier,1);
    }
    public boolean setSavedLenguage(LenguageType lenguageType){
        if(lenguageType == null) return false;
        String prefsString = context.getString(R.string.general_settings_pref);
        String identifier = context.getString(R.string.general_settings_lenguage);
        SharedPreferences sharedPref = context.getSharedPreferences(prefsString, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(identifier,LenguageType.toString(context,lenguageType));
        editor.apply();
        return true;
    }

    public LenguageType getSavedLanguage(){
        String prefsString = context.getString(R.string.general_settings_pref);
        String identifier = context.getString(R.string.general_settings_lenguage);
        SharedPreferences sharedPref = context.getSharedPreferences(prefsString, Context.MODE_PRIVATE);
        String language = sharedPref.getString(identifier,context.getString(R.string.general_settings_lenguage_english));
        return LenguageType.parseLenguage(context,language);
    }

    public boolean setFlightNotifications(Boolean enabled){
        if(enabled == null) return false;
        String prefsString = context.getString(R.string.general_settings_pref);
        String identifier = context.getString(R.string.general_settings_flight_notification);
        SharedPreferences sharedPref = context.getSharedPreferences(prefsString, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putBoolean(identifier,enabled);
        editor.apply();
        return true;
    }
    public boolean getFlightNotifications(){
        String prefsString = context.getString(R.string.general_settings_pref);
        String identifier = context.getString(R.string.general_settings_flight_notification);
        SharedPreferences sharedPref = context.getSharedPreferences(prefsString, Context.MODE_PRIVATE);

        return sharedPref.getBoolean(identifier,true);
    }

    public boolean setDealsNotifications(Boolean enabled){
        if(enabled == null) return false;
        String prefsString = context.getString(R.string.general_settings_pref);
        String identifier = context.getString(R.string.general_settings_leg_notification);
        SharedPreferences sharedPref = context.getSharedPreferences(prefsString, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putBoolean(identifier,enabled);
        editor.apply();
        return true;
    }
    public boolean getDealsNotifications(){
        String prefsString = context.getString(R.string.general_settings_pref);
        String identifier = context.getString(R.string.general_settings_leg_notification);
        SharedPreferences sharedPref = context.getSharedPreferences(prefsString, Context.MODE_PRIVATE);

        return sharedPref.getBoolean(identifier,true);
    }
}
